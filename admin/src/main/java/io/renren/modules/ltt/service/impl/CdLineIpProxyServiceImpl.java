package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.*;
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
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;
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
        }
        String ip = getIp(cdLineIpProxyDTO, countryCode, proxy,phoneNumberInfo);
        log.info("phone = {} countryCode = {}获取到的ip {}",cdLineIpProxyDTO.getTokenPhone(),countryCode,ip);
        return ip;
    }

    private String getIp(CdLineIpProxyDTO cdLineIpProxyDTO, Long countryCode, Integer proxy,PhoneCountryVO phoneNumberInfo) {
        try{
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
                CurlVO proxyUse = getProxyUse(ipS5, regions, proxy);
                if (proxyUse.isProxyUse()) {
                    //判断ip黑名单缓存中是否有
                    String value = RedisKeys.RedisKeys4.getValue(proxyUse.getIp());
                    String ipCache = redisTemplate.opsForValue().get(value);
                    if (ipCache != null) {
                        redisTemplate.opsForHash().delete(RedisKeys.RedisKeys1.getValue(), outIpv4);
                        return null;
                    }
                    //如果ip相同并且国家一样
                    if (proxyUse.getIp().equals(outIpv4) && regions.toLowerCase().equals(proxyUse.getCountry().toLowerCase())) {
                        return socks5Pre(ipS5);
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
                            return socks5Pre(ipS5);
                        }else {
                            redisTemplate.opsForHash().delete(RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)), cdLineIpProxyDTO.getTokenPhone());
                        }
                    }
                }
            } else {
                //从redis取出50条ip 根据国家获取
                Boolean b1 = redisTemplate.opsForValue().setIfAbsent(RedisKeys.RedisKeys6.getValue(regions), cdLineIpProxyDTO.getTokenPhone());
                Queue<String> getflowip = new LinkedList<>();
                if (b1) {
                    try {
                        //如果是静态ip
                        if (ProxyStatus.ProxyStatus3.getKey().equals(proxy)) {
                            String s = redisTemplate.opsForList().rightPop(RedisKeys.RedisKeys9.getValue(regions));
                            if (s == null) {
                                return null;
                            }
                            getflowip.add(s);
                        }else {
                            for (int i1 = 0; i1 < 50; i1++) {
                                String s = redisTemplate.opsForList().rightPop(RedisKeys.RedisKeys8.getValue(regions));
                                if (s == null) {
                                    break;
                                }
                                getflowip.add(s);
                            }
                        }
                    }catch (Exception e) {
                        log.error("rightPop = {}",e.getMessage());
                    }
                }
                //释放对列
                redisTemplate.delete(regions);
                if (CollUtil.isEmpty(getflowip)) {
                    return null;
                }
                //循环获取ip
                for (String ip : getflowip) {
                    CurlVO proxyUse = getProxyUse(ip, regions, proxy);
                    if (proxyUse.isProxyUse()) {
                        //判断ip黑名单缓存中是否有
                        String ipCache = redisTemplate.opsForValue().get(RedisKeys.RedisKeys4.getValue(proxyUse.getIp()));
                        if (ipCache != null) {
                            continue;
                        }

                        //如果国家一样
                        if (regions.toLowerCase().equals(proxyUse.getCountry().toLowerCase())) {
                            Boolean b = redisTemplate.opsForHash().putIfAbsent(RedisKeys.RedisKeys1.getValue(), proxyUse.getIp(), ip);
                            if (b) {
                                if (StrUtil.isNotEmpty(outIpv4)) {
                                    if (!outIpv4.equals(proxyUse.getIp())) {
                                        redisTemplate.opsForValue().set(RedisKeys.RedisKeys4.getValue(outIpv4), cdLineIpProxyDTO.getTokenPhone(), 1, TimeUnit.DAYS);
                                        redisTemplate.opsForHash().delete(RedisKeys.RedisKeys1.getValue(), outIpv4);
                                    }
                                }
                                redisTemplate.opsForHash().put(RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)), cdLineIpProxyDTO.getTokenPhone(), proxyUse.getIp());
                                return socks5Pre(ip);
                            }
                        }
                    }
                }
            }
        }catch (Exception e) {
            log.error("cdLineIpProxyService = {}",e.getMessage());
        }
        return null;
    }

    private Queue<String> getIpResp(String regions, Integer proxy,PhoneCountryVO phoneNumberInfo) {
        if (ObjectUtil.isNull(proxy)) {
            log.error("getIpResp_error_proxy_null");
            return null;
        }
        String ipResp = null;

        //whiteip
        //137.184.112.207
        //137.184.112.206
        //202.79.171.146
        //143.92.40.151
        //216.83.53.90
        //113.21.242.163
        String number = phoneNumberInfo.getNumber();
        int lastDigit = Character.getNumericValue(number.charAt(number.length() - 1));
        int mod = lastDigit % 4;
        List<String> urls = CollUtil.newArrayList(
                "https://tq.lunaproxy.com/getflowip?neek=1136881&num=500&type=1&sep=1&regions=%s&ip_si=1&level=1&sb=",//luna
                "http://api.proxy.ip2world.com/getProxyIp?return_type=txt&protocol=http&num=500&regions=%s&lb=1",//ip2world
                "https://info.proxy.ipmars.com/extractProxyIp?regions=%s&num=500&protocol=http&return_type=txt&lh=1&st=",//ipmars
                "https://info.proxy.abcproxy.com/extractProxyIp?regions=%s&num=500&protocol=http&return_type=txt&lh=1&mode=1"//abcproxy
        );
        String url = urls.get(mod);
        ipResp = getRandomIp(url, regions);
//        if (proxy == 1) {
//            //lunaproxy
//            ipResp = getLunaIpResp(regions);
//        } else if (proxy == 2) {
//            //ip2world
//            ipResp = getIp2World(regions);
//        } else if (proxy == 3) {
//            //静态代理
//            ipResp = getStaticIpResp(regions);
//        }
        if (StringUtils.isEmpty(ipResp)) {
            log.info("getIpResp_error_proxy_null = {}",url);
            return null;
        }
        String[] split = ipResp.split("\r\n");
        Queue<String> getflowipNew = new LinkedList<>();
        for (String s : split) {
            s = s.trim();
            if (StrUtil.isEmpty(s)) {
                continue;
            }
            getflowipNew.offer(s);
        }
        caffeineCacheListString.put(regions, getflowipNew);

        return getflowipNew;
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



    private static String socks5Pre(String ip) {
        if (ip.contains("socks5://")) {
            return ip;
        }
        return String.format("socks5://%s", ip);
    }

    //43.159.18.174:24496 curl --socks5 43.159.18.174:24496 ipinfo.io
    private CurlVO getProxyUse(String ip,String country, Integer proxy) {
        if (ObjectUtil.isNull(proxy)) {
            log.error("selectProxyUse_error_proxy_null");
            return null;
        }

//        if (proxy == 1) {
//            //lunaproxy
//           return isProxyUse(ip, country);
//        } else if (proxy == 2) {
//            //ip2world
//            return
//        } else if (proxy == 3) {
//            //静态代理
//            return isProxyUseMe(ip, country);
//        }

        // 尝试获取许可，不阻塞
        boolean permitAcquired = semaphore.tryAcquire();
        if (permitAcquired) {
            try {
                log.error("selectProxyUse_error_proxy {}", proxy);
                CurlVO proxyUseMe = isProxyUseMe(ip, country);
                if (proxyUseMe.isProxyUse()) {
                    return proxyUseMe;
                }
                proxyUseMe = isProxyUseMeIpecho(ip, country);
                if (proxyUseMe.isProxyUse()) {
                    return proxyUseMe;
                }
                proxyUseMe = isProxyUse(ip, country);
                if (proxyUseMe.isProxyUse()) {
                    return proxyUseMe;
                }
                return isProxyUseIp2World(ip, country);
            }catch (Exception e){
                log.info("ip = {} country = {} format = {} err = {}",ip,country,e.getMessage());
            }finally {
                // 释放许可
                semaphore.release();
            }
        }
        return null;
    }

    private static final Semaphore semaphore = new Semaphore(200);
    private CurlVO isProxyUse(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        // 尝试获取许可，不阻塞
        boolean permitAcquired = semaphore.tryAcquire();
        if (permitAcquired) {
            try {
                String format1 = String.format("curl -x %s -U user-lu9904136:Ch1433471850 myip.lunaproxy.io",ip);
                List<String> strings = RuntimeUtil.execForLines(format1);
                String s = strings.get(strings.size() - 1);
                String[] split = s.split("\\|");
                System.out.println(split.length);
                if (split.length == 5) {
                    String outIp = split[0];
                    String outCountry = split[2];
                    log.info("ip = {} country = {} format = {}",ip,country,s);
                    return falseCurlVO.setProxyUse(true).setIp(outIp).setCountry(outCountry);
                }
                log.info("ip = {} country = {} format = {}",ip,country,"没有找到JSON数据");
                return falseCurlVO;
            }catch (Exception e){
                log.info("ip = {} country = {} format = {} err = {}",ip,country,e.getMessage());
            }finally {
                // 释放许可
                semaphore.release();
            }
        }
        log.info("ip = {} country = {} format = {} 许可 = {}",ip,country,falseCurlVO.isProxyUse());
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

    public static void main(String[] args) {
        CurlVO proxyUseIp2World = new CdLineIpProxyServiceImpl().isProxyUseIp2World("43.152.113.218:10838", "81");
        System.out.println(proxyUseIp2World);
    }


    private CurlVO isProxyUseMeIpecho(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        // 尝试获取许可，不阻塞
        boolean permitAcquired = semaphore.tryAcquire();
        if (permitAcquired) {
            try {
                String format1 = String.format("curl -x %s https://ipecho.net/plain",ip);
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
            }catch (Exception e){
                log.info("ip = {} country = {} format = {} err = {}",ip,country,e.getMessage());
            }finally {
                // 释放许可
                semaphore.release();
            }
        }
        log.info("ip = {} country = {} format = {} 许可 = {}",ip,country,falseCurlVO.isProxyUse());
        return falseCurlVO;
    }

    private CurlVO isProxyUseIp2World(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        try {
            String format1 = String.format("curl -x socks5://%s ipinfo.io?token=6061ac44cda439",ip);
            log.info("format1 = {}",format1);
            List<String> strings = RuntimeUtil.execForLines(format1);
            log.info("curl resp = {}",CollUtil.join(strings,""));
            boolean flag = false;

            List<String> newStr = new ArrayList<>();
            for (String string : strings) {
                if (string.contains("{") || flag) {
                    flag = true;
                    newStr.add(string);
                }
            }

            String resp = CollUtil.join(newStr, "");
            log.info("ip = {} country = {} format = {}",ip,country,resp);
            IPWorldRespCurl curlVO = JSONUtil.toBean(resp, IPWorldRespCurl.class);
            return falseCurlVO.setProxyUse(true).setIp(curlVO.getIp()).setCountry(curlVO.getCountry());
        }catch (Exception e) {

        }
        return falseCurlVO;
    }


    @EventListener
    @Order(value = 9999)//t35323ha-1027-61697		tha-1027-44108
    public void handlerApplicationReadyEvent(ApplicationReadyEvent event) {
        Set<String> keys = redisTemplate.keys(RedisKeys.RedisKeys2.getValue("*"));
        if (CollUtil.isNotEmpty(keys)) {
            return;
        }
        List<CdLineIpProxyEntity> list = this.list(new QueryWrapper<CdLineIpProxyEntity>().lambda().orderByDesc(CdLineIpProxyEntity::getId));
        //ip关联sockets
        Map<String, List<CdLineIpProxyEntity>> stringListMap = list.stream().collect(Collectors.groupingBy(CdLineIpProxyEntity::getLzCountry));
        Map<String, String> getOutIpv4IpMap = new HashMap<>();
        for (String s : stringListMap.keySet()) {
            //根据国家分配缓存
            List<CdLineIpProxyEntity> cdLineIpProxyEntities = stringListMap.get(s);
            Map<String, CdLineIpProxyEntity> tokenPhoneCdLineIpProxyEntityMap = cdLineIpProxyEntities.stream().filter(item -> StrUtil.isNotEmpty(item.getTokenPhone())).collect(Collectors.toMap(CdLineIpProxyEntity::getTokenPhone, item -> item, (a, b) -> a));
            Map<String, String> tokenPhoneIpV4Map = new HashMap<>();
            for (String string : tokenPhoneCdLineIpProxyEntityMap.keySet()) {
                CdLineIpProxyEntity cdLineIpProxyEntity = tokenPhoneCdLineIpProxyEntityMap.get(string);
                tokenPhoneIpV4Map.put(string,cdLineIpProxyEntity.getOutIpv4());
                getOutIpv4IpMap.put(cdLineIpProxyEntity.getOutIpv4(),cdLineIpProxyEntity.getIp());
            }
            redisTemplate.opsForHash().putAll(RedisKeys.RedisKeys2.getValue(s),tokenPhoneIpV4Map);
        }
        redisTemplate.opsForHash().putAll(RedisKeys.RedisKeys1.getValue(),getOutIpv4IpMap);
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
    public void cleanInvalidIp(Long expireHours) {
        Set<String> keys = redisTemplate.keys(RedisKeys.RedisKeys4.getValue("*"));
        log.info("清理ip黑名单开始，剩余过期时间:{}，共:{}条", expireHours, keys.size());
        for (String key : keys) {
            threadPoolTaskExecutor.execute(() -> {
                Long expire = redisTemplate.getExpire(key, TimeUnit.HOURS);
                //删除这个范围的数据
                System.out.println(key + "---" + expire);
                if (expireHours >= expire) {
                    redisTemplate.delete(key);
                }
            });
        }
        log.info("清理ip黑名单结束，剩余过期时间:{}，共:{}条", expireHours, keys.size());
    }

}
