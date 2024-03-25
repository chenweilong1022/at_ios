package io.renren.modules.task;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.client.LineService;
import io.renren.modules.client.ProxyService;
import io.renren.modules.client.dto.AddFriendsByMid;
import io.renren.modules.client.dto.SearchPhoneDTO;
import io.renren.modules.client.dto.UpdateProfileImageDTO;
import io.renren.modules.client.dto.UpdateProfileImageResultDTO;
import io.renren.modules.client.vo.SearchPhoneVO;
import io.renren.modules.client.vo.The818051863582;
import io.renren.modules.client.vo.UpdateProfileImageResultVO;
import io.renren.modules.client.vo.UpdateProfileImageVO;
import io.renren.modules.ltt.entity.*;
import io.renren.modules.ltt.enums.GroupType;
import io.renren.modules.ltt.enums.TaskStatus;
import io.renren.modules.ltt.enums.UserStatusCode;
import io.renren.modules.ltt.service.*;
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
import java.util.concurrent.CountDownLatch;
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
    private ProxyService proxyService;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private AtAvatarService atAvatarService;

    @Autowired
    private AtDataTaskService atDataTaskService;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;


    /**
     * 更新头像结果返回
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
            //需要更换头像的任务
            List<AtDataTaskEntity> atDataTaskEntities = atDataTaskService.list(new QueryWrapper<AtDataTaskEntity>().lambda()
                    .eq(AtDataTaskEntity::getTaskStatus, TaskStatus.TaskStatus2.getKey())
                    .last("limit 5")
            );

            if (CollUtil.isEmpty(atDataTaskEntities)) {
                log.info("DataTask task4 atDataTaskEntities isEmpty");
                return;
            }

            List<Integer> ids = atDataTaskEntities.stream().map(AtDataTaskEntity::getId).collect(Collectors.toList());
            //需要更换头像的任务
            List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                    .in(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus4.getKey(),TaskStatus.TaskStatus5.getKey(),TaskStatus.TaskStatus8.getKey(),TaskStatus.TaskStatus10.getKey())
                    .in(AtDataSubtaskEntity::getDataTaskId,ids)
            );
            if (CollUtil.isEmpty(atDataSubtaskEntities)) {
                log.info("DataTask task4 atDataSubtaskEntities isEmpty");
                return;
            }

            Map<Integer, List<AtDataSubtaskEntity>> integerListMap = atDataSubtaskEntities.stream().collect(Collectors.groupingBy(AtDataSubtaskEntity::getDataTaskId));
            List<AtDataTaskEntity> atDataTaskEntityList = new ArrayList<>();
            for (AtDataTaskEntity atDataTaskEntity : atDataTaskEntities) {
                //获取所有子任务
                List<AtDataSubtaskEntity> DataSubtaskEntities = integerListMap.get(atDataTaskEntity.getId());
                if (CollUtil.isEmpty(DataSubtaskEntities)) {
                    continue;
                }
                //成功的任务
                long fail = DataSubtaskEntities.stream().filter(item -> TaskStatus.TaskStatus5.getKey().equals(item.getTaskStatus())).count();
                //失败的任务
                long success8 = DataSubtaskEntities.stream().filter(item -> TaskStatus.TaskStatus8.getKey().equals(item.getTaskStatus())).count();
                long success10 = DataSubtaskEntities.stream().filter(item -> TaskStatus.TaskStatus10.getKey().equals(item.getTaskStatus())).count();
                AtDataTaskEntity update = new AtDataTaskEntity();
                if (GroupType.GroupType1.getKey().equals(atDataTaskEntity.getGroupType())) {
                    update.setSuccessfulQuantity((int) success8);
                    update.setFailuresQuantity((int) fail);
                    update.setId(atDataTaskEntity.getId());
                    if (success8 + fail == atDataTaskEntity.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }else if (GroupType.GroupType5.getKey().equals(atDataTaskEntity.getGroupType())) {
                    update.setSuccessfulQuantity((int) success10);
                    update.setFailuresQuantity((int) fail);
                    update.setId(atDataTaskEntity.getId());
                    if (success10 + fail == atDataTaskEntity.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }else if (GroupType.GroupType2.getKey().equals(atDataTaskEntity.getGroupType())) {
                    update.setSuccessfulQuantity((int) success8);
                    update.setFailuresQuantity((int) fail);
                    update.setId(atDataTaskEntity.getId());
                    if (success8 + fail == atDataTaskEntity.getAddTotalQuantity()) {
                        update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                    }
                    atDataTaskEntityList.add(update);
                }
            }
            if (CollUtil.isNotEmpty(atDataTaskEntityList)) {
                synchronized (atAtDataTaskEntityObj) {
                    atDataTaskService.updateBatchById(atDataTaskEntityList);
                }
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task4Lock.unlock();
        }
    }


    /**
     * 更新头像结果返回
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
            List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                    .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus7.getKey())
                    .eq(AtDataSubtaskEntity::getGroupType,GroupType.GroupType1.getKey())
                    .last("limit 5")
            );
            //任务为空
            if (CollUtil.isEmpty(atDataSubtaskEntities)) {
                log.info("DataTask task2 atDataSubtaskEntities isEmpty");
                return;
            }

            //获取用户MAP
            List<Integer> userIds = atDataSubtaskEntities.stream().map(AtDataSubtaskEntity::getUserId).collect(Collectors.toList());
            List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
            List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
            List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
            Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
            Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId())));

            final CountDownLatch latch = new CountDownLatch(atDataSubtaskEntities.size());
            List<AtDataSubtaskEntity> updates = new ArrayList<>();
            for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntities) {
                threadPoolTaskExecutor.submit(new Thread(()->{
                    //获取用户token
                    AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(atDataSubtaskEntity.getUserId());
                    if (ObjectUtil.isNull(atUserTokenEntity)) {
                        latch.countDown();
                        return;
                    }

                    String getflowip = proxyService.getflowip();
                    if (StrUtil.isEmpty(getflowip)) {
                        latch.countDown();
                        return;
                    }

                    AddFriendsByMid addFriendsByMid = new AddFriendsByMid();
                    addFriendsByMid.setProxy(getflowip);
                    addFriendsByMid.setPhone(atDataSubtaskEntity.getContactKey());
                    addFriendsByMid.setMid(atDataSubtaskEntity.getMid());
                    addFriendsByMid.setFriendAddType("phoneSearch");
                    addFriendsByMid.setToken(atUserTokenEntity.getToken());
                    SearchPhoneVO searchPhoneVO = lineService.addFriendsByMid(addFriendsByMid);
                    AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                    update.setId(atDataSubtaskEntity.getId());
                    if (ObjectUtil.isNull(searchPhoneVO)) {
                        latch.countDown();
                        return;
                    }
                    update.setMsg(searchPhoneVO.getMsg());
                    if (200 == searchPhoneVO.getCode()) {
                        update.setTaskStatus(TaskStatus.TaskStatus8.getKey());
                    }else {
                        update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                    }
                    updates.add(update);
                    latch.countDown();
                }));
            }
            latch.await();
            if (CollUtil.isNotEmpty(updates)) {
                synchronized (atAtDataSubtaskObj) {
                    atDataSubtaskService.updateBatchById(updates);
                }
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task3Lock.unlock();
        }
    }


    /**
     * 获取type为1 加粉类型为手机号模式
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task2() {
        boolean b = task2Lock.tryLock();
        if (!b) {
            return;
        }
        try{
            List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                    .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus2.getKey())
                    .eq(AtDataSubtaskEntity::getGroupType,GroupType.GroupType1.getKey())
                    .last("limit 5")
            );
            //任务为空
            if (CollUtil.isEmpty(atDataSubtaskEntities)) {
                log.info("DataTask task2 atDataSubtaskEntities isEmpty");
                return;
            }

            //获取用户MAP
            List<Integer> userIds = atDataSubtaskEntities.stream().map(AtDataSubtaskEntity::getUserId).collect(Collectors.toList());
            List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
            List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
            List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
            Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
            Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId())));
            final CountDownLatch latch = new CountDownLatch(atDataSubtaskEntities.size());
            List<AtDataSubtaskEntity> updates = new ArrayList<>();
            for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntities) {
                threadPoolTaskExecutor.submit(new Thread(()->{
                    //获取用户token
                    AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(atDataSubtaskEntity.getUserId());
                    if (ObjectUtil.isNull(atUserTokenEntity)) {
                        latch.countDown();
                        return;
                    }
                    String contactKey = atDataSubtaskEntity.getContactKey();
                    if (StrUtil.isNotEmpty(contactKey)) {
                        String getflowip = proxyService.getflowip();
                        if (StrUtil.isEmpty(getflowip)) {
                            latch.countDown();
                            return;
                        }
                        SearchPhoneDTO searchPhoneDTO = new SearchPhoneDTO();
                        searchPhoneDTO.setProxy(getflowip);
                        searchPhoneDTO.setPhone(contactKey);
                        searchPhoneDTO.setToken(atUserTokenEntity.getToken());
                        SearchPhoneVO searchPhoneVO = lineService.searchPhone(searchPhoneDTO);
                        AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                        update.setId(atDataSubtaskEntity.getId());
                        if (ObjectUtil.isNull(searchPhoneVO)) {
                            latch.countDown();
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
                                update.setMusicProfile(value.getMusicProfile());
                                update.setVideoProfile(value.getVideoProfile());
                            }
                        }else if (201 == searchPhoneVO.getCode()) {
                            //需要刷新token
                            if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode4.getValue())) {
                                update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode6.getValue())) {
                                update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                            }
                        }
                        updates.add(update);
                        latch.countDown();
                    }else {
                        latch.countDown();
                    }
                }));
            }
            latch.await();
            if (CollUtil.isNotEmpty(updates)) {
                synchronized (atAtDataSubtaskObj) {
                    atDataSubtaskService.updateBatchById(updates);
                }
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task2Lock.unlock();
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
                    .last("limit 5")
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
