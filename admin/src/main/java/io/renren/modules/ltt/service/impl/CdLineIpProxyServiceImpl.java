package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RuntimeUtil;
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
import io.renren.modules.ltt.conver.CdLineIpProxyConver;
import io.renren.modules.ltt.dao.CdLineIpProxyDao;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.CdLineIpProxyEntity;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.LockMapKeyResource;
import io.renren.modules.ltt.service.CdLineIpProxyService;
import io.renren.modules.ltt.vo.CdLineIpProxyVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


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
        try {
            //获取缓存
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            if (ObjectUtil.isNull(projectWorkEntity)) {
                return null;
            }
            //获取ip代理的国家
            PhoneCountryVO phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(cdLineIpProxyDTO.getTokenPhone());
            Long countryCode = phoneNumberInfo.getCountryCode();
            String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource3, cdLineIpProxyDTO.getTokenPhone());
            Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
            boolean triedLock = lock.tryLock();
            log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
            if(triedLock) {
                try{
                    String regions = EnumUtil.queryValueByKey(countryCode.intValue(), CountryCode.values());
                    if (!cdLineIpProxyDTO.isNewIp()) {
                        //查询是否已经有这个国家的ip了
                        CdLineIpProxyEntity one = this.getOne(new QueryWrapper<CdLineIpProxyEntity>().lambda()
                                .eq(CdLineIpProxyEntity::getTokenPhone,cdLineIpProxyDTO.getTokenPhone())
                                .eq(CdLineIpProxyEntity::getLzCountry,String.valueOf(countryCode))
                                .orderByDesc(CdLineIpProxyEntity::getId)
                                .last("limit 1")
                        );
                        //如果有直接返回
                        if (ObjectUtil.isNotNull(one)) {
                            String ip = one.getIp();
                            CurlVO proxyUse = isProxyUse(ip, regions);
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
                                    try {
                                        this.save(save);
                                        return socks5Pre(ip);
                                    }catch (Exception e) {
                                        log.info("e = {}",e.getMessage());
                                        log.info("e = {}",e.getLocalizedMessage());
                                        log.info("e = {}",e.toString());
                                    }

                                }
                            }
                        }
                    }

                    String ip = null;
                    Queue<String> getflowip = caffeineCacheListString.getIfPresent(regions);

                    String keyByResource1 = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource3, countryCode.intValue());
                    Lock lock1 = lockMap.computeIfAbsent(keyByResource1, k -> new ReentrantLock());
                    boolean triedLock2 = lock1.tryLock();
                    log.info("keyByResource = {} 获取的锁为 = {}",keyByResource1,triedLock2);
                    try {
                        if(triedLock2) {
                            if (CollUtil.isEmpty(getflowip)) {
                                String getPhoneHttp = String.format("https://tq.lunaproxy.com/getflowip?neek=1136881&num=500&type=1&sep=1&regions=%s&ip_si=1&level=1&sb=",regions);
                                String resp = HttpUtil.get(getPhoneHttp);
                                if (JSONUtil.isJson(resp)) {
                                    return null;
                                }
                                String[] split = resp.split("\r\n");
                                Queue<String> getflowipNew = new LinkedList<>();
                                for (String s : split) {
                                    getflowipNew.offer(s);
                                }
                                ip = getflowipNew.poll();
                                caffeineCacheListString.put(regions,getflowipNew);
                            }else {
                                ip = getflowip.poll();
                                caffeineCacheListString.put(regions,getflowip);
                            }
                        }else {
                            return null;
                        }
                    } catch (Exception e) {
                        log.error("e = {}",e.getMessage());
                        log.error("e = {}",e.getLocalizedMessage());
                        log.error("e = {}",e.toString());
                    }finally {
                        lock1.unlock();
                    }


                    CurlVO proxyUse = isProxyUse(ip, regions);
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
                            this.save(save);
                            return socks5Pre(ip);
                        }
                        return null;
                    }
                }finally {
                    lock.unlock();
                }
            }else {
                log.info("keyByResource = {} 在执行",keyByResource);
            }
        } catch (Exception e) {
            log.error("e = {}",e.getMessage());
            log.error("e = {}",e.getLocalizedMessage());
            log.error("e = {}",e.toString());
        }
        return null;
    }

    private static String socks5Pre(String ip) {
        if (ip.contains("socks5://")) {
            return ip;
        }
        return String.format("socks5://%s", ip);
    }

    //43.159.18.174:24496 curl --socks5 43.159.18.174:24496 ipinfo.io
    private CurlVO isProxyUse(String ip,String country) {
        CurlVO falseCurlVO = new CurlVO().setProxyUse(false);
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
}
