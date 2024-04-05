package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
                    if (success8 + fail == groupCountByDataTaskIdVO.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }else if (GroupType.GroupType5.getKey().equals(groupCountByDataTaskIdVO.getGroupType())) {
                    update.setSuccessfulQuantity((int) success10);
                    update.setFailuresQuantity((int) fail);
                    update.setId(groupCountByDataTaskIdVO.getDataTaskId());
                    if (success10 + fail == groupCountByDataTaskIdVO.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }else if (GroupType.GroupType2.getKey().equals(groupCountByDataTaskIdVO.getGroupType())) {
                    update.setSuccessfulQuantity((int) success8);
                    update.setFailuresQuantity((int) fail);
                    update.setId(groupCountByDataTaskIdVO.getDataTaskId());
                    if (success8 + fail == groupCountByDataTaskIdVO.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }else if (GroupType.GroupType3.getKey().equals(groupCountByDataTaskIdVO.getGroupType())) {
                    update.setSuccessfulQuantity((int) success8);
                    update.setFailuresQuantity((int) fail);
                    update.setId(groupCountByDataTaskIdVO.getDataTaskId());
                    if (success8 + fail == groupCountByDataTaskIdVO.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }else if (GroupType.GroupType4.getKey().equals(groupCountByDataTaskIdVO.getGroupType())) {
                    update.setSuccessfulQuantity((int) success8);
                    update.setFailuresQuantity((int) fail);
                    update.setId(groupCountByDataTaskIdVO.getDataTaskId());
                    if (success8 + fail == groupCountByDataTaskIdVO.getAddTotalQuantity()) {
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


//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task4() {
//        boolean b = task4Lock.tryLock();
//        if (!b) {
//            return;
//        }
//        try {
//            //需要更换头像的任务
//            List<AtDataTaskEntity> atDataTaskEntities = atDataTaskService.list(new QueryWrapper<AtDataTaskEntity>().lambda()
//                    .eq(AtDataTaskEntity::getTaskStatus, TaskStatus.TaskStatus2.getKey())
//                    .last("limit 5")
//            );
//
//            if (CollUtil.isEmpty(atDataTaskEntities)) {
//                log.info("DataTask task4 atDataTaskEntities isEmpty");
//                return;
//            }
//
//            List<Integer> ids = atDataTaskEntities.stream().map(AtDataTaskEntity::getId).collect(Collectors.toList());
//            //需要更换头像的任务
//            List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
//                    .in(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus4.getKey(),TaskStatus.TaskStatus5.getKey(),TaskStatus.TaskStatus8.getKey(),TaskStatus.TaskStatus10.getKey())
//                    .in(AtDataSubtaskEntity::getDataTaskId,ids)
//            );
//            if (CollUtil.isEmpty(atDataSubtaskEntities)) {
//                log.info("DataTask task4 atDataSubtaskEntities isEmpty");
//                return;
//            }
//
//            Map<Integer, List<AtDataSubtaskEntity>> integerListMap = atDataSubtaskEntities.stream().collect(Collectors.groupingBy(AtDataSubtaskEntity::getDataTaskId));
//            List<AtDataTaskEntity> atDataTaskEntityList = new ArrayList<>();
//            for (AtDataTaskEntity atDataTaskEntity : atDataTaskEntities) {
//                //获取所有子任务
//                List<AtDataSubtaskEntity> DataSubtaskEntities = integerListMap.get(atDataTaskEntity.getId());
//                if (CollUtil.isEmpty(DataSubtaskEntities)) {
//                    continue;
//                }
//                //成功的任务
//                long fail = DataSubtaskEntities.stream().filter(item -> TaskStatus.TaskStatus5.getKey().equals(item.getTaskStatus())).count();
//                //失败的任务
//                long success8 = DataSubtaskEntities.stream().filter(item -> TaskStatus.TaskStatus8.getKey().equals(item.getTaskStatus())).count();
//                long success10 = DataSubtaskEntities.stream().filter(item -> TaskStatus.TaskStatus10.getKey().equals(item.getTaskStatus())).count();
//                AtDataTaskEntity update = new AtDataTaskEntity();
//                if (GroupType.GroupType1.getKey().equals(atDataTaskEntity.getGroupType())) {
//                    update.setSuccessfulQuantity((int) success8);
//                    update.setFailuresQuantity((int) fail);
//                    update.setId(atDataTaskEntity.getId());
//                    if (success8 + fail == atDataTaskEntity.getAddTotalQuantity()) {
//                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
//                    }
//                    atDataTaskEntityList.add(update);
//                }else if (GroupType.GroupType5.getKey().equals(atDataTaskEntity.getGroupType())) {
//                    update.setSuccessfulQuantity((int) success10);
//                    update.setFailuresQuantity((int) fail);
//                    update.setId(atDataTaskEntity.getId());
//                    if (success10 + fail == atDataTaskEntity.getAddTotalQuantity()) {
//                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
//                    }
//                    atDataTaskEntityList.add(update);
//                }else if (GroupType.GroupType2.getKey().equals(atDataTaskEntity.getGroupType())) {
//                    update.setSuccessfulQuantity((int) success8);
//                    update.setFailuresQuantity((int) fail);
//                    update.setId(atDataTaskEntity.getId());
//                    if (success8 + fail == atDataTaskEntity.getAddTotalQuantity()) {
//                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
//                    }
//                    atDataTaskEntityList.add(update);
//                }else if (GroupType.GroupType3.getKey().equals(atDataTaskEntity.getGroupType())) {
//                    update.setSuccessfulQuantity((int) success8);
//                    update.setFailuresQuantity((int) fail);
//                    update.setId(atDataTaskEntity.getId());
//                    if (success8 + fail == atDataTaskEntity.getAddTotalQuantity()) {
//                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
//                    }
//                    atDataTaskEntityList.add(update);
//                }else if (GroupType.GroupType4.getKey().equals(atDataTaskEntity.getGroupType())) {
//                    update.setSuccessfulQuantity((int) success8);
//                    update.setFailuresQuantity((int) fail);
//                    update.setId(atDataTaskEntity.getId());
//                    if (success8 + fail == atDataTaskEntity.getAddTotalQuantity()) {
//                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
//                    }
//                    atDataTaskEntityList.add(update);
//                }
//
//                //如果拉群成功
//                if (TaskStatus.TaskStatus3.getKey().equals(update.getTaskStatus())) {
//                    if (ObjectUtil.isNotNull(atDataTaskEntity.getGroupId())) {
//                        AtGroupEntity atGroupEntity = new AtGroupEntity();
//                        atGroupEntity.setId(atDataTaskEntity.getGroupId());
//                        atGroupEntity.setGroupStatus(GroupStatus.GroupStatus7.getKey());
//                        atGroupService.updateById(atGroupEntity);
//                    }
//                }
//            }
//            if (CollUtil.isNotEmpty(atDataTaskEntityList)) {
//                synchronized (atAtDataTaskEntityObj) {
//                    atDataTaskService.updateBatchById(atDataTaskEntityList);
//                }
//            }
//        }catch (Exception e) {
//            log.error("err = {}",e.getMessage());
//        }finally {
//            task4Lock.unlock();
//        }
//    }

//
//    /**
//     * 更新头像结果返回
//     */
//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task3() {
//        AtDataSubtaskEntity dto = new AtDataSubtaskEntity().setTaskStatus(TaskStatus.TaskStatus7.getKey()).setGroupType(GroupType.GroupType1.getKey());
//        List<AtDataSubtaskVO> atDataSubtaskEntities = atDataSubtaskService.groupByUserId(dto);
//        //任务为空
//        if (CollUtil.isEmpty(atDataSubtaskEntities)) {
//            log.info("DataTask task2 atDataSubtaskEntities isEmpty");
//            return;
//        }
//
//        //获取用户MAP
//        List<Integer> userIds = atDataSubtaskEntities.stream().map(AtDataSubtaskVO::getUserId).collect(Collectors.toList());
//        List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
//        List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
//        List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
//        Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
//        Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId()).setTelephone(item.getTelephone())));
//        for (AtDataSubtaskVO atDataSubtaskEntity : atDataSubtaskEntities) {
//            threadPoolTaskExecutor.execute(() -> {
//                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource7, atDataSubtaskEntity.getDataTaskId());
//                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
//                boolean triedLock = lock.tryLock();
//                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
//                if(triedLock) {
//                    try{
//                        //获取用户token
//                        AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(atDataSubtaskEntity.getUserId());
//                        if (ObjectUtil.isNull(atUserTokenEntity)) {
//                            return;
//                        }
//                        String contactKey = atDataSubtaskEntity.getContactKeys();
//                        if (StrUtil.isEmpty(contactKey)) {
//                            return;
//                        }
//                        String mid = atDataSubtaskEntity.getMids();
//                        if (StrUtil.isEmpty(mid)) {
//                            return;
//                        }
//                        //获取代理
//                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
//                        cdLineIpProxyDTO.setTokenPhone(atUserTokenEntity.getTelephone());
//                        cdLineIpProxyDTO.setLzPhone(contactKey);
//                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
//                        if (StrUtil.isEmpty(proxyIp)) {
//                            return;
//                        }
//
//                        AddFriendsByMid addFriendsByMid = new AddFriendsByMid();
//                        addFriendsByMid.setProxy(proxyIp);
//                        addFriendsByMid.setPhone(contactKey);
//                        addFriendsByMid.setMid(mid);
//                        addFriendsByMid.setFriendAddType("phoneSearch");
//                        addFriendsByMid.setToken(atUserTokenEntity.getToken());
//                        SearchPhoneVO searchPhoneVO = lineService.addFriendsByMid(addFriendsByMid);
//                        AtDataSubtaskEntity update = new AtDataSubtaskEntity();
//                        if (ObjectUtil.isNull(searchPhoneVO)) {
//                            return;
//                        }
//                        update.setMsg(searchPhoneVO.getMsg());
//                        if (200 == searchPhoneVO.getCode()) {
//                            update.setTaskStatus(TaskStatus.TaskStatus8.getKey());
//                        }else {
//                            UserStatus userStatus = UserStatus.UserStatus4;
//                            //需要刷新token
//                            if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode4.getValue())) {
//                                userStatus = UserStatus.UserStatus3;
//                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode6.getValue())) {
//                                userStatus = UserStatus.UserStatus3;
//                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode7.getValue())) {
//                                userStatus = UserStatus.UserStatus2;
//                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode8.getValue())) {
//                                userStatus = UserStatus.UserStatus2;
//                            }
//
//                            //如果失败，修改状态
//                            update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
//                            //任务失败
//                            AtDataTaskEntity atDataTaskEntity = new AtDataTaskEntity();
//                            atDataTaskEntity.setId(atDataSubtaskEntity.getDataTaskId());
//                            atDataTaskEntity.setTaskStatus(TaskStatus.TaskStatus5.getKey());
//                            atDataTaskService.updateById(atDataTaskEntity);
//
//                            if (ObjectUtil.isNotNull(atDataSubtaskEntity.getGroupId())) {
//                                //拉群改状态
//                                AtGroupEntity atGroupEntity = new AtGroupEntity();
//                                atGroupEntity.setId(atDataSubtaskEntity.getGroupId());
//                                atGroupEntity.setGroupStatus(GroupStatus.GroupStatus11.getKey());
//                                atGroupService.updateById(atGroupEntity);
//                            }
//
//                            update.setId(null);
//                            atDataSubtaskService.update(update,new QueryWrapper<AtDataSubtaskEntity>().lambda()
//                                    .eq(AtDataSubtaskEntity::getDataTaskId,atDataSubtaskEntity.getDataTaskId())
//                                    .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus2.getKey())
//                            );
//                            atUserService.unlock(atDataSubtaskEntity.getUserId(),userStatus);
//                        }
//                        update.setId(atDataSubtaskEntity.getId());
//                        atDataSubtaskService.updateById(update);
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        log.error("err = {}",e.getMessage());
//                    } finally {
//                        lock.unlock();
//                    }
//                }else {
//                    log.info("keyByResource = {} 在执行",keyByResource);
//                }
//            });
//
//        }
//    }
//
//
//    /**
//     * 获取type为1 加粉类型为手机号模式
//     */
//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task2() {
//        AtDataSubtaskEntity dto = new AtDataSubtaskEntity().setTaskStatus(TaskStatus.TaskStatus2.getKey()).setGroupType(GroupType.GroupType1.getKey());
//        List<AtDataSubtaskVO> atDataSubtaskEntities = atDataSubtaskService.groupByUserId(dto);
//        //任务为空
//        if (CollUtil.isEmpty(atDataSubtaskEntities)) {
//            log.info("DataTask task2 atDataSubtaskEntities isEmpty");
//            return;
//        }
//        //获取用户MAP
//        List<Integer> userIds = atDataSubtaskEntities.stream().map(AtDataSubtaskVO::getUserId).collect(Collectors.toList());
//        List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
//        List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
//        List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
//        Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
//        Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId()).setTelephone(item.getTelephone())));
//        for (AtDataSubtaskVO atDataSubtaskEntity : atDataSubtaskEntities) {
//
//            threadPoolTaskExecutor.execute(() -> {
//                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource7, atDataSubtaskEntity.getDataTaskId());
//                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
//                boolean triedLock = lock.tryLock();
//                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
//                if(triedLock) {
//                    try{
//                        int i = RandomUtil.randomInt(3, 5);
//                        Thread.sleep(i * 1000L);
//                        //获取用户token
//                        AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(atDataSubtaskEntity.getUserId());
//                        if (ObjectUtil.isNull(atUserTokenEntity)) {
//                            return;
//                        }
//                        String contactKey = atDataSubtaskEntity.getContactKeys();
//                        if (StrUtil.isNotEmpty(contactKey)) {
//                            //获取代理
//                            CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
//                            cdLineIpProxyDTO.setTokenPhone(atUserTokenEntity.getTelephone());
//                            cdLineIpProxyDTO.setLzPhone(contactKey);
//                            String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
//                            if (StrUtil.isEmpty(proxyIp)) {
//                                return;
//                            }
//
//                            SearchPhoneDTO searchPhoneDTO = new SearchPhoneDTO();
//                            searchPhoneDTO.setProxy(proxyIp);
//                            searchPhoneDTO.setPhone(contactKey);
//                            searchPhoneDTO.setToken(atUserTokenEntity.getToken());
//                            SearchPhoneVO searchPhoneVO = lineService.searchPhone(searchPhoneDTO);
//                            AtDataSubtaskEntity update = new AtDataSubtaskEntity();
//                            if (ObjectUtil.isNull(searchPhoneVO)) {
//                                return;
//                            }
//
//                            update.setMsg(searchPhoneVO.getMsg());
//                            if (200 == searchPhoneVO.getCode()) {
//                                Map<String, The818051863582> data = searchPhoneVO.getData();
//                                Collection<The818051863582> values = data.values();
//                                for (The818051863582 value : values) {
//                                    update.setTaskStatus(TaskStatus.TaskStatus7.getKey());
//                                    update.setMid(value.getMid());
//                                    update.setDisplayName(value.getDisplayName());
//                                    update.setPhoneticName(value.getPhoneticName());
//                                    update.setPictureStatus(value.getPictureStatus());
//                                    update.setThumbnailUrl(value.getThumbnailUrl());
//                                    update.setStatusMessage(value.getStatusMessage());
//                                    update.setPicturePath(value.getPicturePath());
//                                    update.setRecommendpArams(value.getRecommendParams());
//                                    update.setMusicProfile(proxyIp);
//                                    update.setVideoProfile(value.getVideoProfile());
//                                }
//                            }else if (201 == searchPhoneVO.getCode()) {
//                                UserStatus userStatus = UserStatus.UserStatus4;
//                                //需要刷新token
//                                if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode4.getValue())) {
//                                    userStatus = UserStatus.UserStatus3;
//                                } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode6.getValue())) {
//                                    userStatus = UserStatus.UserStatus3;
//                                } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode7.getValue())) {
//                                    userStatus = UserStatus.UserStatus2;
//                                } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode8.getValue())) {
//                                    userStatus = UserStatus.UserStatus2;
//                                }
//
//                                //如果失败，修改状态
//                                update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
//                                //任务失败
//                                AtDataTaskEntity atDataTaskEntity = new AtDataTaskEntity();
//                                atDataTaskEntity.setId(atDataSubtaskEntity.getDataTaskId());
//                                atDataTaskEntity.setTaskStatus(TaskStatus.TaskStatus5.getKey());
//                                atDataTaskService.updateById(atDataTaskEntity);
//                                if (ObjectUtil.isNotNull(atDataSubtaskEntity.getGroupId())) {
//                                    //拉群改状态
//                                    AtGroupEntity atGroupEntity = new AtGroupEntity();
//                                    atGroupEntity.setId(atDataSubtaskEntity.getGroupId());
//                                    atGroupEntity.setGroupStatus(GroupStatus.GroupStatus13.getKey());
//                                    atGroupService.updateById(atGroupEntity);
//                                }
//
//                                update.setId(null);
//                                atDataSubtaskService.update(update,new QueryWrapper<AtDataSubtaskEntity>().lambda()
//                                        .eq(AtDataSubtaskEntity::getDataTaskId,atDataSubtaskEntity.getDataTaskId())
//                                        .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus2.getKey())
//                                );
//                                atUserService.unlock(atDataSubtaskEntity.getUserId(),userStatus);
//                            }
//                            update.setId(atDataSubtaskEntity.getId());
//                            atDataSubtaskService.updateById(update);
//                        }
//                    } catch (InterruptedException e) {
//                        log.error("err = ",e.getMessage());
//                    } finally {
//                        lock.unlock();
//                    }
//                }else {
//                    log.info("keyByResource = {} 在执行",keyByResource);
//                }
//            });
//        }
//
//    }

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
                    .last("limit 5")
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
