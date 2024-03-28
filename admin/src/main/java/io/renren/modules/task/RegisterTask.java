package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.utils.ConfigConstant;
import io.renren.modules.client.FirefoxService;
import io.renren.modules.client.LineService;
import io.renren.modules.client.ProxyService;
import io.renren.modules.client.dto.LineRegisterDTO;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.client.vo.DataLineRegisterVO;
import io.renren.modules.client.vo.LineRegisterVO;
import io.renren.modules.ltt.dto.CdGetPhoneDTO;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import io.renren.modules.ltt.entity.CdRegisterSubtasksEntity;
import io.renren.modules.ltt.entity.CdRegisterTaskEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.service.impl.AsyncService;
import io.renren.modules.ltt.vo.CdRegisterSubtasksVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static io.renren.modules.ltt.enums.PhoneStatus.PhoneStatus5;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/2 00:45
 */
@Component
@Slf4j
public class RegisterTask {



    @Autowired
    private CdRegisterTaskService cdRegisterTaskService;
    @Autowired
    private CdRegisterSubtasksService cdRegisterSubtasksService;
    @Autowired
    private CdGetPhoneService cdGetPhoneService;
    @Autowired
    private CdLineRegisterService cdLineRegisterService;

    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtUserTokenService atUserTokenService;
    @Autowired
    private AtUserGroupService atUserGroupService;

    @Autowired
    private ConcurrentHashMap<String, Lock> lockMap;

    static ReentrantLock task1Lock = new ReentrantLock();
    static ReentrantLock task2Lock = new ReentrantLock();
    static ReentrantLock task3Lock = new ReentrantLock();
    static ReentrantLock task4Lock = new ReentrantLock();
    private static final Object lockCdRegisterSubtasksEntity = new Object();
    private static final Object lockCdGetPhoneEntity = new Object();
    private static final Object lockCdLineRegisterEntity = new Object();
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private LineService lineService;
    @Autowired
    private ProxyService proxyService;
    @Autowired
    private AtAvatarService atAvatarService;

    @Resource(name = "poolExecutor")
    private ThreadPoolTaskExecutor poolExecutor;
    @Autowired
    private AtDataTaskService atDataTaskService;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;
    @Resource(name = "caffeineCacheProjectWorkEntity")
    private Cache<String, ProjectWorkEntity> caffeineCacheProjectWorkEntity;
    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;
    @Resource(name = "cardMeServiceImpl")
    private FirefoxService firefoxService;
    @Resource(name = "caffeineCacheCode")
    private Cache<String, String> caffeineCacheCode;


    /**
     *
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task7() {
    }



    /**
     *
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task6() {
        //获取验证码
        List<CdGetPhoneEntity> list = cdGetPhoneService.list(new QueryWrapper<CdGetPhoneEntity>().lambda()
                .in(CdGetPhoneEntity::getPhoneStatus, PhoneStatus.PhoneStatus2.getKey(), PhoneStatus5.getKey())
        );
        if (CollUtil.isEmpty(list)) {
            log.info("task2 list isEmpty");
            return;
        }
        for (CdGetPhoneEntity cdGetPhoneEntity : list) {
             poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource4, cdGetPhoneEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        //如果需要释放
                        if(PhoneStatus5.getKey().equals(cdGetPhoneEntity.getPhoneStatus())) {
                            boolean b = firefoxService.setRel(cdGetPhoneEntity.getPkey());
                            if (b) {
                                cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus6.getKey());
                                cdGetPhoneService.updateById(cdGetPhoneEntity);
                            }
                            //如果需要修改验证码
                        }else {
                            String phoneCode = cdGetPhoneEntity.getCode();
                            //超过20分
                            long between = DateUtil.between(cdGetPhoneEntity.getCreateTime(), DateUtil.date(), DateUnit.MINUTE);
                            if (between > 7) {
                                cdGetPhoneEntity.setCode("验证码超时");
                                cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus3.getKey());
                                cdGetPhoneService.updateById(cdGetPhoneEntity);
                                return;
                            }
                            if (StrUtil.isEmpty(phoneCode)) {
                                phoneCode = firefoxService.getPhoneCode(cdGetPhoneEntity.getPkey());
                            }
                            if (StrUtil.isEmpty(phoneCode)) {
                                return;
                            }
                            log.info("phoneCode = {}", phoneCode);
                            if (StrUtil.isNotEmpty(phoneCode)) {
                                cdGetPhoneEntity.setCode(phoneCode);
                                cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus3.getKey());
                                caffeineCacheCode.put(cdGetPhoneEntity.getPhone(),phoneCode);
                                cdGetPhoneService.updateById(cdGetPhoneEntity);
                            }
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


    /**
     * 使用协议去注册
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task5() {
        ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
        if (ObjectUtil.isNull(projectWorkEntity)) {
            return;
        }
        List<CdGetPhoneEntity> list = cdGetPhoneService.list(new QueryWrapper<CdGetPhoneEntity>().lambda()
                .eq(CdGetPhoneEntity::getPhoneStatus, PhoneStatus.PhoneStatus1.getKey())
                .last("limit 10")
        );
        if (CollUtil.isEmpty(list)) {
            log.info("task1 list isEmpty");
            return;
        }
        for (CdGetPhoneEntity cdGetPhoneEntity : list) {
             poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource4, cdGetPhoneEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{

                        LineRegisterDTO lineRegisterDTO = new LineRegisterDTO();
                        lineRegisterDTO.setAb(projectWorkEntity.getLineAb());
                        lineRegisterDTO.setAppVersion(projectWorkEntity.getLineAppVersion());
                        lineRegisterDTO.setCountryCode(projectWorkEntity.getFirefoxCountry1());
                        lineRegisterDTO.setPhone(cdGetPhoneEntity.getPhone());
                        //获取代理
                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(cdGetPhoneEntity.getPhone());
                        cdLineIpProxyDTO.setLzPhone(cdGetPhoneEntity.getPhone());
                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                        if (StrUtil.isEmpty(proxyIp)) {
                            return;
                        }

                        lineRegisterDTO.setProxy(proxyIp);
                        lineRegisterDTO.setTxtToken(projectWorkEntity.getLineTxtToken());
                        LineRegisterVO lineRegisterVO = lineService.lineRegister(lineRegisterDTO);
                        if (ObjectUtil.isNull(lineRegisterVO)) {
                            return;
                        }
                        log.info("lineRegisterVO = {}", JSONUtil.toJsonStr(lineRegisterVO));
                        // 提交成功
                        if (200 == lineRegisterVO.getCode()) {
                            CdLineRegisterEntity cdLineRegisterDTO = new CdLineRegisterEntity();
                            cdLineRegisterDTO.setAb(lineRegisterDTO.getAb());
                            cdLineRegisterDTO.setAppVersion(lineRegisterDTO.getAppVersion());
                            cdLineRegisterDTO.setCountryCode(lineRegisterDTO.getCountryCode());
                            cdLineRegisterDTO.setPhone(lineRegisterDTO.getPhone());
                            cdLineRegisterDTO.setProxy(lineRegisterDTO.getProxy());
                            cdLineRegisterDTO.setTxtToken(lineRegisterDTO.getTxtToken());
                            DataLineRegisterVO data = lineRegisterVO.getData();
                            cdLineRegisterDTO.setTaskId(data.getTaskId());
                            cdLineRegisterDTO.setRegisterStatus(RegisterStatus.RegisterStatus1.getKey());
                            cdLineRegisterDTO.setDeleteFlag(DeleteFlag.NO.getKey());
                            cdLineRegisterDTO.setCreateTime(DateUtil.date());
                            cdLineRegisterDTO.setGetPhoneId(cdGetPhoneEntity.getId());
                            cdLineRegisterDTO.setPkey(cdGetPhoneEntity.getPkey());
                            cdLineRegisterDTO.setSubtasksId(cdGetPhoneEntity.getSubtasksId());
                            //设置代理类型
                            cdLineRegisterDTO.setProxyStatus(projectWorkEntity.getProxy());

                            CdGetPhoneEntity update = new CdGetPhoneEntity();
                            update.setId(cdGetPhoneEntity.getId());
                            update.setPhoneStatus(PhoneStatus.PhoneStatus2.getKey());
                            //修改注册
                            synchronized (lockCdGetPhoneEntity) {
                                cdGetPhoneService.updateById(update);
                            }

                            synchronized (lockCdLineRegisterEntity) {
                                cdLineRegisterService.save(cdLineRegisterDTO);
                            }
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


    /**
     *
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task4() {
        boolean b = task4Lock.tryLock();
        if (!b) {
            return;
        }
        try {
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task4Lock.unlock();
        }
    }


    /**
     *
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task3() {
        boolean b = task3Lock.tryLock();
        if (!b) {
            return;
        }
        try {
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task3Lock.unlock();
        }
    }


    /**
     * 根据任务去获取手机号
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task2() {
        //获取子任务
        List<CdRegisterSubtasksVO> cdRegisterSubtasksVOS = cdRegisterSubtasksService.groupByTaskId();
        if (CollUtil.isEmpty(cdRegisterSubtasksVOS)) {
            log.info("RegisterTask task2 list isEmpty");
            return;
        }
        for (CdRegisterSubtasksVO cdRegisterSubtasksEntity : cdRegisterSubtasksVOS) {
             poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource2, cdRegisterSubtasksEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        //如果获取的状态为2跳出循环
                        if (RegistrationStatus.RegistrationStatus2.getKey().equals(cdRegisterSubtasksEntity.getRegistrationStatus())) {
                            //获取子任务数量
                            int count = cdGetPhoneService.count(new QueryWrapper<CdGetPhoneEntity>().lambda()
                                    .eq(CdGetPhoneEntity::getSubtasksId,cdRegisterSubtasksEntity.getId())
                            );
                            if (!cdRegisterSubtasksEntity.getNumberRegistrations().equals(count)) {
                                CdGetPhoneDTO cdGetPhoneDTO = new CdGetPhoneDTO();
                                cdGetPhoneDTO.setCount(cdRegisterSubtasksEntity.getNumberRegistrations() - count);
                                cdGetPhoneDTO.setSubtasksId(cdRegisterSubtasksEntity.getId());
                                List<CdGetPhoneEntity> cdGetPhoneEntities = cdGetPhoneService.addCount(cdGetPhoneDTO);
                                //如果数量相等
                                if (cdGetPhoneDTO.getCount().equals(cdGetPhoneEntities.size() + count)) {
                                    CdRegisterSubtasksEntity update = new CdRegisterSubtasksEntity();
                                    update.setId(cdRegisterSubtasksEntity.getId());
                                    update.setRegistrationStatus(RegistrationStatus.RegistrationStatus6.getKey());
                                    synchronized (lockCdRegisterSubtasksEntity) {
                                        cdRegisterSubtasksService.updateById(update);
                                    }
                                }
                            }
                            return;
                        }
                        CdGetPhoneDTO cdGetPhoneDTO = new CdGetPhoneDTO();
                        cdGetPhoneDTO.setCount(cdRegisterSubtasksEntity.getNumberRegistrations());
                        cdGetPhoneDTO.setSubtasksId(cdRegisterSubtasksEntity.getId());
                        List<CdGetPhoneEntity> cdGetPhoneEntities = cdGetPhoneService.addCount(cdGetPhoneDTO);
                        //如果数量相等
                        if (CollUtil.isNotEmpty(cdGetPhoneEntities)) {
                            CdRegisterSubtasksEntity update = new CdRegisterSubtasksEntity();
                            update.setId(cdRegisterSubtasksEntity.getId());
                            update.setRegistrationStatus(RegistrationStatus.RegistrationStatus2.getKey());
                            if (cdRegisterSubtasksEntity.getNumberRegistrations().equals(cdGetPhoneEntities.size())) {
                                update.setRegistrationStatus(RegistrationStatus.RegistrationStatus6.getKey());
                            }
                            synchronized (lockCdRegisterSubtasksEntity) {
                                cdRegisterSubtasksService.updateById(update);
                            }
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

    /**
     * 开始分任务
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task1() {
        //查询是否有注册任务 或者注册任务为充满的
        List<CdRegisterTaskEntity> list = cdRegisterTaskService.list(new QueryWrapper<CdRegisterTaskEntity>().lambda()
                .eq(CdRegisterTaskEntity::getRegistrationStatus, RegistrationStatus.RegistrationStatus1.getKey())
                .or().eq(CdRegisterTaskEntity::getFillUp, FillUp.YES.getKey())
        );
        if (CollUtil.isEmpty(list)) {
            log.info("注册任务没有查到数量为【0】");
            return;
        }

        for (CdRegisterTaskEntity cdRegisterTaskEntity : list) {
            poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource1, cdRegisterTaskEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        //获取子任务数量
                        int count = cdRegisterSubtasksService.count(new QueryWrapper<CdRegisterSubtasksEntity>().lambda()
                                .eq(CdRegisterSubtasksEntity::getTaskId, cdRegisterTaskEntity.getId())
                        );
                        //查询子任务是否在注册
                        if (count == 0) {
                            List<CdRegisterSubtasksEntity> cdRegisterSubtasksEntities = new ArrayList<>();
                            boolean flag = true;
                            while (flag) {
                                //总数量
                                Integer totalAmount = cdRegisterTaskEntity.getTotalAmount();
                                //线程数
                                Integer numberThreads = cdRegisterTaskEntity.getNumberThreads();
                                //注册数量 0
                                Integer numberRegistered = cdRegisterTaskEntity.getNumberRegistered();
                                //剩余注册数量
                                Integer newNumberRegistrations = totalAmount - numberRegistered;
                                if (newNumberRegistrations > numberThreads) {
                                    newNumberRegistrations = numberThreads;
                                }else{
                                    flag = false;
                                    cdRegisterTaskEntity.setRegistrationStatus(RegistrationStatus.RegistrationStatus2.getKey());
                                }
                                CdRegisterSubtasksEntity cdRegisterSubtasksEntity = new CdRegisterSubtasksEntity();
                                cdRegisterSubtasksEntity.setTaskId(cdRegisterTaskEntity.getId());
                                cdRegisterSubtasksEntity.setNumberRegistrations(newNumberRegistrations);
                                cdRegisterSubtasksEntity.setNumberSuccesses(0);
                                cdRegisterSubtasksEntity.setNumberFailures(0);
                                cdRegisterSubtasksEntity.setRegistrationStatus(RegistrationStatus.RegistrationStatus1.getKey());
                                cdRegisterSubtasksEntities.add(cdRegisterSubtasksEntity);
                                //设置主表注册数量
                                cdRegisterTaskEntity.setNumberRegistered(cdRegisterTaskEntity.getNumberRegistered() + newNumberRegistrations);
                            }
                            try {
                                Thread.sleep(50000);
                            } catch (InterruptedException e) {

                            }
                            synchronized (lockCdRegisterSubtasksEntity) {
                                cdRegisterSubtasksService.saveBatch(cdRegisterSubtasksEntities);
                            }
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


//     poolExecutor.execute(() -> {
//        String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource1, cdRegisterTaskEntity.getId());
//        Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
//        boolean triedLock = lock.tryLock();
//        log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
//        if(triedLock) {
//            try{
//
//            }finally {
//                lock.unlock();
//            }
//        }else {
//            log.info("keyByResource = {} 在执行",keyByResource);
//        }
//    });


}
