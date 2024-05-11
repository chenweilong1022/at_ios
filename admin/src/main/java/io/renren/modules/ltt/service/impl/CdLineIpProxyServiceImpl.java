package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.utils.*;
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

//        public static void main(String[] args) throws IOException {
//
//            String format1 = String.format("curl -x %s -U user-lu9904136:Ch1433471850 myip.lunaproxy.io","43.159.18.174:20584");
//            List<String> strings = RuntimeUtil.execForLines(format1);
//            String s = strings.get(strings.size() - 1);
//            String[] split = s.split("\\|");
//            System.out.println(split.length);
//            if (split.length == 5) {
//                String ip = split[0];
//                String country = split[2];
//                System.out.println(ip);
//                System.out.println(country);
//            }
//
//        }

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
        //获取缓存
        ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
        if (ObjectUtil.isNull(projectWorkEntity)) {
            return null;
        }
        //获取ip代理的国家
        PhoneCountryVO phoneNumberInfo = null;
        try {
            phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(cdLineIpProxyDTO.getTokenPhone());
        } catch (Exception e) {
            return null;
        }
        Long countryCode = phoneNumberInfo.getCountryCode();
        if (ObjectUtil.isNotNull(cdLineIpProxyDTO.getCountryCode())) {
            countryCode = cdLineIpProxyDTO.getCountryCode();
        }

        Integer proxy = ObjectUtil.isNotNull(cdLineIpProxyDTO.getSelectProxyStatus()) ?
                cdLineIpProxyDTO.getSelectProxyStatus() : projectWorkEntity.getProxy();
//        String keyByResource1 = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource3, countryCode.intValue());
//        Lock lock1 = lockMap.computeIfAbsent(keyByResource1, k -> new ReentrantLock());
//        countryCode = 1L;
        String ip = getIp(cdLineIpProxyDTO, countryCode, proxy);
//        if (StrUtil.isEmpty(ip)) {
//            ip = getIp(cdLineIpProxyDTO,82L);
//            if (StrUtil.isEmpty(ip)) {
//                ip = getIp(cdLineIpProxyDTO,1L);
//            }
//        }
        log.info("phone = {} countryCode = {}获取到的ip {}",cdLineIpProxyDTO.getTokenPhone(),countryCode,ip);
        return ip;
    }

    private String getIp(CdLineIpProxyDTO cdLineIpProxyDTO, Long countryCode, Integer proxy) {

        String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource3, cdLineIpProxyDTO.getTokenPhone());
        Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
        boolean triedLock = lock.tryLock();
        log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
        if(triedLock) {
            try{
                String regions = EnumUtil.queryValueByKey(countryCode.intValue(), CountryCode.values());
                //出口ip
                String outIpv4 = (String) redisTemplate.opsForHash().get(RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)), cdLineIpProxyDTO.getTokenPhone());
                String ipS5 = null;
                if (StrUtil.isNotEmpty(outIpv4)) {
                    ipS5 = (String) redisTemplate.opsForHash().get(RedisKeys.RedisKeys1.getValue(), outIpv4);
                }
                //如果有直接返回
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
                        } else {
                            if (proxy == 3) {
                                //静态代理时，无法获取出口ip，人工处理，不重复取新的ip
                                log.error("getProxyIp_error 静态ip异常 {},", proxyUse);
                                return null;
                            }
                        }
                    } else {
                        if (proxy == 3) {
                            //静态代理时，无法获取出口ip，人工处理，不重复取新的ip
                            log.error("getProxyIp_error 静态ip异常 {}", proxyUse);
                            return null;
                        }
                    }

                } else {
                    int i = 0;
                    int len = 50;
                    while (i < len) {
                        i++;
                        String ip = null;
                        String ipKey = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource14, regions);
                        Lock ipLock = lockMap.computeIfAbsent(ipKey, k -> new ReentrantLock());
                        boolean ipLockFlag = ipLock.tryLock();
                        log.info("keyByResource = {} 获取的锁为 = {}", keyByResource, ipLockFlag);
                        if (ipLockFlag) {
                            try {
                                Queue<String> getflowip = caffeineCacheListString.getIfPresent(regions);
                                if (CollUtil.isEmpty(getflowip)) {
                                    //获取ip
                                    getflowip = getIpResp(regions, proxy);
                                }
                                if (CollUtil.isNotEmpty(getflowip)) {
                                    ip = getflowip.poll();
                                    caffeineCacheListString.put(regions, getflowip);
                                }
                            } finally {
                                try {
                                    ipLock.unlock();
                                } catch (Exception e) {
                                    log.error("lock = {}", "没有上锁");
                                }
                            }
                        } else {
                            return null;
                        }
                        if (StringUtils.isEmpty(ip)) {
                            continue;
                        }

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
                            } else {
                                if (proxy == 3) {
                                    //静态代理时，无法获取出口ip，人工处理，不重复取新的ip
                                    log.error("getProxyIp_error 静态ip异常 {}, {}", ip, proxyUse);
                                    return null;
                                }
                            }
                        } else {
                            if (proxy == 3) {
                                //静态代理时，无法获取出口ip，人工处理，不重复取新的ip
                                log.error("getProxyIp_error 静态ip异常 {}, {}", ip, proxyUse);
                                return null;
                            }
                        }
                    }
                }
            } finally {
                try {
                    lock.unlock();
                } catch (Exception e) {
                    log.error("lock = {}", "没有上锁");
                }
            }
        } else {
            log.info("keyByResource = {} 在执行",keyByResource);
        }
        return null;
    }

    private Queue<String> getIpResp(String regions, Integer proxy) {
        if (ObjectUtil.isNull(proxy)) {
            log.error("getIpResp_error_proxy_null");
            return null;
        }
        String ipResp = null;
        if (proxy == 1) {
            //lunaproxy
            ipResp = getLunaIpResp(regions);
        } else if (proxy == 2) {
            //ip2world
            ipResp = getIp2World(regions);
        } else if (proxy == 3) {
            //静态代理
            ipResp = getStaticIpResp(regions);
        }
        if (StringUtils.isEmpty(ipResp)) {
            log.error("getIpResp_error_proxy_null");
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


    private static String getLunaIpResp(String regions) {
        String getPhoneHttp = String.format("https://tq.lunaproxy.com/getflowip?neek=1136881&num=500&type=1&sep=1&regions=%s&ip_si=1&level=1&sb=", regions);
        String resp = HttpUtil.get(getPhoneHttp);
        log.info("getLunaIpResp resp = {}",resp);
        if (JSONUtil.isJson(resp)) {
            return null;
        }
        return resp;
    }

    private static String getIp2World(String regions) {
        String getPhoneHttp = String.format("http://api.proxy.ip2world.com/getProxyIp?return_type=txt&protocol=http&num=500&regions=%s&lb=1", regions);
        String resp = HttpUtil.get(getPhoneHttp);
        log.info("getIp2World resp = {}",resp);
        if (JSONUtil.isJson(resp)) {
            return null;
        }
        return resp;
    }


//    public static void main(String[] args) {
//
//        boolean match = ReUtil.isMatch(IPV4, "1.46.11.249");
//        System.out.println(match);
//
//
//    }



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
        if (proxy == 1) {
            //lunaproxy
           return isProxyUse(ip, country);
        } else if (proxy == 2) {
            //ip2world
            return isProxyUseMe(ip, country);
        } else if (proxy == 3) {
            //静态代理
            return isProxyUseMe(ip, country);
        }
        log.error("selectProxyUse_error_proxy {}", proxy);
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

    public static void main(String[] args) {
        //45.195.152.211	2000/2333	song062	1612132sd
        String ip ="song062:1612132sd@45.195.152.211:2333";
        String country = "th";
        String format1 = String.format("curl -x socks5://43.159.29.119:20120 202.79.171.146:8080",ip);

        System.out.println(format1);
        System.out.println("************");

        List<String> strings = RuntimeUtil.execForLines(format1);
        String outIp = strings.get(strings.size() - 1);

        System.out.println(outIp);
        System.out.println("************");

        boolean match = ReUtil.isMatch(IPV4, outIp);
        System.out.println(match);
        if (match) {
            log.info("ip = {} country = {} format = {}",ip,country,outIp);
        } else {
            log.info("----------");
        }
    }

    private CurlVO isProxyUseMe(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        // 尝试获取许可，不阻塞
        boolean permitAcquired = semaphore.tryAcquire();
        if (permitAcquired) {
            try {
                String format1 = String.format("curl -x %s 202.79.171.146:8080",ip);
                List<String> strings = RuntimeUtil.execForLines(format1);
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

//    public static void main(String[] args) {
//        String text = "这是一段文本，其中包含JSON数据: {\"name\": \"张三\", \"age\": 30, \"city\": \"北京\"}。后面还有更多文本。";
//
//        // 定义一个正则表达式来查找JSON对象
//        // 这个正则表达式假设JSON数据简单且不含嵌套结构
//        String jsonPattern = "\\{[^\\{\\}]*\\}";
//        Pattern pattern = Pattern.compile(jsonPattern);
//        Matcher matcher = pattern.matcher(text);
//
//        if (matcher.find()) {
//            String jsonStr = matcher.group(0);
//            System.out.println("找到的JSON数据: " + jsonStr);
//        } else {
//            System.out.println("没有找到JSON数据");
//        }
//    }

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
            String outIpv4 = (String) redisTemplate.opsForHash().get(
                    RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)), phone);

            //删除手机号对应的出口ip
            redisTemplate.opsForHash().delete(RedisKeys.RedisKeys2.getValue(String.valueOf(countryCode)), phone);

            //获取手机号出口ip对应的s5
            if (StrUtil.isNotEmpty(outIpv4)) {
                redisTemplate.opsForHash().delete(RedisKeys.RedisKeys1.getValue(), outIpv4);
            }
        }
        log.info("清理ip结束，国家:{}，共:{}条", countryCode, phoneSet.size());
    }

    @Override
    @Async
    public void cleanInvalidIp(Long expireHours) {
        Set<String> keys = redisTemplate.keys(RedisKeys.RedisKeys4.getValue("*"));
        log.info("清理ip黑名单开始，剩余过期时间:{}，共:{}条", expireHours, keys.size());
        for (String key : keys) {
            Long expire = redisTemplate.getExpire(key, TimeUnit.HOURS);
            //删除这个范围的数据
            if (expireHours >= expire) {
                System.out.println(key+"---"+expire);
                redisTemplate.delete(key);
            }
        }
        log.info("清理ip黑名单结束，剩余过期时间:{}，共:{}条", expireHours, keys.size());
    }

}
