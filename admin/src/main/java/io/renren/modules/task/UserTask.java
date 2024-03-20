package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.client.LineService;
import io.renren.modules.client.ProxyService;
import io.renren.modules.client.dto.GetAllContactIdsDTO;
import io.renren.modules.client.dto.GetContactsInfoV3DTO;
import io.renren.modules.client.dto.IssueLiffViewDTO;
import io.renren.modules.client.dto.LineTokenJson;
import io.renren.modules.client.vo.GetAllContactIdsVO;
import io.renren.modules.client.vo.GetContactsInfoV3VO;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.entity.AtUserGroupEntity;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.AtDataSubtaskService;
import io.renren.modules.ltt.service.AtUserGroupService;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.modules.ltt.service.AtUserTokenService;
import io.renren.modules.ltt.vo.IssueLiffViewVO;
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
public class UserTask {


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

    public static final Object atUserlockObj = new Object();
    public static final Object atUserTokenlockObj = new Object();
    @Autowired
    private LineService lineService;
    @Autowired
    private ProxyService proxyService;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
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
            List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                    .eq(AtDataSubtaskEntity::getRefreshContactStatus, RefreshContactStatus.RefreshContactStatus1.getKey())
                    .last("limit 5")
            );
            //任务为空
            if (CollUtil.isEmpty(atDataSubtaskEntities)) {
                log.info("UserTask task4 atDataSubtaskEntities isEmpty");
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
                    if (StrUtil.isEmpty(atDataSubtaskEntity.getMid())) {
                        latch.countDown();
                        return;
                    }
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
                    GetContactsInfoV3DTO getContactsInfoV3DTO = new GetContactsInfoV3DTO();
                    getContactsInfoV3DTO.setProxy(getflowip);
                    getContactsInfoV3DTO.setUserMid(atDataSubtaskEntity.getMid());
                    getContactsInfoV3DTO.setToken(atUserTokenEntity.getToken());
                    GetContactsInfoV3VO getContactsInfoV3VO = lineService.getContactsInfoV3(getContactsInfoV3DTO);
                    if (ObjectUtil.isNull(getContactsInfoV3VO)) {
                        latch.countDown();
                        return;
                    }
                    AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                    update.setId(atDataSubtaskEntity.getId());
                    //成功获取返回zhi
                    if (200 == getContactsInfoV3VO.getCode()) {
                        Map<String, GetContactsInfoV3VO.Data.DMap> contacts = getContactsInfoV3VO.getData().getContacts();
                        if (CollUtil.isEmpty(contacts)) {
                            latch.countDown();
                            return;
                        }
                        Collection<GetContactsInfoV3VO.Data.DMap> values = contacts.values();
                        for (GetContactsInfoV3VO.Data.DMap value : values) {
                            GetContactsInfoV3VO.Data.Contact contact = value.getContact();
                            update.setMid(contact.getMid());
                            update.setType(contact.getType());
                            update.setStatus(contact.getStatus());
                            update.setRelation(contact.getRelation());
                            update.setDisplayName(contact.getDisplayName());
                            update.setPhoneticName(contact.getPhoneticName());
                            update.setPictureStatus(contact.getPictureStatus());
                            update.setThumbnailUrl(contact.getThumbnailUrl());
                            update.setStatusMessage(contact.getStatusMessage());
                            update.setDisplayNameOverridden(contact.getDisplayNameOverridden());
                            update.setPicturePath(contact.getPicturePath());
                            update.setRecommendpArams(contact.getRecommendParams());
                            update.setFriendRequestStatus(contact.getFriendRequestStatus());
                            update.setRefreshContactStatus(RefreshContactStatus.RefreshContactStatus3.getKey());
                            updates.add(update);
                        }
                    }
                    latch.countDown();
                }));
            }
            latch.await();
            //修改老得
            if (CollUtil.isNotEmpty(updates)) {
                synchronized (DataTask.atAtDataSubtaskObj) {
                    atDataSubtaskService.updateBatchById(updates);
                }
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task4Lock.unlock();
        }
    }


    /**
     * 同步token信息到用户表
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
            //获取用户未验证的状态
            List<AtUserEntity> atUserEntities = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
                    .eq(AtUserEntity::getRefreshContactStatus, RefreshContactStatus.RefreshContactStatus1.getKey())
                    .last("limit 5")
            );
            if (CollUtil.isEmpty(atUserEntities)) {
                log.info("GroupTask task2 atUserEntities isEmpty");
                return;
            }

            //用户tokenIds
            List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
            List<AtUserTokenEntity> atUserTokenEntities = atUserTokenService.listByIds(userTokenIds);
            if (CollUtil.isEmpty(atUserEntities)) {
                log.info("GroupTask task2 atUserTokenEntities isEmpty");
                return;
            }
            //用户tokenMap
            Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = atUserTokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));

            final CountDownLatch latch = new CountDownLatch(atUserEntities.size());

            List<AtDataSubtaskEntity> updates = new ArrayList<>();
            List<AtDataSubtaskEntity> saves = new ArrayList<>();
            List<AtUserEntity> atUserEntityListUpdate = new ArrayList<>();

            for (AtUserEntity atUserEntity : atUserEntities) {
                threadPoolTaskExecutor.submit(new Thread(()->{
                    AtUserTokenEntity atUserTokenEntity = atUserTokenEntityMap.get(atUserEntity.getUserTokenId());
                    if (ObjectUtil.isNull(atUserTokenEntity)) {
                        latch.countDown();
                        return;
                    }
                    String getflowip = proxyService.getflowip();
                    if (StrUtil.isEmpty(getflowip)) {
                        latch.countDown();
                        return;
                    }

                    GetAllContactIdsDTO getAllContactIdsDTO = new GetAllContactIdsDTO();
                    getAllContactIdsDTO.setProxy(getflowip);
                    getAllContactIdsDTO.setToken(atUserTokenEntity.getToken());
                    GetAllContactIdsVO getAllContactIdsVO = lineService.getAllContactIds(getAllContactIdsDTO);
                    if (ObjectUtil.isNull(getAllContactIdsVO)) {
                        latch.countDown();
                        return;
                    }

                    if (200 == getAllContactIdsVO.getCode()) {
                        //获取所有的mid
                        List<String> data = getAllContactIdsVO.getData();
                        if (CollUtil.isEmpty(data)) {
                            //获取该用户的的好友
                            AtUserEntity update = new AtUserEntity();
                            update.setId(atUserEntity.getId());
                            update.setNumberFriends(0);
                            update.setRefreshContactStatus(RefreshContactStatus.RefreshContactStatus3.getKey());
                            atUserEntityListUpdate.add(update);
                            latch.countDown();
                            return;
                        }
                        List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                .eq(AtDataSubtaskEntity::getUserId,atUserEntity.getId())
                        );
                        Map<String, AtDataSubtaskEntity> atDataSubtaskEntityMap = atDataSubtaskEntities.stream().filter(item -> StrUtil.isNotEmpty(item.getMid())).collect(Collectors.toMap(AtDataSubtaskEntity::getMid, item -> item));
                        for (String datum : data) {
                            //包含了mid
                            if (atDataSubtaskEntityMap.containsKey(datum)) {
                                AtDataSubtaskEntity atDataSubtaskEntity = atDataSubtaskEntityMap.get(datum);
                                AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                                update.setId(atDataSubtaskEntity.getId());
                                update.setRefreshContactStatus(RefreshContactStatus.RefreshContactStatus1.getKey());
                                updates.add(update);
                                //不包含mid
                            }else {
                                AtDataSubtaskEntity save = new AtDataSubtaskEntity();
                                save.setUserId(atUserEntity.getId());
                                save.setMid(datum);
                                save.setRefreshContactStatus(RefreshContactStatus.RefreshContactStatus1.getKey());
                                saves.add(save);
                            }
                        }
                        //获取该用户的的好友
                        AtUserEntity update = new AtUserEntity();
                        update.setId(atUserEntity.getId());
                        update.setNumberFriends(data.size());
                        update.setRefreshContactStatus(RefreshContactStatus.RefreshContactStatus3.getKey());
                        atUserEntityListUpdate.add(update);
                    }
                    latch.countDown();
                }));
            }
            latch.await();
            //修改老得
            if (CollUtil.isNotEmpty(updates)) {
                synchronized (DataTask.atAtDataSubtaskObj) {
                    atDataSubtaskService.updateBatchById(updates);
                }
            }
            // 保存新的
            if (CollUtil.isNotEmpty(saves)) {
                synchronized (DataTask.atAtDataSubtaskObj) {
                    atDataSubtaskService.saveBatch(saves);
                }
            }
            //修改用户
            if (CollUtil.isNotEmpty(atUserEntityListUpdate)) {
                synchronized (atUserlockObj) {
                    atUserService.updateBatchById(atUserEntityListUpdate);
                }
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task3Lock.unlock();
        }
    }


    /**
     * 同步token信息到用户表
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
            //获取用户未验证的状态
            List<AtUserEntity> atUserEntities = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
                    .eq(AtUserEntity::getStatus,UserStatus.UserStatus1.getKey())
                    .last("limit 5")
            );
            if (CollUtil.isEmpty(atUserEntities)) {
                log.info("GroupTask task2 atUserEntities isEmpty");
                return;
            }
            //用户tokenIds
            List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
            List<AtUserTokenEntity> atUserTokenEntities = atUserTokenService.listByIds(userTokenIds);
            if (CollUtil.isEmpty(atUserEntities)) {
                log.info("GroupTask task2 atUserTokenEntities isEmpty");
                return;
            }
            //用户tokenMap
            Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = atUserTokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));

            final CountDownLatch latch = new CountDownLatch(atUserEntities.size());
            List<AtUserEntity> updates = new ArrayList<>();
            for (AtUserEntity atUserEntity : atUserEntities) {
                threadPoolTaskExecutor.submit(new Thread(()->{
                    AtUserTokenEntity atUserTokenEntity = atUserTokenEntityMap.get(atUserEntity.getUserTokenId());
                    if (ObjectUtil.isNull(atUserTokenEntity)) {
                        latch.countDown();
                        return;
                    }

                    String getflowip = proxyService.getflowip();
                    if (StrUtil.isEmpty(getflowip)) {
                        latch.countDown();
                        return;
                    }
                    IssueLiffViewDTO issueLiffViewDTO = new IssueLiffViewDTO();
                    issueLiffViewDTO.setProxy(getflowip);
                    issueLiffViewDTO.setToken(atUserTokenEntity.getToken());
                    IssueLiffViewVO issueLiffViewVO = lineService.issueLiffView(issueLiffViewDTO);
                    AtUserEntity update = new AtUserEntity();
                    update.setId(atUserEntity.getId());
                    if (ObjectUtil.isNull(issueLiffViewVO)) {
                        latch.countDown();
                        return;
                    }
                    update.setMsg(issueLiffViewVO.getMsg());
                    //号被封号了
                    if (201 == issueLiffViewVO.getCode()) {
                        //用户添加群过多 封号
                        if (issueLiffViewVO.getMsg().contains(UserStatusCode.UserStatusCode1.getValue())) {
                            update.setStatus(UserStatus.UserStatus2.getKey());
                        }else if (issueLiffViewVO.getMsg().contains(UserStatusCode.UserStatusCode2.getValue())) {
                            update.setStatus(UserStatus.UserStatus4.getKey());
                        }else if (issueLiffViewVO.getMsg().contains(UserStatusCode.UserStatusCode3.getValue())) {
                            update.setStatus(UserStatus.UserStatus5.getKey());
                        }else if (issueLiffViewVO.getMsg().contains(UserStatusCode.UserStatusCode5.getValue())) {
                            update.setStatus(UserStatus.UserStatus4.getKey());
                        }
                    }else if(300 == issueLiffViewVO.getCode()) {
                        update.setStatus(UserStatus.UserStatus1.getKey());
                    }
                    updates.add(update);
                    latch.countDown();
                }));
            }
            latch.await();
            //修改
            synchronized (atUserlockObj) {
                atUserService.updateBatchById(updates);
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task2Lock.unlock();
        }
    }



    /**
     * 同步token信息到用户表
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
            //获取刚导入的token去转化为账号
            List<AtUserTokenEntity> atUserTokenEntities = atUserTokenService.list(new QueryWrapper<AtUserTokenEntity>().lambda()
                    .eq(AtUserTokenEntity::getUseFlag, UseFlag.NO.getKey())
                    .last("limit 10")
            );
            if (CollUtil.isEmpty(atUserTokenEntities)) {
                log.info("GroupTask task1 atUserTokenEntities isEmpty");
                return;
            }
            //获取用户分组的map
            List<AtUserGroupEntity> list = atUserGroupService.list();
            Map<Integer, AtUserGroupEntity> atUserGroupEntityMap = list.stream().collect(Collectors.toMap(AtUserGroupEntity::getId, item -> item));

            List<AtUserEntity> atUserEntities = new ArrayList<>();
            List<AtUserTokenEntity> atUserTokenUpdateEntitys = new ArrayList<>();
            for (AtUserTokenEntity atUserTokenEntity : atUserTokenEntities) {
                //格式化token
                LineTokenJson lineTokenJson = JSON.parseObject(atUserTokenEntity.getToken(), LineTokenJson.class);
                AtUserEntity atUserEntity = new AtUserEntity();
                atUserEntity.setNation(lineTokenJson.getCountryCode());
                atUserEntity.setTelephone(lineTokenJson.getPhone());
                atUserEntity.setNickName(lineTokenJson.getNickName());
                atUserEntity.setPassword(lineTokenJson.getPassword());
                atUserEntity.setUserGroupId(atUserTokenEntity.getUserGroupId());
                atUserEntity.setNumberFriends(0);
                //未验证账号
                atUserEntity.setStatus(UserStatus.UserStatus1.getKey());
                AtUserGroupEntity atUserGroupEntity = atUserGroupEntityMap.get(atUserTokenEntity.getUserGroupId());
                if (ObjectUtil.isNotNull(atUserGroupEntity)) {
                    atUserEntity.setUserGroupName(atUserGroupEntity.getName());
                }
                //将添加token添加到用户
                atUserEntity.setDeleteFlag(DeleteFlag.NO.getKey());
                atUserEntity.setCreateTime(DateUtil.date());
                atUserEntity.setUserTokenId(atUserTokenEntity.getId());
                atUserEntities.add(atUserEntity);
                //修改数据使用状态
                AtUserTokenEntity update = new AtUserTokenEntity();
                update.setId(atUserTokenEntity.getId());
                update.setUseFlag(UseFlag.YES.getKey());
                atUserTokenUpdateEntitys.add(update);
            }
            //保存用户信息
            synchronized (atUserlockObj) {
                atUserService.saveBatch(atUserEntities);
            }
            //修改用户token信息
            synchronized (atUserTokenlockObj) {
                atUserTokenService.updateBatchById(atUserTokenUpdateEntitys);
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task1Lock.unlock();
        }

    }
}
