package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.constant.SystemConstant;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.AddFriendsByMid;
import io.renren.modules.client.dto.SearchPhoneDTO;
import io.renren.modules.client.vo.GroupCountByDataTaskIdVO;
import io.renren.modules.client.vo.SearchPhoneVO;
import io.renren.modules.client.vo.The818051863582;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.*;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.AtDataSubtaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/21 18:45
 */
@Component
@Slf4j
@EnableAsync
@Profile({"prod","task"})
public class DataTask {


    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtUserTokenService atUserTokenService;
    @Autowired
    private AtUserGroupService atUserGroupService;


    static ReentrantLock task1Lock = new ReentrantLock();
    static ReentrantLock task2Lock = new ReentrantLock();
    static ReentrantLock task3Lock = new ReentrantLock();
    static ReentrantLock task4Lock = new ReentrantLock();

    public static final Object atAtDataSubtaskObj = new Object();
    public static final Object atAtDataTaskEntityObj = new Object();
    @Autowired
    private LineService lineService;


    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private AtAvatarService atAvatarService;
    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;
    @Autowired
    private ConcurrentHashMap<String, Lock> lockMap;
    @Autowired
    private AtGroupService atGroupService;

    @Autowired
    private AtDataTaskService atDataTaskService;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;
    @Resource(name = "caffeineCacheDate")
    private Cache<Integer, Date> caffeineCacheDate;
    @Resource(name = "caffeineCacheDateSearch")
    private Cache<Integer, Date> caffeineCacheDateSearch;
    @Resource
    private SystemConstant systemConstant;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 更新
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task7() {
        //服务器更新锁
        Integer mod = systemConstant.getSERVERS_MOD();
        String key = String.valueOf(mod);
        String MOD_NX_KEY = RedisKeys.MOD_NX.getValue(key);
        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(MOD_NX_KEY, key);
        if (!b) {
            return;
        }
        redisTemplate.expire(MOD_NX_KEY, 2, TimeUnit.MINUTES);
        try {
            //加粉任务保存
            List<AtDataTaskEntity> elements = new ArrayList<>();
            AtDataTaskEntity element = null;
            while ((element = (AtDataTaskEntity) redisTemplate.opsForList().rightPop(RedisKeys.ATDATATASKENTITY_LIST.getValue(key))) != null) {
                elements.add(element);
            }
            if (CollUtil.isNotEmpty(elements)) {
                try{
                    boolean b1 = atDataTaskService.updateBatchById(elements);
                    //如果保存失败，把数据重新保存
                    if (!b1) {
                        for (AtDataTaskEntity atDataTaskEntity : elements) {
                            Long l = redisTemplate.opsForList().leftPush(RedisKeys.ATDATATASKENTITY_LIST.getValue(key), atDataTaskEntity);
                        }
                    }
                    //如果保存失败，把数据重新保存
                }catch (Exception e) {
                    for (AtDataTaskEntity atDataTaskEntity : elements) {
                        Long l = redisTemplate.opsForList().leftPush(RedisKeys.ATDATATASKENTITY_LIST.getValue(key), atDataTaskEntity);
                    }
                }
            }

            //群任务保存
            List<AtGroupEntity> elementsGroups = new ArrayList<>();
            AtGroupEntity group = null;
            while ((group = (AtGroupEntity) redisTemplate.opsForList().rightPop(RedisKeys.ATGROUPENTITY_LIST.getValue(key))) != null) {
                elementsGroups.add(group);
            }
            if (CollUtil.isNotEmpty(elementsGroups)) {
                try{
                    boolean b1 = atGroupService.updateBatchById(elementsGroups);
                    //如果保存失败，把数据重新保存
                    if (!b1) {
                        for (AtGroupEntity elementsGroup : elementsGroups) {
                            Long l = redisTemplate.opsForList().leftPush(RedisKeys.ATGROUPENTITY_LIST.getValue(key), elementsGroup);
                        }
                    }
                    //如果保存失败，把数据重新保存
                }catch (Exception e) {
                    for (AtGroupEntity elementsGroup : elementsGroups) {
                        Long l = redisTemplate.opsForList().leftPush(RedisKeys.ATGROUPENTITY_LIST.getValue(key), elementsGroup);
                    }
                }
            }

            //用户任务队列保存
            List<AtUserEntity> elementsUsers = new ArrayList<>();
            AtUserEntity user = null;
            while ((user = (AtUserEntity) redisTemplate.opsForList().rightPop(RedisKeys.ATUSERENTITY_LIST.getValue(key))) != null) {
                elementsUsers.add(user);
            }
            if (CollUtil.isNotEmpty(elementsUsers)) {
                try{
                    boolean b1 = atUserService.updateBatchById(elementsUsers);
                    //如果保存失败，把数据重新保存
                    if (!b1) {
                        for (AtUserEntity elementsUser : elementsUsers) {
                            Long l = redisTemplate.opsForList().leftPush(RedisKeys.ATUSERENTITY_LIST.getValue(key), elementsUser);
                        }
                    }
                    //如果保存失败，把数据重新保存
                }catch (Exception e) {
                    for (AtUserEntity elementsUser : elementsUsers) {
                        Long l = redisTemplate.opsForList().leftPush(RedisKeys.ATUSERENTITY_LIST.getValue(key), elementsUser);
                    }
                }
            }
        }catch (Exception e){
            log.error("data atDataTaskService.updateBatchById atGroupService.updateBatchById atUserService.updateBatchById task7 err = {}",e.getMessage());
        }finally {
            if (b) {
                stringRedisTemplate.delete(MOD_NX_KEY);
            }
        }

    }

    /**
     * 更新加粉子任务任务状态
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task6() {
        //获取在工作的所有任务
        Set<String> members = stringRedisTemplate.opsForSet().members(RedisKeys.USER_TASKS_POOL.getValue(String.valueOf(systemConstant.getSERVERS_MOD())));
        //任务为空
        if (CollUtil.isEmpty(members)) {
            log.info("Data2Task task2 atDataSubtaskEntities isEmpty");
            return;
        }
        for (String userId : members) {
            threadPoolTaskExecutor.execute(() -> {
                String USER_TASKWORK_FINISH_KEY = RedisKeys.USER_TASKS_WORK_FINISH_NX.getValue(userId);
                Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(USER_TASKWORK_FINISH_KEY, userId);
                if (!b) {
                    return;
                }
                redisTemplate.expire(USER_TASKWORK_FINISH_KEY, 2, TimeUnit.MINUTES);
                try {
                    List<AtDataSubtaskEntity> elements = new ArrayList<>();
                    AtDataSubtaskEntity element = null;
                    while ((element = (AtDataSubtaskEntity) redisTemplate.opsForList().rightPop(RedisKeys.USER_TASKS_WORK_FINISH.getValue(userId))) != null) {
                        elements.add(element);
                    }
                    if (CollUtil.isNotEmpty(elements)) {
                        try{
                            boolean b1 = atDataSubtaskService.updateBatchById(elements);
                            //如果保存失败，把数据重新保存
                            if (!b1) {
                                for (AtDataSubtaskEntity atDataSubtaskEntity : elements) {
                                    Long l = redisTemplate.opsForList().leftPush(RedisKeys.USER_TASKS_WORK_FINISH.getValue(userId), atDataSubtaskEntity);
                                }
                            }
                            //如果保存失败，把数据重新保存
                        }catch (Exception e) {
                            for (AtDataSubtaskEntity atDataSubtaskEntity : elements) {
                                Long l = redisTemplate.opsForList().leftPush(RedisKeys.USER_TASKS_WORK_FINISH.getValue(userId), atDataSubtaskEntity);
                            }
                        }
                    }
                }catch (Exception e) {
                    log.error("data atDataSubtaskService.updateBatchById err = {}",e.getMessage());
                }finally {
                    if (b) {
                        //任务完成移除锁
                        stringRedisTemplate.delete(USER_TASKWORK_FINISH_KEY);
                    }
                }
            });
        }
    }

    @Scheduled(fixedDelay = 8000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task5() {
        //服务器更新锁
        Integer mod = systemConstant.getSERVERS_MOD();
        String key = String.valueOf(mod);
        String MOD_KEY = RedisKeys.MOD_NX.getValue(key);
        Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(MOD_KEY, key);
        if (!b) {
            return;
        }
        redisTemplate.expire(MOD_KEY, 2, TimeUnit.MINUTES);
        try {
            List<GroupCountByDataTaskIdVO> groupCountByDataTaskIdVOS = atDataTaskService.groupCountByDataTaskId();
            if (CollUtil.isEmpty(groupCountByDataTaskIdVOS)) {
                log.info("DataTask task4 atDataTaskEntities isEmpty");
                return;
            }
            List<AtDataTaskEntity> atDataTaskEntityList = new ArrayList<>();
            for (GroupCountByDataTaskIdVO groupCountByDataTaskIdVO : groupCountByDataTaskIdVOS) {
                //成功的任务
                long fail = groupCountByDataTaskIdVO.getFail5();
                //失败的任务
                long success8 = groupCountByDataTaskIdVO.getSuccess8();
                long success10 = groupCountByDataTaskIdVO.getSuccess10();
                AtDataTaskEntity update = new AtDataTaskEntity();
                if (GroupType.GroupType1.getKey().equals(groupCountByDataTaskIdVO.getGroupType())) {
                    update.setSuccessfulQuantity((int) success8);
                    update.setFailuresQuantity((int) fail);
                    update.setId(groupCountByDataTaskIdVO.getDataTaskId());
                    if (success8 + fail >= groupCountByDataTaskIdVO.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }else if (GroupType.GroupType5.getKey().equals(groupCountByDataTaskIdVO.getGroupType())) {
                    update.setSuccessfulQuantity((int) success10);
                    update.setFailuresQuantity((int) fail);
                    update.setId(groupCountByDataTaskIdVO.getDataTaskId());
                    if (success10 + fail >= groupCountByDataTaskIdVO.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }else if (GroupType.GroupType6.getKey().equals(groupCountByDataTaskIdVO.getGroupType())) {
                    update.setSuccessfulQuantity((int) success10 + (int) success8);
                    update.setFailuresQuantity((int) fail);
                    update.setId(groupCountByDataTaskIdVO.getDataTaskId());
                    if (success10 + success8 + fail >= groupCountByDataTaskIdVO.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }else if (GroupType.GroupType2.getKey().equals(groupCountByDataTaskIdVO.getGroupType())) {
                    update.setSuccessfulQuantity((int) success8);
                    update.setFailuresQuantity((int) fail);
                    update.setId(groupCountByDataTaskIdVO.getDataTaskId());
                    if (success8 + fail >= groupCountByDataTaskIdVO.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }else if (GroupType.GroupType3.getKey().equals(groupCountByDataTaskIdVO.getGroupType())) {
                    update.setSuccessfulQuantity((int) success8);
                    update.setFailuresQuantity((int) fail);
                    update.setId(groupCountByDataTaskIdVO.getDataTaskId());
                    if (success8 + fail >= groupCountByDataTaskIdVO.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }else if (GroupType.GroupType4.getKey().equals(groupCountByDataTaskIdVO.getGroupType())) {
                    update.setSuccessfulQuantity((int) success8);
                    update.setFailuresQuantity((int) fail);
                    update.setId(groupCountByDataTaskIdVO.getDataTaskId());
                    if (success8 + fail >= groupCountByDataTaskIdVO.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }

                //如果拉群成功
                if (TaskStatus.TaskStatus3.getKey().equals(update.getTaskStatus())) {
                    if (ObjectUtil.isNotNull(groupCountByDataTaskIdVO.getGroupId())) {

                        AtGroupEntity atGroupEntity = atGroupService.getById((Serializable) groupCountByDataTaskIdVO.getGroupId());
                        if (ObjectUtil.isNotNull(atGroupEntity)) {
                            if (OpenApp.OpenApp1.getKey().equals(atGroupEntity.getAutoPullGroup())) {
                                atGroupEntity.setGroupStatus(GroupStatus.GroupStatus14.getKey());
                            }else if (OpenApp.OpenApp2.getKey().equals(atGroupEntity.getAutoPullGroup())) {
                                atGroupEntity.setGroupStatus(GroupStatus.GroupStatus7.getKey());
                            }else {
                                atGroupEntity.setGroupStatus(GroupStatus.GroupStatus14.getKey());
                            }
                            // 任务失败 设置群任务队列
                            redisTemplate.opsForList().leftPush(RedisKeys.ATGROUPENTITY_LIST.getValue(String.valueOf(systemConstant.getSERVERS_MOD())), atGroupEntity);
                        }
                    }
                }
            }
            if (CollUtil.isNotEmpty(atDataTaskEntityList)) {
                for (AtDataTaskEntity atDataTaskEntity : atDataTaskEntityList) {
                    redisTemplate.opsForList().leftPush(RedisKeys.ATDATATASKENTITY_LIST.getValue(String.valueOf(systemConstant.getSERVERS_MOD())), atDataTaskEntity);
                }
            }
        }
        catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            if (b) {
                stringRedisTemplate.delete(MOD_KEY);
            }
        }

    }

    /**
     * 通过手机号搜索成功 添加好友
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task3() {
        AtDataSubtaskEntity dto = new AtDataSubtaskEntity();
        dto.setTaskStatus(TaskStatus.TaskStatus7.getKey());
        dto.setGroupType(GroupType.GroupType1.getKey());
        List<AtDataSubtaskVO> atDataSubtaskEntities = atDataSubtaskService.groupByUserId(dto);
        //任务为空
        if (CollUtil.isEmpty(atDataSubtaskEntities)) {
            log.info("DataTask task2 atDataSubtaskEntities isEmpty");
            return;
        }

        //获取用户MAP
        List<Integer> userIds = atDataSubtaskEntities.stream().map(AtDataSubtaskVO::getUserId).collect(Collectors.toList());
        List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
        List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
        List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
        Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
        Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId()).setTelephone(item.getTelephone())));
        for (AtDataSubtaskVO atDataSubtaskEntity : atDataSubtaskEntities) {
            threadPoolTaskExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource7, atDataSubtaskEntity.getDataTaskId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        AtGroupEntity atGroupEntityConfig = null;
                        if (ObjectUtil.isNotNull(atDataSubtaskEntity.getGroupId())) {
                            atGroupEntityConfig = atGroupService.getByIdCache(atDataSubtaskEntity.getGroupId());
                            if (ObjectUtil.isNull(atGroupEntityConfig)) {
                                return;
                            }
                            Date nextTime = caffeineCacheDate.getIfPresent(atGroupEntityConfig.getId());
                            //如果没有下一次的时间 设置默认的时间
                            if (ObjectUtil.isNotNull(nextTime)) {
                                DateTime now = DateUtil.date();
                                boolean after = now.after(nextTime);
                                if (!after) {
                                    return;
                                }
                            }else {
                                int i = RandomUtil.randomInt(3, 5);
                                Thread.sleep(i * 1000L);
                            }
                        }else {
                            int i = RandomUtil.randomInt(3, 5);
                            Thread.sleep(i * 1000L);
                        }
                        //获取用户token
                        AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(atDataSubtaskEntity.getUserId());
                        if (ObjectUtil.isNull(atUserTokenEntity)) {
                            return;
                        }
                        //查询mid
                        AtDataSubtaskVO atDataSubtaskVO = atDataSubtaskService.getById(atDataSubtaskEntity.getId());
                        if (ObjectUtil.isNull(atDataSubtaskVO)) {
                            return;
                        }
                        String contactKey = atDataSubtaskVO.getContactKey();
                        String mid = atDataSubtaskVO.getMid();
                        //获取代理
                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(atUserTokenEntity.getTelephone());
                        cdLineIpProxyDTO.setLzPhone(contactKey);
                        //去设置区号
                        if (ObjectUtil.isNotNull(atGroupEntityConfig.getIpCountryCode())) {
                            cdLineIpProxyDTO.setCountryCode(atGroupEntityConfig.getIpCountryCode().longValue());
                        }
                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                        if (StrUtil.isEmpty(proxyIp)) {
                            return;
                        }

                        AddFriendsByMid addFriendsByMid = new AddFriendsByMid();
                        addFriendsByMid.setProxy(proxyIp);
                        addFriendsByMid.setPhone(contactKey);
                        addFriendsByMid.setMid(mid);
                        addFriendsByMid.setFriendAddType("phoneSearch");
                        addFriendsByMid.setToken(atUserTokenEntity.getToken());
                        SearchPhoneVO searchPhoneVO = lineService.addFriendsByMid(addFriendsByMid);
                        AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                        if (ObjectUtil.isNull(searchPhoneVO)) {
                            return;
                        }
                        update.setMsg(searchPhoneVO.getMsg());
                        if (200 == searchPhoneVO.getCode()) {
                            update.setTaskStatus(TaskStatus.TaskStatus8.getKey());
                        } else if (201 == searchPhoneVO.getCode()) {
                            if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode10.getValue())) {
                                update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode5.getValue())) {
                                update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode11.getValue())) {
                                update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                            } else {
                                UserStatus userStatus = UserStatus.UserStatus4;
                                update.setTaskStatus(TaskStatus.TaskStatus13.getKey());
                                //需要刷新token
                                if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode4.getValue())) {
                                    userStatus = UserStatus.UserStatus7;
                                } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode6.getValue())) {
                                    userStatus = UserStatus.UserStatus3;
                                } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode7.getValue())) {
                                    userStatus = UserStatus.UserStatus2;
                                    //如果失败，修改状态
                                    update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode8.getValue())) {
                                    userStatus = UserStatus.UserStatus3;
                                }


                                //任务失败
                                AtDataTaskEntity atDataTaskEntity = new AtDataTaskEntity();
                                atDataTaskEntity.setId(atDataSubtaskEntity.getDataTaskId());
                                atDataTaskEntity.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                atDataTaskService.updateById(atDataTaskEntity);
                                if (ObjectUtil.isNotNull(atDataSubtaskEntity.getGroupId())) {
                                    //拉群改状态
                                    AtGroupEntity atGroupEntity = new AtGroupEntity();
                                    atGroupEntity.setId(atDataSubtaskEntity.getGroupId());
                                    atGroupEntity.setGroupStatus(GroupStatus.GroupStatus11.getKey());
                                    atGroupService.updateById(atGroupEntity);
                                }

                                if (TaskStatus.TaskStatus5.getKey().equals(update.getTaskStatus()) || TaskStatus.TaskStatus13.getKey().equals(update.getTaskStatus())) {
                                    update.setId(null);
                                    atDataSubtaskService.update(update,new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                            .eq(AtDataSubtaskEntity::getDataTaskId,atDataSubtaskEntity.getDataTaskId())
                                            .eq(AtDataSubtaskEntity::getUserId,atDataSubtaskEntity.getUserId())
                                            .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus2.getKey())
                                    );
                                    atUserService.unlock(atDataSubtaskEntity.getUserId(),userStatus);
                                }
                            }
                        }

                        update.setId(atDataSubtaskEntity.getId());
                        //设置加好友的时间
                        update.setCreateTime(DateUtil.date());
                        atDataSubtaskService.updateById(update);
                    } catch (InterruptedException e) {
                        log.error("err = {}",e.getMessage());
                    } finally {
                        lock.unlock();
                    }
                }else {
                    log.info("keyByResource = {} 在执行",keyByResource);
                }
            });

        }
    }


    /**
     * 获取type为1 加粉类型为手机号模式
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task2() {
        AtDataSubtaskEntity dto = new AtDataSubtaskEntity();
        dto.setTaskStatus(TaskStatus.TaskStatus2.getKey());
        dto.setGroupType(GroupType.GroupType1.getKey());
        List<AtDataSubtaskVO> atDataSubtaskEntities = atDataSubtaskService.groupByUserId(dto);
        //任务为空
        if (CollUtil.isEmpty(atDataSubtaskEntities)) {
            log.info("DataTask task2 atDataSubtaskEntities isEmpty");
            return;
        }
        //获取用户MAP
        List<Integer> userIds = atDataSubtaskEntities.stream().map(AtDataSubtaskVO::getUserId).collect(Collectors.toList());
        List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
        List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
        List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
        Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
        Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId()).setTelephone(item.getTelephone())));
        for (AtDataSubtaskVO atDataSubtaskEntity : atDataSubtaskEntities) {

            threadPoolTaskExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource7, atDataSubtaskEntity.getDataTaskId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        AtGroupEntity atGroupEntityConfig = null;
                        if (ObjectUtil.isNotNull(atDataSubtaskEntity.getGroupId())) {
                            atGroupEntityConfig = atGroupService.getByIdCache(atDataSubtaskEntity.getGroupId());
                            if (ObjectUtil.isNull(atGroupEntityConfig)) {
                                return;
                            }//curl -x chenweilong122-zone-resi:ch1433471850@43.152.113.218:10838 202.79.171.146:8080
                            Date nextTime = caffeineCacheDateSearch.getIfPresent(atGroupEntityConfig.getId());
                            //如果没有下一次的时间 设置默认的时间
                            if (ObjectUtil.isNotNull(nextTime)) {
                                DateTime now = DateUtil.date();
                                boolean after = now.after(nextTime);
                                if (!after) {
                                    return;
                                }
                            }else {
                                int i = RandomUtil.randomInt(3, 5);
                                Thread.sleep(i * 1000L);
                            }
                        }else {
                            int i = RandomUtil.randomInt(3, 5);
                            Thread.sleep(i * 1000L);
                        }
                        //获取用户token
                        AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(atDataSubtaskEntity.getUserId());
                        if (ObjectUtil.isNull(atUserTokenEntity)) {
                            return;
                        }
                        //查询mid
                        AtDataSubtaskVO atDataSubtaskVO = atDataSubtaskService.getById(atDataSubtaskEntity.getId());
                        if (ObjectUtil.isNull(atDataSubtaskVO)) {
                            return;
                        }
                        //是否需要打开app,如果需要打开，直接去打开app
                        Integer openApp = atDataSubtaskVO.getOpenApp();
                        if (OpenApp.OpenApp2.getKey().equals(openApp)) {
                            //如果为空或者小于0，直接去设置一下需要打开app
                            if (ObjectUtil.isNull(atUserTokenEntity.getDataSubtaskId()) || atUserTokenEntity.getDataSubtaskId() <= 0) {
                                // 设置立即打开app
                                atUserTokenEntity.setOpenTime(DateUtil.date());
                                atUserTokenEntity.setOpenStatus(OpenStatus.OpenStatus1.getKey());
                                atUserTokenEntity.setDataSubtaskId(atDataSubtaskVO.getId());
                                atUserTokenService.updateById(atUserTokenEntity);
                            }
                            return;
                        }
                        String contactKey = atDataSubtaskVO.getContactKey();
                        if (StrUtil.isNotEmpty(contactKey)) {
                            //获取代理
                            CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                            cdLineIpProxyDTO.setTokenPhone(atUserTokenEntity.getTelephone());
                            cdLineIpProxyDTO.setLzPhone(contactKey);
                            //去设置区号
                            if (ObjectUtil.isNotNull(atGroupEntityConfig.getIpCountryCode())) {
                                cdLineIpProxyDTO.setCountryCode(atGroupEntityConfig.getIpCountryCode().longValue());
                            }
                            String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                            if (StrUtil.isEmpty(proxyIp)) {
                                return;
                            }

                            SearchPhoneDTO searchPhoneDTO = new SearchPhoneDTO();
                            searchPhoneDTO.setProxy(proxyIp);
                            searchPhoneDTO.setPhone(contactKey);
                            searchPhoneDTO.setToken(atUserTokenEntity.getToken());
                            SearchPhoneVO searchPhoneVO = lineService.searchPhone(searchPhoneDTO);
                            AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                            if (ObjectUtil.isNull(searchPhoneVO)) {
                                return;
                            }

                            update.setMsg(searchPhoneVO.getMsg());
                            if (200 == searchPhoneVO.getCode()) {
                                update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                Map<String, The818051863582> data = searchPhoneVO.getData();
                                Collection<The818051863582> values = data.values();
                                for (The818051863582 value : values) {
                                    update.setMid(value.getMid());
                                    update.setTaskStatus(TaskStatus.TaskStatus7.getKey());
                                    update.setDisplayName(value.getDisplayName());
                                    update.setPhoneticName(value.getPhoneticName());
                                    update.setPictureStatus(value.getPictureStatus());
                                    update.setThumbnailUrl(value.getThumbnailUrl());
                                    update.setStatusMessage(value.getStatusMessage());
                                    update.setPicturePath(value.getPicturePath());
                                    update.setRecommendpArams(value.getRecommendParams());
                                    update.setMusicProfile(proxyIp);
                                    update.setVideoProfile(value.getVideoProfile());
                                }
                            } else if (201 == searchPhoneVO.getCode()) {
                                if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode10.getValue())) {
                                    update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode5.getValue())) {
                                    update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode11.getValue())) {
                                    update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                } else {
                                    UserStatus userStatus = UserStatus.UserStatus4;
                                    update.setTaskStatus(TaskStatus.TaskStatus13.getKey());
                                    //需要刷新token
                                    if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode4.getValue())) {
                                        userStatus = UserStatus.UserStatus7;
                                    } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode6.getValue())) {
                                        userStatus = UserStatus.UserStatus3;
                                    } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode7.getValue())) {
                                        userStatus = UserStatus.UserStatus2;
                                        //如果失败，修改状态
                                        update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                    } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode8.getValue())) {
                                        userStatus = UserStatus.UserStatus3;
                                    }


                                    //任务失败
                                    AtDataTaskEntity atDataTaskEntity = new AtDataTaskEntity();
                                    atDataTaskEntity.setId(atDataSubtaskEntity.getDataTaskId());
                                    atDataTaskEntity.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                    atDataTaskService.updateById(atDataTaskEntity);
                                    if (ObjectUtil.isNotNull(atDataSubtaskEntity.getGroupId())) {
                                        //拉群改状态
                                        AtGroupEntity atGroupEntity = new AtGroupEntity();
                                        atGroupEntity.setId(atDataSubtaskEntity.getGroupId());
                                        atGroupEntity.setGroupStatus(GroupStatus.GroupStatus11.getKey());
                                        atGroupService.updateById(atGroupEntity);
                                    }

                                    if (TaskStatus.TaskStatus5.getKey().equals(update.getTaskStatus()) || TaskStatus.TaskStatus13.getKey().equals(update.getTaskStatus())) {
                                        update.setId(null);
                                        atDataSubtaskService.update(update,new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                                .eq(AtDataSubtaskEntity::getDataTaskId,atDataSubtaskEntity.getDataTaskId())
                                                .eq(AtDataSubtaskEntity::getUserId,atDataSubtaskEntity.getUserId())
                                                .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus2.getKey())
                                        );
                                        atUserService.unlock(atDataSubtaskEntity.getUserId(),userStatus);
                                    }
                                }
                            }
                            update.setId(atDataSubtaskEntity.getId());
                            //设置加好友的时间
                            update.setCreateTime(DateUtil.date());
                            atDataSubtaskService.updateById(update);
                            //如果配置不为空
                            if (ObjectUtil.isNotNull(atGroupEntityConfig)) {
                                //获取间隔的秒,设置下次可以搜索的时间
                                Integer searchIntervalSecond = atGroupEntityConfig.getSearchIntervalSecond();
                                if (ObjectUtil.isNotNull(searchIntervalSecond)) {
                                    int i = RandomUtil.randomInt(searchIntervalSecond, searchIntervalSecond + 2);
                                    DateTime nextTime = DateUtil.offsetSecond(DateUtil.date(), i);
                                    caffeineCacheDateSearch.put(atGroupEntityConfig.getId(),nextTime);
                                }

                                //获取间隔的秒,设置下次加好友的时间
                                Integer intervalSecond = atGroupEntityConfig.getIntervalSecond();
                                if (ObjectUtil.isNotNull(intervalSecond)) {
                                    int i = RandomUtil.randomInt(intervalSecond, intervalSecond + 2);
                                    DateTime nextTime = DateUtil.offsetSecond(DateUtil.date(), i);
                                    caffeineCacheDate.put(atGroupEntityConfig.getId(),nextTime);
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        log.error("err = ",e.getMessage());
                    } finally {
                        lock.unlock();
                    }
                }else {
                    log.info("keyByResource = {} 在执行",keyByResource);
                }
            });
        }
    }

    /**
     * 获取初始化的添加粉任务
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task1() {
        boolean b = task1Lock.tryLock();
        if (!b) {
            return;
        }
        try{
            //配置总机器
            String format = String.format("and MOD(id, %s) = %s limit 15", systemConstant.getSERVERS_TOTAL_MOD(), systemConstant.getSERVERS_MOD());
            //需要添加好友的任务
            List<AtDataTaskEntity> atDataTaskEntities = atDataTaskService.list(new QueryWrapper<AtDataTaskEntity>().lambda()
                    .eq(AtDataTaskEntity::getTaskStatus, TaskStatus.TaskStatus1.getKey())
                    .last(format)
            );
            if (CollUtil.isEmpty(atDataTaskEntities)) {
                log.info("DataTask task1 atDataTaskEntities isEmpty");
                return;
            }
            //需要添加好友的子任务任务
            List<Integer> ids = atDataTaskEntities.stream().map(AtDataTaskEntity::getId).collect(Collectors.toList());
            List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                    .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus1.getKey())
                    .in(AtDataSubtaskEntity::getDataTaskId,ids)
            );

            if (CollUtil.isEmpty(atDataSubtaskEntities)) {
                log.info("DataTask task1 atDataSubtaskEntities isEmpty");

                List<AtDataTaskEntity> atDataTaskEntityList = new ArrayList<>();
                for (AtDataTaskEntity atDataTaskEntity : atDataTaskEntities) {
                    AtDataTaskEntity update = new AtDataTaskEntity();
                    update.setId(atDataTaskEntity.getId());
                    update.setTaskStatus(TaskStatus.TaskStatus2.getKey());
                    atDataTaskEntityList.add(update);
                }
                synchronized (atAtDataTaskEntityObj) {
                    atDataTaskService.updateBatchById(atDataTaskEntityList);
                }
                return;
            }

            List<AtDataSubtaskEntity> updates = new ArrayList<>();
            for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntities) {
                AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                update.setId(atDataSubtaskEntity.getId());
                update.setTaskStatus(TaskStatus.TaskStatus2.getKey());
                atDataSubtaskEntity.setTaskStatus(update.getTaskStatus());
                updates.add(update);
            }
            synchronized (atAtDataSubtaskObj) {
                boolean b1 = atDataSubtaskService.updateBatchById(updates);
                if(b1) {
                    //用户任务池子 暂时过滤只要mid加粉的
                    Map<Integer, List<AtDataSubtaskEntity>> userIdTaskSubEntitys = atDataSubtaskEntities.stream().filter(item -> GroupType.GroupType2.getKey().equals(item.getGroupType())).collect(Collectors.groupingBy(AtDataSubtaskEntity::getUserId));
                    for (Integer userId : userIdTaskSubEntitys.keySet()) {
                        String USER_TASKS_WORKING_NX_KEY = RedisKeys.USER_TASKS_WORKING_NX.getValue(String.valueOf(userId));
                        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(USER_TASKS_WORKING_NX_KEY, String.valueOf(userId));
                        if (!flag) {
                            return;
                        }
                        redisTemplate.expire(USER_TASKS_WORKING_NX_KEY, 2, TimeUnit.MINUTES);
                        try {
                            //设置到当前机器任务池
                            Long add = stringRedisTemplate.opsForSet().add(RedisKeys.USER_TASKS_POOL.getValue(String.valueOf(systemConstant.getSERVERS_MOD())), String.valueOf(userId));
                            log.info("任务池保存成功条数 ====》 {}",add);
                            if (add > 0) {
                                List<AtDataSubtaskEntity> atDataSubtaskEntityList = userIdTaskSubEntitys.get(userId);
                                for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntityList) {
                                    //设置用户id任务队列
                                    Long l = redisTemplate.opsForList().leftPush(RedisKeys.USER_TASKS_WORKING.getValue(String.valueOf(userId)), atDataSubtaskEntity);
                                    log.info("用户任务池保存成功条数 ====》 {}",l);
                                }
                            }
                        }finally {
                            if (flag) {
                                stringRedisTemplate.delete(USER_TASKS_WORKING_NX_KEY);
                            }
                        }
                    }
                }
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task1Lock.unlock();
        }
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task10() {
        //获取当前用户的池子
        Set<String> members = stringRedisTemplate.opsForSet().members(RedisKeys.USER_TASKS_POOL.getValue(String.valueOf(systemConstant.getSERVERS_MOD())));
        //任务为空
        if (CollUtil.isEmpty(members)) {
            log.info("Data2Task task2 atDataSubtaskEntities isEmpty");
            return;
        }
        for (String userIdCache : members) {
            threadPoolTaskExecutor.execute(() -> {
                String value = RedisKeys.USER_TASKS_WORKING_CLEAN_NX.getValue(userIdCache);
                //锁住当前用户清理
                Boolean b = stringRedisTemplate.opsForValue().setIfAbsent(value, userIdCache);
                if (!b) {
                    return;
                }
                redisTemplate.expire(value, 2, TimeUnit.MINUTES);

                String USER_TASKS_WORKING_NX_KEY = RedisKeys.USER_TASKS_WORKING_NX.getValue(String.valueOf(userIdCache));
                try {
                    //查询是否有数据
                    Long size = redisTemplate.opsForList().size(RedisKeys.USER_TASKS_WORKING.getValue(userIdCache));
                    if (size > 0) {
                        return;
                    }
                    Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(USER_TASKS_WORKING_NX_KEY, String.valueOf(userIdCache));
                    if (!flag) {
                        return;
                    }
                    redisTemplate.expire(USER_TASKS_WORKING_NX_KEY, 2, TimeUnit.MINUTES);

                    List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                            .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus2.getKey())
                            .in(AtDataSubtaskEntity::getUserId,Integer.valueOf(userIdCache))
                    );
                    if (CollUtil.isNotEmpty(atDataSubtaskEntities)) {
                        //用户任务池子 暂时过滤只要mid加粉的
                        Map<Integer, List<AtDataSubtaskEntity>> userIdTaskSubEntitys = atDataSubtaskEntities.stream().filter(item -> GroupType.GroupType2.getKey().equals(item.getGroupType())).collect(Collectors.groupingBy(AtDataSubtaskEntity::getUserId));
                        for (Integer userId : userIdTaskSubEntitys.keySet()) {
                            try {
                                //设置到当前机器任务池
                                Long add = stringRedisTemplate.opsForSet().add(RedisKeys.USER_TASKS_POOL.getValue(String.valueOf(systemConstant.getSERVERS_MOD())), String.valueOf(userId));
                                log.info("任务池保存成功条数 ====》 {}",add);
                                List<AtDataSubtaskEntity> atDataSubtaskEntityList = userIdTaskSubEntitys.get(userId);
                                for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntityList) {
                                    //设置用户id任务队列
                                    Long l = redisTemplate.opsForList().leftPush(RedisKeys.USER_TASKS_WORKING.getValue(String.valueOf(userId)), atDataSubtaskEntity);
                                    log.info("用户任务池保存成功条数 ====》 {}",l);
                                }
                            }finally {

                            }
                        }
                    }else {
                        //清理set
                        stringRedisTemplate.opsForSet().remove(RedisKeys.USER_TASKS_POOL.getValue(String.valueOf(systemConstant.getSERVERS_MOD())),userIdCache);
                    }
                }catch (Exception e){
                    log.error("dataTask10 error = {}",e.getMessage());
                }finally {
                    stringRedisTemplate.delete(USER_TASKS_WORKING_NX_KEY);
                    stringRedisTemplate.delete(value);
                }
            });
        }


    }

}
