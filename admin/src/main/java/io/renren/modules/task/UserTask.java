package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.base.vo.EnumVo;
import io.renren.common.constant.SystemConstant;
import io.renren.common.utils.EnumUtil;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.IssueLiffViewDTO;
import io.renren.modules.client.dto.LineTokenJson;
import io.renren.modules.client.vo.ConversionAppTokenVO;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.entity.AtUserTokenIosEntity;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.IssueLiffViewVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static io.renren.modules.ltt.enums.AtUserTokenTypeEnum.AtUserTokenType2;

/**
 * @author liuyuchana
 * @email liuyuchan286@gmail.com
 * @date 2023/11/21 18:45
 */
@Component
@Slf4j
@EnableAsync
@Profile({"prod","verify"})
public class UserTask {


    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtUserTokenService atUserTokenService;
    @Autowired
    private AtUserGroupService atUserGroupService;
    @Autowired
    private AtUserTokenIosService atUserTokenIosService;
    @Autowired
    private ConcurrentHashMap<String, Lock> lockMap;
    static ReentrantLock task5Lock = new ReentrantLock();

    @Resource
    private CdGetPhoneService cdGetPhoneService;


    public static final Object atUserlockObj = new Object();
    public static final Object atUserTokenlockObj = new Object();
    @Autowired
    private LineService lineService;
    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;
    @Autowired
    private CdLineRegisterService cdLineRegisterService;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    static ReentrantLock task1Lock = new ReentrantLock();


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
        log.info("resp = {}",resp);
        if (JSONUtil.isJson(resp) || resp.contains("Please request again in 2 seconds")) {
            log.info("{} resp = {}",getPhoneHttp,resp);
            cardJpSmsOver.put(getPhoneHttp,"again");
            return null;
        }
        return resp;
    }


    @Autowired
    private SystemConstant systemConstant;

    /**
     * 同步token信息到用户表
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task2() {
        String format = String.format("MOD(id, %s) = %s", systemConstant.getSERVERS_TOTAL_MOD(), systemConstant.getSERVERS_MOD());
        //获取用户未验证的状态
        List<AtUserEntity> atUserEntities = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
                .eq(AtUserEntity::getStatus,UserStatus.UserStatus1.getKey())
                .and(item -> item.last(format))
                .orderByAsc(AtUserEntity::getStatus)
                .last("limit 150")
        );
        if (CollUtil.isEmpty(atUserEntities)) {
            log.info("UserTask task2 atUserEntities isEmpty");
            return;
        }
        //用户tokenIds
        List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
        List<AtUserTokenEntity> atUserTokenEntities = atUserTokenService.listByIds(userTokenIds);
        if (CollUtil.isEmpty(atUserEntities)) {
            log.info("UserTask task2 atUserTokenEntities isEmpty");
            return;
        }
        //用户tokenMap
        Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = atUserTokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
        for (AtUserEntity atUserEntity : atUserEntities) {
            threadPoolTaskExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource9, atUserEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        AtUserTokenEntity atUserTokenEntity = atUserTokenEntityMap.get(atUserEntity.getUserTokenId());
                        if (ObjectUtil.isNull(atUserTokenEntity)) {
                            atUserService.removeById(atUserEntity.getId());
                            return;
                        }

                        //获取代理
                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(atUserEntity.getTelephone());
                        cdLineIpProxyDTO.setLzPhone(atUserEntity.getTelephone());
                        cdLineIpProxyDTO.setCountryCode(CountryCode.CountryCode7.getKey().longValue());
                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                        if (StrUtil.isEmpty(proxyIp)) {
                            return;
                        }

                        IssueLiffViewDTO issueLiffViewDTO = new IssueLiffViewDTO();
                        issueLiffViewDTO.setProxy(proxyIp);
                        issueLiffViewDTO.setToken(atUserTokenEntity.getToken());
                        IssueLiffViewVO issueLiffViewVO = lineService.issueLiffView(issueLiffViewDTO);
                        AtUserEntity update = new AtUserEntity();
                        update.setId(atUserEntity.getId());
                        if (ObjectUtil.isNull(issueLiffViewVO)) {
                            return;
                        }
                        update.setMsg(issueLiffViewVO.getMsg());
                        update.setStatus(UserStatus.UserStatus4.getKey());
                        //号被封号了
                        if (201 == issueLiffViewVO.getCode()) {
                            //用户添加群过多 封号
                            if (issueLiffViewVO.getMsg().contains(UserStatusCode.UserStatusCode9.getValue())) {
                                update.setStatus(UserStatus.UserStatus2.getKey());
                            }else  if (issueLiffViewVO.getMsg().contains(UserStatusCode.UserStatusCode13.getValue())) {
                                update.setStatus(UserStatus.UserStatus2.getKey());
                            }else  if (issueLiffViewVO.getMsg().contains(UserStatusCode.UserStatusCode14.getValue())) {
                                update.setStatus(UserStatus.UserStatus3.getKey());
                            }
                        }else if(300 == issueLiffViewVO.getCode()) {
                            update.setStatus(UserStatus.UserStatus1.getKey());
                        }
                        atUserService.updateById(update);
                    }finally {
                        lock.unlock();
                    }
                }else {
                    log.info("keyByResource = {} 在执行",keyByResource);
                }
            });

        }

    }



    /**
     * 同步token信息到用户表
     */
    @Scheduled(fixedDelay = 10 * 1000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task1() {
        String format = String.format("MOD(id, %s) = %s", systemConstant.getSERVERS_TOTAL_MOD(), systemConstant.getSERVERS_MOD());
        //获取刚导入的token去转化为账号
        List<AtUserTokenEntity> atUserTokenEntities = atUserTokenService.list(new QueryWrapper<AtUserTokenEntity>().lambda()
                .eq(AtUserTokenEntity::getUseFlag, UseFlag.NO.getKey())
                .and(item -> item.last(format))
                .last("limit 150")
        );
        if (CollUtil.isEmpty(atUserTokenEntities)) {
            log.info("UserTask task1 atUserTokenEntities isEmpty");
            return;
        }

        //获取用户分组的map
        List<Integer> userGroupIdList = atUserTokenEntities.stream()
                .filter(i -> ObjectUtil.isNotNull(i.getUserGroupId()))
                .map(AtUserTokenEntity::getUserGroupId).distinct().collect(Collectors.toList());
        Map<Integer, String> atUserGroupMap = atUserGroupService.getMapByIds(userGroupIdList);
        for (AtUserTokenEntity atUserTokenEntity : atUserTokenEntities) {
            threadPoolTaskExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource10, atUserTokenEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        //格式化token
                        LineTokenJson lineTokenJson = JSON.parseObject(atUserTokenEntity.getToken(), LineTokenJson.class);
                        AtUserEntity atUserEntity = new AtUserEntity();
                        atUserEntity.setNation(lineTokenJson.getCountryCode());
                        String telephone = StrUtil.cleanBlank(lineTokenJson.getPhone()).replaceAll("-", "");
//                        AtUserEntity one = atUserService.getOne(new QueryWrapper<AtUserEntity>().lambda()
//                                .eq(AtUserEntity::getTelephone,telephone)
//                        );
                        atUserEntity.setTelephone(telephone);
                        atUserEntity.setNickName(lineTokenJson.getNickName());
                        atUserEntity.setPassword(lineTokenJson.getPassword());
                        atUserEntity.setUserGroupId(atUserTokenEntity.getUserGroupId());
                        atUserEntity.setNumberFriends(0);
                        //未验证账号
                        atUserEntity.setStatus(UserStatus.UserStatus4.getKey());
                        atUserEntity.setUserGroupName(atUserGroupMap.get(atUserTokenEntity.getUserGroupId()));
                        //将添加token添加到用户
                        atUserEntity.setDeleteFlag(DeleteFlag.NO.getKey());
                        atUserEntity.setCreateTime(DateUtil.date());
                        atUserEntity.setUserTokenId(atUserTokenEntity.getId());
                        atUserEntity.setSysUserId(atUserTokenEntity.getSysUserId());
                        if (ObjectUtil.isNotNull(atUserTokenEntity.getTokenType())
                                && AtUserTokenType2.getKey().equals(atUserTokenEntity.getTokenType())) {
                            atUserEntity.setUserSource(AtUserSourceEnum.AtUserSource2.getKey());
                        }

                        //查询手机号注册次数
                        atUserEntity.setRegisterCount(cdGetPhoneService.getPhoneRegisterCount(atUserEntity.getTelephone()));

                        //注册信息
                        CdLineRegisterEntity cdLineRegisterEntity = cdLineRegisterService.queryLineRegisterByPhone(telephone);
                        if (ObjectUtil.isNotNull(cdLineRegisterEntity)) {
                            atUserEntity.setRegisterTime(cdLineRegisterEntity.getCreateTime());
                        } else {
                            atUserEntity.setRegisterTime(new Date());
                        }

//                        if (ObjectUtil.isNotNull(one)) {
//                            atUserEntity.setId(one.getId());
//                            atUserService.updateById(atUserEntity);
//                        }else {
                            //存账户信息
                            atUserService.save(atUserEntity);
//                        }
                        //修改数据使用状态
                        AtUserTokenEntity update = new AtUserTokenEntity();
                        update.setId(atUserTokenEntity.getId());
                        update.setUseFlag(UseFlag.YES.getKey());
                        atUserTokenService.updateById(update);
                    }finally {
                        lock.unlock();
                    }
                }else {
                    log.info("keyByResource = {} 在执行",keyByResource);
                }
            });
        }

    }

    /**
     * 生成真机token
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task5() {
        boolean b = task5Lock.tryLock();
        if (!b) {
            return;
        }
        try {
            String format = String.format("MOD(id, %s) = %s", systemConstant.getSERVERS_TOTAL_MOD(), systemConstant.getSERVERS_MOD());
            //获取刚导入的token去转化为账号
            List<AtUserTokenIosEntity> atUserTokenIosEntityList = atUserTokenIosService.list(new QueryWrapper<AtUserTokenIosEntity>().lambda().
                    isNull(AtUserTokenIosEntity::getAtUserTokenId).last("limit 150").and(item -> item.last(format)));
            if (CollUtil.isEmpty(atUserTokenIosEntityList)) {
                log.info("UserTask task5 atUserTokenIosEntityList isEmpty");
                return;
            }
            final CountDownLatch latch = new CountDownLatch(atUserTokenIosEntityList.size());

            for (AtUserTokenIosEntity tokenIosEntity : atUserTokenIosEntityList) {
                threadPoolTaskExecutor.submit(new Thread(() -> {
                    if (StrUtil.isEmpty(tokenIosEntity.getIosToken())) {
                        latch.countDown();
                        return;
                    }
                    //获取用户token
                    ConversionAppTokenVO conversionAppToken = lineService.conversionAppToken(tokenIosEntity.getIosToken());
                    if (ObjectUtil.isNull(conversionAppToken)) {
                        latch.countDown();
                        return;
                    }
                    //成功获取返回zhi
                    if (0 == conversionAppToken.getCode() && conversionAppToken.getData() != null) {
                        String token = conversionAppToken.getData().getToken();
                        if (StrUtil.isEmpty(token)) {
                            latch.countDown();
                            return;
                        }

                        //插入token
                        AtUserTokenEntity updateAtUserToken = new AtUserTokenEntity();
                        updateAtUserToken.setToken(token);
                        updateAtUserToken.setUserGroupId(null);
                        updateAtUserToken.setSysUserId(1L);
                        updateAtUserToken.setTokenType(AtUserTokenType2.getKey());//token类型 1协议token 2真机token
                        updateAtUserToken.setUseFlag(UseFlag.NO.getKey());
                        updateAtUserToken.setDeleteFlag(DeleteFlag.NO.getKey());
                        updateAtUserToken.setCreateTime(new Date());
                        updateAtUserToken.setPlatform(Platform.IOS.getKey());
                        atUserTokenService.save(updateAtUserToken);

                        AtUserTokenIosEntity updateAtUserTokenIos = new AtUserTokenIosEntity();
                        updateAtUserTokenIos.setId(tokenIosEntity.getId());
                        updateAtUserTokenIos.setAtUserTokenId(updateAtUserToken.getId());
                        atUserTokenIosService.updateById(updateAtUserTokenIos);
                    }
                    latch.countDown();
                }));
            }
            latch.await();
        } catch (Exception e) {
            log.error("UserTask task5 err = {}", e.getMessage());
        } finally {
            task5Lock.unlock();
        }
    }

}
