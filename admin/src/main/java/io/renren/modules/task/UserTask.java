package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.IssueLiffViewDTO;
import io.renren.modules.client.dto.LineTokenJson;
import io.renren.modules.client.vo.ConversionAppTokenVO;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.entity.AtUserTokenIosEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.IssueLiffViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static io.renren.modules.ltt.enums.AtUserTokenTypeEnum.AtUserTokenType2;

/**
 * @author liuyuchana
 * @email liuyuchan286@gmail.com
 * @date 2023/11/21 18:45
 */
@Component
@Slf4j
@EnableAsync
@Profile({"prod","verify"})
public class UserTask {


    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtUserTokenService atUserTokenService;
    @Autowired
    private AtUserGroupService atUserGroupService;
    @Autowired
    private AtUserTokenIosService atUserTokenIosService;
    @Autowired
    private ConcurrentHashMap<String, Lock> lockMap;
    static ReentrantLock task5Lock = new ReentrantLock();


    public static final Object atUserlockObj = new Object();
    public static final Object atUserTokenlockObj = new Object();
    @Autowired
    private LineService lineService;
    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;

//    /**
//     * 更新头像结果返回
//     */
//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task4() {
//        boolean b = task4Lock.tryLock();
//        if (!b) {
//            return;
//        }
//        try {
//            List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
//                    .eq(AtDataSubtaskEntity::getRefreshContactStatus, RefreshContactStatus.RefreshContactStatus1.getKey())
//                    .last("limit 5")
//            );
//            //任务为空
//            if (CollUtil.isEmpty(atDataSubtaskEntities)) {
//                log.info("UserTask task4 atDataSubtaskEntities isEmpty");
//                return;
//            }
//
//            //获取用户MAP
//            List<Integer> userIds = atDataSubtaskEntities.stream().map(AtDataSubtaskEntity::getUserId).collect(Collectors.toList());
//            List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
//            List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
//            List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
//            Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
//            Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId()).setTelephone(item.getTelephone())));
//
//            final CountDownLatch latch = new CountDownLatch(atDataSubtaskEntities.size());
//            List<AtDataSubtaskEntity> updates = new ArrayList<>();
//            for (AtDataSubtaskEntity atDataSubtaskEntity : atDataSubtaskEntities) {
//                threadPoolTaskExecutor.submit(new Thread(()->{
//                    if (StrUtil.isEmpty(atDataSubtaskEntity.getMid())) {
//                        latch.countDown();
//                        return;
//                    }
//                    //获取用户token
//                    AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(atDataSubtaskEntity.getUserId());
//                    if (ObjectUtil.isNull(atUserTokenEntity)) {
//                        latch.countDown();
//                        return;
//                    }
//
//                    //获取代理
//                    CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
//                    cdLineIpProxyDTO.setTokenPhone(atUserTokenEntity.getTelephone());
//                    cdLineIpProxyDTO.setLzPhone(atUserTokenEntity.getTelephone());
//                    String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
//                    if (StrUtil.isEmpty(proxyIp)) {
//                        latch.countDown();
//                        return;
//                    }
//
//                    GetContactsInfoV3DTO getContactsInfoV3DTO = new GetContactsInfoV3DTO();
//                    getContactsInfoV3DTO.setProxy(proxyIp);
//                    getContactsInfoV3DTO.setUserMid(atDataSubtaskEntity.getMid());
//                    getContactsInfoV3DTO.setToken(atUserTokenEntity.getToken());
//                    GetContactsInfoV3VO getContactsInfoV3VO = lineService.getContactsInfoV3(getContactsInfoV3DTO);
//                    if (ObjectUtil.isNull(getContactsInfoV3VO)) {
//                        latch.countDown();
//                        return;
//                    }
//                    AtDataSubtaskEntity update = new AtDataSubtaskEntity();
//                    update.setId(atDataSubtaskEntity.getId());
//                    //成功获取返回zhi
//                    if (200 == getContactsInfoV3VO.getCode()) {
//                        Map<String, GetContactsInfoV3VO.Data.DMap> contacts = getContactsInfoV3VO.getData().getContacts();
//                        if (CollUtil.isEmpty(contacts)) {
//                            latch.countDown();
//                            return;
//                        }
//                        Collection<GetContactsInfoV3VO.Data.DMap> values = contacts.values();
//                        for (GetContactsInfoV3VO.Data.DMap value : values) {
//                            GetContactsInfoV3VO.Data.Contact contact = value.getContact();
//                            update.setMid(contact.getMid());
//                            update.setType(contact.getType());
//                            update.setStatus(contact.getStatus());
//                            update.setRelation(contact.getRelation());
//                            update.setDisplayName(contact.getDisplayName());
//                            update.setPhoneticName(contact.getPhoneticName());
//                            update.setPictureStatus(contact.getPictureStatus());
//                            update.setThumbnailUrl(contact.getThumbnailUrl());
//                            update.setStatusMessage(contact.getStatusMessage());
//                            update.setDisplayNameOverridden(contact.getDisplayNameOverridden());
//                            update.setPicturePath(contact.getPicturePath());
//                            update.setRecommendpArams(contact.getRecommendParams());
//                            update.setFriendRequestStatus(contact.getFriendRequestStatus());
//                            update.setRefreshContactStatus(RefreshContactStatus.RefreshContactStatus3.getKey());
//                            updates.add(update);
//                        }
//                    }
//                    latch.countDown();
//                }));
//            }
//            latch.await();
//            //修改老得
//            if (CollUtil.isNotEmpty(updates)) {
//                synchronized (DataTask.atAtDataSubtaskObj) {
//                    atDataSubtaskService.updateBatchById(updates);
//                }
//            }
//        }catch (Exception e) {
//            log.error("err = {}",e.getMessage());
//        }finally {
//            task4Lock.unlock();
//        }
//    }
//
//
//    /**
//     * 同步token信息到用户表
//     */
//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task3() {
//        boolean b = task3Lock.tryLock();
//        if (!b) {
//            return;
//        }
//
//        try {
//            //获取用户未验证的状态
//            List<AtUserEntity> atUserEntities = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
//                    .eq(AtUserEntity::getRefreshContactStatus, RefreshContactStatus.RefreshContactStatus1.getKey())
//                    .last("limit 100")
//            );
//            if (CollUtil.isEmpty(atUserEntities)) {
//                log.info("GroupTask task2 atUserEntities isEmpty");
//                return;
//            }
//
//            //用户tokenIds
//            List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
//            List<AtUserTokenEntity> atUserTokenEntities = atUserTokenService.listByIds(userTokenIds);
//            if (CollUtil.isEmpty(atUserEntities)) {
//                log.info("GroupTask task2 atUserTokenEntities isEmpty");
//                return;
//            }
//            //用户tokenMap
//            Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = atUserTokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
//
//            final CountDownLatch latch = new CountDownLatch(atUserEntities.size());
//
//            List<AtDataSubtaskEntity> updates = new ArrayList<>();
//            List<AtDataSubtaskEntity> saves = new ArrayList<>();
//            List<AtUserEntity> atUserEntityListUpdate = new ArrayList<>();
//
//            for (AtUserEntity atUserEntity : atUserEntities) {
//                threadPoolTaskExecutor.submit(new Thread(()->{
//                    AtUserTokenEntity atUserTokenEntity = atUserTokenEntityMap.get(atUserEntity.getUserTokenId());
//                    if (ObjectUtil.isNull(atUserTokenEntity)) {
//                        latch.countDown();
//                        return;
//                    }
//                    //获取代理
//                    CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
//                    cdLineIpProxyDTO.setTokenPhone(atUserEntity.getTelephone());
//                    cdLineIpProxyDTO.setLzPhone(atUserEntity.getTelephone());
//                    String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
//                    if (StrUtil.isEmpty(proxyIp)) {
//                        latch.countDown();
//                        return;
//                    }
//
//                    GetAllContactIdsDTO getAllContactIdsDTO = new GetAllContactIdsDTO();
//                    getAllContactIdsDTO.setProxy(proxyIp);
//                    getAllContactIdsDTO.setToken(atUserTokenEntity.getToken());
//                    GetAllContactIdsVO getAllContactIdsVO = lineService.getAllContactIds(getAllContactIdsDTO);
//                    if (ObjectUtil.isNull(getAllContactIdsVO)) {
//                        latch.countDown();
//                        return;
//                    }
//
//                    if (200 == getAllContactIdsVO.getCode()) {
//                        //获取所有的mid->data
//                        List<String> data = getAllContactIdsVO.getData();
//                        if (CollUtil.isEmpty(data)) {
//                            //获取该用户的的好友
//                            AtUserEntity update = new AtUserEntity();
//                            update.setId(atUserEntity.getId());
//                            update.setNumberFriends(0);
//                            update.setRefreshContactStatus(RefreshContactStatus.RefreshContactStatus3.getKey());
//                            atUserEntityListUpdate.add(update);
//                            latch.countDown();
//                            return;
//                        }
//                        List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
//                                .eq(AtDataSubtaskEntity::getUserId,atUserEntity.getId())
//                        );
//                        Map<String, AtDataSubtaskEntity> atDataSubtaskEntityMap = atDataSubtaskEntities.stream().filter(item -> StrUtil.isNotEmpty(item.getMid())).collect(Collectors.toMap(AtDataSubtaskEntity::getMid, item -> item));
//                        for (String datum : data) {
//                            //包含了mid
//                            if (atDataSubtaskEntityMap.containsKey(datum)) {
//                                AtDataSubtaskEntity atDataSubtaskEntity = atDataSubtaskEntityMap.get(datum);
//                                AtDataSubtaskEntity update = new AtDataSubtaskEntity();
//                                update.setId(atDataSubtaskEntity.getId());
//                                update.setRefreshContactStatus(RefreshContactStatus.RefreshContactStatus1.getKey());
//                                updates.add(update);
//                                //不包含mid
//                            }else {
//                                AtDataSubtaskEntity save = new AtDataSubtaskEntity();
//                                save.setUserId(atUserEntity.getId());
//                                save.setSysUserId(atUserEntity.getSysUserId());
//                                save.setMid(datum);
//                                save.setRefreshContactStatus(RefreshContactStatus.RefreshContactStatus1.getKey());
//                                saves.add(save);
//                            }
//                        }
//                        //获取该用户的的好友
//                        AtUserEntity update = new AtUserEntity();
//                        update.setId(atUserEntity.getId());
//                        update.setNumberFriends(data.size());
//                        update.setRefreshContactStatus(RefreshContactStatus.RefreshContactStatus3.getKey());
//                        atUserEntityListUpdate.add(update);
//                    }
//                    latch.countDown();
//                }));
//            }
//            latch.await();
//            //修改老得
//            if (CollUtil.isNotEmpty(updates)) {
//                synchronized (DataTask.atAtDataSubtaskObj) {
//                    atDataSubtaskService.updateBatchById(updates);
//                }
//            }
//            // 保存新的
//            if (CollUtil.isNotEmpty(saves)) {
//                synchronized (DataTask.atAtDataSubtaskObj) {
//                    atDataSubtaskService.saveBatch(saves);
//                }
//            }
//            //修改用户
//            if (CollUtil.isNotEmpty(atUserEntityListUpdate)) {
//                synchronized (atUserlockObj) {
//                    atUserService.updateBatchById(atUserEntityListUpdate);
//                }
//            }
//        }catch (Exception e) {
//            log.error("err = {}",e.getMessage());
//        }finally {
//            task3Lock.unlock();
//        }
//    }

//
//    /**
//     * 同步token信息到用户表
//     */
//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task2() {
//
//        //获取用户未验证的状态
//        List<AtUserEntity> atUserEntities = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
//                .eq(AtUserEntity::getStatus,UserStatus.UserStatus1.getKey())
//                .last("limit 50")
//                .orderByAsc(AtUserEntity::getStatus)
//        );
//        if (CollUtil.isEmpty(atUserEntities)) {
//            log.info("UserTask task2 atUserEntities isEmpty");
//            return;
//        }
//        //用户tokenIds
//        List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
//        List<AtUserTokenEntity> atUserTokenEntities = atUserTokenService.listByIds(userTokenIds);
//        if (CollUtil.isEmpty(atUserEntities)) {
//            log.info("UserTask task2 atUserTokenEntities isEmpty");
//            return;
//        }
//        //用户tokenMap
//        Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = atUserTokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
//        for (AtUserEntity atUserEntity : atUserEntities) {
//            threadPoolTaskExecutor.execute(() -> {
//                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource9, atUserEntity.getId());
//                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
//                boolean triedLock = lock.tryLock();
//                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
//                if(triedLock) {
//                    try{
//                        AtUserTokenEntity atUserTokenEntity = atUserTokenEntityMap.get(atUserEntity.getUserTokenId());
//                        if (ObjectUtil.isNull(atUserTokenEntity)) {
//                            return;
//                        }
//
//                        //获取代理
//                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
//                        cdLineIpProxyDTO.setTokenPhone(atUserEntity.getTelephone());
//                        cdLineIpProxyDTO.setLzPhone(atUserEntity.getTelephone());
//                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
//                        if (StrUtil.isEmpty(proxyIp)) {
//                            return;
//                        }
//
//                        IssueLiffViewDTO issueLiffViewDTO = new IssueLiffViewDTO();
//                        issueLiffViewDTO.setProxy(proxyIp);
//                        issueLiffViewDTO.setToken(atUserTokenEntity.getToken());
//                        IssueLiffViewVO issueLiffViewVO = lineService.issueLiffView(issueLiffViewDTO);
//                        AtUserEntity update = new AtUserEntity();
//                        update.setId(atUserEntity.getId());
//                        if (ObjectUtil.isNull(issueLiffViewVO)) {
//                            return;
//                        }
//                        update.setMsg(issueLiffViewVO.getMsg());
//                        update.setStatus(UserStatus.UserStatus4.getKey());
//                        //号被封号了
//                        if (201 == issueLiffViewVO.getCode()) {
//                            //用户添加群过多 封号
//                            if (issueLiffViewVO.getMsg().contains(UserStatusCode.UserStatusCode9.getValue())) {
//                                update.setStatus(UserStatus.UserStatus2.getKey());
//                            }else  if (issueLiffViewVO.getMsg().contains(UserStatusCode.UserStatusCode13.getValue())) {
//                                update.setStatus(UserStatus.UserStatus2.getKey());
//                            }else  if (issueLiffViewVO.getMsg().contains(UserStatusCode.UserStatusCode14.getValue())) {
//                                update.setStatus(UserStatus.UserStatus3.getKey());
//                            }
//                        }else if(300 == issueLiffViewVO.getCode()) {
//                            update.setStatus(UserStatus.UserStatus1.getKey());
//                        }
//                        atUserService.updateById(update);
//                    }finally {
//                        lock.unlock();
//                    }
//                }else {
//                    log.info("keyByResource = {} 在执行",keyByResource);
//                }
//            });
//
//        }
//
//    }



    /**
     * 同步token信息到用户表
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task1() {


        //获取刚导入的token去转化为账号
        List<AtUserTokenEntity> atUserTokenEntities = atUserTokenService.list(new QueryWrapper<AtUserTokenEntity>().lambda()
                .eq(AtUserTokenEntity::getUseFlag, UseFlag.NO.getKey())
                .last("limit 20")
        );
        if (CollUtil.isEmpty(atUserTokenEntities)) {
            log.info("UserTask task1 atUserTokenEntities isEmpty");
            return;
        }

        //获取用户分组的map
        List<Integer> userGroupIdList = atUserTokenEntities.stream()
                .filter(i -> ObjectUtil.isNotNull(i.getUserGroupId()))
                .map(AtUserTokenEntity::getUserGroupId).distinct().collect(Collectors.toList());
        Map<Integer, String> atUserGroupMap = atUserGroupService.getMapByIds(userGroupIdList);
        for (AtUserTokenEntity atUserTokenEntity : atUserTokenEntities) {
            threadPoolTaskExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource10, atUserTokenEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        //格式化token
                        LineTokenJson lineTokenJson = JSON.parseObject(atUserTokenEntity.getToken(), LineTokenJson.class);
                        AtUserEntity atUserEntity = new AtUserEntity();
                        atUserEntity.setNation(lineTokenJson.getCountryCode());
                        String telephone = StrUtil.cleanBlank(lineTokenJson.getPhone()).replaceAll("-", "");
//                        AtUserEntity one = atUserService.getOne(new QueryWrapper<AtUserEntity>().lambda()
//                                .eq(AtUserEntity::getTelephone,telephone)
//                        );
                        atUserEntity.setTelephone(telephone);
                        atUserEntity.setNickName(lineTokenJson.getNickName());
                        atUserEntity.setPassword(lineTokenJson.getPassword());
                        atUserEntity.setUserGroupId(atUserTokenEntity.getUserGroupId());
                        atUserEntity.setNumberFriends(0);
                        //未验证账号
                        atUserEntity.setStatus(UserStatus.UserStatus4.getKey());
                        atUserEntity.setUserGroupName(atUserGroupMap.get(atUserTokenEntity.getUserGroupId()));
                        //将添加token添加到用户
                        atUserEntity.setDeleteFlag(DeleteFlag.NO.getKey());
                        atUserEntity.setCreateTime(DateUtil.date());
                        atUserEntity.setUserTokenId(atUserTokenEntity.getId());
                        atUserEntity.setSysUserId(atUserTokenEntity.getSysUserId());
                        if (ObjectUtil.isNotNull(atUserTokenEntity.getTokenType())
                                && AtUserTokenType2.getKey().equals(atUserTokenEntity.getTokenType())) {
                            atUserEntity.setUserSource(AtUserSourceEnum.AtUserSource2.getKey());
                        }
//                        if (ObjectUtil.isNotNull(one)) {
//                            atUserEntity.setId(one.getId());
//                            atUserService.updateById(atUserEntity);
//                        }else {
                            atUserService.save(atUserEntity);
//                        }
                        //修改数据使用状态
                        AtUserTokenEntity update = new AtUserTokenEntity();
                        update.setId(atUserTokenEntity.getId());
                        update.setUseFlag(UseFlag.YES.getKey());
                        atUserTokenService.updateById(update);
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
     * 生成真机token
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task5() {
        boolean b = task5Lock.tryLock();
        if (!b) {
            return;
        }
        try {
            //获取刚导入的token去转化为账号
            List<AtUserTokenIosEntity> atUserTokenIosEntityList = atUserTokenIosService.list(new QueryWrapper<AtUserTokenIosEntity>().lambda().isNull(AtUserTokenIosEntity::getAtUserTokenId).last("limit 10"));
            if (CollUtil.isEmpty(atUserTokenIosEntityList)) {
                log.info("UserTask task5 atUserTokenIosEntityList isEmpty");
                return;
            }
            final CountDownLatch latch = new CountDownLatch(atUserTokenIosEntityList.size());

            for (AtUserTokenIosEntity tokenIosEntity : atUserTokenIosEntityList) {
                threadPoolTaskExecutor.submit(new Thread(() -> {
                    if (StrUtil.isEmpty(tokenIosEntity.getIosToken())) {
                        latch.countDown();
                        return;
                    }
                    //获取用户token
                    ConversionAppTokenVO conversionAppToken = lineService.conversionAppToken(tokenIosEntity.getIosToken());
                    if (ObjectUtil.isNull(conversionAppToken)) {
                        latch.countDown();
                        return;
                    }
                    //成功获取返回zhi
                    if (0 == conversionAppToken.getCode() && conversionAppToken.getData() != null) {
                        String token = conversionAppToken.getData().getToken();
                        if (StrUtil.isEmpty(token)) {
                            latch.countDown();
                            return;
                        }

                        //插入token
                        AtUserTokenEntity updateAtUserToken = new AtUserTokenEntity();
                        updateAtUserToken.setToken(token);
                        updateAtUserToken.setUserGroupId(null);
                        updateAtUserToken.setSysUserId(1L);
                        updateAtUserToken.setTokenType(AtUserTokenType2.getKey());//token类型 1协议token 2真机token
                        updateAtUserToken.setUseFlag(UseFlag.NO.getKey());
                        updateAtUserToken.setDeleteFlag(DeleteFlag.NO.getKey());
                        updateAtUserToken.setCreateTime(new Date());
                        updateAtUserToken.setPlatform(Platform.IOS.getKey());
                        atUserTokenService.save(updateAtUserToken);

                        AtUserTokenIosEntity updateAtUserTokenIos = new AtUserTokenIosEntity();
                        updateAtUserTokenIos.setId(tokenIosEntity.getId());
                        updateAtUserTokenIos.setAtUserTokenId(updateAtUserToken.getId());
                        atUserTokenIosService.updateById(updateAtUserTokenIos);
                    }
                    latch.countDown();
                }));
            }
            latch.await();
        } catch (Exception e) {
            log.error("UserTask task5 err = {}", e.getMessage());
        } finally {
            task5Lock.unlock();
        }
    }

}
