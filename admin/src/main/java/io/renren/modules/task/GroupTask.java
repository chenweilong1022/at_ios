package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.CreateGroupMax;
import io.renren.modules.client.dto.GetChatsDTO;
import io.renren.modules.client.dto.InviteIntoChatDTO;
import io.renren.modules.client.dto.RegisterResultDTO;
import io.renren.modules.client.vo.CreateGroupResultVO;
import io.renren.modules.client.vo.GetChatsVO;
import io.renren.modules.client.vo.LineRegisterVO;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import io.renren.modules.ltt.entity.AtGroupEntity;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.enums.GroupStatus;
import io.renren.modules.ltt.enums.LockMapKeyResource;
import io.renren.modules.ltt.enums.TaskStatus;
import io.renren.modules.ltt.enums.UserStatus;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.AtUserTokenVO;
import io.renren.modules.ltt.vo.AtUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
@Profile({"prod","task"})
public class GroupTask {


    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtUserTokenService atUserTokenService;
    @Autowired
    private AtUserGroupService atUserGroupService;

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
//    public void task4() { }


    /**
     *
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task3() {


        //获取当前需要同步通讯的任务
        List<AtGroupEntity> cdGroupTasksEntities = atGroupService.list(new QueryWrapper<AtGroupEntity>().lambda()
                .last("limit 50")
                .eq(AtGroupEntity::getGroupStatus,GroupStatus.GroupStatus5.getKey())
        );
        if (CollUtil.isEmpty(cdGroupTasksEntities)) {
            log.info("GroupTask task8 list isEmpty");
            return;
        }



        //获取用户MAP
        List<Integer> userIds = cdGroupTasksEntities.stream().map(AtGroupEntity::getUserId).collect(Collectors.toList());
        List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
        List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
        List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
        Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
        Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId()).setTelephone(item.getTelephone())));


        for (AtGroupEntity cdGroupTasksEntity : cdGroupTasksEntities) {

            threadPoolTaskExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource8, cdGroupTasksEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{

                        List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                .eq(AtDataSubtaskEntity::getGroupId,cdGroupTasksEntity.getId())
                        );

                        //获取用户token
                        AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(cdGroupTasksEntity.getUserId());
                        if (ObjectUtil.isNull(atUserTokenEntity)) {
                            return;
                        }

                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(atUserTokenEntity.getTelephone());
                        cdLineIpProxyDTO.setLzPhone(atUserTokenEntity.getTelephone());
                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                        if (StrUtil.isEmpty(proxyIp)) {
                            return;
                        }
                        GetChatsDTO getChatsDTO = new GetChatsDTO();
                        getChatsDTO.setProxy(proxyIp);
                        getChatsDTO.setChatRoomId(cdGroupTasksEntity.getRoomId());
                        getChatsDTO.setToken(atUserTokenEntity.getToken());
                        GetChatsVO chats = lineService.getChats(getChatsDTO);
                        if (ObjectUtil.isNotNull(chats) && 200 == chats.getCode()) {
                            GetChatsVO.Data data = chats.getData();
                            if (ObjectUtil.isNull(data)) return;
                            List<GetChatsVO.Data.Chat> dataChats = data.getChats();
                            if (CollUtil.isEmpty(dataChats)) return;
                            GetChatsVO.Data.Chat chat = dataChats.get(0);
                            if (ObjectUtil.isNull(chat)) return;
                            GetChatsVO.Data.Chat.Extra extra = chat.getExtra();
                            if (ObjectUtil.isNull(extra)) return;
                            GetChatsVO.Data.Chat.Extra.GroupExtra groupExtra = extra.getGroupExtra();
                            if (ObjectUtil.isNull(groupExtra)) return;
                            Map<String, Long> memberMids = groupExtra.getMemberMids();
                            Map<String, AtDataSubtaskEntity> midCdMaterialPhoneEntityMap = atDataSubtaskEntities.stream().filter(item -> StrUtil.isNotEmpty(item.getMid())).collect(Collectors.toMap(AtDataSubtaskEntity::getMid, s -> s,(v1,v2) -> v2));

                            List<AtDataSubtaskEntity> atDataSubtaskEntityList = new ArrayList<>();
                            for (String key : memberMids.keySet()) {
                                AtDataSubtaskEntity cdMaterialPhoneEntity = midCdMaterialPhoneEntityMap.get(key);
                                if (ObjectUtil.isNotNull(cdMaterialPhoneEntity)) {
                                    cdMaterialPhoneEntity.setTaskStatus(TaskStatus.TaskStatus11.getKey());
                                    atDataSubtaskEntityList.add(cdMaterialPhoneEntity);
                                }
                            }

                            cdGroupTasksEntity.setSuccessfullyAttractGroupsNumber(memberMids.keySet().size());
                            cdGroupTasksEntity.setGroupStatus(GroupStatus.GroupStatus9.getKey());
                            cdGroupTasksEntity.setMsg(StrUtil.concat(true,cdGroupTasksEntity.getMsg(),chats.getMsg()));

                            atDataSubtaskService.updateBatchById(atDataSubtaskEntityList);
                            atGroupService.updateById(cdGroupTasksEntity);
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
     * 获取type为1 加粉类型为手机号模式
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task2() {

        //获取当前需要同步通讯的任务
        List<AtGroupEntity> cdGroupTasksEntities = atGroupService.list(new QueryWrapper<AtGroupEntity>().lambda()
                .last("limit 50")
                .eq(AtGroupEntity::getGroupStatus,GroupStatus.GroupStatus3.getKey())
        );
        if (CollUtil.isEmpty(cdGroupTasksEntities)) {
            log.info("GroupTask task2 list isEmpty");
            return;
        }

        for (AtGroupEntity cdGroupTasksEntity : cdGroupTasksEntities) {
            threadPoolTaskExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource8, cdGroupTasksEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        RegisterResultDTO registerResultDTO = new RegisterResultDTO();
                        registerResultDTO.setTaskId(cdGroupTasksEntity.getTaskId());
                        CreateGroupResultVO groupResult = lineService.createGroupResult(registerResultDTO);
                        cdGroupTasksEntity.setMsg(groupResult.getMsg());
                        if (ObjectUtil.isNotNull(groupResult) && 200 == groupResult.getCode()) {
                            if (2 == groupResult.getData().getStatus()) {
                                cdGroupTasksEntity.setRoomId(groupResult.getData().getGroupInfo().getRoomId());
                                cdGroupTasksEntity.setChatRoomUrl(groupResult.getData().getGroupInfo().getChatRoomUrl());
                                cdGroupTasksEntity.setRoomTicketId(groupResult.getData().getGroupInfo().getRoomTicketId());
                                cdGroupTasksEntity.setGroupStatus(GroupStatus.GroupStatus5.getKey());
                                //如果拉群成功
                                List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                        .eq(AtDataSubtaskEntity::getGroupId,cdGroupTasksEntity.getId())
                                        .notIn(AtDataSubtaskEntity::getUserId,cdGroupTasksEntity.getUserId())
                                        .in(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus10.getKey(),TaskStatus.TaskStatus8.getKey())
                                );
                                if (CollUtil.isNotEmpty(atDataSubtaskEntities)) {
                                    Integer userId = atDataSubtaskEntities.get(0).getUserId();
                                    AtUserVO atUserVO = atUserService.getById(userId);
                                    AtUserTokenVO atUserTokenVO = atUserTokenService.getById(atUserVO.getUserTokenId());

                                    //获取代理
                                    CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                                    cdLineIpProxyDTO.setTokenPhone(atUserVO.getTelephone());
                                    cdLineIpProxyDTO.setLzPhone(atUserVO.getTelephone());
                                    String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                                    if (StrUtil.isEmpty(proxyIp)) {
                                        return;
                                    }
                                    // 邀请好友
                                    List<String> userMids = atDataSubtaskEntities.stream().map(AtDataSubtaskEntity::getMid).collect(Collectors.toList());
                                    InviteIntoChatDTO inviteIntoChatDTO = new InviteIntoChatDTO();
                                    inviteIntoChatDTO.setProxy(proxyIp);
                                    inviteIntoChatDTO.setChatRoomId(cdGroupTasksEntity.getRoomId());
                                    inviteIntoChatDTO.setUserMids(userMids);
                                    inviteIntoChatDTO.setToken(atUserTokenVO.getToken());
                                    LineRegisterVO lineRegisterVO = lineService.inviteIntoChat(inviteIntoChatDTO);
                                    if (200 != lineRegisterVO.getCode()) {
                                        cdGroupTasksEntity.setMsg(StrUtil.concat(true,groupResult.getMsg(),lineRegisterVO.getMsg()));
                                    }
                                }
                            }else if (-1 == groupResult.getData().getStatus()) {
                                cdGroupTasksEntity.setGroupStatus(GroupStatus.GroupStatus4.getKey());
                                atUserService.unlock(cdGroupTasksEntity.getUserId(),UserStatus.UserStatus2);
                            }else if (-2 == groupResult.getData().getStatus()) {
                                cdGroupTasksEntity.setGroupStatus(GroupStatus.GroupStatus4.getKey());
                            }
                        }else {
                            cdGroupTasksEntity.setRoomId(groupResult.getMsg());
                            cdGroupTasksEntity.setGroupStatus(GroupStatus.GroupStatus4.getKey());
                        }
                        cdGroupTasksEntity.setMsg(StrUtil.concat(true,cdGroupTasksEntity.getMsg(),groupResult.getMsg(),groupResult.getData().getRemark()));
                        atGroupService.updateById(cdGroupTasksEntity);
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
     * 获取初始化的添加粉任务
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task1() {

        //获取当前需要同步通讯的任务
        List<AtGroupEntity> cdGroupTasksEntities = atGroupService.list(new QueryWrapper<AtGroupEntity>().lambda()
                .last("limit 50")
                .eq(AtGroupEntity::getGroupStatus,GroupStatus.GroupStatus7.getKey())
        );
        if (CollUtil.isEmpty(cdGroupTasksEntities)) {
            log.info("GroupTask task1 list isEmpty");
            return;
        }

        //获取用户MAP
        List<Integer> userIds = cdGroupTasksEntities.stream().map(AtGroupEntity::getUserId).collect(Collectors.toList());
        List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
        List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
        List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
        Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
        Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId()).setTelephone(item.getTelephone())));

        for (AtGroupEntity cdGroupTasksEntity : cdGroupTasksEntities) {
             threadPoolTaskExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource8, cdGroupTasksEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        //获取用户token
                        AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(cdGroupTasksEntity.getUserId());
                        if (ObjectUtil.isNull(atUserTokenEntity)) {
                            return;
                        }

                        List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                .eq(AtDataSubtaskEntity::getGroupId,cdGroupTasksEntity.getId())
                                .eq(AtDataSubtaskEntity::getUserId,cdGroupTasksEntity.getUserId())
                                .in(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus10.getKey(),TaskStatus.TaskStatus8.getKey())
                        );

                        if (CollUtil.isEmpty(atDataSubtaskEntities)) {
                            return;
                        }

                        List<String> mids = atDataSubtaskEntities.stream().map(AtDataSubtaskEntity::getMid).distinct().collect(Collectors.toList());

                        //获取代理
                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(atUserTokenEntity.getTelephone());
                        cdLineIpProxyDTO.setLzPhone(atUserTokenEntity.getTelephone());
                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                        if (StrUtil.isEmpty(proxyIp)) {
                            return;
                        }

                        CreateGroupMax createGroupMax = new CreateGroupMax();
                        createGroupMax.setUserMidList(mids);
                        createGroupMax.setProxy(proxyIp);
                        createGroupMax.setGroupName(cdGroupTasksEntity.getGroupName());
                        createGroupMax.setToken(atUserTokenEntity.getToken());
                        LineRegisterVO lineRegisterVO = lineService.createGroupMax(createGroupMax);
                        if (ObjectUtil.isNotNull(lineRegisterVO) && 200 == lineRegisterVO.getCode()) {
                            cdGroupTasksEntity.setTaskId(lineRegisterVO.getData().getTaskId());
                            cdGroupTasksEntity.setGroupStatus(GroupStatus.GroupStatus3.getKey());
                            cdGroupTasksEntity.setMsg(StrUtil.concat(true,cdGroupTasksEntity.getMsg(),lineRegisterVO.getMsg()));
                            atGroupService.updateById(cdGroupTasksEntity);
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
