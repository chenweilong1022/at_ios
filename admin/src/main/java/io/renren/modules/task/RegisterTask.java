package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.utils.ConfigConstant;
import io.renren.common.utils.DateUtils;
import io.renren.modules.client.FirefoxService;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.*;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.client.vo.*;
import io.renren.modules.ltt.dto.CdGetPhoneDTO;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import io.renren.modules.ltt.entity.CdRegisterSubtasksEntity;
import io.renren.modules.ltt.entity.CdRegisterTaskEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.service.impl.AsyncService;
import io.renren.modules.ltt.vo.CdLineRegisterVO;
import io.renren.modules.ltt.vo.CdRegisterSubtasksVO;
import io.renren.modules.ltt.vo.GetCountBySubTaskIdVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static io.renren.modules.ltt.enums.PhoneStatus.*;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/2 00:45
 */
@Component
@Slf4j
@Profile({"prod","register"})
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
//    public static final Object lockCdRegisterSubtasksEntity = new Object();
//    public static final Object lockCdRegisterTaskEntity = new Object();
//    public static final Object lockCdGetPhoneEntity = new Object();
//    public static final Object lockCdLineRegisterEntity = new Object();
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private LineService lineService;
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

    @Resource(name = "cardJpServiceImpl")
    private FirefoxService cardJpService;

    @Resource(name = "firefoxServiceImpl")
    private FirefoxService firefoxServiceImpl;
    @Resource(name = "caffeineCacheCode")
    private Cache<String, String> caffeineCacheCode;

    @Resource(name = "cardJpSms")
    private Cache<String, Date> cardJpSms;
    /**
     *
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task7() {
        //获取所有已经发起注册的机器
        List<CdLineRegisterEntity> cdLineRegisterEntities = cdLineRegisterService.list(new QueryWrapper<CdLineRegisterEntity>().lambda()
                .in(CdLineRegisterEntity::getRegisterStatus,RegisterStatus.RegisterStatus3.getKey(),RegisterStatus.RegisterStatus1.getKey())
        );
        if (CollUtil.isEmpty(cdLineRegisterEntities)) {
            log.info("RegisterTask task7 cdLineRegisterEntities isEmpty");
            return;
        }
        for (CdLineRegisterEntity cdLineRegisterEntity : cdLineRegisterEntities) {
            poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource5, cdLineRegisterEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                CdLineRegisterEntity update = null;
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        RegisterResultDTO registerResultDTO = new RegisterResultDTO();
                        registerResultDTO.setTaskId(cdLineRegisterEntity.getTaskId());
                        RegisterResultVO registerResultVO = lineService.registerResult(registerResultDTO);
                        if (ObjectUtil.isNull(registerResultVO)) {
                            return;
                        }
                        if (200 == registerResultVO.getCode()) {
                            Long status = registerResultVO.getData().getStatus();
                            if (2 == status || 1 == status || 0 == status || Long.valueOf(20001).equals(status)) {
                                if (2 == status) {
                                    SyncLineTokenDTO syncLineTokenDTO = new SyncLineTokenDTO();
                                    syncLineTokenDTO.setTaskId(cdLineRegisterEntity.getTaskId());
                                    SyncLineTokenVO syncLineTokenVO = lineService.SyncLineTokenDTO(syncLineTokenDTO);
                                    if (ObjectUtil.isNull(syncLineTokenVO)) {
                                        return;
                                    }
                                    if (200 == syncLineTokenVO.getCode() && CollUtil.isNotEmpty(syncLineTokenVO.getData())) {
                                        SyncLineTokenVOData syncLineTokenVOData = syncLineTokenVO.getData().get(0);
                                        cdLineRegisterEntity.setRegisterStatus(RegisterStatus.RegisterStatus4.getKey());
                                        String token = syncLineTokenVOData.getToken();
                                        LineTokenJson lineTokenJson = JSON.parseObject(token, LineTokenJson.class);
                                        boolean accountExistStatus = lineTokenJson.isAccountExistStatus();
                                        if (accountExistStatus) {
                                            cdLineRegisterEntity.setAccountExistStatus(AccountExistStatus.AccountExistStatus2.getKey());
                                        }else {
                                            cdLineRegisterEntity.setAccountExistStatus(AccountExistStatus.AccountExistStatus1.getKey());
                                        }
                                        cdLineRegisterEntity.setToken(token);
                                        cdLineRegisterService.updateById(cdLineRegisterEntity);
                                    }
                                }
                                return;
                            }
                            cdLineRegisterEntity.setRegisterStatus(RegisterStatus.RegisterStatus5.getKey());
                            cdLineRegisterEntity.setErrMsg(registerResultVO.getData().getRemark());

                            CdGetPhoneEntity cdGetPhoneEntity = new CdGetPhoneEntity();
                            cdGetPhoneEntity.setId(cdLineRegisterEntity.getGetPhoneId());
                            cdGetPhoneEntity.setPhoneStatus(PhoneStatus6.getKey());
                            if (StrUtil.isNotEmpty(cdLineRegisterEntity.getPkey())) {
                                if (StringUtils.isNotEmpty(cdGetPhoneEntity.getCountrycode())
                                        && CountryCode.CountryCode3.getValue().equals(cdGetPhoneEntity.getCountrycode())) {
                                    //日本
                                    cardJpService.withBlackMobile(cdLineRegisterEntity.getPkey());
                                } else if (StringUtils.isNotEmpty(cdGetPhoneEntity.getCountrycode())
                                        && CountryCode.CountryCode5.getValue().equals(cdGetPhoneEntity.getCountrycode())) {
                                    firefoxServiceImpl.withBlackMobile(cdLineRegisterEntity.getPkey());
                                } else {
                                    firefoxService.withBlackMobile(cdLineRegisterEntity.getPkey());
                                }
                                cdGetPhoneEntity.setPhoneStatus(PhoneStatus6.getKey());
                            }

                            cdLineRegisterService.updateById(cdLineRegisterEntity);

                            cdGetPhoneService.updateById(cdGetPhoneEntity);
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


    Lock task1Lock = new ReentrantLock();


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
            log.info("RegisterTask task6 list isEmpty");
            return;
        }
        for (CdGetPhoneEntity cdGetPhoneEntity : list) {
            poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource4, cdGetPhoneEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                CdLineRegisterEntity update = null;
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        //如果需要释放
                        if(PhoneStatus5.getKey().equals(cdGetPhoneEntity.getPhoneStatus())) {
                            boolean b;

                            if (StringUtils.isNotEmpty(cdGetPhoneEntity.getCountrycode())
                                    && CountryCode.CountryCode3.getValue().equals(cdGetPhoneEntity.getCountrycode())) {
                                //日本
                                b = cardJpService.setRel(cdGetPhoneEntity.getPkey());
                            } else if (StringUtils.isNotEmpty(cdGetPhoneEntity.getCountrycode())
                                    && CountryCode.CountryCode5.getValue().equals(cdGetPhoneEntity.getCountrycode())) {
                                //香港
                                b = firefoxServiceImpl.setRel(cdGetPhoneEntity.getPkey());
                            } else {
                                b = firefoxService.setRel(cdGetPhoneEntity.getPkey());
                            }

                            if (b) {
                                cdGetPhoneEntity.setPhoneStatus(PhoneStatus6.getKey());
                            }
                            //如果需要修改验证码
                        }else {
                            String phoneCode = cdGetPhoneEntity.getCode();
                            //超过20分
                            long between = DateUtil.between(cdGetPhoneEntity.getCreateTime(), DateUtil.date(), DateUnit.MINUTE);
                            if (between > 6) {
                                cdGetPhoneEntity.setCode("验证码超时");
                                cdGetPhoneEntity.setPhoneStatus(PhoneStatus5.getKey());
                                return;
                            }
                            if (StrUtil.isEmpty(phoneCode) && StringUtils.isNotEmpty(cdGetPhoneEntity.getCountrycode())) {
                                if (CountryCode.CountryCode3.getValue().equals(cdGetPhoneEntity.getCountrycode())) {
                                    //日本
                                    cardJpSms.put(cdGetPhoneEntity.getPkey(), cdGetPhoneEntity.getCreateTime());
                                    phoneCode = cardJpService.getPhoneCode(cdGetPhoneEntity.getPkey());
                                } else if (CountryCode.CountryCode5.getValue().equals(cdGetPhoneEntity.getCountrycode())) {
                                    //香港
                                    phoneCode = firefoxServiceImpl.getPhoneCode(cdGetPhoneEntity.getPkey());
                                } else {
                                    phoneCode = firefoxService.getPhoneCode(cdGetPhoneEntity.getPkey());
                                }
                            }
                            if (StrUtil.isEmpty(phoneCode)) {
                                return;
                            }
                            log.info("phoneCode = {}", phoneCode);
                            if (StrUtil.isNotEmpty(phoneCode)) {
                                cdGetPhoneEntity.setCode(phoneCode);
                                cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus3.getKey());

                                CdLineRegisterVO lineRegisterVO = cdLineRegisterService.getById(cdGetPhoneEntity.getLineRegisterId());
                                if (ObjectUtil.isNull(lineRegisterVO)) {
                                    return;
                                }

                                SMSCodeDTO smsCodeDTO = new SMSCodeDTO();
                                smsCodeDTO.setsmsCode(phoneCode);
                                smsCodeDTO.setTaskId(lineRegisterVO.getTaskId());
                                SMSCodeVO smsCodeVO = lineService.smsCode(smsCodeDTO);
                                if (ObjectUtil.isNull(smsCodeVO)) {
                                    return;
                                }
                                log.info("smsCodeVO = {}", JSONUtil.toJsonStr(smsCodeVO));
                                if (200 == smsCodeVO.getCode()) {
                                    update = new CdLineRegisterEntity();
                                    update.setId(lineRegisterVO.getId());
                                    update.setRegisterStatus(RegisterStatus.RegisterStatus3.getKey());
                                    update.setSmsCode(phoneCode);
                                    cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus4.getKey());
                                }
                            }
                        }
                    }finally {
                        cdGetPhoneService.updateById(cdGetPhoneEntity);
                        if (ObjectUtil.isNotNull(update)) {
                            cdLineRegisterService.updateById(update);
                        }
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
        );
        if (CollUtil.isEmpty(list)) {
            log.info("RegisterTask task5 list isEmpty");
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
                        lineRegisterDTO.setCountryCode(cdGetPhoneEntity.getCountrycode());
                        lineRegisterDTO.setPhone(cdGetPhoneEntity.getPhone());
                        //获取代理
                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(cdGetPhoneEntity.getPhone());
                        cdLineIpProxyDTO.setLzPhone(cdGetPhoneEntity.getPhone());
                        if (ProxyStatus.ProxyStatus3.getKey().equals(projectWorkEntity.getProxy())) {
                            cdLineIpProxyDTO.setSelectProxyStatus(ProxyStatus.ProxyStatus2.getKey());
                        }
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
                            update.setCreateTime(DateUtil.date());

                            cdLineRegisterService.save(cdLineRegisterDTO);

                            update.setLineRegisterId(cdLineRegisterDTO.getId());
                            cdGetPhoneService.updateById(update);
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
    @Scheduled(fixedDelay = 10000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task3() {
        //获取所有子任务保存完成的
        List<CdRegisterTaskEntity> cdRegisterTaskEntities = cdRegisterTaskService.list(new QueryWrapper<CdRegisterTaskEntity>().lambda()
                .eq(CdRegisterTaskEntity::getRegistrationStatus,RegistrationStatus.RegistrationStatus2.getKey())
                .lt(CdRegisterTaskEntity::getFillUpRegisterTaskId,0)
                .or(item -> item.eq(CdRegisterTaskEntity::getFillUp,FillUp.YES.getKey()))
        );
        if (CollUtil.isEmpty(cdRegisterTaskEntities)) {
            log.info("RegisterTask task3 list isEmpty");
            return;
        }

        //所有注册的任务
        for (CdRegisterTaskEntity cdRegisterTaskEntity : cdRegisterTaskEntities) {
            poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource1, cdRegisterTaskEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        //获取所有子任务
                        List<CdRegisterSubtasksEntity> cdRegisterSubtasksEntities = cdRegisterSubtasksService.list(new QueryWrapper<CdRegisterSubtasksEntity>().lambda()
                                .eq(CdRegisterSubtasksEntity::getTaskId,cdRegisterTaskEntity.getId())
                        );
                        //获取所有的子任务Ids
                        List<Integer> registerSubtasksIds = cdRegisterSubtasksEntities.stream().map(CdRegisterSubtasksEntity::getId).collect(Collectors.toList());
                        if (CollUtil.isEmpty(registerSubtasksIds)) {
                            return;
                        }
                        List<GetCountBySubTaskIdVO> getCountBySubTaskIdVOS = cdLineRegisterService.getCountBySubTaskId(registerSubtasksIds);
                        Map<Integer, GetCountBySubTaskIdVO> integerGetCountBySubTaskIdVOMap = getCountBySubTaskIdVOS.stream().collect(Collectors.toMap(GetCountBySubTaskIdVO::getSubtasksId, item -> item));

                        Integer successTotal = 0;
                        Integer registerSuccessCount = 0;
                        Integer errorTotal = 0;
                        Integer totalNumber = 0;
                        for (CdRegisterSubtasksEntity cdRegisterSubtasksEntity : cdRegisterSubtasksEntities) {
                            GetCountBySubTaskIdVO getCountBySubTaskIdVO = integerGetCountBySubTaskIdVOMap.get(cdRegisterSubtasksEntity.getId());
                            if (ObjectUtil.isNull(getCountBySubTaskIdVO)) {
                                continue;
                            }
                            //设置成功数量
                            cdRegisterSubtasksEntity.setNumberSuccesses(getCountBySubTaskIdVO.getSuccessCount());
                            //设置失败数量
                            cdRegisterSubtasksEntity.setNumberFailures(getCountBySubTaskIdVO.getErrorCount());
                            errorTotal = errorTotal + cdRegisterSubtasksEntity.getNumberFailures();
                            successTotal = successTotal + cdRegisterSubtasksEntity.getNumberSuccesses();
                            registerSuccessCount = registerSuccessCount + getCountBySubTaskIdVO.getRegisterSuccessCount();
                            totalNumber = totalNumber + cdRegisterSubtasksEntity.getNumberRegistrations();
                        }
                        //如果 注册成功了，去修改状态
                        if (registerSuccessCount >= cdRegisterTaskEntity.getNumberRegistered()) {
                            cdRegisterTaskEntity.setRegistrationStatus(RegistrationStatus.RegistrationStatus7.getKey());
                        }else {
                            Integer count = cdRegisterTaskService.sumByTaskId(cdRegisterTaskEntity.getId());
                            //说明都已经去注册了，注册剩余的数量去
                            if (successTotal + errorTotal >= count) {
                                CdRegisterTaskEntity newCdRegisterTaskEntity = new CdRegisterTaskEntity();
                                Integer newTotalAmount = cdRegisterTaskEntity.getTotalAmount() - successTotal;
                                if (newTotalAmount > 0) {
                                    newCdRegisterTaskEntity.setTotalAmount(newTotalAmount);
                                    newCdRegisterTaskEntity.setNumberThreads(cdRegisterTaskEntity.getNumberThreads());
                                    newCdRegisterTaskEntity.setNumberRegistered(0);
                                    newCdRegisterTaskEntity.setNumberSuccesses(0);
                                    newCdRegisterTaskEntity.setNumberFailures(0);
                                    newCdRegisterTaskEntity.setRegistrationStatus(RegistrationStatus.RegistrationStatus1.getKey());
                                    newCdRegisterTaskEntity.setDeleteFlag(DeleteFlag.NO.getKey());
                                    newCdRegisterTaskEntity.setCountryCode(cdRegisterTaskEntity.getCountryCode());
                                    newCdRegisterTaskEntity.setFillUp(cdRegisterTaskEntity.getFillUp());
                                    newCdRegisterTaskEntity.setFillUpRegisterTaskId(cdRegisterTaskEntity.getId());
                                    newCdRegisterTaskEntity.setCreateTime(DateUtil.date());
                                    cdRegisterTaskService.save(newCdRegisterTaskEntity);
                                }
                            }
                        }
                        //成功数量
                        cdRegisterTaskEntity.setNumberSuccesses(successTotal);
                        //失败数量
                        cdRegisterTaskEntity.setNumberFailures(errorTotal);

                        cdRegisterTaskService.updateById(cdRegisterTaskEntity);

                        cdRegisterSubtasksService.updateBatchById(cdRegisterSubtasksEntities);
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
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource2, 1);
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = false;
                try {
                    triedLock = lock.tryLock();
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
                                    cdGetPhoneDTO.setCountrycode(CountryCode.getValueByKey(cdRegisterSubtasksEntity.getCountryCode()));
                                    List<CdGetPhoneEntity> cdGetPhoneEntities = cdGetPhoneService.addCount(cdGetPhoneDTO);
                                    //如果数量相等
                                    if (cdRegisterSubtasksEntity.getNumberRegistrations().equals(cdGetPhoneEntities.size() + count)) {
                                        CdRegisterSubtasksEntity update = new CdRegisterSubtasksEntity();
                                        update.setId(cdRegisterSubtasksEntity.getId());
                                        update.setRegistrationStatus(RegistrationStatus.RegistrationStatus6.getKey());
                                        cdRegisterSubtasksService.updateById(update);
                                    }
                                }
                                return;
                            }
                            CdGetPhoneDTO cdGetPhoneDTO = new CdGetPhoneDTO();
                            cdGetPhoneDTO.setCount(cdRegisterSubtasksEntity.getNumberRegistrations());
                            cdGetPhoneDTO.setSubtasksId(cdRegisterSubtasksEntity.getId());
                            cdGetPhoneDTO.setCountrycode(CountryCode.getValueByKey(cdRegisterSubtasksEntity.getCountryCode()));
                            List<CdGetPhoneEntity> cdGetPhoneEntities = cdGetPhoneService.addCount(cdGetPhoneDTO);
                            //如果数量相等
                            if (CollUtil.isNotEmpty(cdGetPhoneEntities)) {
                                CdRegisterSubtasksEntity update = new CdRegisterSubtasksEntity();
                                update.setId(cdRegisterSubtasksEntity.getId());
                                update.setRegistrationStatus(RegistrationStatus.RegistrationStatus2.getKey());
                                if (cdRegisterSubtasksEntity.getNumberRegistrations().equals(cdGetPhoneEntities.size())) {
                                    update.setRegistrationStatus(RegistrationStatus.RegistrationStatus6.getKey());
                                }
                                cdRegisterSubtasksService.updateById(update);
                            }
                        }finally {
                            lock.unlock();
                        }
                    }else {
                        log.info("keyByResource = {} 在执行",keyByResource);
                    }
                } catch (Exception e) {
                    log.error("task2_error {}", e);
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
                                if (cdRegisterTaskEntity.getFillUpRegisterTaskId() > 0) {
                                    cdRegisterSubtasksEntity.setTaskId(cdRegisterTaskEntity.getFillUpRegisterTaskId());
                                }
                                cdRegisterSubtasksEntity.setCountryCode(cdRegisterTaskEntity.getCountryCode());
                                cdRegisterSubtasksEntity.setNumberRegistrations(newNumberRegistrations > 0 ? newNumberRegistrations : numberRegistered);
                                cdRegisterSubtasksEntity.setNumberSuccesses(0);
                                cdRegisterSubtasksEntity.setNumberFailures(0);
                                cdRegisterSubtasksEntity.setRegistrationStatus(RegistrationStatus.RegistrationStatus1.getKey());
                                cdRegisterSubtasksEntity.setCreateTime(DateUtil.date());
                                cdRegisterSubtasksEntity.setDeleteFlag(DeleteFlag.NO.getKey());
                                cdRegisterSubtasksEntities.add(cdRegisterSubtasksEntity);
                                //设置主表注册数量
                                cdRegisterTaskEntity.setNumberRegistered(cdRegisterTaskEntity.getNumberRegistered() + newNumberRegistrations);
                            }
                            //保存子任务
                            cdRegisterSubtasksService.saveBatch(cdRegisterSubtasksEntities,cdRegisterSubtasksEntities.size());
                            //修改状态
                            cdRegisterTaskService.updateById(cdRegisterTaskEntity);
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

//
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
