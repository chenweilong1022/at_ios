package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.constant.SystemConstant;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.LineTokenJson;
import io.renren.modules.client.dto.OpenApp;
import io.renren.modules.client.dto.RefreshAccessTokenDTO;
import io.renren.modules.client.dto.RegisterResultDTO;
import io.renren.modules.client.vo.LineRegisterVO;
import io.renren.modules.client.vo.OpenAppResult;
import io.renren.modules.client.vo.RefreshAccessTokenVO;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.enums.AtUserTokenTypeEnum;
import io.renren.modules.ltt.enums.LockMapKeyResource;
import io.renren.modules.ltt.enums.OpenStatus;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/2 00:45
 */
@Component
@Slf4j
@EnableAsync
@Profile({"open"}) //打开服务
public class OpenAppTask {



    @Autowired
    private CdRegisterTaskService cdRegisterTaskService;
    @Autowired
    private CdRegisterSubtasksService cdRegisterSubtasksService;
    @Autowired
    private CdGetPhoneService cdGetPhoneService;
    @Autowired
    private CdLineRegisterService cdLineRegisterService;
    @Autowired
    private LineService lineService;
    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;
    @Autowired
    private ConcurrentHashMap<String, Lock> lockMap;
    @Autowired
    private AtUserTokenService atUserTokenService;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;

    private static final Object lockObj4 = new Object();

    @Resource(name = "poolExecutor")
    private ThreadPoolTaskExecutor poolExecutor;
    static ReentrantLock task4Lock = new ReentrantLock();

    @Scheduled(fixedDelay = 10000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task4() {
        String format = String.format("and MOD(id, %s) = %s limit 200", systemConstant.getSERVERS_TOTAL_MOD(), systemConstant.getSERVERS_MOD());
        List<AtUserTokenEntity> atUserTokenEntities = atUserTokenService.list(new QueryWrapper<AtUserTokenEntity>().lambda()
                .eq(AtUserTokenEntity::getOpenStatus, OpenStatus.OpenStatus5.getKey())
                .eq(AtUserTokenEntity::getTokenType, AtUserTokenTypeEnum.AtUserTokenType1.getKey())
                .eq(AtUserTokenEntity::getUseFlag, UseFlag.YES.getKey())
                .isNotNull(AtUserTokenEntity::getToken)
                .last(format)
        );

        if (CollUtil.isEmpty(atUserTokenEntities)) {
            return;
        }

        for (AtUserTokenEntity atUserTokenEntity : atUserTokenEntities) {
            poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource10, atUserTokenEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        LineTokenJson lineTokenJson = JSON.parseObject(atUserTokenEntity.getToken(), LineTokenJson.class);
                        //获取代理
                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(lineTokenJson.getPhone());
                        cdLineIpProxyDTO.setLzPhone(lineTokenJson.getPhone());
                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                        if (StrUtil.isEmpty(proxyIp)) {
                            return;
                        }

                        RefreshAccessTokenDTO refreshAccessTokenDTO = new RefreshAccessTokenDTO();
                        refreshAccessTokenDTO.setProxy(proxyIp);
                        refreshAccessTokenDTO.setToken(atUserTokenEntity.getToken());
                        RefreshAccessTokenVO refreshAccessTokenVO = lineService.refreshAccessToken(refreshAccessTokenDTO);
                        if (ObjectUtil.isNotNull(refreshAccessTokenVO) && 200 == refreshAccessTokenVO.getCode()) {
                            lineTokenJson.setAccessToken(refreshAccessTokenVO.getData().getAccessToken());
                            lineTokenJson.setRefreshToken(refreshAccessTokenVO.getData().getRefreshToken());
                            String token = JSONUtil.toJsonStr(lineTokenJson);
                            atUserTokenEntity.setToken(token);
                            atUserTokenEntity.setOpenStatus(OpenStatus.OpenStatus1.getKey());
                        }else if (ObjectUtil.isNotNull(refreshAccessTokenVO) && 201 == refreshAccessTokenVO.getCode()) {
                            atUserTokenEntity.setOpenStatus(OpenStatus.OpenStatus4.getKey());
                        }
                        atUserTokenService.updateById(atUserTokenEntity);
                    }finally {
                        lock.unlock();
                    }
                }else {
                    log.info("keyByResource = {} 在执行",keyByResource);
                }
            });
        }

    }

    static ReentrantLock task2Lock = new ReentrantLock();


    @Scheduled(fixedDelay = 10000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task2() {
        boolean b = task2Lock.tryLock();
        if (!b) {
            return;
        }
        try {
            String format = String.format("and MOD(id, %s) = %s limit 200", systemConstant.getSERVERS_TOTAL_MOD(), systemConstant.getSERVERS_MOD());
            List<AtUserTokenEntity> atUserTokenEntities = atUserTokenService.list(new QueryWrapper<AtUserTokenEntity>().lambda()
                    .eq(AtUserTokenEntity::getOpenStatus, OpenStatus.OpenStatus2.getKey())
                    .eq(AtUserTokenEntity::getTokenType, AtUserTokenTypeEnum.AtUserTokenType1.getKey())
                    .eq(AtUserTokenEntity::getUseFlag, UseFlag.YES.getKey())
                    .isNotNull(AtUserTokenEntity::getToken)
                    .lt(AtUserTokenEntity::getOpenTime, DateUtil.date())
                    .last(format)
            );

            if (CollUtil.isEmpty(atUserTokenEntities)) {
                return;
            }
            for (AtUserTokenEntity atUserTokenEntity : atUserTokenEntities) {

                 poolExecutor.execute(() -> {
                     LineTokenJson lineTokenJson = JSON.parseObject(atUserTokenEntity.getToken(), LineTokenJson.class);
                     String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource10, atUserTokenEntity.getId());
                    Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                    boolean triedLock = lock.tryLock();
                    log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                    if(triedLock) {
                        try{
                            RegisterResultDTO registerResultDTO = new RegisterResultDTO();
                            registerResultDTO.setTaskId(atUserTokenEntity.getTaskId());
                            OpenAppResult openAppResult = lineService.openAppResult(registerResultDTO);
                            if (ObjectUtil.isNotNull(openAppResult) && 200 == openAppResult.getCode()) {
                                OpenAppResult.Data data = openAppResult.getData();
                                if (ObjectUtil.isNull(data)) {
                                    return;
                                }
                                DateTime openTime = DateUtil.offsetHour(DateUtil.date(), 24);
                                AtUserTokenEntity update = new AtUserTokenEntity();
                                update.setId(atUserTokenEntity.getId());
                                String concat = StrUtil.concat(true, data.getRemark(), openAppResult.getMsg());
                                update.setErrMsg(concat);
                                if (2 == data.getStatus()) {
                                    update.setOpenStatus(OpenStatus.OpenStatus1.getKey());
                                    update.setOpenTime(openTime);
                                    update.setDataSubtaskId(-1);
                                    if (ObjectUtil.isNotNull(atUserTokenEntity.getDataSubtaskId())) {
                                        AtDataSubtaskEntity atDataSubtaskEntity = new AtDataSubtaskEntity();
                                        atDataSubtaskEntity.setId(atUserTokenEntity.getDataSubtaskId());
                                        atDataSubtaskEntity.setOpenApp(io.renren.modules.ltt.enums.OpenApp.OpenApp1.getKey());
                                        atDataSubtaskService.updateById(atDataSubtaskEntity);
                                    }
                                }else if (Long.valueOf(10001).equals(data.getStatus())) {
                                    update.setOpenStatus(OpenStatus.OpenStatus5.getKey());
                                    update.setOpenTime(openTime);
                                }else if (-1 == data.getStatus()) {
                                    update.setOpenStatus(OpenStatus.OpenStatus4.getKey());
                                    if (concat.contains("网络异常")) {
                                        update.setOpenStatus(OpenStatus.OpenStatus1.getKey());
                                    }else if (concat.contains("TOKEN_CLIENT_LOGGED_OUT")) {
                                        update.setOpenStatus(OpenStatus.OpenStatus3.getKey());
                                    }
                                }else if (1 == data.getStatus()) {
                                    return;
                                }else {
                                    return;
                                }
                                atUserTokenService.updateById(update);
                            }else if (201 == openAppResult.getCode()){
                                AtUserTokenEntity update = new AtUserTokenEntity();
                                update.setId(atUserTokenEntity.getId());
                                String concat = StrUtil.concat(true, openAppResult.getMsg());
                                update.setErrMsg(concat);
                                update.setOpenStatus(OpenStatus.OpenStatus1.getKey());
                                update.setOpenTime(DateUtil.date());
                                atUserTokenService.updateById(update);
                            }
                        }finally {
                            lock.unlock();
                        }
                    }else {
                        log.info("keyByResource = {} 在执行",keyByResource);
                    }
                });
            }

        }finally {
            task2Lock.unlock();
        }
    }



    static ReentrantLock task1Lock = new ReentrantLock();

    @Autowired
    private SystemConstant systemConstant;
    @Scheduled(fixedDelay = 10000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task1() {
        String format = String.format("and MOD(id, %s) = %s limit 200", systemConstant.getSERVERS_TOTAL_MOD(), systemConstant.getSERVERS_MOD());
        List<AtUserTokenEntity> atUserTokenEntities = atUserTokenService.list(new QueryWrapper<AtUserTokenEntity>().lambda()
                .eq(AtUserTokenEntity::getOpenStatus, OpenStatus.OpenStatus1.getKey())
                .eq(AtUserTokenEntity::getTokenType, AtUserTokenTypeEnum.AtUserTokenType1.getKey())
                .isNotNull(AtUserTokenEntity::getToken)
                .eq(AtUserTokenEntity::getUseFlag, UseFlag.YES.getKey())
                .lt(AtUserTokenEntity::getOpenTime, DateUtil.date())
                .last(format)
        );


        if (CollUtil.isEmpty(atUserTokenEntities)) {
            return;
        }
        for (AtUserTokenEntity atUserTokenEntity : atUserTokenEntities) {
             poolExecutor.execute(() -> {
                LineTokenJson lineTokenJson = JSON.parseObject(atUserTokenEntity.getToken(), LineTokenJson.class);
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource10, atUserTokenEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        //获取代理
                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(lineTokenJson.getPhone());
                        cdLineIpProxyDTO.setLzPhone(lineTokenJson.getPhone());
                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                        if (StrUtil.isEmpty(proxyIp)) {
                            return;
                        }
                        OpenApp openApp = new OpenApp();
                        openApp.setProxy(proxyIp);
                        openApp.setToken(atUserTokenEntity.getToken());
                        LineRegisterVO lineRegisterVO = lineService.openApp(openApp);
                        if (ObjectUtil.isNotNull(lineRegisterVO) && 200 == lineRegisterVO.getCode()) {
                            AtUserTokenEntity update = new AtUserTokenEntity();
                            update.setId(atUserTokenEntity.getId());
                            update.setOpenStatus(OpenStatus.OpenStatus2.getKey());
                            update.setTaskId(lineRegisterVO.getData().getTaskId());
                            atUserTokenService.updateById(update);
                        }
                    }finally {
                        lock.unlock();
                    }
                }else {
                    log.info("keyByResource = {} 在执行",keyByResource);
                }
            });
        }
    }

}
