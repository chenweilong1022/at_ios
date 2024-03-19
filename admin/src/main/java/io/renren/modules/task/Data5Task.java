package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.client.LineService;
import io.renren.modules.client.ProxyService;
import io.renren.modules.client.dto.AddFriendsByMid;
import io.renren.modules.client.dto.SearchPhoneDTO;
import io.renren.modules.client.dto.SyncContentsDTO;
import io.renren.modules.client.dto.SyncContentsResultDTO;
import io.renren.modules.client.vo.LineRegisterVO;
import io.renren.modules.client.vo.SearchPhoneVO;
import io.renren.modules.client.vo.SyncContentsResultVO;
import io.renren.modules.client.vo.The818051863582;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import io.renren.modules.ltt.entity.AtDataTaskEntity;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
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

import java.util.*;
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
public class Data5Task {


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
                            .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus9.getKey())
                            .eq(AtDataSubtaskEntity::getGroupType,GroupType.GroupType5.getKey())
//                    .last("limit 5")
            );
            //任务为空
            if (CollUtil.isEmpty(atDataSubtaskEntities)) {
                log.info("Data5Task task2 atDataSubtaskEntities isEmpty");
                return;
            }
            //获取用户分组
            Map<Integer, List<AtDataSubtaskEntity>> userIdsListMap = atDataSubtaskEntities.stream().collect(Collectors.groupingBy(AtDataSubtaskEntity::getUserId));
            Set<Integer> userIds = userIdsListMap.keySet();
            //获取用户MAP
            List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
            List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
            List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
            Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
            Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId())));

            final CountDownLatch latch = new CountDownLatch(userIdsListMap.keySet().size());
            List<AtDataSubtaskEntity> updates = new ArrayList<>();

            for (Integer userId : userIdsListMap.keySet()) {
                threadPoolTaskExecutor.submit(new Thread(() -> {
                    String getflowip = proxyService.getflowip();
                    if (StrUtil.isEmpty(getflowip)) {
                        latch.countDown();
                        return;
                    }
                    //获取用户token
                    AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(userId);
                    if (ObjectUtil.isNull(atUserTokenEntity)) {
                        latch.countDown();
                        return;
                    }

                    //获取该账户需要通讯录同步的任务
                    List<AtDataSubtaskEntity> atDataSubtaskEntityList = userIdsListMap.get(userId);
                    if (CollUtil.isEmpty(atDataSubtaskEntityList)) {
                        latch.countDown();
                        return;
                    }
                    SyncContentsResultDTO syncContentsResultDTO = new SyncContentsResultDTO();
                    syncContentsResultDTO.setTaskId(atDataSubtaskEntityList.get(0).getLineTaskId());
                    SyncContentsResultVO syncContentsResultVO = lineService.syncContentsResult(syncContentsResultDTO);
                    if (ObjectUtil.isNull(syncContentsResultVO)) {
                        latch.countDown();
                        return;
                    }

                    if (ObjectUtil.isNotNull(syncContentsResultVO) && 200 == syncContentsResultVO.getCode()) {
                        SyncContentsResultVO.Data data = syncContentsResultVO.getData();
                        //同步成功
                        if (2 == data.getStatus()) {
                            //获取所有的联系方式map
                            Map<String, SyncContentsResultVO.Data.ContactsMap> contactsMap = data.getContactsMap();
                            for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntityList) {
                                SyncContentsResultVO.Data.ContactsMap contactsMap1 = contactsMap.get(atDataSubtaskEntity.getContactKey());
                                atDataSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                if (ObjectUtil.isNotNull(contactsMap1)) {
                                    atDataSubtaskEntity.setLuid(contactsMap1.getLuid());
                                    atDataSubtaskEntity.setContactType(contactsMap1.getContactType());
                                    atDataSubtaskEntity.setContactKey(contactsMap1.getContactKey());
                                    atDataSubtaskEntity.setMid(contactsMap1.getContact().getMid());
                                    atDataSubtaskEntity.setType(contactsMap1.getContact().getType());
                                    atDataSubtaskEntity.setStatus(contactsMap1.getContact().getStatus());
                                    atDataSubtaskEntity.setRelation(contactsMap1.getContact().getRelation());
                                    atDataSubtaskEntity.setDisplayName(contactsMap1.getContact().getDisplayName());
                                    atDataSubtaskEntity.setPhoneticName(contactsMap1.getContact().getPhoneticName());
                                    atDataSubtaskEntity.setPictureStatus(contactsMap1.getContact().getPictureStatus());
                                    atDataSubtaskEntity.setThumbnailUrl(contactsMap1.getContact().getThumbnailUrl());
                                    atDataSubtaskEntity.setStatusMessage(contactsMap1.getContact().getStatusMessage());
                                    atDataSubtaskEntity.setDisplayNameOverridden(contactsMap1.getContact().getDisplayNameOverridden());
                                    atDataSubtaskEntity.setPicturePath(contactsMap1.getContact().getPicturePath());
                                    atDataSubtaskEntity.setRecommendpArams(contactsMap1.getContact().getRecommendParams());
                                    atDataSubtaskEntity.setFriendRequestStatus(contactsMap1.getContact().getFriendRequestStatus());
                                    atDataSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus10.getKey());
                                }
                                atDataSubtaskEntity.setMsg(syncContentsResultVO.getMsg());
                                updates.add(atDataSubtaskEntity);
                            }
                            //失败
                        }else if (-1 == data.getStatus()) {
                            for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntityList) {
                                AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                                update.setId(atDataSubtaskEntity.getId());
                                update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                update.setMsg(syncContentsResultVO.getMsg());
                                updates.add(update);
                            }
                            //网络异常
                        }else if (-2 == data.getStatus()) {
                            for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntityList) {
                                AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                                update.setId(atDataSubtaskEntity.getId());
                                update.setTaskStatus(TaskStatus.TaskStatus2.getKey());
                                update.setMsg(syncContentsResultVO.getMsg());
                                updates.add(update);
                            }
                        }
                    }
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
                    .eq(AtDataSubtaskEntity::getGroupType,GroupType.GroupType5.getKey())
//                    .last("limit 5")
            );
            //任务为空
            if (CollUtil.isEmpty(atDataSubtaskEntities)) {
                log.info("Data5Task task2 atDataSubtaskEntities isEmpty");
                return;
            }
            //获取用户分组
            Map<Integer, List<AtDataSubtaskEntity>> userIdsListMap = atDataSubtaskEntities.stream().collect(Collectors.groupingBy(AtDataSubtaskEntity::getUserId));
            Set<Integer> userIds = userIdsListMap.keySet();
            //获取用户MAP
            List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
            List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
            List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
            Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
            Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId())));

            final CountDownLatch latch = new CountDownLatch(userIdsListMap.keySet().size());
            List<AtDataSubtaskEntity> updates = new ArrayList<>();

            for (Integer userId : userIdsListMap.keySet()) {
                threadPoolTaskExecutor.submit(new Thread(()->{
                    //获取该账户需要通讯录同步的任务
                    List<AtDataSubtaskEntity> atDataSubtaskEntityList = userIdsListMap.get(userId);
                    //所有手机号
                    List<String> phoneList = atDataSubtaskEntityList.stream().map(AtDataSubtaskEntity::getContactKey).collect(Collectors.toList());
                    if (CollUtil.isEmpty(phoneList)) {
                        latch.countDown();
                        return;
                    }
                    String getflowip = proxyService.getflowip();
                    if (StrUtil.isEmpty(getflowip)) {
                        latch.countDown();
                        return;
                    }
                    //获取用户token
                    AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(userId);
                    if (ObjectUtil.isNull(atUserTokenEntity)) {
                        latch.countDown();
                        return;
                    }
                    //请求同步通讯录
                    SyncContentsDTO syncContentsDTO = new SyncContentsDTO();
                    syncContentsDTO.setProxy(getflowip);
                    syncContentsDTO.setPhoneList(phoneList);
                    syncContentsDTO.setToken(atUserTokenEntity.getToken());
                    LineRegisterVO lineRegisterVO = lineService.syncContents(syncContentsDTO);
                    if (ObjectUtil.isNull(lineRegisterVO)) {
                        latch.countDown();
                        return;
                    }
                    if (200 == lineRegisterVO.getCode()) {
                        for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntityList) {
                            AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                            update.setId(atDataSubtaskEntity.getId());
                            update.setLineTaskId(lineRegisterVO.getData().getTaskId());
                            update.setMsg(lineRegisterVO.getMsg());
                            update.setTaskStatus(TaskStatus.TaskStatus9.getKey());
                            updates.add(update);
                        }
                    }
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
            task2Lock.unlock();
        }
    }


    @Autowired
    private AtDataTaskService atDataTaskService;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;

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

        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task1Lock.unlock();
        }

    }
}
