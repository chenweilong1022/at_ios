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
    private RedisTemplate<String, Object> redisObjectTemplate;

    /**
     * 注册任务处理的国家
     */
    private static final List<Integer> registerCountryList = Arrays.asList(81, 8101, 66);

    /**
     *
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task7() {
        //获取所有已经发起注册的机器 todo redis
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
                                //注册成功
                                if (2 == status) {
                                    SyncLineTokenDTO syncLineTokenDTO = new SyncLineTokenDTO();
                                    syncLineTokenDTO.setTaskId(cdLineRegisterEntity.getTaskId());
                                    SyncLineTokenVO syncLineTokenVO = lineService.SyncLineTokenDTO(syncLineTokenDTO);
                                    if (ObjectUtil.isNull(syncLineTokenVO)) {
                                        return;
                                    }
                                    if (200 == syncLineTokenVO.getCode() && CollUtil.isNotEmpty(syncLineTokenVO.getData())) {
                                        //更新手机号注册次数
                                        this.savePhoneRegisterCount(cdLineRegisterEntity);

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
    public void task10() {

        boolean b3 = task1Lock.tryLock();
        if (!b3) {
            log.error("task1Lock ip推送队列 = {}",b3);
            return;
        }
        try{

            List<Object> waitRegisterList = redisTemplate.opsForHash()
                    .values(RedisKeys.WAIT_SMS_PHONE.getValue("81")).subList(0, 100);

            if (CollUtil.isEmpty(waitRegisterList)) {
                log.info("无获取验证码的任务81");
                return;
            }

            List<CdRegisterRedisDto> registerRedisDtoList = new ArrayList<>();
            List<String> pkeyList = new ArrayList();
            Map<String, CdRegisterRedisDto> registerRedisMap = new HashMap<>();
            for (Object object : waitRegisterList) {
                CdRegisterRedisDto phoneRedisDto =  JSON.parseObject((String) object, CdRegisterRedisDto.class);
                registerRedisDtoList.add(phoneRedisDto);
                CdRegisterRedisDto registerRedisDto =  JSON.parseObject((String) object, CdRegisterRedisDto.class);
                CdGetPhoneEntity phoneEntity = registerRedisDto.getPhoneEntity();
                pkeyList.add(phoneEntity.getPkey());
                registerRedisMap.put(phoneEntity.getPkey())

            }

            String pks = pkeyList.stream().collect(Collectors.joining(","));
            Map<Long, CardJpGetPhoneSmsVO.Data.Ret.Sm> phoneCodes = cardJpService.getPhoneCodes(pks);
            Map<String, CdGetPhoneEntity> collect = list.stream().collect(Collectors.toMap(CdGetPhoneEntity::getPkey, item -> item, (a, b) -> a));
            List<CdGetPhoneEntity> cdGetPhoneEntities = new ArrayList<>();
            List<CdLineRegisterEntity> cdLineRegisterEntities = new ArrayList<>();


            for (Long l : phoneCodes.keySet()) {
                CdGetPhoneEntity cdGetPhoneEntity = collect.get(String.valueOf(l));
                if (ObjectUtil.isNull(cdGetPhoneEntity)) {
                    continue;
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
                    cdGetPhoneEntities.add(cdGetPhoneEntity);

                    CdLineRegisterEntity lineRegisterVO = cdLineRegisterService.getById((Serializable) cdGetPhoneEntity.getLineRegisterId());
                    if (ObjectUtil.isNull(lineRegisterVO)) {
                        lineRegisterVO = cdLineRegisterService.getOne(new QueryWrapper<CdLineRegisterEntity>().lambda()
                                .eq(CdLineRegisterEntity::getGetPhoneId,cdGetPhoneEntity.getId())
                        );
                        if (ObjectUtil.isNull(lineRegisterVO)) {
                            return;
                        }
                    }

                    SMSCodeDTO smsCodeDTO = new SMSCodeDTO();
                    smsCodeDTO.setsmsCode(phoneCode);
                    smsCodeDTO.setTaskId(lineRegisterVO.getTaskId());
                    SMSCodeVO smsCodeVO = lineService.smsCode(smsCodeDTO);
                    if (ObjectUtil.isNull(smsCodeVO)) {
                        return;
                    }
                    if (200 == smsCodeVO.getCode()) {
                        CdLineRegisterEntity update = new CdLineRegisterEntity();
                        update.setId(lineRegisterVO.getId());
                        update.setRegisterStatus(RegisterStatus.RegisterStatus3.getKey());
                        update.setSmsCode(phoneCode);
                        cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus4.getKey());
                        cdLineRegisterEntities.add(update);
                    }

                }
            }
            if (CollUtil.isNotEmpty(cdGetPhoneEntities)) {
                cdGetPhoneService.updateBatchById(cdGetPhoneEntities);
            }
            if (CollUtil.isNotEmpty(cdLineRegisterEntities)) {
                cdLineRegisterService.updateBatchById(cdLineRegisterEntities);
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task1Lock.unlock();
        }

    }


    /**
     *
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task6() {
        //获取验证码
        List<Object> waitRegisterList = redisTemplate.opsForHash()
                .values(RedisKeys.WAIT_SMS_PHONE.getValue("8101")).subList(0, 100);

        if (CollUtil.isEmpty(waitRegisterList)) {
            log.info("无获取验证码的任务");
            return;
        }

        for (Object object : waitRegisterList) {
            CdRegisterRedisDto phoneRedisDto =  JSON.parseObject((String) object, CdRegisterRedisDto.class);
            poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource
                        .getKeyByResource(LockMapKeyResource.LockMapKeyResource4, phoneRedisDto.getTelPhone());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("获取验证码 keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    Boolean isUpdatePhone = false;//是否需要更新数据库，未取到验证码时，重试不需要更新数据库
                    try{
                        phoneRedisDto.setSmsFlag(false);
                        CdGetPhoneEntity phoneEntity = phoneRedisDto.getPhoneEntity();
                        CdLineRegisterEntity lineRegisterEntity = phoneRedisDto.getLineRegister();
                        //如果需要释放
                        if(PhoneStatus5.getKey().equals(phoneEntity.getPhoneStatus())) {
                            boolean b;

                            if (StringUtils.isNotEmpty(phoneEntity.getCountrycode())
                                    && CountryCode.CountryCode3.getValue().equals(phoneEntity.getCountrycode())) {
                                //日本
                                b = cardJpService.setRel(phoneEntity.getPkey());
                            } else if (StringUtils.isNotEmpty(phoneEntity.getCountrycode())
                                    && CountryCode.CountryCode5.getValue().equals(phoneEntity.getCountrycode())) {
                                //香港
                                b = firefoxServiceImpl.setRel(phoneEntity.getPkey());
                            } else {
                                b = firefoxService.setRel(phoneEntity.getPkey());
                            }

                            if (b) {
                                phoneEntity.setPhoneStatus(PhoneStatus6.getKey());
                            }
                            isUpdatePhone = true;
                            //如果需要修改验证码
                        }else {
                            String phoneCode = phoneEntity.getCode();
                            //超过20分
                            long between = DateUtil.between(phoneEntity.getCreateTime(), DateUtil.date(), DateUnit.MINUTE);
                            if (between > 10) {
                                isUpdatePhone = true;
                                phoneEntity.setCode("验证码超时");
                                phoneEntity.setPhoneStatus(PhoneStatus5.getKey());
                                return;
                            }
                            if (StrUtil.isEmpty(phoneCode) && StringUtils.isNotEmpty(phoneEntity.getCountrycode())) {
                                if (CountryCode.CountryCode3.getValue().equals(phoneEntity.getCountrycode())
                                        && CountryCode.CountryCode3.getKey().toString().equals(phoneEntity.getCountry())) {
                                    //日本
                                    if (cardJpSmsOver.getIfPresent("jpSmsOverFlag") != null) {
                                        return;
                                    }
                                    cardJpSms.put(phoneEntity.getPkey(), phoneEntity.getCreateTime());
                                    phoneCode = cardJpService.getPhoneCode(phoneEntity.getPkey());
                                }else if (CountryCode.CountryCode8.getValue().equals(phoneEntity.getCountrycode())
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
                                return;
                            }
                            log.info("phoneCode = {}", phoneCode);
                            if (StrUtil.isNotEmpty(phoneCode)) {
                                //更新验证码信息
                                isUpdatePhone = true;
                                phoneEntity.setCode(phoneCode);
                                phoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus3.getKey());


                                SMSCodeDTO smsCodeDTO = new SMSCodeDTO();
                                smsCodeDTO.setsmsCode(phoneCode);
                                smsCodeDTO.setTaskId(lineRegisterEntity.getTaskId());
                                SMSCodeVO smsCodeVO = lineService.smsCode(smsCodeDTO);
                                if (ObjectUtil.isNull(smsCodeVO)) {
                                    return;
                                }
                                log.info("smsCodeVO = {}", JSONUtil.toJsonStr(smsCodeVO));
                                if (200 == smsCodeVO.getCode()) {
                                    lineRegisterEntity.setRegisterStatus(RegisterStatus.RegisterStatus3.getKey());
                                    lineRegisterEntity.setSmsCode(phoneCode);
                                    phoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus4.getKey());
                                    phoneRedisDto.setSmsFlag(true);
                                }
                            }
                        }
                        phoneRedisDto.setPhoneEntity(phoneEntity);
                        phoneRedisDto.setLineRegister(lineRegisterEntity);
                    }finally {
                        try {
                            if(Boolean.TRUE.equals(isUpdatePhone)) {
                                //删除此条数据
                                redisTemplate.opsForHash()
                                        .delete(RedisKeys.WAIT_SMS_PHONE.getValue("8101"), phoneRedisDto.getTelPhone());

                                //存redis，异步保存，避免卡顿超时
                                redisTemplate.opsForList().rightPush(RedisKeys.SAVE_WAIT_REGISTER_PHONE.getValue(),
                                        phoneRedisDto.getTelPhone(), JSON.toJSONString(phoneRedisDto));
                            }
                        } catch (Exception e) {
                            log.error("获取验证码保存失败 {},{}", phoneRedisDto, e);
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

        List<Object> waitRegisterList = redisTemplate.opsForHash()
                .values(RedisKeys.WAIT_START_REGISTER_PHONE.getValue()).subList(0, 100);
        if (CollUtil.isEmpty(waitRegisterList)) {
            log.info("无待注册的任务");
            return;
        }

        for (Object object : waitRegisterList) {
            CdRegisterRedisDto registerRedisDto =  JSON.parseObject((String) object, CdRegisterRedisDto.class);
            CdGetPhoneEntity phone = registerRedisDto.getPhoneEntity();
            poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource
                        .getKeyByResource(LockMapKeyResource.LockMapKeyResource4, registerRedisDto.getTelPhone());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        LineRegisterDTO lineRegisterDTO = new LineRegisterDTO();
                        lineRegisterDTO.setAb(projectWorkEntity.getLineAb());
                        lineRegisterDTO.setAppVersion(projectWorkEntity.getLineAppVersion());
                        lineRegisterDTO.setCountryCode(phone.getCountrycode());
                        lineRegisterDTO.setPhone(phone.getPhone());
                        //获取代理
                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(phone.getPhone());
                        cdLineIpProxyDTO.setLzPhone(phone.getPhone());
                        //注册任务设置代理
                        CdRegisterSubtasksVO registerSubtasksVO = cdRegisterSubtasksService.getById(phone.getSubtasksId());
                        if (ObjectUtil.isNotNull(registerSubtasksVO)) {
                            String proxyId = (String) redisTemplate.opsForHash().get(RedisKeys.RedisKeys5.getValue(), String.valueOf(registerSubtasksVO.getTaskId()));
                            if (StrUtil.isNotEmpty(proxyId)) {
                                Integer i = Integer.valueOf(proxyId);
                                cdLineIpProxyDTO.setSelectProxyStatus(i);
                            }
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
                            cdLineRegisterDTO.setGetPhoneId(phone.getId());
                            cdLineRegisterDTO.setPkey(phone.getPkey());
                            cdLineRegisterDTO.setSubtasksId(phone.getSubtasksId());
                            //设置代理类型
                            cdLineRegisterDTO.setProxyStatus(projectWorkEntity.getProxy());

                            CdGetPhoneEntity update = new CdGetPhoneEntity();
                            update.setId(phone.getId());
                            update.setPhoneStatus(PhoneStatus.PhoneStatus2.getKey());
                            update.setCreateTime(DateUtil.date());

                            //删除待注册
                            redisTemplate.opsForHash().delete(RedisKeys.WAIT_START_REGISTER_PHONE.getValue(), phone.getPhone());

                            //注册状态变更
                            phone.setPhoneStatus(PhoneStatus.PhoneStatus2.getKey());
                            phone.setCreateTime(DateUtil.date());

                            //line信息
                            registerRedisDto.setLineRegister(cdLineRegisterDTO);
                            registerRedisDto.setPhoneEntity(phone);

                            //存redis，异步保存，避免卡顿超时
                            redisTemplate.opsForList().rightPush(RedisKeys.SAVE_WAIT_SMS_PHONE.getValue(),
                                    registerRedisDto.getTelPhone(), JSON.toJSONString(registerRedisDto));
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
     * 更新手机号注册次数
     */
    private Integer savePhoneRegisterCount(CdLineRegisterEntity cdLineRegisterEntity) {
        try {
            String phone = cdLineRegisterEntity.getPhone();
            Integer registerCount = cdGetPhoneService.getPhoneRegisterCount(phone) + 1;

            log.error("更新手机号注册次数 {}, 次数：{}", phone, registerCount);
            redisTemplate.opsForHash().put(RedisKeys.RedisKeys10.getValue(), phone, String.valueOf(registerCount));

            //大于等于3次的卡，与前两次的做对比，超过24小时，才为可用状态
            if (registerCount >= 3) {
                Map<Integer, Date> userMap = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
                                .eq(AtUserEntity::getTelephone, phone)).stream()
                        .filter(i -> i.getRegisterCount() != null)
                        .collect(Collectors.toMap(AtUserEntity::getRegisterCount,
                                i -> i.getRegisterTime() != null ? i.getRegisterTime() : i.getCreateTime(),(a,b)->b));
                Integer judgeFrequency = registerCount - 2;//与前两次的对比

                Date time = ObjectUtil.isNotNull(userMap.get(judgeFrequency)) ?
                        userMap.get(judgeFrequency) : new Date();

                //在此时间上加24小时+30分钟
                Date expireDate = DateUtils.addDateMinutes(time, (24 * 60) + 30);

                Long expireMinutes = DateUtils.betweenMinutes(time, expireDate);

                redisTemplate.opsForValue().set(RedisKeys.RedisKeys12.getValue(phone), String.valueOf(registerCount), expireMinutes, TimeUnit.MINUTES);
            }
            return registerCount;
        } catch (Exception e) {
            log.error("更新手机号注册次数异常 {}, {}", cdLineRegisterEntity, e);
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
                log.info("注册任务，插入子任务开始 keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
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


    /**
     * 保存待获取验证码数据
     */
    @Scheduled(fixedDelay = 5000)
    @Async
    public void saveWaitSmsData() {
        ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
        if (ObjectUtil.isNull(projectWorkEntity)) {
            return;
        }

        Long size = redisTemplate.opsForList().size(RedisKeys.SAVE_WAIT_SMS_PHONE.getValue());
        if (size == 0) {
            log.info("无需要保存待获取验证码数据");
            return;
        }
        for (Integer i = 0; i < size; i++) {
            String s = redisTemplate.opsForList()
                    .leftPop(RedisKeys.SAVE_WAIT_SMS_PHONE.getValue());
            if (StringUtils.isEmpty(s)) {
                log.warn("无需要保存待获取验证码数据，取数据为空");
                return;
            }
            CdRegisterRedisDto phoneRedisDto = JSON.parseObject(s, CdRegisterRedisDto.class);
            poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource
                        .getKeyByResource(LockMapKeyResource.SAVE_WAIT_SMS_PHONE, phoneRedisDto.getTelPhone());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("保存待获取验证码数据 keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        CdGetPhoneEntity phone = phoneRedisDto.getPhoneEntity();
                        CdLineRegisterEntity lineRegister = phoneRedisDto.getLineRegister();

                        //保存line
                        cdLineRegisterService.save(lineRegister);

                        phone.setLineRegisterId(lineRegister.getId());
                        cdGetPhoneService.updateById(phone);

                        //保存redis--》待获取验证码
                        redisTemplate.opsForHash().put(RedisKeys.WAIT_SMS_PHONE.getValue(String.valueOf(phone.getCountry())),
                                phoneRedisDto.getTelPhone(), JSON.toJSONString(phoneRedisDto));
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
     * 保存已获取到验证码数据
     */
    @Scheduled(fixedDelay = 5000)
    @Async
    public void saveWaitRegisterData() {
        ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
        if (ObjectUtil.isNull(projectWorkEntity)) {
            return;
        }

        Long size = redisTemplate.opsForList().size(RedisKeys.SAVE_WAIT_REGISTER_PHONE.getValue());
        if (size == 0) {
            log.info("保存验证码数据为空，无需保存");
            return;
        }
        for (Integer i = 0; i < size; i++) {
            String s = redisTemplate.opsForList()
                    .leftPop(RedisKeys.SAVE_WAIT_REGISTER_PHONE.getValue());
            if (StringUtils.isEmpty(s)) {
                log.warn("保存验证码数据为空，取数据为空");
                return;
            }
            CdRegisterRedisDto phoneRedisDto = JSON.parseObject(s, CdRegisterRedisDto.class);
            poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource
                        .getKeyByResource(LockMapKeyResource.SAVE_WAIT_REGISTER_PHONE, phoneRedisDto.getTelPhone());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("保存验证码数据 keyByResource = {} 获取的锁为 = {}", keyByResource, triedLock);
                if(triedLock) {
                    try{
                        CdGetPhoneEntity phoneEntity = phoneRedisDto.getPhoneEntity();
                        CdLineRegisterEntity lineRegister = phoneRedisDto.getLineRegister();

                        cdGetPhoneService.updateById(phoneEntity);
                        if (Boolean.TRUE.equals(phoneRedisDto.getSmsFlag())) {
                            cdLineRegisterService.updateById(lineRegister);
                        }

                        if(PhoneStatus5.getKey().equals(phoneEntity.getPhoneStatus())) {
                            //释放手机号
                            redisTemplate.opsForHash().put(RedisKeys.WAIT_SMS_PHONE.getValue(String.valueOf(phoneEntity.getCountry())),
                                    phoneRedisDto.getTelPhone(), JSON.toJSONString(phoneRedisDto));
                        } else {
                            //提交验证码，开始注册
                            redisTemplate.opsForHash().put(RedisKeys.WAIT_REGISTER_PHONE.getValue(),
                                    phoneRedisDto.getTelPhone(), JSON.toJSONString(phoneRedisDto));
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
