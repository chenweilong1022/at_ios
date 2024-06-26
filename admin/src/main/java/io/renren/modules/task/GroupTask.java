package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.constant.SystemConstant;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.*;
import io.renren.modules.client.vo.CreateGroupResultVO;
import io.renren.modules.client.vo.GetChatsVO;
import io.renren.modules.client.vo.LineRegisterVO;
import io.renren.modules.ltt.dto.AtGroupDTO;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.*;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.enums.OpenApp;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.AtDataTaskVO;
import io.renren.modules.ltt.vo.AtUserTokenVO;
import io.renren.modules.ltt.vo.AtUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
    @Autowired
    private CdLineRegisterService cdLineRegisterService;
    @Resource
    private CdGetPhoneService cdGetPhoneService;
    @Resource
    private SystemConstant systemConstant;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplateObj;

//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task4() { }

    @Scheduled(fixedDelay = 10 * 1000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task5() {
        String format = String.format("and MOD(change_user_ids, %s) = %s limit 50", systemConstant.getSERVERS_TOTAL_MOD(), systemConstant.getSERVERS_MOD());
        //获取群名和真实名称不一样的列表，同步服务器真实群名
        List<AtGroupEntity> cdGroupTasksEntities = atGroupService.list(new QueryWrapper<AtGroupEntity>().lambda()
                .last(format)
                .eq(AtGroupEntity::getGroupStatus, GroupStatus.GroupStatus15.getKey())
                .eq(AtGroupEntity::getRandomGroupName, OpenApp.OpenApp2.getKey())
                .apply("group_name <> real_group_name")
        );
        if (CollUtil.isEmpty(cdGroupTasksEntities)) {
            log.info("GroupTask task5 list isEmpty");
            return;
        }
        List<Integer> groupIdList = cdGroupTasksEntities.stream().map(AtGroupEntity::getId).collect(Collectors.toList());

        atGroupService.getRealGroupName(new AtGroupDTO().setIds(groupIdList));
    }

    @Scheduled(fixedDelay = 10 * 1000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task4() {

        String format = String.format("and MOD(change_user_ids, %s) = %s limit 50", systemConstant.getSERVERS_TOTAL_MOD(), systemConstant.getSERVERS_MOD());
        //获取群人数同步,且需要更改群名的任务
        List<AtGroupEntity> cdGroupTasksEntities = atGroupService.list(new QueryWrapper<AtGroupEntity>().lambda()
                .last(format)
                .eq(AtGroupEntity::getGroupStatus, GroupStatus.GroupStatus9.getKey())
                .eq(AtGroupEntity::getRandomGroupName, OpenApp.OpenApp2.getKey())
        );
        if (CollUtil.isEmpty(cdGroupTasksEntities)) {
            log.info("GroupTask task4 list isEmpty");
            return;
        }
        Integer mod = systemConstant.getSERVERS_MOD();
        String key = String.valueOf(mod);


        //获取用户MAP
        List<Integer> userIds = cdGroupTasksEntities.stream().map(AtGroupEntity::getUserId).collect(Collectors.toList());
        List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
        Map<Integer, AtUserEntity> atUserMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, i -> i));

        Map<String, CdGetPhoneEntity> phoneEntity = null;
        List<String> telephoneList = atUserEntities.stream().map(AtUserEntity::getTelephone).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(telephoneList)) {
            phoneEntity = cdGetPhoneService.list(new QueryWrapper<CdGetPhoneEntity>().lambda()
                    .in(CdGetPhoneEntity::getPhone, telephoneList)).stream().collect(Collectors
                    .toMap(CdGetPhoneEntity::getPhone, i -> i, (a1, b1) -> b1));
        } else {
            phoneEntity = new HashMap<>();
        }

        Map<String, CdGetPhoneEntity> phoneEntityMap = phoneEntity;

        for (AtGroupEntity cdGroupTasksEntity : cdGroupTasksEntities) {
            cdGroupTasksEntity.setChangUserIdListTemp(JSON.parseArray(cdGroupTasksEntity.getChangeUserIds(), Integer.class));
            threadPoolTaskExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource13,
                        ObjectUtil.isNotNull(cdGroupTasksEntity.getChangUserIdListTemp())
                                && ObjectUtil.isNotNull(cdGroupTasksEntity.getChangUserIdListTemp().get(0)) ?
                                cdGroupTasksEntity.getChangUserIdListTemp().get(0) : cdGroupTasksEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}", keyByResource, triedLock);
                if (triedLock) {
                    try {
                        AtUserEntity atUserEntity = atUserMap.get(cdGroupTasksEntity.getUserId());
                        if (atUserEntity != null) {
                            CdGetPhoneEntity getPhoneEntity = phoneEntityMap.get(atUserEntity.getTelephone());
                            if (ObjectUtil.isNotNull(getPhoneEntity)
                                    && getPhoneEntity.getCreateTime().after(cdGroupTasksEntity.getCreateTime())) {
                                if (PhoneStatus.PhoneStatus8.getKey().equals(getPhoneEntity.getPhoneStatus())
                                        || PhoneStatus.PhoneStatus9.getKey().equals(getPhoneEntity.getPhoneStatus())|| PhoneStatus.PhoneStatus7.getKey().equals(getPhoneEntity.getPhoneStatus())) {
                                    //注册成功、已购买/ 作废
                                    atGroupService.updateGroupName(new AtGroupDTO().setIds(Arrays.asList(cdGroupTasksEntity.getId())));
                                } else if (PhoneStatus.PhoneStatus6.getKey().equals(getPhoneEntity.getPhoneStatus())) {
                                    //注册出现问题，判断为封号，则直接注册
                                    CdLineRegisterEntity cdLineRegisterEntity = (CdLineRegisterEntity) redisTemplateObj.opsForHash()
                                            .get(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key),
                                                    String.valueOf(getPhoneEntity.getId()));
                                    if (ObjectUtil.isNotNull(cdLineRegisterEntity)
                                            && StringUtils.isNotEmpty(cdLineRegisterEntity.getErrMsg())
                                            && !cdLineRegisterEntity.getErrMsg().contains("セッションがタイムアウトしました。 もう一度お試しください")
                                            && cdLineRegisterEntity.getErrMsg().contains("Code:100")) {
                                        //发起重新注册
                                        atGroupService.updateGroupName(new AtGroupDTO().setIds(Arrays.asList(cdGroupTasksEntity.getId())));
                                    }
                                }
                            }
                        }

                    } finally {
                        lock.unlock();
                    }
                } else {
                    log.info("keyByResource = {} 在执行", keyByResource);
                }
            });

        }
    }

    /**
     *
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task3() {

        String format = String.format("and MOD(id, %s) = %s limit 50", systemConstant.getSERVERS_TOTAL_MOD(), systemConstant.getSERVERS_MOD());
        //获取当前需要同步通讯的任务
        List<AtGroupEntity> cdGroupTasksEntities = atGroupService.list(new QueryWrapper<AtGroupEntity>().lambda()
                .last(format)
                .eq(AtGroupEntity::getGroupStatus,GroupStatus.GroupStatus5.getKey())
        );
        if (CollUtil.isEmpty(cdGroupTasksEntities)) {
            log.info("GroupTask task8 list isEmpty");
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
                        //获取用户token
                        AtUserTokenEntity atUserTokenEntity = atUserTokenService.getByUserIdCache(cdGroupTasksEntity.getUserId());
                        if (ObjectUtil.isNull(atUserTokenEntity)) {
                            return;
                        }
                        List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                .eq(AtDataSubtaskEntity::getGroupId,cdGroupTasksEntity.getId())
                        );
                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(atUserTokenEntity.getTelephone());
                        cdLineIpProxyDTO.setLzPhone(atUserTokenEntity.getTelephone());
                        //去设置区号
                        if (ObjectUtil.isNotNull(cdGroupTasksEntity.getIpCountryCode())) {
                            cdLineIpProxyDTO.setCountryCode(cdGroupTasksEntity.getIpCountryCode().longValue());
                        }
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
                            if (StrUtil.isNotEmpty(chat.getChatName())) {
                                cdGroupTasksEntity.setRealGroupName(chat.getChatName());
                            }
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
                            //拉完群重新注册出来
                            String telephone = atUserTokenEntity.getTelephone();
                            cdLineRegisterService.registerAgain(telephone);
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

        String format = String.format("and MOD(id, %s) = %s limit 50", systemConstant.getSERVERS_TOTAL_MOD(), systemConstant.getSERVERS_MOD());
        //获取当前需要同步通讯的任务
        List<AtGroupEntity> cdGroupTasksEntities = atGroupService.list(new QueryWrapper<AtGroupEntity>().lambda()
                .last(format)
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
                                    //去设置区号
                                    if (ObjectUtil.isNotNull(cdGroupTasksEntity.getIpCountryCode())) {
                                        cdLineIpProxyDTO.setCountryCode(cdGroupTasksEntity.getIpCountryCode().longValue());
                                    }
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
     * 同步通讯路成功的群
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task1() {

        //配置总机器
        String format = String.format("and MOD(id, %s) = %s limit 50", systemConstant.getSERVERS_TOTAL_MOD(), systemConstant.getSERVERS_MOD());
        //获取当前需要同步通讯的任务
        List<AtGroupEntity> cdGroupTasksEntities = atGroupService.list(new QueryWrapper<AtGroupEntity>().lambda()
                .last(format)
                .eq(AtGroupEntity::getGroupStatus,GroupStatus.GroupStatus7.getKey())
        );
        if (CollUtil.isEmpty(cdGroupTasksEntities)) {
            log.info("GroupTask task1 list isEmpty");
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
                        AtUserTokenEntity atUserTokenEntity = atUserTokenService.getByUserIdCache(cdGroupTasksEntity.getUserId());
                        if (ObjectUtil.isNull(atUserTokenEntity)) {
                            return;
                            //这里如果群同步成功删除任务队列
                        }else {
                            //任务完成，去
                            AtDataTaskEntity dataTaskVO = atDataTaskService.getOne(new QueryWrapper<AtDataTaskEntity>().lambda()
                                    .eq(AtDataTaskEntity::getGroupId,cdGroupTasksEntity.getId())
                                    .last("limit 1")
                            );
                            if (ObjectUtil.isNotNull(dataTaskVO)) {
                                int serverMod = dataTaskVO.getId() % systemConstant.getSERVERS_TOTAL_MOD();
                                Long add = stringRedisTemplate.opsForSet().remove(RedisKeys.USER_TASKS_POOL.getValue(String.valueOf(serverMod)), String.valueOf(cdGroupTasksEntity.getUserId()));
                                log.info("GroupTask add opsForSet remove = {}",add);
                            }
                        }

                        List<AtDataSubtaskEntity> atDataSubtaskEntities = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                .eq(AtDataSubtaskEntity::getGroupId,cdGroupTasksEntity.getId())
                                .eq(AtDataSubtaskEntity::getUserId,cdGroupTasksEntity.getUserId())
                                .in(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus10.getKey(),TaskStatus.TaskStatus8.getKey())
                        );

                        if (CollUtil.isEmpty(atDataSubtaskEntities)) {
                            cdGroupTasksEntity.setGroupStatus(GroupStatus.GroupStatus10.getKey());
                            cdGroupTasksEntity.setMsg(StrUtil.concat(true,cdGroupTasksEntity.getMsg(),"好友没有加成功"));
                            atGroupService.updateById(cdGroupTasksEntity);
                            return;
                        }


                        List<String> mids = atDataSubtaskEntities.stream().map(AtDataSubtaskEntity::getMid).distinct().collect(Collectors.toList());
                        AtDataTaskVO dataTaskVO = atDataTaskService.getById(atDataSubtaskEntities.get(0).getDataTaskId());
                        Integer groupType = dataTaskVO.getGroupType();

                        if (GroupType.GroupType6.getKey().equals(groupType)) {
                            //获取不是当前用户的合群user
                            AtDataSubtaskEntity atDataSubtaskEntity = atDataSubtaskService.getOne(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                    .eq(AtDataSubtaskEntity::getGroupId, cdGroupTasksEntity.getId())
                                    .notIn(AtDataSubtaskEntity::getUserId, cdGroupTasksEntity.getUserId())
                                    .in(AtDataSubtaskEntity::getTaskStatus, TaskStatus.TaskStatus10.getKey(), TaskStatus.TaskStatus8.getKey())
                                    .last("limit 1")
                            );
                            if (ObjectUtil.isNotNull(atDataSubtaskEntity)) {
                                AtUserVO atUserVO = atUserService.getById(atDataSubtaskEntity.getUserId());
                                AtUserTokenVO atUserTokenVO = atUserTokenService.getById(atUserVO.getUserTokenId());
                                if (ObjectUtil.isNotNull(atUserTokenVO)) {
                                    String token = atUserTokenVO.getToken();
                                    LineTokenJson lineTokenJson = JSON.parseObject(token, LineTokenJson.class);
                                    mids.add(lineTokenJson.getMid());
                                }
                            }
                        }

                        //获取代理
                        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
                        cdLineIpProxyDTO.setTokenPhone(atUserTokenEntity.getTelephone());
                        cdLineIpProxyDTO.setLzPhone(atUserTokenEntity.getTelephone());
                        //去设置区号
                        if (ObjectUtil.isNotNull(cdGroupTasksEntity.getIpCountryCode())) {
                            cdLineIpProxyDTO.setCountryCode(cdGroupTasksEntity.getIpCountryCode().longValue());
                        }
                        String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
                        if (StrUtil.isEmpty(proxyIp)) {
                            return;
                        }

                        CreateGroupMax createGroupMax = new CreateGroupMax();
                        createGroupMax.setUserMidList(mids);
                        createGroupMax.setProxy(proxyIp);
                        if (cdGroupTasksEntity.getRandomGroupName() != null
                                && OpenApp.OpenApp2.getKey().equals(cdGroupTasksEntity.getRandomGroupName())) {
                            createGroupMax.setGroupName(atUserTokenEntity.getNickName());
                        } else {
                            createGroupMax.setGroupName(cdGroupTasksEntity.getGroupName());
                        }
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
