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
import io.renren.common.base.vo.EnumVo;
import io.renren.common.constant.SystemConstant;
import io.renren.common.utils.ConfigConstant;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.EnumUtil;
import io.renren.modules.client.FirefoxService;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.*;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.client.vo.*;
import io.renren.modules.ltt.dto.CdGetPhoneDTO;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.*;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static io.renren.modules.client.impl.CardJpServiceImpl.extractVerificationCode;
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

//curl -x socks5h://chenweilong_112233-country-jp:ch1433471850@proxyus.rola.vip:2000 http://www.ip234.in/ip.json

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

    @Resource(name = "cardJpSFServiceImpl")
    private FirefoxService cardJpSFService;

    @Resource(name = "firefoxServiceImpl")
    private FirefoxService firefoxServiceImpl;
    @Resource(name = "caffeineCacheCode")
    private Cache<String, String> caffeineCacheCode;

    @Resource(name = "cardJpSms")
    private Cache<String, Date> cardJpSms;


    @Resource(name = "cardJpSmsOver")
    private Cache<String, String> cardJpSmsOver;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplateObj;

    /**
     * 更新实体类
     */
    @Scheduled(fixedDelay = 5000)
    @Async
    public void task9() {
        //服务器更新锁先锁住方法
        Integer mod = systemConstant.getSERVERS_MOD();
        String key = String.valueOf(mod);
        try{
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            if (ObjectUtil.isNull(projectWorkEntity)) {
                return;
            }
            List<CdGetPhoneEntity> list = cdGetPhoneService.list(new QueryWrapper<CdGetPhoneEntity>().lambda()
                    .eq(CdGetPhoneEntity::getPhoneStatus, PhoneStatus8.getKey())
                    .last("limit 200")
                    .orderByDesc(CdGetPhoneEntity::getId)
            );
            if (CollUtil.isEmpty(list)) {
                log.info("RegisterTask task5 list isEmpty");
                return;
            }
            for (CdGetPhoneEntity cdGetPhoneEntity : list) {
                poolExecutor.execute(() -> {
                    try{
                        CdLineRegisterEntity lineRegisterVO = (CdLineRegisterEntity) redisTemplateObj.opsForHash().get(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                        if (ObjectUtil.isNull(lineRegisterVO)) {
                            return;
                        }
                        Boolean b1 = redisTemplateObj.opsForHash().putIfAbsent(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST_STATUS8.getValue(key), String.valueOf(cdGetPhoneEntity.getId()), cdGetPhoneEntity.getId());
                        if (!b1) {
                            return;
                        }
                        cdGetPhoneEntity.setPhoneStatus(PhoneStatus9.getKey());
                        //生成token
                        AtUserTokenEntity userTokenEntity = new AtUserTokenEntity();
                        userTokenEntity.setToken(lineRegisterVO.getToken());
                        userTokenEntity.setUserGroupId(null);
                        boolean save = atUserTokenService.save(userTokenEntity);
                        if (save) {
                            //设置修改队列
                            Long l2 = redisTemplateObj.opsForList().leftPush(RedisKeys.CDGETPHONEENTITY_UPDATE_LIST.getValue(key), cdGetPhoneEntity);
                        }
                    }catch (Exception e){
                        log.error("lineRegister err = {}",e);
                    }
                });
            }
        }catch (Exception e){
            log.error(" err = {}",e.getMessage());
        }
    }


    /**
     * 更新实体类
     */
    @Scheduled(fixedDelay = 5000)
    @Async
    public void task8() {
        //服务器更新锁
        Integer mod = systemConstant.getSERVERS_MOD();
        String key = String.valueOf(mod);
        String MOD_REGISTER_NX_KEY = RedisKeys.MOD_REGISTER_NX.getValue(key+8);
        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(MOD_REGISTER_NX_KEY, key);
        if (!b) {
            return;
        }
        redisTemplate.expire(MOD_REGISTER_NX_KEY, 2, TimeUnit.MINUTES);
        try {
            //用户任务队列保存
            List<CdGetPhoneEntity> elementsGetPhone = new ArrayList<>();
            CdGetPhoneEntity cdGetPhoneEntity = null;
            while ((cdGetPhoneEntity = (CdGetPhoneEntity) redisTemplateObj.opsForList().rightPop(RedisKeys.CDGETPHONEENTITY_UPDATE_LIST.getValue(key))) != null) {
                elementsGetPhone.add(cdGetPhoneEntity);
            }
            if (CollUtil.isNotEmpty(elementsGetPhone)) {
                try{
                    boolean b1 = cdGetPhoneService.updateBatchById(elementsGetPhone);
                    //如果保存失败，把数据重新保存
                    if (!b1) {
                        for (CdGetPhoneEntity elementscdGetPhoneEntity : elementsGetPhone) {
                            Long l = redisTemplateObj.opsForList().leftPush(RedisKeys.CDGETPHONEENTITY_UPDATE_LIST.getValue(key), elementscdGetPhoneEntity);
                        }
                    }
                    //如果保存失败，把数据重新保存
                }catch (Exception e) {
                    for (CdGetPhoneEntity elementscdGetPhoneEntity : elementsGetPhone) {
                        Long l = redisTemplateObj.opsForList().leftPush(RedisKeys.CDGETPHONEENTITY_UPDATE_LIST.getValue(key), elementscdGetPhoneEntity);
                    }
                }
            }
        }catch (Exception e){
            log.error("data atDataTaskService.updateBatchById atGroupService.updateBatchById atUserService.updateBatchById task7 err = {}",e.getMessage());
        }finally {
            if (b) {
                stringRedisTemplate.delete(MOD_REGISTER_NX_KEY);
            }
        }

    }



    /**
     *
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task7() {
        //服务器更新锁先锁住方法
        Integer mod = systemConstant.getSERVERS_MOD();
        String key = String.valueOf(mod);

        List<CdGetPhoneEntity> cdGetPhoneEntities = cdGetPhoneService.list(new QueryWrapper<CdGetPhoneEntity>().lambda()
                .in(CdGetPhoneEntity::getPhoneStatus, PhoneStatus4.getKey(),PhoneStatus2.getKey())
                .last("limit 200")
                .orderByDesc(CdGetPhoneEntity::getId)
        );
        if (CollUtil.isEmpty(cdGetPhoneEntities)) {
            log.info("RegisterTask task7 cdLineRegisterEntities isEmpty");
            return;
        }
        for (CdGetPhoneEntity cdGetPhoneEntity : cdGetPhoneEntities) {
            poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource5, cdGetPhoneEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                CdLineRegisterEntity update = null;
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        CdLineRegisterEntity lineRegisterVO = (CdLineRegisterEntity) redisTemplateObj.opsForHash().get(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                        if (ObjectUtil.isNull(lineRegisterVO)) {
                            return;
                        }
                        if (RegisterStatus.RegisterStatus4.getKey().equals(lineRegisterVO.getRegisterStatus())) {
                            return;
                        }
                        if (StrUtil.isEmpty(lineRegisterVO.getTaskId())) {
                            return;
                        }
                        RegisterResultDTO registerResultDTO = new RegisterResultDTO();
                        registerResultDTO.setTaskId(lineRegisterVO.getTaskId());
                        RegisterResultVO registerResultVO = lineService.registerResult(registerResultDTO);
                        if (ObjectUtil.isNull(registerResultVO)) {
                            //
                            return;
                        }
                        if (200 == registerResultVO.getCode()) {
                            Long status = registerResultVO.getData().getStatus();
                            if (2 == status || 1 == status || 0 == status || Long.valueOf(20001).equals(status)) {
                                if (2 == status) {
                                    SyncLineTokenDTO syncLineTokenDTO = new SyncLineTokenDTO();
                                    syncLineTokenDTO.setTaskId(lineRegisterVO.getTaskId());
                                    SyncLineTokenVO syncLineTokenVO = lineService.SyncLineTokenDTO(syncLineTokenDTO);
                                    if (ObjectUtil.isNull(syncLineTokenVO)) {
                                        return;
                                    }
                                    if (200 == syncLineTokenVO.getCode() && CollUtil.isNotEmpty(syncLineTokenVO.getData())) {
                                        //更新手机号注册次数
                                        cdGetPhoneService.savePhoneRegisterCount(cdGetPhoneEntity.getPhone());

                                        SyncLineTokenVOData syncLineTokenVOData = syncLineTokenVO.getData().get(0);
                                        lineRegisterVO.setRegisterStatus(RegisterStatus.RegisterStatus4.getKey());
                                        String token = syncLineTokenVOData.getToken();
                                        LineTokenJson lineTokenJson = JSON.parseObject(token, LineTokenJson.class);
                                        boolean accountExistStatus = lineTokenJson.isAccountExistStatus();
                                        if (accountExistStatus) {
                                            lineRegisterVO.setAccountExistStatus(AccountExistStatus.AccountExistStatus2.getKey());
                                        }else {
                                            lineRegisterVO.setAccountExistStatus(AccountExistStatus.AccountExistStatus1.getKey());
                                        }
                                        lineRegisterVO.setToken(token);

                                        CdGetPhoneEntity cdGetPhoneEntityUpdate = new CdGetPhoneEntity();
                                        cdGetPhoneEntityUpdate.setId(lineRegisterVO.getGetPhoneId());
                                        cdGetPhoneEntityUpdate.setPhoneStatus(PhoneStatus8.getKey());
                                        //设置修改队列
                                        Long l2 = redisTemplateObj.opsForList().leftPush(RedisKeys.CDGETPHONEENTITY_UPDATE_LIST.getValue(key), cdGetPhoneEntityUpdate);
                                    }
                                }
                                redisTemplateObj.opsForHash().put(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()),lineRegisterVO);
                                return;
                            }

                            lineRegisterVO.setRegisterStatus(RegisterStatus.RegisterStatus5.getKey());
                            lineRegisterVO.setErrMsg(registerResultVO.getData().getRemark());
                            CdGetPhoneEntity cdGetPhoneEntityUpdate = new CdGetPhoneEntity();
                            cdGetPhoneEntityUpdate.setId(lineRegisterVO.getGetPhoneId());
                            cdGetPhoneEntityUpdate.setPhoneStatus(PhoneStatus6.getKey());
                            //设置修改队列
                            Long l2 = redisTemplateObj.opsForList().leftPush(RedisKeys.CDGETPHONEENTITY_UPDATE_LIST.getValue(key), cdGetPhoneEntityUpdate);

                            redisTemplateObj.opsForHash().put(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()),lineRegisterVO);
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
     * 获取验证码
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    public void task10() {
        //服务器更新锁
        Integer mod = systemConstant.getSERVERS_MOD();
        String key = String.valueOf(mod);
        try{
            //获取验证码
            List<CdGetPhoneEntity> list = cdGetPhoneService.list(new QueryWrapper<CdGetPhoneEntity>().lambda()
                            .in(CdGetPhoneEntity::getPhoneStatus, PhoneStatus.PhoneStatus2.getKey())
                            .eq(CdGetPhoneEntity::getCountry,"81")
                            .orderByDesc(CdGetPhoneEntity::getId)
            );
            String pks = list.stream().map(CdGetPhoneEntity::getPkey).collect(Collectors.joining(","));
            if (StrUtil.isEmpty(pks)) {
                return;
            }
            Map<Long, CardJpGetPhoneSmsVO.Data.Ret.Sm> phoneCodes = cardJpService.getPhoneCodes(pks);
            Map<String, CdGetPhoneEntity> collect = list.stream().collect(Collectors.toMap(CdGetPhoneEntity::getPkey, item -> item, (a, b) -> a));
            for (Long l : phoneCodes.keySet()) {
                poolExecutor.submit(() -> {
                    CdGetPhoneEntity cdGetPhoneEntity = collect.get(String.valueOf(l));
                    if (ObjectUtil.isNull(cdGetPhoneEntity)) {
                        return;
                    }
                    Boolean b1 = redisTemplateObj.opsForHash().putIfAbsent(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST_STATUS2.getValue(key), String.valueOf(cdGetPhoneEntity.getId()), cdGetPhoneEntity.getId());
                    if (!b1) {
                        return;
                    }
                    long between = DateUtil.between(cdGetPhoneEntity.getCreateTime(), DateUtil.date(), DateUnit.MINUTE);
                    if (between > 20) {
                        cdGetPhoneEntity.setCode("验证码超时");
                        cdGetPhoneEntity.setPhoneStatus(PhoneStatus5.getKey());
                        CdLineRegisterEntity lineRegisterVO = (CdLineRegisterEntity) redisTemplateObj.opsForHash().get(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                        if (ObjectUtil.isNull(lineRegisterVO)) {
                            return;
                        }
                        lineRegisterVO.setRegisterStatus(RegisterStatus.RegisterStatus5.getKey());
                        redisTemplateObj.opsForHash().put(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()),lineRegisterVO);
                        //设置修改队列
                        Long l2 = redisTemplateObj.opsForList().leftPush(RedisKeys.CDGETPHONEENTITY_UPDATE_LIST.getValue(key), cdGetPhoneEntity);
                        return;
                    }
                    CardJpGetPhoneSmsVO.Data.Ret.Sm sm = phoneCodes.get(l);
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(sm.getTime()), ZoneId.of("Asia/Shanghai"));
                    ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
                    Date recvTime = Date.from(zonedDateTime.toInstant());
                    boolean before = cdGetPhoneEntity.getCreateTime().before(recvTime);
                    if (before) {
                        String phoneCode = extractVerificationCode(sm.getContent());
                        cdGetPhoneEntity.setCode(phoneCode);
                        cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus3.getKey());
                        CdLineRegisterEntity lineRegisterVO = (CdLineRegisterEntity) redisTemplateObj.opsForHash().get(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                        if (ObjectUtil.isNull(lineRegisterVO)) {
                            return;
                        }
                        SMSCodeDTO smsCodeDTO = new SMSCodeDTO();
                        smsCodeDTO.setsmsCode(phoneCode);
                        smsCodeDTO.setTaskId(lineRegisterVO.getTaskId());
                        SMSCodeVO smsCodeVO = lineService.smsCode(smsCodeDTO);
                        if (ObjectUtil.isNull(smsCodeVO)) {
                            redisTemplateObj.opsForHash().delete(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST_STATUS2.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                            return;
                        }
                        if (200 == smsCodeVO.getCode()) {
                            lineRegisterVO.setRegisterStatus(RegisterStatus.RegisterStatus3.getKey());
                            lineRegisterVO.setSmsCode(phoneCode);
                            cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus4.getKey());
                            //设置修改队列
                            Long l2 = redisTemplateObj.opsForList().leftPush(RedisKeys.CDGETPHONEENTITY_UPDATE_LIST.getValue(key), cdGetPhoneEntity);
                            log.info("修改队列插入成功 {}",l2);
                            redisTemplateObj.opsForHash().put(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()),lineRegisterVO);
                        }
                    }else {
                        redisTemplateObj.opsForHash().delete(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST_STATUS2.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                    }
                });
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {

        }

    }


    /**
     *
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task6() {
        //服务器更新锁
        Integer mod = systemConstant.getSERVERS_MOD();
        String key = String.valueOf(mod);
        try {
            //获取验证码
            List<CdGetPhoneEntity> list = cdGetPhoneService.list(new QueryWrapper<CdGetPhoneEntity>().lambda()
                            .in(CdGetPhoneEntity::getPhoneStatus, PhoneStatus.PhoneStatus2.getKey())
                            .notIn(CdGetPhoneEntity::getCountry,"81")
                            .orderByDesc(CdGetPhoneEntity::getId)
            );
            if (CollUtil.isEmpty(list)) {
                log.info("RegisterTask task6 list isEmpty");
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
                            Boolean b1 = redisTemplateObj.opsForHash().putIfAbsent(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST_STATUS2.getValue(key), String.valueOf(cdGetPhoneEntity.getId()), String.valueOf(cdGetPhoneEntity.getId()));
                            if (!b1) {
                                return;
                            }
                            String phoneCode = cdGetPhoneEntity.getCode();
                            long between = DateUtil.between(cdGetPhoneEntity.getCreateTime(), DateUtil.date(), DateUnit.MINUTE);
                            if (between > 20) {
                                cdGetPhoneEntity.setCode("验证码超时");
                                cdGetPhoneEntity.setPhoneStatus(PhoneStatus5.getKey());

                                CdLineRegisterEntity lineRegisterVO = (CdLineRegisterEntity) redisTemplateObj.opsForHash().get(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                                if (ObjectUtil.isNull(lineRegisterVO)) {
                                    return;
                                }
                                lineRegisterVO.setRegisterStatus(RegisterStatus.RegisterStatus5.getKey());
                                redisTemplateObj.opsForHash().put(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()),lineRegisterVO);
                                //设置修改队列
                                Long l2 = redisTemplateObj.opsForList().leftPush(RedisKeys.CDGETPHONEENTITY_UPDATE_LIST.getValue(key), cdGetPhoneEntity);
                                return;
                            }
                            if (StrUtil.isEmpty(phoneCode) && StringUtils.isNotEmpty(cdGetPhoneEntity.getCountrycode())) {
                                if (CountryCode.CountryCode3.getValue().equals(cdGetPhoneEntity.getCountrycode()) && CountryCode.CountryCode3.getKey().toString().equals(cdGetPhoneEntity.getCountry())) {
                                    //日本
                                    if (cardJpSmsOver.getIfPresent("jpSmsOverFlag") != null) {
                                        redisTemplateObj.opsForHash().delete(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                                        return;
                                    }
                                    cardJpSms.put(cdGetPhoneEntity.getPkey(), cdGetPhoneEntity.getCreateTime());
                                    phoneCode = cardJpService.getPhoneCode(cdGetPhoneEntity.getPkey());
                                }else if (CountryCode.CountryCode8.getValue().equals(cdGetPhoneEntity.getCountrycode())  && CountryCode.CountryCode8.getKey().toString().equals(cdGetPhoneEntity.getCountry())) {
                                    cardJpSms.put(cdGetPhoneEntity.getPkey(), cdGetPhoneEntity.getCreateTime());
                                    String s = cdGetPhoneEntity.getPkey() + "#" + cdGetPhoneEntity.getSfApi() + "#" + cdGetPhoneEntity.getTimeZone();
                                    //日本-四方
                                    phoneCode = cardJpSFService.getPhoneCode(s);
                                } else if (CountryCode.CountryCode5.getValue().equals(cdGetPhoneEntity.getCountrycode())) {
                                    //香港
                                    phoneCode = firefoxServiceImpl.getPhoneCode(cdGetPhoneEntity.getPkey());
                                } else {
                                    phoneCode = firefoxService.getPhoneCode(cdGetPhoneEntity.getPkey());
                                }
                            }
                            if (StrUtil.isEmpty(phoneCode)) {
                                redisTemplateObj.opsForHash().delete(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST_STATUS2.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                                return;
                            }
                            log.info("phoneCode = {}", phoneCode);
                            cdGetPhoneEntity.setCode(phoneCode);
                            cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus3.getKey());
                            CdLineRegisterEntity lineRegisterVO = (CdLineRegisterEntity) redisTemplateObj.opsForHash().get(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                            if (ObjectUtil.isNull(lineRegisterVO)) {
                                return;
                            }
                            SMSCodeDTO smsCodeDTO = new SMSCodeDTO();
                            smsCodeDTO.setsmsCode(phoneCode);
                            smsCodeDTO.setTaskId(lineRegisterVO.getTaskId());
                            SMSCodeVO smsCodeVO = lineService.smsCode(smsCodeDTO);
                            if (ObjectUtil.isNull(smsCodeVO)) {
                                redisTemplateObj.opsForHash().delete(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST_STATUS2.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                                return;
                            }
                            log.info("smsCodeVO = {}", JSONUtil.toJsonStr(smsCodeVO));
                            if (200 == smsCodeVO.getCode()) {
                                lineRegisterVO.setId(lineRegisterVO.getId());
                                lineRegisterVO.setRegisterStatus(RegisterStatus.RegisterStatus3.getKey());
                                lineRegisterVO.setSmsCode(phoneCode);
                                cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus4.getKey());
                                //设置修改队列
                                Long l2 = redisTemplateObj.opsForList().leftPush(RedisKeys.CDGETPHONEENTITY_UPDATE_LIST.getValue(key), cdGetPhoneEntity);
                                log.info("修改队列插入成功 PhoneStatus4 {}",l2);
                                redisTemplateObj.opsForHash().put(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()),lineRegisterVO);
                            }
                        }finally {
                            lock.unlock();
                        }
                    }else {
                        log.info("keyByResource = {} 在执行",keyByResource);
                    }
                });
            }
        }catch (Exception e){
            log.error("err = {}",e.getMessage());
        }finally {
        }

    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SystemConstant systemConstant;

    /**
    /**
     * 使用协议去注册
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task5() {
        //服务器更新锁先锁住方法
        Integer mod = systemConstant.getSERVERS_MOD();
        String key = String.valueOf(mod);
        try{
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            if (ObjectUtil.isNull(projectWorkEntity)) {
                return;
            }
            List<CdGetPhoneEntity> list = cdGetPhoneService.list(new QueryWrapper<CdGetPhoneEntity>().lambda()
                    .eq(CdGetPhoneEntity::getPhoneStatus, PhoneStatus.PhoneStatus1.getKey())
                    .last("limit 500")
                    .orderByDesc(CdGetPhoneEntity::getId)
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
                            //注册表整理
                            CdLineRegisterEntity cdLineRegisterDTO = new CdLineRegisterEntity();
                            cdLineRegisterDTO.setRegisterStatus(RegisterStatus.RegisterStatus1.getKey());
                            cdLineRegisterDTO.setDeleteFlag(DeleteFlag.NO.getKey());
                            cdLineRegisterDTO.setCreateTime(DateUtil.date());
                            cdLineRegisterDTO.setGetPhoneId(cdGetPhoneEntity.getId());
                            cdLineRegisterDTO.setPkey(cdGetPhoneEntity.getPkey());
                            cdLineRegisterDTO.setSubtasksId(cdGetPhoneEntity.getSubtasksId());
                            //加锁只能成功一次
                            Boolean b1 = redisTemplateObj.opsForHash().putIfAbsent(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()), cdLineRegisterDTO);
                            if (!b1) {
                                return;
                            }

                            LineRegisterDTO lineRegisterDTO = new LineRegisterDTO();
                            lineRegisterDTO.setAb(projectWorkEntity.getLineAb());
                            lineRegisterDTO.setAppVersion(projectWorkEntity.getLineAppVersion());
                            lineRegisterDTO.setCountryCode(cdGetPhoneEntity.getCountrycode());
                            lineRegisterDTO.setPhone(cdGetPhoneEntity.getPhone());
                            //获取代理
                            CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                            cdLineIpProxyDTO.setTokenPhone(cdGetPhoneEntity.getPhone());
                            cdLineIpProxyDTO.setLzPhone(cdGetPhoneEntity.getPhone());
                            //注册任务设置代理
                            CdRegisterSubtasksVO registerSubtasksVO = cdRegisterSubtasksService.getById(cdGetPhoneEntity.getSubtasksId());
                            if (ObjectUtil.isNotNull(registerSubtasksVO)) {
                                String proxyId = (String) redisTemplate.opsForHash().get(RedisKeys.RedisKeys5.getValue(), String.valueOf(registerSubtasksVO.getTaskId()));
                                if (StrUtil.isNotEmpty(proxyId)) {
                                    Integer i = Integer.valueOf(proxyId);
                                    cdLineIpProxyDTO.setSelectProxyStatus(i);
                                }
                            }
                            String proxyId = (String) redisTemplate.opsForHash().get(RedisKeys.RedisKeys5.getValue(), String.valueOf(cdGetPhoneEntity.getPhone()));
                            if (StrUtil.isNotEmpty(proxyId)) {
                                Integer i = Integer.valueOf(proxyId);
                                cdLineIpProxyDTO.setSelectProxyStatus(i);
                            }


                            String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                            if (StrUtil.isEmpty(proxyIp)) {
                                //设置保存队列
                                redisTemplateObj.opsForHash().delete(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                                return;
                            }

                            lineRegisterDTO.setProxy(proxyIp);
                            lineRegisterDTO.setTxtToken(projectWorkEntity.getLineTxtToken());
                            LineRegisterVO lineRegisterVO = lineService.lineRegister(lineRegisterDTO);
                            if (ObjectUtil.isNull(lineRegisterVO)) {
                                //如果获取失败删除这个id
                                redisTemplateObj.opsForHash().delete(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()));
                                return;
                            }
                            //注册成功数据
                            log.info("lineRegisterVO = {}", JSONUtil.toJsonStr(lineRegisterVO));
                            // 提交成功
                            if (200 == lineRegisterVO.getCode()) {
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
                                //设置修改队列getphone
                                Long l2 = redisTemplateObj.opsForList().leftPush(RedisKeys.CDGETPHONEENTITY_UPDATE_LIST.getValue(key), update);
                                log.info("redisTemplateObj.opsForList().leftPush l2 = {}",l2);
                                //设置修改队列lineregister
                                redisTemplateObj.opsForHash().put(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhoneEntity.getId()), cdLineRegisterDTO);
                            }
                        }catch (Exception e){
                            log.error("lineRegister err = {}",e);
                        }finally {
                            lock.unlock();
                        }
                    }else {
                        log.info("keyByResource = {} 在执行",keyByResource);
                    }
                });
            }
        }catch (Exception e){
            log.error(" err = {}",e.getMessage());
        }finally {

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
                            int count = getCountBySubTaskIdVO.getSuccessCount() + getCountBySubTaskIdVO.getErrorCount();
                            if (count >= cdRegisterSubtasksEntity.getNumberRegistrations()) {
                                cdRegisterSubtasksEntity.setRegistrationStatus(RegistrationStatus.RegistrationStatus6.getKey());
                            }

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
                                    newCdRegisterTaskEntity.setTaskName("");
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
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource2, cdRegisterSubtasksEntity.getCountryCode());
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
                                if (cdRegisterSubtasksEntity.getNumberRegistrations() > count) {
                                    CdGetPhoneDTO cdGetPhoneDTO = new CdGetPhoneDTO();
                                    cdGetPhoneDTO.setCount(cdRegisterSubtasksEntity.getNumberRegistrations() - count);
                                    cdGetPhoneDTO.setSubtasksId(cdRegisterSubtasksEntity.getId());
                                    cdGetPhoneDTO.setCountrycode(CountryCode.getValueByKey(cdRegisterSubtasksEntity.getCountryCode()));
                                    cdGetPhoneDTO.setCountrycodeKey(cdRegisterSubtasksEntity.getCountryCode());
                                    List<CdGetPhoneEntity> cdGetPhoneEntities = cdGetPhoneService.addCount(cdGetPhoneDTO);
                                    //如果数量相等
                                    if (cdRegisterSubtasksEntity.getNumberRegistrations().equals(cdGetPhoneEntities.size() + count)) {
                                        CdRegisterSubtasksEntity update = new CdRegisterSubtasksEntity();
                                        update.setId(cdRegisterSubtasksEntity.getId());
                                        update.setRegistrationStatus(RegistrationStatus.RegistrationStatus2.getKey());
                                        cdRegisterSubtasksService.updateById(update);
                                    }
                                }
                                return;
                            }
                            CdGetPhoneDTO cdGetPhoneDTO = new CdGetPhoneDTO();
                            cdGetPhoneDTO.setCount(cdRegisterSubtasksEntity.getNumberRegistrations());
                            cdGetPhoneDTO.setSubtasksId(cdRegisterSubtasksEntity.getId());
                            cdGetPhoneDTO.setCountrycode(CountryCode.getValueByKey(cdRegisterSubtasksEntity.getCountryCode()));
                            cdGetPhoneDTO.setCountrycodeKey(cdRegisterSubtasksEntity.getCountryCode());
                            List<CdGetPhoneEntity> cdGetPhoneEntities = cdGetPhoneService.addCount(cdGetPhoneDTO);
                            //如果数量相等
                            if (CollUtil.isNotEmpty(cdGetPhoneEntities)) {
                                CdRegisterSubtasksEntity update = new CdRegisterSubtasksEntity();
                                update.setId(cdRegisterSubtasksEntity.getId());
                                update.setRegistrationStatus(RegistrationStatus.RegistrationStatus2.getKey());
//                                if (cdRegisterSubtasksEntity.getNumberRegistrations().equals(cdGetPhoneEntities.size())) {
//                                    update.setRegistrationStatus(RegistrationStatus.RegistrationStatus6.getKey());
//                                }
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
                        //如果是注册四方直接返回
                        if (CountryCode.CountryCode8.getKey().equals(cdRegisterTaskEntity.getCountryCode())) {
                            return;
                        }
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
