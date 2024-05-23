package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.*;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.sun.xml.internal.ws.api.pipe.Fiber;
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
                        //如果没有静态 去取动态
                        String ipOld = getDyIp(regions,phoneNumberInfo);
                        CurlVO proxyUseOld = getProxyUse(ipOld, regions);
                        proxyUseOld.setS5Ip(ipOld);
                        ips.add(proxyUseOld);
                    }else {
                        CurlVO proxyUseOld = getProxyUse(s, regions);
                        proxyUseOld.setS5Ip(s);
                        ips.add(proxyUseOld);
                    }
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
        ExecutorService executorService = Executors.newFixedThreadPool(200);
        List<String> strings = FileUtil.readLines("/Users/chenweilong/Downloads/chenweilong1022@gmail.com-100010627711-日本 (2).txt", "UTF-8");
        ArrayList<String> objects = CollUtil.newArrayList();
        for (String string : strings) {
            String[] split = string.split(":");
            String format = String.format("%s:%s@%s", split[2], split[3], split[0]+":"+split[1]);
            objects.add(format);
        }
        Set<String> ips = ConcurrentHashMap.newKeySet();
        for (int i = 0; i < 5000; i++) {
            CdLineIpProxyServiceImpl cdLineIpProxyService = new CdLineIpProxyServiceImpl();
            int finalI = i;
            String jp = objects.get(finalI);
            CurlVO jp1 = cdLineIpProxyService.isProxyUseIp2World(jp, "jp");
            if (jp1.isProxyUse()) {
                ips.add(jp1.getIp());
                log.info("i = {} set size = {}",finalI,ips.size());
            }
//            executorService.submit(() -> {
////                String jp = cdLineIpProxyService.getIpwebIp("jp");
//                String jp = objects.get(finalI);
//                CurlVO jp1 = cdLineIpProxyService.isProxyUseIp2World(jp, "jp");
//                if (jp1.isProxyUse()) {
//                    ips.add(jp1.getIp());
//                    log.info("i = {} set size = {}",finalI,ips.size());
//                }
//            });
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



    private String getIpwebIp(String regions) {
        //curl -x  ipinfo.io
        String format = String.format("B_36811_%s___10_%s:ch1433@gate2.ipweb.cc:7778",regions.toUpperCase(),RandomStr.randomString(8));
        System.out.println(format);
        return format;
    }


    private CurlVO isProxyUseIpwebIp(String ip,String country) {
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




    private String getHyIp(String regions) {
        //curl -x  ipinfo.io
        String format = String.format("huyan_%s-country-%s:Cmn5ep@gate3.rola.vip:2139" , RandomUtil.randomInt(1,100000),regions);
        System.out.println(format);
        return format;
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
            String format1 = String.format("curl -x %s ipinfo.io",ip);
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
            String format1 = String.format("curl -x %s ipinfo.io",ip);
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
            if (JSONUtil.isJson(resp)) {
                return falseCurlVO;
            }
            IPWorldRespCurl curlVO = JSONUtil.toBean(resp, IPWorldRespCurl.class);
            if (ObjectUtil.isNull(curlVO)) {
                return falseCurlVO;
            }
            if (curlVO.getCountry().toLowerCase().equals(country.toLowerCase())) {
                return falseCurlVO.setProxyUse(true).setIp(curlVO.getIp()).setCountry(curlVO.getCountry());
            }
        }catch (Exception e) {
            log.info("e = {}",e);
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
            String format1 = String.format("curl -x %s ipinfo.io",ip);
            System.out.println(format1);
            List<String> strings = RuntimeUtil.execForLines(format1);
            System.out.println(strings);
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
