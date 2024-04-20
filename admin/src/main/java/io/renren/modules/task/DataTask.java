package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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


    @Scheduled(fixedDelay = 20000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task5() {
        boolean b = task4Lock.tryLock();
        if (!b) {
            return;
        }
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
                        AtGroupEntity atGroupEntity = new AtGroupEntity();
                        atGroupEntity.setId(groupCountByDataTaskIdVO.getGroupId());
                        atGroupEntity.setGroupStatus(GroupStatus.GroupStatus7.getKey());
                        atGroupService.updateById(atGroupEntity);
                    }
                }
            }
            if (CollUtil.isNotEmpty(atDataTaskEntityList)) {
                synchronized (atAtDataTaskEntityObj) {
                    atDataTaskService.updateBatchById(atDataTaskEntityList);
                }
            }
        }
        catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task4Lock.unlock();
        }

    }

    /**
     * 通过手机号搜索成功 添加好友
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task3() {
        AtDataSubtaskEntity dto = new AtDataSubtaskEntity().setTaskStatus(TaskStatus.TaskStatus7.getKey()).setGroupType(GroupType.GroupType1.getKey());
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
                        if (ObjectUtil.isNotNull(atGroupEntityConfig)) {
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
        AtDataSubtaskEntity dto = new AtDataSubtaskEntity().setTaskStatus(TaskStatus.TaskStatus2.getKey()).setGroupType(GroupType.GroupType1.getKey());
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
                        if (StrUtil.isNotEmpty(contactKey)) {
                            //获取代理
                            CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                            cdLineIpProxyDTO.setTokenPhone(atUserTokenEntity.getTelephone());
                            cdLineIpProxyDTO.setLzPhone(contactKey);
                            //去设置区号
                            if (ObjectUtil.isNotNull(atGroupEntityConfig)) {
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
                                Map<String, The818051863582> data = searchPhoneVO.getData();
                                Collection<The818051863582> values = data.values();
                                for (The818051863582 value : values) {
                                    update.setTaskStatus(TaskStatus.TaskStatus7.getKey());
                                    update.setMid(value.getMid());
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
                                //获取间隔的秒,设置下次可以执行的时间
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
            //需要添加好友的任务
            List<AtDataTaskEntity> atDataTaskEntities = atDataTaskService.list(new QueryWrapper<AtDataTaskEntity>().lambda()
                    .eq(AtDataTaskEntity::getTaskStatus, TaskStatus.TaskStatus1.getKey())
                    .last("limit 15")
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
                updates.add(update);
            }
            synchronized (atAtDataSubtaskObj) {
                atDataSubtaskService.updateBatchById(updates);
            }

        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task1Lock.unlock();
        }

    }
}
