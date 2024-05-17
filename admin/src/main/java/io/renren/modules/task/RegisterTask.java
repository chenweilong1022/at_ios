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
import io.renren.modules.ltt.dto.CdRegisterRedisDto;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.*;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.service.impl.AsyncService;
import io.renren.modules.ltt.vo.CdRegisterSubtasksVO;
import io.renren.modules.ltt.vo.GetCountBySubTaskIdVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.concurrent.CountDownLatch;
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
@Profile({"prod", "dev"})
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

    @Resource(name = "cardJpSms")
    private Cache<String, Date> cardJpSms;


    @Resource(name = "cardJpSmsOver")
    private Cache<String, String> cardJpSmsOver;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisObjectTemplate;

    public CdRegisterRedisDto submitRegister(ProjectWorkEntity projectWorkEntity,
                                             CdRegisterRedisDto registerRedisDto) {
        //获取所有已经发起注册的机器
        if (ObjectUtil.isNull(projectWorkEntity)
                || ObjectUtil.isNull(registerRedisDto)
                || ObjectUtil.isNull(registerRedisDto.getPhoneEntity())
                || ObjectUtil.isNull(registerRedisDto.getLineRegister())) {
            log.info("发起注册，入参为空，请检查 {}, {}", registerRedisDto, projectWorkEntity);
            return registerRedisDto;
        }

        CdLineRegisterEntity lineRegisterEntity = registerRedisDto.getLineRegister();


        RegisterResultDTO registerResultDTO = new RegisterResultDTO();
        registerResultDTO.setTaskId(lineRegisterEntity.getTaskId());
        RegisterResultVO registerResultVO = lineService.registerResult(registerResultDTO);
        log.info("发起注册返回结果 {}, {}", lineRegisterEntity, registerResultVO);
        if (ObjectUtil.isNull(registerResultVO)) {
            return registerRedisDto;
        }
        if (200 == registerResultVO.getCode()) {
            Long status = registerResultVO.getData().getStatus();
            if (2 == status || 1 == status || 0 == status || Long.valueOf(20001).equals(status)) {
                //注册成功
                if (2 == status) {
                    SyncLineTokenDTO syncLineTokenDTO = new SyncLineTokenDTO();
                    syncLineTokenDTO.setTaskId(lineRegisterEntity.getTaskId());
                    SyncLineTokenVO syncLineTokenVO = lineService.SyncLineTokenDTO(syncLineTokenDTO);
                    if (ObjectUtil.isNull(syncLineTokenVO)) {
                        return registerRedisDto;
                    }
                    if (200 == syncLineTokenVO.getCode() && CollUtil.isNotEmpty(syncLineTokenVO.getData())) {

                        SyncLineTokenVOData syncLineTokenVOData = syncLineTokenVO.getData().get(0);
                        lineRegisterEntity.setRegisterStatus(RegisterStatus.RegisterStatus4.getKey());
                        String token = syncLineTokenVOData.getToken();
                        LineTokenJson lineTokenJson = JSON.parseObject(token, LineTokenJson.class);
                        boolean accountExistStatus = lineTokenJson.isAccountExistStatus();
                        if (accountExistStatus) {
                            lineRegisterEntity.setAccountExistStatus(AccountExistStatus.AccountExistStatus2.getKey());
                        } else {
                            lineRegisterEntity.setAccountExistStatus(AccountExistStatus.AccountExistStatus1.getKey());
                        }
                        lineRegisterEntity.setToken(token);
                    }
                }
            } else {
                lineRegisterEntity.setRegisterStatus(RegisterStatus.RegisterStatus5.getKey());
                lineRegisterEntity.setErrMsg(registerResultVO.getData().getRemark());
            }
        }
        return registerRedisDto;
    }


    /**
     *
     */
    public CdRegisterRedisDto getSmsJp(CdRegisterRedisDto registerRedisDto,
                                       List<String> pkeys) {
        if (registerRedisDto == null || CollectionUtils.isEmpty(pkeys)) {
            log.info("获取验证码，入参为空，请检查 {}, {}", registerRedisDto);

        }
        String pkey = String.join(", ", pkeys);


        CdGetPhoneEntity cdGetPhoneEntity = registerRedisDto.getPhoneEntity();
        CdLineRegisterEntity lineRegisterEntity = registerRedisDto.getLineRegister();

        String phoneCode = cdGetPhoneEntity.getCode();

        //超过20分
        long between = DateUtil.between(cdGetPhoneEntity.getCreateTime(), DateUtil.date(), DateUnit.MINUTE);
        if (between > 20) {
            cdGetPhoneEntity.setCode("验证码超时");
            cdGetPhoneEntity.setPhoneStatus(PhoneStatus5.getKey());
            lineRegisterEntity.setRegisterStatus(RegisterStatus.RegisterStatus5.getKey());
            return registerRedisDto;
        }

        if (StringUtils.isEmpty(phoneCode)) {
            CardJpGetPhoneSmsVO.Data.Ret.Sm sm = (CardJpGetPhoneSmsVO.Data.Ret.Sm) redisObjectTemplate.opsForValue()
                    .get(RedisKeys.JP_SMS_SG.getValue(String.valueOf(cdGetPhoneEntity.getPkey())));
            if (ObjectUtil.isNull(sm)) {
                Map<Long, CardJpGetPhoneSmsVO.Data.Ret.Sm> smMap = cardJpService
                        .getPhoneCodes(pkey);
                sm = smMap.get(cdGetPhoneEntity.getPkey());
            }
            if (ObjectUtil.isNull(sm)) {
                log.error("获取验证码为空，山谷 {}", cdGetPhoneEntity.getPkey());
                return registerRedisDto;
            }

            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(sm.getTime()),
                    ZoneId.of("Asia/Shanghai"));
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
            Date recvTime = Date.from(zonedDateTime.toInstant());

            boolean before = cdGetPhoneEntity.getCreateTime().before(recvTime);
            if (before) {
                phoneCode = extractVerificationCode(sm.getContent());
                cdGetPhoneEntity.setCode(phoneCode);
                cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus3.getKey());
            } else {
                redisObjectTemplate.delete(RedisKeys.JP_SMS_SG.getValue(String.valueOf(cdGetPhoneEntity.getPkey())));
            }
        }
        if (StringUtils.isEmpty(phoneCode)) {
            return registerRedisDto;
        }
        log.info("获取验证码返回,山谷 {}, {}", lineRegisterEntity.getPhone(), phoneCode);

        //提交验证码
        SMSCodeDTO smsCodeDTO = new SMSCodeDTO();
        smsCodeDTO.setsmsCode(phoneCode);
        smsCodeDTO.setTaskId(lineRegisterEntity.getTaskId());
        SMSCodeVO smsCodeVO = lineService.smsCode(smsCodeDTO);
        log.info("提交验证码返回 {}, {}", lineRegisterEntity.getPhone(), smsCodeVO);
        if (ObjectUtil.isNull(smsCodeVO)) {
            return registerRedisDto;
        }
        if (200 == smsCodeVO.getCode()) {
            lineRegisterEntity.setId(lineRegisterEntity.getId());
            lineRegisterEntity.setRegisterStatus(RegisterStatus.RegisterStatus3.getKey());
            lineRegisterEntity.setSmsCode(phoneCode);
            cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus4.getKey());
        }
        return registerRedisDto;
    }


    public CdRegisterRedisDto getSms(ProjectWorkEntity projectWorkEntity,
                                     CdRegisterRedisDto registerRedisDto) {
        if (ObjectUtil.isNull(projectWorkEntity)
                || ObjectUtil.isNull(registerRedisDto)
                || ObjectUtil.isNull(registerRedisDto.getPhoneEntity())
                || ObjectUtil.isNull(registerRedisDto.getLineRegister())) {
            log.info("获取验证码，入参为空，请检查 {}, {}", registerRedisDto, projectWorkEntity);
            return registerRedisDto;
        }

        CdGetPhoneEntity phoneEntity = registerRedisDto.getPhoneEntity();
        CdLineRegisterEntity lineRegisterEntity = registerRedisDto.getLineRegister();
        //如果需要释放
        if (PhoneStatus5.getKey().equals(phoneEntity.getPhoneStatus())) {
            if (StringUtils.isNotEmpty(phoneEntity.getCountrycode())
                    && CountryCode.CountryCode1.getValue().equals(phoneEntity.getCountrycode())) {
                //泰国
                boolean b = firefoxService.setRel(phoneEntity.getPkey());
                if (b) {
                    phoneEntity.setPhoneStatus(PhoneStatus6.getKey());
                }
            }
            lineRegisterEntity.setRegisterStatus(RegisterStatus.RegisterStatus5.getKey());
            return registerRedisDto;
        } else {
            //如果需要修改验证码
            String phoneCode = phoneEntity.getCode();
            //超过20分
            long between = DateUtil.between(phoneEntity.getCreateTime(), DateUtil.date(), DateUnit.MINUTE);
            if (between > 10) {
                phoneEntity.setCode("验证码超时");
                phoneEntity.setPhoneStatus(PhoneStatus5.getKey());
                lineRegisterEntity.setRegisterStatus(RegisterStatus.RegisterStatus5.getKey());
                return registerRedisDto;
            }
            if (StrUtil.isEmpty(phoneCode) && StringUtils.isNotEmpty(phoneEntity.getCountrycode())) {
                if (CountryCode.CountryCode3.getValue().equals(phoneEntity.getCountrycode())
                        && CountryCode.CountryCode3.getKey().toString().equals(phoneEntity.getCountry())) {
                    //日本
                    if (cardJpSmsOver.getIfPresent("jpSmsOverFlag") != null) {
                        return registerRedisDto;
                    }
                    cardJpSms.put(phoneEntity.getPkey(), phoneEntity.getCreateTime());
                    phoneCode = cardJpService.getPhoneCode(phoneEntity.getPkey());
                } else if (CountryCode.CountryCode8.getValue().equals(phoneEntity.getCountrycode())
                        && CountryCode.CountryCode8.getKey().toString().equals(phoneEntity.getCountry())) {
                    cardJpSms.put(phoneEntity.getPkey(), phoneEntity.getCreateTime());
                    String s = phoneEntity.getPkey() + "#" + phoneEntity.getSfApi() + "#" + phoneEntity.getTimeZone();
                    //日本-四方
                    phoneCode = cardJpSFService.getPhoneCode(s);
                } else if (CountryCode.CountryCode5.getValue().equals(phoneEntity.getCountrycode())) {
                    //香港
                    phoneCode = firefoxServiceImpl.getPhoneCode(phoneEntity.getPkey());
                } else {
                    phoneCode = firefoxService.getPhoneCode(phoneEntity.getPkey());
                }
            }
            if (StrUtil.isEmpty(phoneCode)) {
                return registerRedisDto;
            }
            log.info("获取验证码 phoneCode = {}, {}", phoneEntity.getPhone(), phoneCode);
            if (StrUtil.isNotEmpty(phoneCode)) {
                //更新验证码信息
                phoneEntity.setCode(phoneCode);
                phoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus3.getKey());

                SMSCodeDTO smsCodeDTO = new SMSCodeDTO();
                smsCodeDTO.setsmsCode(phoneCode);
                smsCodeDTO.setTaskId(lineRegisterEntity.getTaskId());
                SMSCodeVO smsCodeVO = lineService.smsCode(smsCodeDTO);
                log.info("提交验证码返回 {}, {}", phoneEntity.getPhone(), smsCodeVO);
                if (ObjectUtil.isNull(smsCodeVO)) {
                    return registerRedisDto;
                }
                if (200 == smsCodeVO.getCode()) {
                    lineRegisterEntity.setRegisterStatus(RegisterStatus.RegisterStatus3.getKey());
                    lineRegisterEntity.setSmsCode(phoneCode);
                    phoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus4.getKey());
                }
            }
        }
        return registerRedisDto;
    }

    /**
     * 使用协议去注册
     */
    public CdRegisterRedisDto startRegister(ProjectWorkEntity projectWorkEntity,
                                            CdRegisterRedisDto registerRedisDto) {
        if (ObjectUtil.isNull(projectWorkEntity)
                || ObjectUtil.isNull(registerRedisDto)
                || ObjectUtil.isNull(registerRedisDto.getPhoneEntity())) {
            log.info("启动注册，入参为空，请检查 {}, {}", registerRedisDto, projectWorkEntity);
            return registerRedisDto;
        }
        CdGetPhoneEntity phoneEntity = registerRedisDto.getPhoneEntity();

        //发起注册
        LineRegisterDTO lineRegisterDTO = new LineRegisterDTO();
        lineRegisterDTO.setAb(projectWorkEntity.getLineAb());
        lineRegisterDTO.setAppVersion(projectWorkEntity.getLineAppVersion());
        lineRegisterDTO.setCountryCode(phoneEntity.getCountrycode());
        lineRegisterDTO.setPhone(phoneEntity.getPhone());

        //获取代理
        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
        cdLineIpProxyDTO.setTokenPhone(phoneEntity.getPhone());
        cdLineIpProxyDTO.setLzPhone(phoneEntity.getPhone());
        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
        if (StrUtil.isEmpty(proxyIp)) {
            log.info("启动注册，获取代理为空 {}", cdLineIpProxyDTO);
            return registerRedisDto;
        }

        lineRegisterDTO.setProxy(proxyIp);
        lineRegisterDTO.setTxtToken(projectWorkEntity.getLineTxtToken());
        //发起注册
        LineRegisterVO lineRegisterVO = lineService.lineRegister(lineRegisterDTO);
        if (ObjectUtil.isNull(lineRegisterVO)) {
            return registerRedisDto;
        }
        log.info("启动注册，注册返回= {} 入参 = {}", lineRegisterVO, lineRegisterDTO);
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
            cdLineRegisterDTO.setGetPhoneId(phoneEntity.getId());
            cdLineRegisterDTO.setPkey(phoneEntity.getPkey());
            cdLineRegisterDTO.setSubtasksId(phoneEntity.getSubtasksId());
            //设置代理类型
            cdLineRegisterDTO.setProxyStatus(projectWorkEntity.getProxy());

            //注册状态变更
            phoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus2.getKey());
            phoneEntity.setCreateTime(DateUtil.date());

            registerRedisDto.setLineRegister(cdLineRegisterDTO);
        }
        return registerRedisDto;
    }

    /**
     * 更新手机号注册次数
     */
    private Integer savePhoneRegisterCount(String phone) {
        try {
            Integer registerCount = cdGetPhoneService.getPhoneRegisterCount(phone) + 1;

            log.error("更新手机号注册次数 {}, 次数：{}", phone, registerCount);
            redisTemplate.opsForHash().put(RedisKeys.RedisKeys10.getValue(), phone, String.valueOf(registerCount));

            //大于等于3次的卡，与前两次的做对比，超过24小时，才为可用状态
            if (registerCount >= 3) {
                Map<Integer, Date> userMap = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
                                .eq(AtUserEntity::getTelephone, phone)).stream()
                        .filter(i -> i.getRegisterCount() != null)
                        .collect(Collectors.toMap(AtUserEntity::getRegisterCount,
                                i -> i.getRegisterTime() != null ? i.getRegisterTime() : i.getCreateTime(), (a, b) -> b));
                Integer judgeFrequency = registerCount - 2;//与前两次的对比

                Date time = ObjectUtil.isNotNull(userMap.get(judgeFrequency)) ?
                        userMap.get(judgeFrequency) : new Date();

                //在此时间上加24小时+30分钟
                Date expireDate = DateUtils.addDateMinutes(time, (24 * 60) + 30);

                Long expireMinutes = DateUtils.betweenMinutes(new Date(), expireDate);

                redisTemplate.opsForValue().set(RedisKeys.RedisKeys12.getValue(phone), String.valueOf(registerCount), expireMinutes, TimeUnit.MINUTES);
            }
            return registerCount;
        } catch (Exception e) {
            log.error("更新手机号注册次数异常 {}, {}", phone, e);
        }
        return 0;
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
                .eq(CdRegisterTaskEntity::getRegistrationStatus, RegistrationStatus.RegistrationStatus2.getKey())
                .lt(CdRegisterTaskEntity::getFillUpRegisterTaskId, 0)
                .or(item -> item.eq(CdRegisterTaskEntity::getFillUp, FillUp.YES.getKey()))
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
                log.info("keyByResource = {} 获取的锁为 = {}", keyByResource, triedLock);
                if (triedLock) {
                    try {
                        //获取所有子任务
                        List<CdRegisterSubtasksEntity> cdRegisterSubtasksEntities = cdRegisterSubtasksService.list(new QueryWrapper<CdRegisterSubtasksEntity>().lambda()
                                .eq(CdRegisterSubtasksEntity::getTaskId, cdRegisterTaskEntity.getId())
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
                        } else {
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
                    } finally {
                        lock.unlock();
                    }
                } else {
                    log.info("keyByResource = {} 在执行", keyByResource);
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
                    log.info("keyByResource = {} 获取的锁为 = {}", keyByResource, triedLock);
                    if (triedLock) {
                        try {
                            //如果获取的状态为2跳出循环
                            if (RegistrationStatus.RegistrationStatus2.getKey().equals(cdRegisterSubtasksEntity.getRegistrationStatus())) {
                                //获取子任务数量
                                int count = cdGetPhoneService.count(new QueryWrapper<CdGetPhoneEntity>().lambda()
                                        .eq(CdGetPhoneEntity::getSubtasksId, cdRegisterSubtasksEntity.getId())
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
                        } finally {
                            lock.unlock();
                        }
                    } else {
                        log.info("keyByResource = {} 在执行", keyByResource);
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
                log.info("注册任务，插入子任务开始 keyByResource = {} 获取的锁为 = {}", keyByResource, triedLock);
                if (triedLock) {
                    try {
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
                                } else {
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
                            cdRegisterSubtasksService.saveBatch(cdRegisterSubtasksEntities, cdRegisterSubtasksEntities.size());
                            //修改状态
                            cdRegisterTaskService.updateById(cdRegisterTaskEntity);
                        }
                    } finally {
                        lock.unlock();
                    }
                } else {
                    log.info("keyByResource = {} 在执行", keyByResource);
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
    static ReentrantLock registerTaskLock = new ReentrantLock();

    /**
     * 使用协议去注册
     */
    @Scheduled(fixedDelay = 5 * 1000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void registerTaskDistribution() {
        ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
        if (ObjectUtil.isNull(projectWorkEntity)) {
            return;
        }
        boolean b = registerTaskLock.tryLock();
        if (!b) {
            return;
        }
        try {

            List<Object> registerList = redisObjectTemplate.opsForHash()
                    .values(RedisKeys.REGISTER_TASK.getValue());
            if (CollUtil.isEmpty(registerList)) {
                log.info("注册任务分发，暂无执行的");
                return;
            }

            List<String> pkeys = new ArrayList<>();
            for (Object object : registerList) {
                CdRegisterRedisDto registerRedisDto = (CdRegisterRedisDto) object;
                if (ObjectUtil.isNotNull(registerRedisDto) && ObjectUtil.isNotNull(registerRedisDto.getLineRegister())
                        && RegisterStatus.RegisterStatus2.getKey().equals(registerRedisDto.getLineRegister().getRegisterStatus())
                        && CountryCode.CountryCode3.getKey().toString().equals(registerRedisDto.getPhoneEntity().getCountry())) {
                    pkeys.add(registerRedisDto.getPhoneEntity().getPkey());
                }
            }
            final CountDownLatch latch = new CountDownLatch(registerList.size());


            for (Object object : registerList) {
                poolExecutor.execute(() -> {
                    CdRegisterRedisDto registerRedisDto = (CdRegisterRedisDto) object;
                    if (registerRedisDto == null || registerRedisDto.getPhoneEntity() == null) {
                        log.info("注册任务分发，数据为空 {}", object);
                        latch.countDown();
                        return;
                    }
                    CdGetPhoneEntity phoneEntity = registerRedisDto.getPhoneEntity();
                    CdLineRegisterEntity lineRegisterEntity = registerRedisDto.getLineRegister();

                    String keyByResource = LockMapKeyResource
                            .getKeyByResource(LockMapKeyResource.REGISTER_TASK_DISTRIBUTION, registerRedisDto.getTelPhone());
                    Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                    boolean triedLock = lock.tryLock();
                    log.info("注册任务分发{}，keyByResource = {} 获取的锁为 = {},状态 = {}",
                            registerRedisDto.getTelPhone(), keyByResource, triedLock, phoneEntity.getPhoneStatus());
                    if (triedLock) {
                        try {
                            if (PhoneStatus1.getKey().equals(phoneEntity.getPhoneStatus())) {
                                //去注册
                                registerRedisDto = this.startRegister(projectWorkEntity, registerRedisDto);
                            } else if (ObjectUtil.isNotNull(lineRegisterEntity)
                                    && RegisterStatus.RegisterStatus2.getKey().equals(lineRegisterEntity.getRegisterStatus())) {
                                //去获取验证码
                                if (CountryCode.CountryCode3.getKey().toString().equals(phoneEntity.getCountry())) {
                                    //山谷
                                    registerRedisDto = this.getSmsJp(registerRedisDto, pkeys);
                                } else {
                                    registerRedisDto = this.getSms(projectWorkEntity, registerRedisDto);
                                }
                            } else if (PhoneStatus5.getKey().equals(phoneEntity.getPhoneStatus())) {
                                registerRedisDto = this.getSms(projectWorkEntity, registerRedisDto);
                            } else if (ObjectUtil.isNotNull(lineRegisterEntity)
                                    && RegisterStatus.RegisterStatus3.getKey().equals(lineRegisterEntity.getRegisterStatus())) {
                                //去提交验证码，完成注册
                                registerRedisDto = this.submitRegister(projectWorkEntity, registerRedisDto);
                            } else {
                                latch.countDown();
                                return;
                            }

                            log.info("注册发起流程结束 {}, {}", registerRedisDto.getTelPhone(), JSON.toJSONString(registerRedisDto).toString());

                            //存redis，异步保存，避免卡顿超时
                            redisObjectTemplate.opsForHash().put(RedisKeys.REGISTER_TASK.getValue(),
                                    registerRedisDto.getTelPhone(), registerRedisDto);
                            latch.countDown();
                        } finally {
                            lock.unlock();
                        }
                    } else {
                        log.info("注册任务分发{}在执行，keyByResource = {} 获取的锁为 = {}", registerRedisDto.getTelPhone(), keyByResource);
                    }

                });
            }
            latch.await();
        } catch (Exception e) {
            log.info("注册分发异常 {}", e);
        } finally {
            registerTaskLock.unlock();
        }


    }

    static ReentrantLock registerSaveTaskLock = new ReentrantLock();


    /**
     * 注册流程保存数据库
     */
    @Scheduled(fixedDelay = 5 * 1000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void saveRegisterTask() {
        boolean flag = registerTaskLock.tryLock();
        if (!flag) {
            return;
        }
        List<Object> registerList = redisObjectTemplate.opsForHash()
                .values(RedisKeys.REGISTER_TASK.getValue());
        log.info("注册任务保存 {}", registerList);
        if (CollUtil.isEmpty(registerList)) {
            log.info("注册任务保存，暂无执行的");
            return;
        }
        try {
            final CountDownLatch latch = new CountDownLatch(registerList.size());
            for (Object object : registerList) {
                poolExecutor.execute(() -> {
                    CdRegisterRedisDto registerRedisDto = (CdRegisterRedisDto) object;
                    if (registerRedisDto == null || registerRedisDto.getPhoneEntity() == null) {
                        log.info("注册任务保存，数据为空 {}", object);
                        latch.countDown();
                        return;
                    }
                    String keyByResource = LockMapKeyResource
                            .getKeyByResource(LockMapKeyResource.REGISTER_TASK_SAVE, registerRedisDto.getTelPhone());
                    Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                    boolean triedLock = lock.tryLock();
                    if (triedLock) {
                        try {
                            log.info("注册任务保存{}，keyByResource = {} 获取的锁为 = {}", registerRedisDto.getTelPhone(), keyByResource, triedLock);
                            CdGetPhoneEntity phoneEntity = registerRedisDto.getPhoneEntity();
                            CdLineRegisterEntity lineRegisterEntity = registerRedisDto.getLineRegister();
                            if (PhoneStatus1.getKey().equals(phoneEntity.getPhoneStatus())) {
                                //去注册
                                latch.countDown();
                                return;
                            }

                            if (ObjectUtil.isNotNull(lineRegisterEntity)
                                    && RegisterStatus.RegisterStatus1.getKey().equals(lineRegisterEntity.getRegisterStatus())) {
                                lineRegisterEntity.setRegisterStatus(RegisterStatus.RegisterStatus2.getKey());
                                try {
                                    //先查
                                    Integer lineRegisterId = null;
                                    CdLineRegisterEntity queryLine = cdLineRegisterService.queryByPhone(lineRegisterEntity.getPhone());
                                    if (ObjectUtil.isNotNull(queryLine) && StringUtils.isEmpty(queryLine.getToken())) {
                                        log.warn("注册表重复 {}", queryLine);
                                        lineRegisterId = queryLine.getId();
                                    } else {
                                        cdLineRegisterService.save(lineRegisterEntity);
                                        lineRegisterId = lineRegisterEntity.getId();
                                    }
                                    phoneEntity.setLineRegisterId(lineRegisterId);
                                    lineRegisterEntity.setId(lineRegisterId);

                                    //保存line
                                    boolean b = cdGetPhoneService.updateById(phoneEntity);
                                    if (b) {
                                        registerRedisDto.setLineRegister(lineRegisterEntity);
                                        log.info("保存line结果 {},{},{} ", lineRegisterEntity.getPhone(), LocalDateTime.now(), JSONUtil.toJsonStr(registerRedisDto));
                                        redisObjectTemplate.opsForHash().put(RedisKeys.REGISTER_TASK.getValue(),
                                                registerRedisDto.getTelPhone(), registerRedisDto);
                                    }
                                } catch (Exception e) {
                                    log.warn("注册表重复 {}", lineRegisterEntity, e);
                                    latch.countDown();
                                    return;
                                }
                            } else {
                                //保存line
                                boolean b1 = cdGetPhoneService.updateById(phoneEntity);
                                boolean b2 = cdLineRegisterService.updateById(lineRegisterEntity);

                                //删除redis缓存
                                if (RegisterStatus.RegisterStatus4.getKey().equals(lineRegisterEntity.getRegisterStatus())
                                        || RegisterStatus.RegisterStatus5.getKey().equals(lineRegisterEntity.getRegisterStatus())) {
                                    redisObjectTemplate.opsForHash().delete(RedisKeys.REGISTER_TASK.getValue(), registerRedisDto.getTelPhone());
                                }
                            }
                            if (ObjectUtil.isNotNull(lineRegisterEntity)
                                    && RegisterStatus.RegisterStatus4.getKey().equals(lineRegisterEntity.getRegisterStatus())) {
                                //更新手机号注册次数
                                this.savePhoneRegisterCount(lineRegisterEntity.getPhone());
                            }
                            log.info("注册任务保存流程结束 {}, {}", registerRedisDto.getTelPhone(), registerRedisDto);
                        } finally {
                            lock.unlock();
                        }
                        latch.countDown();
                    } else {
                        log.info("注册任务分发{}在执行，keyByResource = {} 获取的锁为 = {}", registerRedisDto.getTelPhone(), keyByResource);
                    }
                });
            }
            latch.await();
        } catch (Exception e) {
            log.info("保存注册异常 {}", e);
        } finally {
            registerTaskLock.unlock();
        }
    }

}
