package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.AddFriendsByHomeRecommendDTO;
import io.renren.modules.client.vo.SearchPhoneVO;
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
public class Data3Task {


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
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private AtAvatarService atAvatarService;
    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;
    @Autowired
    private ConcurrentHashMap<String, Lock> lockMap;
    @Autowired
    private AtDataTaskService atDataTaskService;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;
    @Autowired
    private AtGroupService atGroupService;


//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task4() {}



//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task3() {}


    /**
     * 获取type为1 加粉类型为手机号模式
     */
    @Scheduled(fixedDelay = 5000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task2() {

        AtDataSubtaskEntity dto = new AtDataSubtaskEntity().setTaskStatus(TaskStatus.TaskStatus2.getKey()).setGroupType(GroupType.GroupType3.getKey());
        List<AtDataSubtaskVO> atDataSubtaskEntities = atDataSubtaskService.groupByUserId(dto);

        //任务为空
        if (CollUtil.isEmpty(atDataSubtaskEntities)) {
            log.info("Data3Task task2 atDataSubtaskEntities isEmpty");
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
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource7, atDataSubtaskEntity.getId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("keyByResource = {} 获取的锁为 = {}",keyByResource,triedLock);
                if(triedLock) {
                    try{
                        String mid = atDataSubtaskEntity.getMids();
                        if (StrUtil.isEmpty(mid)) {
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

                        AddFriendsByHomeRecommendDTO addFriendsByHomeRecommendDTO = new AddFriendsByHomeRecommendDTO();
                        addFriendsByHomeRecommendDTO.setProxy(proxyIp);
                        addFriendsByHomeRecommendDTO.setMid(mid);
                        addFriendsByHomeRecommendDTO.setToken(atUserTokenEntity.getToken());

                        SearchPhoneVO searchPhoneVO = lineService.addFriendsBySearchV3(addFriendsByHomeRecommendDTO);
                        AtDataSubtaskEntity update = new AtDataSubtaskEntity();
                        if (ObjectUtil.isNull(searchPhoneVO)) {
                            return;
                        }
                        update.setMsg(searchPhoneVO.getMsg());
                        if (200 == searchPhoneVO.getCode()) {
                            update.setTaskStatus(TaskStatus.TaskStatus8.getKey());
                        }else {
                            update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                            UserStatus userStatus = UserStatus.UserStatus4;
                            //需要刷新token
                            if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode4.getValue())) {
                                userStatus = UserStatus.UserStatus3;
                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode6.getValue())) {
                                userStatus = UserStatus.UserStatus3;
                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode7.getValue())) {
                                userStatus = UserStatus.UserStatus2;
                            } else if (searchPhoneVO.getMsg().contains(UserStatusCode.UserStatusCode8.getValue())) {
                                userStatus = UserStatus.UserStatus2;
                            }

                            //如果失败，修改状态
                            update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
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

                            update.setId(null);
                            atDataSubtaskService.update(update,new QueryWrapper<AtDataSubtaskEntity>().lambda()
                                    .eq(AtDataSubtaskEntity::getDataTaskId,atDataSubtaskEntity.getDataTaskId())
                                    .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus2.getKey())
                            );
                            atUserService.unlock(atDataSubtaskEntity.getUserId(),userStatus);
                        }
                        update.setId(atDataSubtaskEntity.getId());
                        atDataSubtaskService.updateById(update);
                    }finally {
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