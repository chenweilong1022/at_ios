package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.SyncContentsDTO;
import io.renren.modules.client.dto.SyncContentsResultDTO;
import io.renren.modules.client.vo.LineRegisterVO;
import io.renren.modules.client.vo.SyncContentsResultVO;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import io.renren.modules.ltt.entity.AtGroupEntity;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
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

    @Autowired
    private LineService lineService;
    @Autowired
    private ConcurrentHashMap<String, Lock> lockMap;
    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private AtAvatarService atAvatarService;
    @Autowired
    private AtGroupService atGroupService;


    @Autowired
    private AtDataTaskService atDataTaskService;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;


//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task4() {}


    /**
     *
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task3() {

        AtDataSubtaskEntity dto = new AtDataSubtaskEntity().setTaskStatus(TaskStatus.TaskStatus9.getKey()).setGroupType(GroupType.GroupType5.getKey());
        List<AtDataSubtaskVO> atDataSubtaskEntities = atDataSubtaskService.groupByUserId(dto);
        //任务为空
        if (CollUtil.isEmpty(atDataSubtaskEntities)) {
            log.info("Data5Task task2 atDataSubtaskEntities isEmpty");
            return;
        }

        //获取用户MAP
        List<Integer> userIds = atDataSubtaskEntities.stream().map(AtDataSubtaskVO::getUserId).collect(Collectors.toList());
        List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
        List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
        List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
        Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
        Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId()).setTelephone(item.getTelephone())));

        for (AtDataSubtaskVO atDataSubtaskVO : atDataSubtaskEntities) {

            threadPoolTaskExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource7, atDataSubtaskVO.getUserId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{

                        //获取用户token
                        AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(atDataSubtaskVO.getUserId());
                        if (ObjectUtil.isNull(atUserTokenEntity)) {
                            return;
                        }

                        List<AtDataSubtaskEntity> atDataSubtaskEntityList = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                .eq(AtDataSubtaskEntity::getDataTaskId,atDataSubtaskVO.getDataTaskId())
                                .eq(AtDataSubtaskEntity::getUserId,atDataSubtaskVO.getUserId())
                                .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus9.getKey())
                        );
                        if (CollUtil.isEmpty(atDataSubtaskEntityList)) {
                            return;
                        }
                        SyncContentsResultDTO syncContentsResultDTO = new SyncContentsResultDTO();
                        syncContentsResultDTO.setTaskId(atDataSubtaskEntityList.get(0).getLineTaskId());
                        SyncContentsResultVO syncContentsResultVO = lineService.syncContentsResult(syncContentsResultDTO);
                        if (ObjectUtil.isNull(syncContentsResultVO)) {
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
                                }
                                atDataSubtaskService.updateBatchById(atDataSubtaskEntityList);
                                if (ObjectUtil.isNotNull(atDataSubtaskVO.getGroupId())) {
                                    //拉群改状态
                                    AtGroupEntity atGroupEntity = new AtGroupEntity();
                                    atGroupEntity.setId(atDataSubtaskVO.getGroupId());
                                    atGroupEntity.setGroupStatus(GroupStatus.GroupStatus7.getKey());
                                    atGroupService.updateById(atGroupEntity);
                                }
                                //失败
                            }else if (-1 == data.getStatus()) {
                                for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntityList) {
                                    atDataSubtaskEntity.setId(atDataSubtaskEntity.getId());
                                    atDataSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                    atDataSubtaskEntity.setMsg(syncContentsResultVO.getMsg());
                                }
                                atDataSubtaskService.updateBatchById(atDataSubtaskEntityList);
                                //同步失败，账号风控
                                atUserService.unlock(atDataSubtaskVO.getUserId(), UserStatus.UserStatus2);
                                if (ObjectUtil.isNotNull(atDataSubtaskVO.getGroupId())) {
                                    //拉群改状态
                                    AtGroupEntity atGroupEntity = new AtGroupEntity();
                                    atGroupEntity.setId(atDataSubtaskVO.getGroupId());
                                    atGroupEntity.setGroupStatus(GroupStatus.GroupStatus8.getKey());
                                    atGroupService.updateById(atGroupEntity);
                                }
                                //网络异常
                            }else if (-2 == data.getStatus()) {
                                for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntityList) {
                                    atDataSubtaskEntity.setId(atDataSubtaskEntity.getId());
                                    atDataSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus2.getKey());
                                    atDataSubtaskEntity.setMsg(syncContentsResultVO.getMsg());
                                }
                                atDataSubtaskService.updateBatchById(atDataSubtaskEntityList);
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
     * 通讯录加粉模式，通过通讯录加粉
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task2() {

        AtDataSubtaskEntity dto = new AtDataSubtaskEntity().setTaskStatus(TaskStatus.TaskStatus2.getKey()).setGroupType(GroupType.GroupType5.getKey());
        List<AtDataSubtaskVO> atDataSubtaskEntities = atDataSubtaskService.groupByUserId(dto);
        //任务为空
        if (CollUtil.isEmpty(atDataSubtaskEntities)) {
            log.info("Data5Task task2 atDataSubtaskEntities isEmpty");
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
                        int i = RandomUtil.randomInt(3, 5);
                        Thread.sleep(i * 1000L);
                        List<AtDataSubtaskEntity> list = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                .eq(AtDataSubtaskEntity::getDataTaskId,atDataSubtaskEntity.getDataTaskId())
                                .eq(AtDataSubtaskEntity::getGroupId,atDataSubtaskEntity.getGroupId())
                                .eq(AtDataSubtaskEntity::getGroupType,GroupType.GroupType5.getKey())
                        );

                        List<String> phoneList = list.stream().map(AtDataSubtaskEntity::getContactKey).collect(Collectors.toList());

                        if (CollUtil.isEmpty(phoneList)) {
                            return;
                        }

                        //获取用户token
                        AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(atDataSubtaskEntity.getUserId());
                        if (ObjectUtil.isNull(atUserTokenEntity)) {
                            return;
                        }

                        //获取代理
                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(atUserTokenEntity.getTelephone());
                        cdLineIpProxyDTO.setLzPhone(atUserTokenEntity.getTelephone());
                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                        if (StrUtil.isEmpty(proxyIp)) {
                            return;
                        }


                        //请求同步通讯录
                        SyncContentsDTO syncContentsDTO = new SyncContentsDTO();
                        syncContentsDTO.setProxy(proxyIp);
                        syncContentsDTO.setPhoneList(phoneList);
                        syncContentsDTO.setToken(atUserTokenEntity.getToken());
                        LineRegisterVO lineRegisterVO = lineService.syncContents(syncContentsDTO);
                        if (ObjectUtil.isNull(lineRegisterVO)) {
                            return;
                        }
                        AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                        update.setLineTaskId(lineRegisterVO.getData().getTaskId());
                        update.setMsg(lineRegisterVO.getMsg());
                        if (200 == lineRegisterVO.getCode()) {
                            update.setTaskStatus(TaskStatus.TaskStatus9.getKey());
                            atDataSubtaskService.update(update,new UpdateWrapper<AtDataSubtaskEntity>().lambda()
                                    .eq(AtDataSubtaskEntity::getDataTaskId,atDataSubtaskEntity.getDataTaskId())
                                    .eq(AtDataSubtaskEntity::getUserId,atDataSubtaskEntity.getUserId())
                                    .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus2.getKey())
                            );
                        }else {
                            update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                            atDataSubtaskService.update(update,new UpdateWrapper<AtDataSubtaskEntity>().lambda()
                                    .eq(AtDataSubtaskEntity::getDataTaskId,atDataSubtaskEntity.getDataTaskId())
                                    .eq(AtDataSubtaskEntity::getUserId,atDataSubtaskEntity.getUserId())
                                    .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus2.getKey())
                            );
                        }
                    } catch (InterruptedException e) {

                    } finally {
                        lock.unlock();
                    }
                }else {
                    log.info("keyByResource = {} 在执行",keyByResource);
                }
            });
        }


    }


//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task1() {}
}
