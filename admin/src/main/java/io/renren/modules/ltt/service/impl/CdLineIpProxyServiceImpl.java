package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
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
import io.renren.modules.ltt.enums.IpTypeEnum;
import io.renren.modules.ltt.enums.LockMapKeyResource;
import io.renren.modules.ltt.service.CdIpConfigService;
import io.renren.modules.ltt.service.CdLineIpProxyService;
import io.renren.modules.ltt.vo.CdLineIpProxyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
        return ip;
    }

    private String getIp(CdLineIpProxyDTO cdLineIpProxyDTO, Long countryCode, Integer proxy) {
        String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource3, cdLineIpProxyDTO.getTokenPhone());
        if (proxy == 3) {
            keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource3, String.valueOf(countryCode));
        }
        Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
        boolean triedLock = lock.tryLock();
        log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
        if(triedLock) {
            try{
                String regions = EnumUtil.queryValueByKey(countryCode.intValue(), CountryCode.values());
                if (!cdLineIpProxyDTO.isNewIp()) {
                    //查询是否已经有这个国家的ip了
                    //todo 先查静态。如果配置的是：静态代理-》给一个静态ip。
                    //先判断是否有静态ip
                    CdLineIpProxyEntity one = this.getOne(new QueryWrapper<CdLineIpProxyEntity>().lambda()
                                    .eq(CdLineIpProxyEntity::getTokenPhone, cdLineIpProxyDTO.getTokenPhone())
                                    .eq(CdLineIpProxyEntity::getLzCountry,String.valueOf(countryCode))
                                    .eq(CdLineIpProxyEntity::getIpType, IpTypeEnum.IpType2.getKey())
                                    .orderByDesc(CdLineIpProxyEntity::getId)
                                    .last("limit 1"));
                    if(one == null) {
                        one = this.getOne(new QueryWrapper<CdLineIpProxyEntity>().lambda()
                                .eq(CdLineIpProxyEntity::getTokenPhone, cdLineIpProxyDTO.getTokenPhone())
                                .eq(CdLineIpProxyEntity::getLzCountry,String.valueOf(countryCode))
                                .orderByDesc(CdLineIpProxyEntity::getId)
                                .last("limit 1"));
                    }

                    //如果有直接返回
                    if (ObjectUtil.isNotNull(one)) {
                        String ip = one.getIp();
                        CurlVO proxyUse = getProxyUse(ip, regions, proxy);
                        if (proxyUse.isProxyUse()) {
                            //如果ip相同并且国家一样
                            if (proxyUse.getIp().equals(one.getOutIpv4()) && regions.toLowerCase().equals(proxyUse.getCountry().toLowerCase())) {
                                return socks5Pre(ip);
                                //如果ip不相同相同并且国家一样
                            }else if (regions.toLowerCase().equals(proxyUse.getCountry().toLowerCase())) {
                                CdLineIpProxyEntity save = new CdLineIpProxyEntity();
                                save.setIp(ip);
                                save.setTokenPhone(cdLineIpProxyDTO.getTokenPhone());
                                save.setLzCountry(String.valueOf(countryCode));
                                save.setOutIpv4(proxyUse.getIp());
                                save.setCountry(proxyUse.getCountry());
                                save.setCreateTime(DateUtil.date());
                                save.setIpType(proxy == 3 ? IpTypeEnum.IpType2.getKey() : IpTypeEnum.IpType1.getKey());
                                try {
                                    this.save(save);
                                    return socks5Pre(ip);
                                }catch (Exception e) {
                                    log.info("e = {}",e.getMessage());
                                    log.info("e = {}",e.getLocalizedMessage());
                                    log.info("e = {}",e.toString());
                                }

                            } else {
                                if (proxy == 3) {
                                    //静态代理时，无法获取出口ip，人工处理，不重复取新的ip
                                    log.error("getProxyIp_error 静态ip异常 {}, {}", one, proxyUse);
                                    return null;
                                }
                            }
                        } else {
                            if (proxy == 3) {
                                //静态代理时，无法获取出口ip，人工处理，不重复取新的ip
                                log.error("getProxyIp_error 静态ip异常 {}, {}", one, proxyUse);
                                return null;
                            }
                        }
                    }
                }

                boolean flag = true;
                int i = 0;
                while(i < 10) {
                    i++;
                    String ip = null;
                    Queue<String> getflowip = caffeineCacheListString.getIfPresent(cdLineIpProxyDTO.getTokenPhone());
                    if (CollUtil.isEmpty(getflowip)) {
                        String resp = getIpResp(regions, proxy);
                        if (resp == null) return null;
                        String[] split = resp.split("\r\n");
                        Queue<String> getflowipNew = new LinkedList<>();
                        for (String s : split) {
                            getflowipNew.offer(s);
                        }
                        ip = getflowipNew.poll();
                        caffeineCacheListString.put(cdLineIpProxyDTO.getTokenPhone(),getflowipNew);
                    }else {
                        ip = getflowip.poll();
                        caffeineCacheListString.put(cdLineIpProxyDTO.getTokenPhone(),getflowip);
                    }

                    CurlVO proxyUse = getProxyUse(ip, regions, proxy);
                    if (proxyUse.isProxyUse()) {
                        //如果国家一样
                        if (regions.toLowerCase().equals(proxyUse.getCountry().toLowerCase())) {
                            CdLineIpProxyEntity save = new CdLineIpProxyEntity();
                            save.setIp(ip);
                            save.setTokenPhone(cdLineIpProxyDTO.getTokenPhone());
                            save.setLzCountry(String.valueOf(countryCode));
                            save.setOutIpv4(proxyUse.getIp());
                            save.setCountry(proxyUse.getCountry());
                            save.setCreateTime(DateUtil.date());
                            save.setIpType(proxy == 3 ? IpTypeEnum.IpType2.getKey() : IpTypeEnum.IpType1.getKey());
                            try {
                                this.save(save);
                                flag = false;
                            }catch (Exception e) {
                                continue;
                            }
                            return socks5Pre(ip);
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
            }finally {
                try {
                    lock.unlock();
                }catch (Exception e) {
                    log.error("lock = {}","没有上锁");
                }
            }
        }else {
            log.info("keyByResource = {} 在执行",keyByResource);
        }
        return null;
    }

    private String getIpResp(String regions, Integer proxy) {
        if (ObjectUtil.isNull(proxy)) {
            log.error("getIpResp_error_proxy_null");
            return null;
        }
        if (proxy == 1) {
            //lunaproxy
            return getLunaIpResp(regions);
        } else if (proxy == 2) {
            //ip2world
            return getIp2World(regions);
        } else if (proxy == 3) {
            //静态代理
            return getStaticIpResp(regions);
        }
        log.error("getIpResp_error_proxy {}", proxy);
        return null;
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
        if (JSONUtil.isJson(resp)) {
            return null;
        }
        return resp;
    }

    private static String getIp2World(String regions) {
        String getPhoneHttp = String.format("http://api.proxy.ip2world.com/getProxyIp?return_type=txt&protocol=http&num=500&regions=%s&lb=1", regions);
        String resp = HttpUtil.get(getPhoneHttp);
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
           return isProxyUseMe(ip, country);
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
    private CurlVO isProxyUse(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
        try {//curl -x 43.152.113.218:13923 202.79.171.146:8080
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
        }catch (Exception e) {

        }
        return falseCurlVO;
    }

    public static void main(String[] args) {
        //45.195.152.211	2000/2333	song062	1612132sd
        String ip ="song062:1612132sd@45.195.152.211:2333";
        String country = "th";
        String format1 = String.format("curl -x %s 202.79.171.146:8080",ip);

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
        }catch (Exception e) {

        }
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

    @Override
    public Integer deleteByTokenPhone(List<String> tokenPhoneList) {
        if (CollUtil.isEmpty(tokenPhoneList)) {
            return 0;
        }
        return baseMapper.delete(new QueryWrapper<CdLineIpProxyEntity>().lambda()
                .in(CdLineIpProxyEntity::getTokenPhone, tokenPhoneList));
    }

}
