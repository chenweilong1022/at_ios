package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.*;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.utils.*;
import io.renren.common.utils.EnumUtil;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.client.vo.CurlVO;
import io.renren.modules.client.vo.IPWorldRespCurl;
import io.renren.modules.ltt.conver.CdLineIpProxyConver;
import io.renren.modules.ltt.dao.CdLineIpProxyDao;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.CdIpConfigEntity;
import io.renren.modules.ltt.entity.CdLineIpProxyEntity;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.LockMapKeyResource;
import io.renren.modules.ltt.enums.ProxyStatus;
import io.renren.modules.ltt.enums.RedisKeys;
import io.renren.modules.ltt.service.CdIpConfigService;
import io.renren.modules.ltt.service.CdLineIpProxyService;
import io.renren.modules.ltt.vo.CdLineIpProxyVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static cn.hutool.core.lang.PatternPool.IPV4;


@Service("cdLineIpProxyService")
@Game
@Slf4j
public class CdLineIpProxyServiceImpl extends ServiceImpl<CdLineIpProxyDao, CdLineIpProxyEntity> implements CdLineIpProxyService {

    @Resource(name = "caffeineCacheListString")
    private Cache<String, Queue<String>> caffeineCacheListString;
    @Resource(name = "caffeineCacheProjectWorkEntity")
    private Cache<String, ProjectWorkEntity> caffeineCacheProjectWorkEntity;
    @Autowired
    private ConcurrentHashMap<String, Lock> lockMap;
    @Resource
    private CdIpConfigService cdIpConfigService;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public PageUtils<CdLineIpProxyVO> queryPage(CdLineIpProxyDTO cdLineIpProxy) {
        IPage<CdLineIpProxyEntity> page = baseMapper.selectPage(
                new Query<CdLineIpProxyEntity>(cdLineIpProxy).getPage(),
                new QueryWrapper<CdLineIpProxyEntity>()
        );

        return PageUtils.<CdLineIpProxyVO>page(page).setList(CdLineIpProxyConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public CdLineIpProxyVO getById(Integer id) {
        return CdLineIpProxyConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(CdLineIpProxyDTO cdLineIpProxy) {
        CdLineIpProxyEntity cdLineIpProxyEntity = CdLineIpProxyConver.MAPPER.converDTO(cdLineIpProxy);
        return this.save(cdLineIpProxyEntity);
    }

    @Autowired
    private StringRedisTemplate redisTemplate;




    @Override
    public boolean updateById(CdLineIpProxyDTO cdLineIpProxy) {
        CdLineIpProxyEntity cdLineIpProxyEntity = CdLineIpProxyConver.MAPPER.converDTO(cdLineIpProxy);
        return this.updateById(cdLineIpProxyEntity);
    }


    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getProxyIp(CdLineIpProxyDTO cdLineIpProxyDTO) {
        log.info("cdLineIpProxyDTO = {}",JSONUtil.toJsonStr(cdLineIpProxyDTO));
        //获取ip代理的国家
        PhoneCountryVO phoneNumberInfo = null;
        try {
            phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(cdLineIpProxyDTO.getTokenPhone());
        } catch (Exception e) {
            return null;
        }
        //设置代理的国家，如果不传入默认是当前是当前手机号的国家
        Long countryCode = phoneNumberInfo.getCountryCode();
        if (ObjectUtil.isNotNull(cdLineIpProxyDTO.getCountryCode())) {
            countryCode = cdLineIpProxyDTO.getCountryCode();
        }
        //设置默认代理 如果不传入默认是动态
        Integer proxy = cdLineIpProxyDTO.getSelectProxyStatus();
        if (ObjectUtil.isNull(proxy)) {
            cdLineIpProxyDTO.setSelectProxyStatus(ProxyStatus.ProxyStatus1.getKey());
            proxy = ProxyStatus.ProxyStatus1.getKey();
        }
        String ip = getIp(cdLineIpProxyDTO, countryCode, proxy,phoneNumberInfo);
        log.info("phone = {} countryCode = {}获取到的ip {}",cdLineIpProxyDTO.getTokenPhone(),countryCode,ip);
        return ip;
    }

    private String getIp(CdLineIpProxyDTO cdLineIpProxyDTO, Long countryCode, Integer proxy,PhoneCountryVO phoneNumberInfo) {
        try{
            ExecutorService executorService = Executors.newFixedThreadPool(3);
            //国家英文
            String regions = EnumUtil.queryValueByKey(countryCode.intValue(), CountryCode.values());
            //出口ip
            String outIpv4 = (String) redisTemplate.opsForHash().get(RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)), cdLineIpProxyDTO.getTokenPhone());
            String ipS5 = null;
            if (StrUtil.isNotEmpty(outIpv4)) {
                ipS5 = (String) redisTemplate.opsForHash().get(RedisKeys.RedisKeys1.getValue(), outIpv4);
            }
            //如果有ip直接返回
            if (StrUtil.isNotEmpty(outIpv4) && StrUtil.isNotEmpty(ipS5)) {
                CurlVO proxyUse = getProxyUse(ipS5, regions);
                if (proxyUse.isProxyUse()) {
                    //判断ip黑名单缓存中是否有
                    String value = RedisKeys.RedisKeys4.getValue(proxyUse.getIp());
                    String ipCache = redisTemplate.opsForValue().get(value);
                    if (ipCache != null) {
                        redisTemplate.opsForHash().delete(RedisKeys.RedisKeys1.getValue(), outIpv4);
                        return null;
                    }
                    if (ProxyStatus.ProxyStatus3.getKey().equals(proxy)) {
                        //如果是静态ip
                        if (ipS5.contains("@")) {
                            //如果ip相同并且国家一样
                            if (extracted(cdLineIpProxyDTO, countryCode, proxyUse, outIpv4, regions, ipS5))
                                return socks5Pre(ipS5);
                        }else {
                            redisTemplate.opsForValue().set(RedisKeys.RedisKeys4.getValue(outIpv4), cdLineIpProxyDTO.getTokenPhone(), 1, TimeUnit.DAYS);
                            redisTemplate.opsForHash().delete(RedisKeys.RedisKeys1.getValue(), outIpv4);
                        }
                    }else {
                        if (extracted(cdLineIpProxyDTO, countryCode, proxyUse, outIpv4, regions, ipS5))
                            return socks5Pre(ipS5);
                    }
                }else {
                    redisTemplate.opsForValue().set(RedisKeys.RedisKeys4.getValue(outIpv4), cdLineIpProxyDTO.getTokenPhone(), 1, TimeUnit.DAYS);
                    redisTemplate.opsForHash().delete(RedisKeys.RedisKeys1.getValue(), outIpv4);
                }
            } else {
                Set<CurlVO> ips = ConcurrentHashMap.newKeySet();
                //如果是静态ip
                if (ProxyStatus.ProxyStatus3.getKey().equals(proxy)) {
                    String s = redisTemplate.opsForList().rightPop(RedisKeys.RedisKeys9.getValue(regions));
                    if (s == null) {
                        return null;
                    }
                    CurlVO proxyUseOld = getProxyUse(s, regions);
                    proxyUseOld.setS5Ip(s);
                    ips.add(proxyUseOld);
                }else {
                    String ipOld = getDyIp(regions,phoneNumberInfo);
                    CurlVO proxyUseOld = getProxyUse(ipOld, regions);
                    proxyUseOld.setS5Ip(ipOld);
                    ips.add(proxyUseOld);
                }

//                for (int i = 0; i < 3; i++) {
//                    executorService.submit(() -> {
//                        String ip = getDyIp(regions,phoneNumberInfo);
//                        CurlVO proxyUse = getProxyUse(ip, regions);
//                        proxyUse.setS5Ip(ip);
//                        ips.add(proxyUse);
//                    });
//                }
                // 关闭线程池
//                executorService.shutdown();
//                // 等待所有任务完成
//                if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
//                    // 如果超时，则强制关闭尚未完成的任务
//                    executorService.shutdownNow();
//                }

                for (CurlVO proxyUse : ips) {
                    String ip = proxyUse.getS5Ip();
                    if (proxyUse.isProxyUse()) {
                        //判断ip黑名单缓存中是否有
                        String ipCache = redisTemplate.opsForValue().get(RedisKeys.RedisKeys4.getValue(proxyUse.getIp()));
                        if (ipCache != null) {
                            return null;
                        }
                        //如果国家一样
                        if (regions.toLowerCase().equals(proxyUse.getCountry().toLowerCase())) {
                            if (StrUtil.isNotEmpty(proxyUse.getIp())) {
                                String s = (String) redisTemplate.opsForHash().get(RedisKeys.RedisKeys1.getValue(), proxyUse.getIp());
                                if (StrUtil.isNotEmpty(s)) {
                                    if (!s.contains("@")) {
                                        redisTemplate.opsForHash().delete(RedisKeys.RedisKeys1.getValue(), proxyUse.getIp());
                                    }
                                }
                            }

                            Boolean b = redisTemplate.opsForHash().putIfAbsent(RedisKeys.RedisKeys1.getValue(), proxyUse.getIp(), ip);
                            if (b) {
                                if (StrUtil.isNotEmpty(outIpv4)) {
                                    if (!outIpv4.equals(proxyUse.getIp())) {
                                        redisTemplate.opsForValue().set(RedisKeys.RedisKeys4.getValue(outIpv4), cdLineIpProxyDTO.getTokenPhone(), 1, TimeUnit.DAYS);
                                        redisTemplate.opsForHash().delete(RedisKeys.RedisKeys1.getValue(), outIpv4);
                                    }
                                }//8107010757560
                                redisTemplate.opsForHash().put(RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)), cdLineIpProxyDTO.getTokenPhone(), proxyUse.getIp());
                                return socks5Pre(ip);
                            }
                        }
                    }else {
                        //如果失败并且是静态代理把ip放回去
                        if (ProxyStatus.ProxyStatus3.getKey().equals(proxy)) {
                            redisTemplate.opsForList().leftPush(RedisKeys.RedisKeys9.getValue(regions),ip);
                        }
                    }
                }
            }
        }catch (Exception e) {
            log.error("cdLineIpProxyService = {}",e.getMessage());
        }
        return null;
    }

    private boolean extracted(CdLineIpProxyDTO cdLineIpProxyDTO, Long countryCode, CurlVO proxyUse, String outIpv4, String regions, String ipS5) {
        //如果ip相同并且国家一样
        if (proxyUse.getIp().equals(outIpv4) && regions.toLowerCase().equals(proxyUse.getCountry().toLowerCase())) {
            return true;
            //如果ip不相同相同并且国家一样
        } else if (regions.toLowerCase().equals(proxyUse.getCountry().toLowerCase())) {
            Boolean b = redisTemplate.opsForHash().putIfAbsent(RedisKeys.RedisKeys1.getValue(), proxyUse.getIp(), ipS5);
            if (b) {
                if (StrUtil.isNotEmpty(outIpv4)) {
                    if (!outIpv4.equals(proxyUse.getIp())) {
                        redisTemplate.opsForValue().set(RedisKeys.RedisKeys4.getValue(outIpv4), cdLineIpProxyDTO.getTokenPhone(), 1, TimeUnit.DAYS);
                        redisTemplate.opsForHash().delete(RedisKeys.RedisKeys1.getValue(), outIpv4);
                    }
                }
                redisTemplate.opsForHash().put(RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)), cdLineIpProxyDTO.getTokenPhone(), proxyUse.getIp());
                return true;
            }else {
                redisTemplate.opsForHash().put(RedisKeys.RedisKeys1.getValue(), proxyUse.getIp(), ipS5);
                redisTemplate.opsForHash().delete(RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)), cdLineIpProxyDTO.getTokenPhone());
            }
        }
        return false;
    }


    private String getStaticIpResp(String regions) {
        Integer countryCode = EnumUtil.queryKeyByValue(regions, CountryCode.values());

        CdIpConfigEntity ipConfig = cdIpConfigService.getIpConfig(countryCode);
        if (ipConfig == null) {
            return null;
        }
        //累加ip使用次数
        cdIpConfigService.updateUsedCountById(ipConfig.getId());
        return String.format("%s:%s@%s:%s",
                ipConfig.getAccount(), ipConfig.getPassword(), ipConfig.getIp(), ipConfig.getSock5Port());
    }

    @Resource(name = "cardJpSmsOver")
    private Cache<String, String> cardJpSmsOver;

    private String getRandomIp(String url,String regions) {
        String getPhoneHttp = String.format(url, regions);
        //日本
        if (cardJpSmsOver.getIfPresent(getPhoneHttp) != null) {
            return null;
        }
//        String getPhoneHttp = String.format("https://tq.lunaproxy.com/getflowip?neek=1136881&num=500&type=1&sep=1&regions=%s&ip_si=1&level=1&sb=", regions);
        String resp = HttpUtil.get(getPhoneHttp);
        if (JSONUtil.isJson(resp) || resp.contains("Please request again in 2 seconds")) {
            log.info("{} resp = {}",getPhoneHttp,resp);
            cardJpSmsOver.put(getPhoneHttp,"Please request again in 2 secondss");
            return null;
        }
        return resp;
    }

////
//    @EventListener
//    @Order(value = 9999)//t35323ha-1027-61697		tha-1027-44108
//    public void handlerApplicationReadyEvent(ApplicationReadyEvent event) throws InterruptedException {
//        ExecutorService executorService = Executors.newFixedThreadPool(50);
//        Set<String> ips = ConcurrentHashMap.newKeySet();
//        for (int i = 0; i < 100000; i++) {
//            CdLineIpProxyServiceImpl cdLineIpProxyService = new CdLineIpProxyServiceImpl();
//            int finalI = i;
//            executorService.submit(() -> {
//                String jp = cdLineIpProxyService.getRolaIp("jp",String.valueOf(finalI));
//                CurlVO jp1 = cdLineIpProxyService.isProxyUseRolaIp(jp, "jp");
//                if (jp1.isProxyUse()) {
//                    ips.add(jp1.getIp());
//                    log.info("i = {} set size = {}",finalI,ips.size());
//                }
//            });
//            executorService.submit(() -> {
//                String getLunaIp = cdLineIpProxyService.getLunaIp("jp");
//                CurlVO getLunaIpVO = cdLineIpProxyService.isProxyUse(getLunaIp, "jp");
//                if (getLunaIpVO.isProxyUse()) {
//                    ips.add(getLunaIpVO.getIp());
//                    log.info("i = {} set size = {}",finalI,ips.size());
//                }
//            });
//            executorService.submit(() -> {
//                String getIp2WorldIp = cdLineIpProxyService.getIp2WorldIp("jp");
//                CurlVO getIp2WorldIpVO = cdLineIpProxyService.isProxyUseIp2World(getIp2WorldIp, "jp");
//                if (getIp2WorldIpVO.isProxyUse()) {
//                    ips.add(getIp2WorldIpVO.getIp());
//                    log.info("i = {} set size = {}",finalI,ips.size());
//                }
//            });
//            executorService.submit(() -> {
//                String getAbcIp = cdLineIpProxyService.getAbcIp("jp");
//                CurlVO getAbcIpVO = cdLineIpProxyService.isProxyUseAbcIp(getAbcIp, "jp");
//                if (getAbcIpVO.isProxyUse()) {
//                    ips.add(getAbcIpVO.getIp());
//                    log.info("i = {} set size = {}",finalI,ips.size());
//                }
//            });
//            executorService.submit(() -> {
//                String getIpmarsIp = cdLineIpProxyService.getIpmarsIp("jp");
//                CurlVO getIpmarsIpVO = cdLineIpProxyService.isProxyUseIpmarsIp(getIpmarsIp, "jp");
//                if (getIpmarsIpVO.isProxyUse()) {
//                    ips.add(getIpmarsIpVO.getIp());
//                    log.info("i = {} set size = {}",finalI,ips.size());
//                }
//            });
//        }
//
//        // 关闭线程池
//        executorService.shutdown();
//
//        // 等待所有任务完成
//        if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
//            // 如果超时，则强制关闭尚未完成的任务
//            executorService.shutdownNow();
//        }
//        System.out.println(ips);
//        System.out.println(ips.size());
//        System.out.println("All tasks are finished.");
//    }


    public static void main(String[] args) throws InterruptedException {

        List<String> ipst = new ArrayList<>();

        ipst.add("chenweilong_static_500:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_499:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_498:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_497:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_496:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_495:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_494:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_493:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_492:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_491:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_490:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_489:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_488:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_487:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_486:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_485:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_484:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_483:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_482:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_481:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_480:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_479:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_478:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_477:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_476:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_475:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_474:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_473:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_472:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_471:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_470:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_469:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_468:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_467:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_466:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_465:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_464:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_463:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_462:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_461:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_460:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_459:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_458:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_457:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_456:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_455:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_454:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_453:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_452:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_451:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_450:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_449:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_448:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_447:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_446:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_445:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_444:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_443:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_442:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_441:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_440:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_439:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_438:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_437:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_436:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_435:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_434:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_433:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_432:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_431:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_430:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_429:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_428:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_427:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_426:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_425:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_424:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_423:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_422:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_421:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_420:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_419:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_418:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_417:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_416:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_415:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_414:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_413:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_412:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_411:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_410:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_409:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_408:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_407:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_406:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_405:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_404:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_403:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_402:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_401:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_400:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_399:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_398:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_397:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_396:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_395:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_394:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_393:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_392:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_391:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_390:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_389:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_388:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_387:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_386:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_385:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_384:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_383:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_382:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_381:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_380:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_379:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_378:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_377:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_376:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_375:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_374:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_373:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_372:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_371:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_370:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_369:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_368:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_367:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_366:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_365:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_364:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_363:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_362:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_361:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_360:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_359:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_358:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_357:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_356:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_355:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_354:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_353:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_352:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_351:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_350:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_349:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_348:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_347:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_346:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_345:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_344:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_343:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_342:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_341:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_340:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_339:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_338:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_337:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_336:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_335:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_334:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_333:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_332:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_331:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_330:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_329:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_328:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_327:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_326:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_325:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_324:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_323:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_322:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_321:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_320:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_319:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_318:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_317:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_316:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_315:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_314:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_313:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_312:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_311:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_310:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_309:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_308:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_307:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_306:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_305:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_304:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_303:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_302:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_301:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_300:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_299:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_298:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_297:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_296:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_295:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_294:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_293:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_292:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_291:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_290:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_289:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_288:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_287:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_286:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_285:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_284:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_283:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_282:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_281:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_280:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_279:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_278:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_277:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_276:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_275:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_274:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_273:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_272:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_271:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_270:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_269:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_268:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_267:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_266:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_265:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_264:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_263:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_262:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_261:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_260:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_259:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_258:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_257:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_256:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_255:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_254:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_253:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_252:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_251:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_250:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_249:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_248:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_247:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_246:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_245:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_244:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_243:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_242:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_241:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_240:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_239:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_238:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_237:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_236:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_235:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_234:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_233:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_232:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_231:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_230:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_229:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_228:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_227:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_226:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_225:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_224:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_223:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_222:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_221:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_220:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_219:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_218:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_217:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_216:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_215:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_214:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_213:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_212:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_211:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_210:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_209:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_208:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_207:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_206:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_205:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_204:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_203:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_202:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_201:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_200:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_199:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_198:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_197:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_196:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_195:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_194:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_193:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_192:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_191:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_190:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_189:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_188:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_187:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_186:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_185:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_184:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_183:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_182:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_181:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_180:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_179:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_178:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_177:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_176:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_175:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_174:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_173:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_172:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_171:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_170:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_169:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_168:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_167:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_166:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_165:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_164:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_163:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_162:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_161:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_160:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_159:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_158:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_157:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_156:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_155:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_154:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_153:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_152:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_151:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_150:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_149:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_148:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_147:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_146:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_145:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_144:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_143:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_142:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_141:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_140:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_139:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_138:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_137:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_136:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_135:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_134:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_133:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_132:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_131:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_130:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_129:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_128:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_127:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_126:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_125:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_124:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_123:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_122:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_121:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_120:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_119:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_118:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_117:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_116:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_115:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_114:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_113:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_112:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_111:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_110:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_109:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_108:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_107:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_106:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_105:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_104:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_103:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_102:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_101:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_100:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_99:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_98:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_97:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_96:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_95:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_94:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_93:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_92:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_91:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_90:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_89:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_88:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_87:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_86:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_85:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_84:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_83:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_82:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_81:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_80:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_79:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_78:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_77:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_76:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_75:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_74:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_73:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_72:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_71:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_70:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_69:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_68:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_67:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_66:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_65:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_64:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_63:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_62:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_61:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_60:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_59:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_58:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_57:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_56:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_55:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_54:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_53:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_52:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_51:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_50:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_49:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_48:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_47:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_46:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_45:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_44:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_43:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_42:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_41:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_40:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_39:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_38:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_37:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_36:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_35:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_34:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_33:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_32:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_31:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_30:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_29:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_28:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_27:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_26:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_25:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_24:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_23:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_22:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_21:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_20:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_19:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_18:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_17:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_16:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_15:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_14:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_13:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_12:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_11:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_10:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_9:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_8:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_7:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_6:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_5:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_4:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_3:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_2:ch1433471850@gate5.rola.info:2031");
        ipst.add("chenweilong_static_1:ch1433471850@gate5.rola.info:2031");

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        Set<String> ips = ConcurrentHashMap.newKeySet();
        for (int i = 0; i < 500; i++) {
            CdLineIpProxyServiceImpl cdLineIpProxyService = new CdLineIpProxyServiceImpl();
            int finalI = i;
//            String jp = cdLineIpProxyService.getSuperProxyIp("jp");

            executorService.submit(() -> {
                String jp = ipst.get(finalI);
                CurlVO jp1 = cdLineIpProxyService.isProxyUseSuperProxyIp(jp, "jp");
                if (jp1.isProxyUse()) {
                    ips.add(jp1.getIp());
                    log.info("i = {} set size = {}",finalI,ips.size());
                }
            });
        }

        // 关闭线程池
        executorService.shutdown();

        // 等待所有任务完成
        if (!executorService.awaitTermination(1, TimeUnit.HOURS)) {
            // 如果超时，则强制关闭尚未完成的任务
            executorService.shutdownNow();
        }
        System.out.println(ips);
        System.out.println(ips.size());
        System.out.println("All tasks are finished.");
    }



    private static String socks5Pre(String ip) {
        if (ip.contains("socks5://")) {
            return ip;
        }
        return String.format("socks5://%s", ip);
    }

    public String getDyIp(String regions,PhoneCountryVO phoneNumberInfo) {
        String number = phoneNumberInfo.getNumber();
        int lastDigit = Character.getNumericValue(number.charAt(number.length() - 1));
        int i = lastDigit % 6;
        String s5Ip = null;
        if (i == 0) {
            s5Ip = getRolaIp(regions);
        }else if (i == 1) {
            s5Ip = getRolaIp(regions);
        }else if (i == 2) {
            s5Ip = getRolaIp(regions);
        }else if (i == 3) {
            s5Ip = getRolaIp(regions);
        }else if (i == 4) {
            s5Ip = getRolaIp(regions);
        }else if (i == 5) {
            s5Ip = getRolaIp(regions);
        }
        return s5Ip;
    }
//
    private CurlVO getProxyUse(String ip,String regions) {
        CurlVO proxyUse = new CurlVO().setProxyUse(false);
        if (ip.contains("lunaproxy")) {
            proxyUse = isProxyUse(ip,regions);
            if (!proxyUse.isProxyUse()) {
                proxyUse = otherProxyOutIpv4(ip, regions, proxyUse);
            }
        }else if (ip.contains("ip2world")) {
            proxyUse = isProxyUseIp2World(ip,regions);
            if (!proxyUse.isProxyUse()) {
                proxyUse = otherProxyOutIpv4(ip, regions, proxyUse);
            }
        }else if (ip.contains("abcproxy")) {
            proxyUse = isProxyUseAbcIp(ip,regions);
            if (!proxyUse.isProxyUse()) {
                proxyUse = otherProxyOutIpv4(ip, regions, proxyUse);
            }
        }else if (ip.contains("ipmars")) {
            proxyUse = isProxyUseIpmarsIp(ip,regions);
            if (!proxyUse.isProxyUse()) {
                proxyUse = otherProxyOutIpv4(ip, regions, proxyUse);
            }
        }else if (ip.contains("rola")) {
            proxyUse = isProxyUseRolaIp(ip,regions);
            if (!proxyUse.isProxyUse()) {
                proxyUse = otherProxyOutIpv4(ip, regions, proxyUse);
            }
        }else if (ip.contains("pyproxy")) {
            proxyUse = isProxyUseProxyUp(ip,regions);
            if (!proxyUse.isProxyUse()) {
                proxyUse = otherProxyOutIpv4(ip, regions, proxyUse);
            }
        }else {
            proxyUse = isProxyUseProxyUp(ip,regions);
            if (!proxyUse.isProxyUse()) {
                proxyUse = otherProxyOutIpv4(ip, regions, proxyUse);
            }
        }
        return proxyUse;
    }

    private CurlVO otherProxyOutIpv4(String ip, String regions, CurlVO proxyUse) {
        if (!proxyUse.isProxyUse()) {
            proxyUse = isProxyUse(ip, regions);
        }
        if (!proxyUse.isProxyUse()) {
            proxyUse = isProxyUseIp2World(ip, regions);
        }
        if (!proxyUse.isProxyUse()) {
            proxyUse = isProxyUseAbcIp(ip, regions);
        }
        if (!proxyUse.isProxyUse()) {
            proxyUse = isProxyUseIpmarsIp(ip, regions);
        }
        if (!proxyUse.isProxyUse()) {
            proxyUse = isProxyUseRolaIp(ip, regions);
        }
        if (!proxyUse.isProxyUse()) {
            proxyUse = isProxyUseMe(ip, regions);
        }
        return proxyUse;
    }
//curl -x brd-customer-hl_7d808016-zone-residential_proxy1-country-jp:2a1ffaiiww1h@brd.superproxy.io:22225 http://lumtest.com/myip.json
// "http://lumtest.com/myip.json"

    private String getSuperProxyIp(String regions) {
        //curl -x  ipinfo.io
        String format = String.format("brd-customer-hl_7d808016-zone-residential_proxy1-country-jp:2a1ffaiiww1h@brd.superproxy.io:22225", RandomUtil.randomString(20));
        System.out.println(format);
        return format;
    }


    private CurlVO isProxyUseSuperProxyIp(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        try {
            String format1 = String.format("curl -x %s http://lumtest.com/myip.json",ip);
            List<String> strings = RuntimeUtil.execForLines(format1);
            boolean flag = false;

            List<String> newStr = new ArrayList<>();
            for (String string : strings) {
                if (string.contains("{") || flag) {
                    flag = true;
                    newStr.add(string);
                }
            }
            String resp = CollUtil.join(newStr, "");
            IPWorldRespCurl curlVO = JSONUtil.toBean(resp, IPWorldRespCurl.class);
            if (curlVO.getCountry().toLowerCase().equals(country.toLowerCase())) {
                return falseCurlVO.setProxyUse(true).setIp(curlVO.getIp()).setCountry(curlVO.getCountry());
            }
        }catch (Exception e) {

        }
        return falseCurlVO;
    }

    private String getIpmoyuIp(String regions) {
        //curl -x  ipinfo.io
        String format = String.format("chen_103_0_0_10_%s_5_1:wei@zm.ipmoyu.com:3000", RandomUtil.randomString(20));
        System.out.println(format);
        return format;
    }


    private CurlVO isProxyUseIpmoyuIp(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        try {
            String format1 = String.format("curl -x %s ipinfo.pyproxy.io",ip);
            List<String> strings = RuntimeUtil.execForLines(format1);
            boolean flag = false;

            List<String> newStr = new ArrayList<>();
            for (String string : strings) {
                if (string.contains("{") || flag) {
                    flag = true;
                    newStr.add(string);
                }
            }
            String resp = CollUtil.join(newStr, "");
            IPWorldRespCurl curlVO = JSONUtil.toBean(resp, IPWorldRespCurl.class);
            if (curlVO.getCountry_code().toLowerCase().equals(country.toLowerCase())) {
                return falseCurlVO.setProxyUse(true).setIp(curlVO.getIp()).setCountry(curlVO.getCountry_code());
            }
        }catch (Exception e) {

        }
        return falseCurlVO;
    }

    private String getProxyUpIp(String regions) {
        //curl -x  ipinfo.io
        String format = String.format("ch1433471850-zone-resi-region-%s-session-%s-sessTime-9:ch143347185@89de9443270b9927.xuw.as.pyproxy.io:16666",regions, RandomUtil.randomString(8));
        System.out.println(format);
        return format;
    }


    private CurlVO isProxyUseProxyUp(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        try {
            String format1 = String.format("curl -x %s ipinfo.pyproxy.io",ip);
            List<String> strings = RuntimeUtil.execForLines(format1);
            boolean flag = false;

            List<String> newStr = new ArrayList<>();
            for (String string : strings) {
                if (string.contains("{") || flag) {
                    flag = true;
                    newStr.add(string);
                }
            }
            String resp = CollUtil.join(newStr, "");
            IPWorldRespCurl curlVO = JSONUtil.toBean(resp, IPWorldRespCurl.class);
            if (curlVO.getCountry_code().toLowerCase().equals(country.toLowerCase())) {
                return falseCurlVO.setProxyUse(true).setIp(curlVO.getIp()).setCountry(curlVO.getCountry_code());
            }
        }catch (Exception e) {

        }
        return falseCurlVO;
    }


    private String getRolaIp(String regions) {
        //curl -x  ipinfo.io
        String format = String.format("chenweilong_%s-country-%s:ch1433471850@proxyus.rola.vip:2000", RandomUtil.randomInt(1,100000),regions);
        System.out.println(format);
        return format;
    }


    private CurlVO isProxyUseRolaIp(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        try {
            String format1 = String.format("curl -x %s http://www.ip234.in/ip.json",ip);
            List<String> strings = RuntimeUtil.execForLines(format1);
            boolean flag = false;

            List<String> newStr = new ArrayList<>();
            for (String string : strings) {
                if (string.contains("{") || flag) {
                    flag = true;
                    newStr.add(string);
                }
            }
            String resp = CollUtil.join(newStr, "");
            IPWorldRespCurl curlVO = JSONUtil.toBean(resp, IPWorldRespCurl.class);
            if (curlVO.getCountry_code().toLowerCase().equals(country.toLowerCase())) {
                return falseCurlVO.setProxyUse(true).setIp(curlVO.getIp()).setCountry(curlVO.getCountry_code());
            }
        }catch (Exception e) {

        }
        return falseCurlVO;
    }



    private String getIpmarsIp(String regions) {
        //curl -x  ipinfo.io

        String format = String.format("CFD5XBNu6O-zone-marstop-region-%s-session-%s-sessTime-10:23941850@na.e52a499f3821702f.ipmars.vip:4900",regions.toUpperCase(), RandomUtil.randomString(18));
        return format;
    }


    private CurlVO isProxyUseIpmarsIp(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        try {
            String format1 = String.format("curl -x %s http://www.ip234.in/ip.json",ip);
            System.out.println(format1);
            List<String> strings = RuntimeUtil.execForLines(format1);

            boolean flag = false;

            List<String> newStr = new ArrayList<>();
            for (String string : strings) {
                if (string.contains("{") || flag) {
                    flag = true;
                    newStr.add(string);
                }
            }
            String resp = CollUtil.join(newStr, "");

            IPWorldRespCurl curlVO = JSONUtil.toBean(resp, IPWorldRespCurl.class);
            if (curlVO.getCountry().toLowerCase().equals(country.toLowerCase())) {
                return falseCurlVO.setProxyUse(true).setIp(curlVO.getIp()).setCountry(curlVO.getCountry());
            }
        }catch (Exception e) {

        }
        return falseCurlVO;
    }




    private String getAbcIp(String regions) {
        //curl -x  ipinfo.io DZHtDGILHC-zone-abc-region-JP:05421929@as.0e03d29f9c28cbfd.abcproxy.vip:4950
        String format = String.format("DZHtDGILHC-zone-abc-region-%s-session-%s-sessTime-10:05421929@na.0e03d29f9c28cbfd.abcproxy.vip:4950",regions.toUpperCase(), RandomUtil.randomString(9));
        System.out.println(format);
        return format;
    }


    private CurlVO isProxyUseAbcIp(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        try {
            String format1 = String.format("curl -x %s http://www.ip234.in/ip.json",ip);
            System.out.println(format1);
            List<String> strings = RuntimeUtil.execForLines(format1);

            boolean flag = false;

            List<String> newStr = new ArrayList<>();
            for (String string : strings) {
                if (string.contains("{") || flag) {
                    flag = true;
                    newStr.add(string);
                }
            }
            String resp = CollUtil.join(newStr, "");

            IPWorldRespCurl curlVO = JSONUtil.toBean(resp, IPWorldRespCurl.class);
            if (curlVO.getCountry().toLowerCase().equals(country.toLowerCase())) {
                return falseCurlVO.setProxyUse(true).setIp(curlVO.getIp()).setCountry(curlVO.getCountry());
            }
        }catch (Exception e) {

        }
        return falseCurlVO;
    }


    private String getIp2WorldIp(String regions) {
        //curl -x  ipinfo.io
        String format = String.format("chenweilong122-zone-resi-region-%s-session-%s-sessTime-5:ch1433471850@4a6974acaeab2113.us.ip2world.vip:6001",regions, RandomUtil.randomString(12));
        System.out.println(format);
        return format;
    }

    private CurlVO isProxyUseIp2World(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        try {
            String format1 = String.format("curl -x %s http://www.ip234.in/ip.json",ip);
            System.out.println(format1);
            List<String> strings = RuntimeUtil.execForLines(format1);

            boolean flag = false;

            List<String> newStr = new ArrayList<>();
            for (String string : strings) {
                if (string.contains("{") || flag) {
                    flag = true;
                    newStr.add(string);
                }
            }
            String resp = CollUtil.join(newStr, "");

            IPWorldRespCurl curlVO = JSONUtil.toBean(resp, IPWorldRespCurl.class);
            if (curlVO.getCountry().toLowerCase().equals(country.toLowerCase())) {
                return falseCurlVO.setProxyUse(true).setIp(curlVO.getIp()).setCountry(curlVO.getCountry());
            }
        }catch (Exception e) {

        }
        return falseCurlVO;
    }



    private String getLunaIp(String regions) {
        String format = String.format("user-lu9904136-region-%s-sessid-%s-sesstime-10:Ch1433471850@na.ej29ly49.lunaproxy.net:12233",regions, RandomUtil.randomString(18));
        System.out.println(format);
        return format;
    }

    private CurlVO isProxyUse(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        String format1 = String.format("curl -x %s myip.lunaproxy.io",ip);
        List<String> strings = RuntimeUtil.execForLines(format1);
        String s = strings.get(strings.size() - 1);
        String[] split = s.split("\\|");
        System.out.println(split.length);
        if (split.length == 5) {
            String outIp = split[0];
            String outCountry = split[2];

            if (outCountry.toLowerCase().equals(country.toLowerCase())) {
                return falseCurlVO.setProxyUse(true).setIp(outIp).setCountry(outCountry);
            }
        }

        return falseCurlVO;
    }

    private CurlVO isProxyUseMe(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        String format1 = String.format("curl -x %s 202.79.171.146:8080",ip);
        log.info("curl = {}",format1);
        List<String> strings = RuntimeUtil.execForLines(format1);
        log.info("curl resp = {}",JSONUtil.toJsonStr(strings));
        String outIp = strings.get(strings.size() - 1);
        boolean match = ReUtil.isMatch(IPV4, outIp);
        if (match) {
            log.info("ip = {} country = {} format = {}",ip,country,outIp);
            return falseCurlVO.setProxyUse(true).setIp(outIp).setCountry(country);
        }
        log.info("ip = {} country = {} format = {}",ip,country,"没有找到JSON数据");
        return falseCurlVO;
    }

    @Override
    public Integer deleteByTokenPhone(List<String> tokenPhoneList) {
        if (CollUtil.isEmpty(tokenPhoneList)) {
            return 0;
        }
        return baseMapper.delete(new QueryWrapper<CdLineIpProxyEntity>().lambda()
                .in(CdLineIpProxyEntity::getTokenPhone, tokenPhoneList));
    }

    @Override
    public Boolean clearTokenPhone(String tokenPhone, Integer countryCode) {
        if (StringUtils.isEmpty(tokenPhone)) {
            return false;
        }

        Object o2 = redisTemplate.opsForHash().get(RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)), tokenPhone);

        if (o2 == null) {
            return true;
        }

        String outIpv4 = String.valueOf(o2);
        if (StringUtils.isEmpty(outIpv4)) {
            return true;
        }
        redisTemplate.opsForValue().set(RedisKeys.RedisKeys4.getValue(outIpv4), tokenPhone, 1, TimeUnit.DAYS);
        return true;
    }





    @Override
    @Async
    public void cleanIpByCountryCode(Integer countryCode) {
        Set<Object> phoneSet = redisTemplate.opsForHash().keys(RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)));
        log.info("清理ip开始，国家:{}，共:{}条", countryCode, phoneSet.size());
        for (Object phone : phoneSet) {
            threadPoolTaskExecutor.execute(() -> {
                String outIpv4 = (String) redisTemplate.opsForHash().get(
                        RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)), phone);

                //删除手机号对应的出口ip
                redisTemplate.opsForHash().delete(RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)), phone);

                //获取手机号出口ip对应的s5
                if (StrUtil.isNotEmpty(outIpv4)) {
                    redisTemplate.opsForHash().delete(RedisKeys.RedisKeys1.getValue(), outIpv4);
                }

            });
        }
        log.info("清理ip结束，国家:{}，共:{}条", countryCode, phoneSet.size());
    }

    @Override
    @Async
    public void cleanInvalidIp(Long beforeMinute) {
        Set<String> keys = redisTemplate.keys(RedisKeys.RedisKeys4.getValue("*"));
        log.info("清理ip黑名单开始，剩余过期时间:{}，共:{}条", beforeMinute, keys.size());
        for (String key : keys) {
            threadPoolTaskExecutor.execute(() -> {
                Long expire = redisTemplate.getExpire(key, TimeUnit.MINUTES);
                if (expire != null) {
                    expire = (24* 60) - expire;
                    log.info("cleanInvalidIp key:{}, expire: {}", key, expire);
                    if (expire > beforeMinute ) {
                        redisTemplate.delete(key);
                    }
                }
            });
        }
        log.info("清理ip黑名单结束，剩余过期时间:{}，共:{}条", beforeMinute, keys.size());
    }

}
