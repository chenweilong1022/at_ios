//package io.renren.modules.task;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.date.DateTime;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.util.ObjectUtil;
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import io.renren.modules.client.LineService;
//import io.renren.modules.client.dto.OpenApp;
//import io.renren.modules.client.dto.RegisterResultDTO;
//import io.renren.modules.client.vo.LineRegisterVO;
//import io.renren.modules.client.vo.OpenAppResult;
//import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
//import io.renren.modules.ltt.entity.CdLineRegisterEntity;
//import io.renren.modules.ltt.enums.LockMapKeyResource;
//import io.renren.modules.ltt.enums.OpenStatus;
//import io.renren.modules.ltt.enums.RegisterStatus;
//import io.renren.modules.ltt.service.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.Resource;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * @author liuyuchan
// * @email liuyuchan286@gmail.com
// * @date 2023/12/2 00:45
// */
//@Component
//@Slf4j
//@EnableAsync
//public class OpenAppTask {
//
//
//
//    @Autowired
//    private CdRegisterTaskService cdRegisterTaskService;
//    @Autowired
//    private CdRegisterSubtasksService cdRegisterSubtasksService;
//    @Autowired
//    private CdGetPhoneService cdGetPhoneService;
//    @Autowired
//    private CdLineRegisterService cdLineRegisterService;
//    @Autowired
//    private LineService lineService;
//    @Autowired
//    private CdLineIpProxyService cdLineIpProxyService;
//    @Autowired
//    private ConcurrentHashMap<String, Lock> lockMap;
//
//    private static final Object lockObj4 = new Object();
//
//    @Resource(name = "poolExecutor")
//    private ThreadPoolTaskExecutor poolExecutor;
//    static ReentrantLock task4Lock = new ReentrantLock();
//
//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task4() {
//        boolean b = task4Lock.tryLock();
//        if (!b) {
//            return;
//        }
//        try {
//
//        }finally {
//            task4Lock.unlock();;
//        }
//    }
//
//    static ReentrantLock task2Lock = new ReentrantLock();
//
//
//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task2() {
//        boolean b = task2Lock.tryLock();
//        if (!b) {
//            return;
//        }
//        try {
//            List<CdLineRegisterEntity> list = cdLineRegisterService.list(new QueryWrapper<CdLineRegisterEntity>().lambda()
//                    .eq(CdLineRegisterEntity::getOpenStatus, OpenStatus.OpenStatus2.getKey())
//                    .isNotNull(CdLineRegisterEntity::getToken)
//                    .lt(CdLineRegisterEntity::getOpenTime, DateUtil.date())
//                    .last("limit 20")
//            );
//
//            if (CollUtil.isEmpty(list)) {
//                return;
//            }
//            for (CdLineRegisterEntity one : list) {
//
//                 poolExecutor.execute(() -> {
//                    String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource5, one.getId());
//                    Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
//                    boolean triedLock = lock.tryLock();
//                    log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
//                    if(triedLock) {
//                        try{
//                            RegisterResultDTO registerResultDTO = new RegisterResultDTO();
//                            registerResultDTO.setTaskId(one.getTaskId());
//                            OpenAppResult openAppResult = lineService.openAppResult(registerResultDTO);
//                            if (ObjectUtil.isNotNull(openAppResult) && 200 == openAppResult.getCode()) {
//                                OpenAppResult.Data data = openAppResult.getData();
//                                if (ObjectUtil.isNull(data)) {
//                                    return;
//                                }
//                                DateTime openTime = DateUtil.offsetHour(DateUtil.date(), 8);
//                                CdLineRegisterEntity update = new CdLineRegisterEntity();
//                                update.setId(one.getId());
//                                if (2 == data.getStatus()) {
//                                    update.setOpenStatus(OpenStatus.OpenStatus1.getKey());
//                                    update.setOpenTime(openTime);
//                                }else if (Long.valueOf(10001).equals(data.getStatus())) {
//                                    update.setOpenStatus(OpenStatus.OpenStatus5.getKey());
//                                    update.setOpenTime(openTime);
//                                }else if (-1 == data.getStatus()) {
//                                    update.setOpenStatus(OpenStatus.OpenStatus4.getKey());
//                                    if (data.getRemark().contains("网络异常")) {
//                                        update.setOpenStatus(OpenStatus.OpenStatus1.getKey());
//                                    }else if (data.getRemark().contains("TOKEN_CLIENT_LOGGED_OUT")) {
//                                        update.setOpenStatus(OpenStatus.OpenStatus3.getKey());
//                                        update.setRegisterStatus(RegisterStatus.RegisterStatus8.getKey());
//                                    }
//                                }
//                                synchronized (RegisterTask.lockCdLineRegisterEntity) {
//                                    cdLineRegisterService.updateById(update);
//                                }
//                            }
//                        }finally {
//                            lock.unlock();
//                        }
//                    }else {
//                        log.info("keyByResource = {} 在执行",keyByResource);
//                    }
//                });
//            }
//
//        }finally {
//            task2Lock.unlock();
//        }
//    }
//
//
//
//    static ReentrantLock task1Lock = new ReentrantLock();
//
//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task1() {
//        //找出小于当前时间的需要打开的 token
//        List<CdLineRegisterEntity> list = cdLineRegisterService.list(new QueryWrapper<CdLineRegisterEntity>().lambda()
//                .eq(CdLineRegisterEntity::getOpenStatus, OpenStatus.OpenStatus1.getKey())
//                .isNotNull(CdLineRegisterEntity::getToken)
//                .lt(CdLineRegisterEntity::getOpenTime, DateUtil.date())
//                .last("limit 20")
//        );
//        if (CollUtil.isEmpty(list)) {
//            return;
//        }
//        for (CdLineRegisterEntity cdLineRegisterEntity : list) {
//             poolExecutor.execute(() -> {
//                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource5, cdLineRegisterEntity.getId());
//                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
//                boolean triedLock = lock.tryLock();
//                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
//                if(triedLock) {
//                    try{
//                        //获取代理
//                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
//                        cdLineIpProxyDTO.setTokenPhone(cdLineRegisterEntity.getPhone());
//                        cdLineIpProxyDTO.setLzPhone(cdLineRegisterEntity.getPhone());
//                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
//                        if (StrUtil.isEmpty(proxyIp)) {
//                            return;
//                        }
//                        OpenApp openApp = new OpenApp();
//                        openApp.setProxy(proxyIp);
//                        openApp.setToken(cdLineRegisterEntity.getToken());
//                        LineRegisterVO lineRegisterVO = lineService.openApp(openApp);
//                        if (ObjectUtil.isNotNull(lineRegisterVO) && 200 == lineRegisterVO.getCode()) {
//                            CdLineRegisterEntity update = new CdLineRegisterEntity();
//                            update.setId(cdLineRegisterEntity.getId());
//                            update.setOpenStatus(OpenStatus.OpenStatus2.getKey());
//                            update.setTaskId(lineRegisterVO.getData().getTaskId());
//                            synchronized (RegisterTask.lockCdLineRegisterEntity) {
//                                cdLineRegisterService.updateById(update);
//                            }
//                        }
//                    }finally {
//                        lock.unlock();
//                    }
//                }else {
//                    log.info("keyByResource = {} 在执行",keyByResource);
//                }
//            });
//        }
//    }
//
//
//}
