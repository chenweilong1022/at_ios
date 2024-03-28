package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.utils.*;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.LockMapKeyResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.modules.ltt.dao.CdLineIpProxyDao;
import io.renren.modules.ltt.entity.CdLineIpProxyEntity;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.vo.CdLineIpProxyVO;
import io.renren.modules.ltt.service.CdLineIpProxyService;
import io.renren.modules.ltt.conver.CdLineIpProxyConver;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
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

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

    @Override
    public String getProxyIp(CdLineIpProxyDTO cdLineIpProxyDTO) {
        try {
            //获取缓存
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            if (ObjectUtil.isNull(projectWorkEntity)) {
                return null;
            }
            //获取ip代理的国家
            PhoneCountryVO phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(cdLineIpProxyDTO.getLzPhone());
            Long countryCode = phoneNumberInfo.getCountryCode();
            String regions = EnumUtil.queryValueByKey(countryCode.intValue(), CountryCode.values());
            //查询是否已经有这个国家的ip了
            CdLineIpProxyEntity one = this.getOne(new QueryWrapper<CdLineIpProxyEntity>().lambda()
                    .eq(CdLineIpProxyEntity::getTokenPhone,cdLineIpProxyDTO.getTokenPhone())
                    .eq(CdLineIpProxyEntity::getLzCountry,String.valueOf(countryCode))
            );
            //如果有直接返回
            if (ObjectUtil.isNotNull(one)) {
                String ip = one.getIp();
                boolean proxyUse = isProxyUse(ip,regions);
                if (proxyUse) {
                    return socks5Pre(ip);
                }
            }
            String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource3, countryCode.intValue());
            Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
            boolean triedLock = lock.tryLock();
            log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
            if(triedLock) {
                try{
                    Queue<String> getflowip = caffeineCacheListString.getIfPresent(regions);
                    String ip = null;
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

                    if (ObjectUtil.isNull(one)) {
                        CdLineIpProxyEntity save = new CdLineIpProxyEntity();
                        save.setIp(ip);
                        save.setTokenPhone(cdLineIpProxyDTO.getTokenPhone());
                        save.setLzCountry(String.valueOf(countryCode));
                        this.save(save);
                    }else {
                        one.setIp(ip);
                        this.updateById(one);
                    }
                    return socks5Pre(ip);
                }finally {
                    lock.unlock();
                }
            }else {
                log.info("keyByResource = {} 在执行",keyByResource);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private static String socks5Pre(String ip) {
        if (ip.contains("socks5://")) {
            return ip;
        }
        return String.format("socks5://%s", ip);
    }

    private boolean isProxyUse(String ip,String country) {
        try {
            String format = String.format("curl --socks5 %s ipinfo.io",ip);
            List<String> strings = RuntimeUtil.execForLines(format);
            for (String string : strings) {
                if (string.toLowerCase().contains("country") && string.toLowerCase().contains(country)) {
                    return true;
                }
            }
            return false;
        }catch (Exception e) {

        }
        return false;
    }
}
