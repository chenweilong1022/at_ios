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
import io.renren.modules.client.dto.UpdateProfileImageDTO;
import io.renren.modules.client.dto.UpdateProfileImageResultDTO;
import io.renren.modules.client.dto.UpdateProfileNameDTO;
import io.renren.modules.client.vo.UpdateProfileImageResultVO;
import io.renren.modules.client.vo.UpdateProfileImageVO;
import io.renren.modules.client.vo.UpdateProfileNameVO;
import io.renren.modules.ltt.entity.*;
import io.renren.modules.ltt.enums.TaskStatus;
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
public class UsernameTask {


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

    public static final Object atAtUsernameSubtaskObj = new Object();
    public static final Object atAtUsernameTaskEntityObj = new Object();
    @Autowired
    private LineService lineService;
    @Autowired
    private ProxyService proxyService;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private AtUsernameService atUsernameService;

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
            List<AtUsernameTaskEntity> atUsernameTaskEntities = atUsernameTaskService.list(new QueryWrapper<AtUsernameTaskEntity>().lambda()
                    .eq(AtUsernameTaskEntity::getTaskStatus, TaskStatus.TaskStatus2.getKey())
                    .last("limit 5")
            );
            if (CollUtil.isEmpty(atUsernameTaskEntities)) {
                log.info("AvatarTask task4 atUsernameTaskEntities isEmpty");
                return;
            }
            List<Integer> ids = atUsernameTaskEntities.stream().map(AtUsernameTaskEntity::getId).collect(Collectors.toList());
            //需要更换头像的任务
            List<AtUsernameSubtaskEntity> atUsernameSubtaskEntities = atUsernameSubtaskService.list(new QueryWrapper<AtUsernameSubtaskEntity>().lambda()
                    .in(AtUsernameSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus4.getKey(),TaskStatus.TaskStatus5.getKey())
                    .in(AtUsernameSubtaskEntity::getUsernameTaskId,ids)
            );
            if (CollUtil.isEmpty(atUsernameSubtaskEntities)) {
                log.info("AvatarTask task4 atUsernameSubtaskEntities isEmpty");
                return;
            }
            //获取子任务分组
            Map<Integer, List<AtUsernameSubtaskEntity>> integerListMap = atUsernameSubtaskEntities.stream().collect(Collectors.groupingBy(AtUsernameSubtaskEntity::getUsernameTaskId));

            List<AtUsernameTaskEntity> atUsernameTaskEntitiesUpdate = new ArrayList<>();
            for (AtUsernameTaskEntity atUsernameTaskEntity : atUsernameTaskEntities) {
                //获取所有子任务
                List<AtUsernameSubtaskEntity> usernameSubtaskEntities = integerListMap.get(atUsernameTaskEntity.getId());
                if (CollUtil.isEmpty(usernameSubtaskEntities)) {
                    continue;
                }
                //成功的任务
                long success = usernameSubtaskEntities.stream().filter(item -> TaskStatus.TaskStatus4.getKey().equals(item.getTaskStatus())).count();
                //失败的任务
                long fail = usernameSubtaskEntities.stream().filter(item -> TaskStatus.TaskStatus5.getKey().equals(item.getTaskStatus())).count();
                AtUsernameTaskEntity update = new AtUsernameTaskEntity();
                update.setSuccessfulQuantity((int) success);
                update.setFailuresQuantity((int) fail);
                update.setId(atUsernameTaskEntity.getId());
                if (success + fail == atUsernameTaskEntity.getExecutionQuantity()) {
                    update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                }
                atUsernameTaskEntitiesUpdate.add(update);
            }
            synchronized (atAtUsernameTaskEntityObj) {
                atUsernameTaskService.updateBatchById(atUsernameTaskEntitiesUpdate);
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task4Lock.unlock();
        }
    }

//
//    /**
//     * 更新头像结果返回
//     */
//    @Scheduled(fixedDelay = 5000)
//    @Transactional(rollbackFor = Exception.class)
//    @Async
//    public void task3() {
//        boolean b = task3Lock.tryLock();
//        if (!b) {
//            return;
//        }
//        try {
//            //需要更换头像的任务
//            List<AtUsernameSubtaskEntity> atAvatarSubtaskEntities = atAvatarSubtaskService.list(new QueryWrapper<AtUsernameSubtaskEntity>().lambda()
//                    .eq(AtUsernameSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus6.getKey())
//                    .last("limit 5")
//            );
//            if (CollUtil.isEmpty(atAvatarSubtaskEntities)) {
//                log.info("AvatarTask task3 atAvatarSubtaskEntities isEmpty");
//                return;
//            }
//
//            List<AtUsernameSubtaskEntity> atAvatarSubtaskEntitiesUpdate = new ArrayList<>();
//
//            final CountDownLatch latch = new CountDownLatch(atAvatarSubtaskEntities.size());
//            for (AtUsernameSubtaskEntity atAvatarSubtaskEntity : atAvatarSubtaskEntities) {
//                threadPoolTaskExecutor.submit(new Thread(()->{
//                    if (StrUtil.isEmpty(atAvatarSubtaskEntity.getLineTaskId())) {
//                        latch.countDown();
//                        return;
//                    }
//                    UpdateProfileImageResultDTO updateProfileImageResultDTO = new UpdateProfileImageResultDTO();
//                    updateProfileImageResultDTO.setTaskId(atAvatarSubtaskEntity.getLineTaskId());
//                    UpdateProfileImageResultVO updateProfileImageResult = lineService.updateProfileImageResult(updateProfileImageResultDTO);
//                    AtUsernameSubtaskEntity update = new AtUsernameSubtaskEntity();
//                    if (ObjectUtil.isNotNull(updateProfileImageResult)) {
//                        if (200 == updateProfileImageResult.getCode()) {
//                            update.setId(atAvatarSubtaskEntity.getId());
//                            update.setMsg(updateProfileImageResult.getMsg() + updateProfileImageResult.getData().getRemark());
//                            if (2 == updateProfileImageResult.getData().getStatus()) {
//                                update.setTaskStatus(TaskStatus.TaskStatus4.getKey());
//                                atAvatarSubtaskEntitiesUpdate.add(update);
//                            }else if (-2 == updateProfileImageResult.getData().getStatus()) {
//                                update.setTaskStatus(TaskStatus.TaskStatus2.getKey());
//                                atAvatarSubtaskEntitiesUpdate.add(update);
//                            }else if (-1 == updateProfileImageResult.getData().getStatus()) {
//                                update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
//                                atAvatarSubtaskEntitiesUpdate.add(update);
//                            }
//                        }
//                    }
//                    latch.countDown();
//                }));
//            }
//            latch.await();
//            synchronized (atAtAvatarSubtaskObj) {
//                atAvatarSubtaskService.updateBatchById(atAvatarSubtaskEntitiesUpdate);
//            }
//
//        }catch (Exception e) {
//            log.error("err = {}",e.getMessage());
//        }finally {
//            task3Lock.unlock();
//        }
//    }
//

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
            //需要更换头像的任务
            List<AtUsernameSubtaskEntity> atUsernameSubtaskEntities = atUsernameSubtaskService.list(new QueryWrapper<AtUsernameSubtaskEntity>().lambda()
                    .eq(AtUsernameSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus2.getKey())
                    .last("limit 5")
            );
            if (CollUtil.isEmpty(atUsernameSubtaskEntities)) {
                log.info("AvatarTask task2 atUsernameSubtaskEntities isEmpty");
                return;
            }
            //获取用户MAP
            List<Integer> userIds = atUsernameSubtaskEntities.stream().map(AtUsernameSubtaskEntity::getUserId).collect(Collectors.toList());
            List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
            List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
            List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
            Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
            Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId())));
            //获取头像MAP
            List<Integer> usernameIds = atUsernameSubtaskEntities.stream().map(AtUsernameSubtaskEntity::getUsernameId).collect(Collectors.toList());
            List<AtUsernameEntity> atUsernameEntities = atUsernameService.listByIds(usernameIds);
            Map<Integer, AtUsernameEntity> atUsernameEntityMap = atUsernameEntities.stream().collect(Collectors.toMap(AtUsernameEntity::getId, item -> item));
            List<AtUsernameSubtaskEntity> atUsernameSubtaskEntitiesUpdate = new ArrayList<>();

            final CountDownLatch latch = new CountDownLatch(atUsernameSubtaskEntities.size());
            for (AtUsernameSubtaskEntity atUsernameSubtaskEntity : atUsernameSubtaskEntities) {
                threadPoolTaskExecutor.submit(new Thread(()->{
                    //获取用户token
                    AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(atUsernameSubtaskEntity.getUserId());
                    if (ObjectUtil.isNull(atUserTokenEntity)) {
                        latch.countDown();
                        return;
                    }
                    AtUsernameEntity atUsernameEntity = atUsernameEntityMap.get(atUsernameSubtaskEntity.getUsernameId());
                    if (ObjectUtil.isNull(atUsernameEntity)) {
                        latch.countDown();
                        return;
                    }
                    String getflowip = proxyService.getflowip();
                    if (StrUtil.isEmpty(getflowip)) {
                        latch.countDown();
                        return;
                    }
                    String username = atUsernameEntity.getUsername();
                    if (StrUtil.isNotEmpty(username)) {
                        UpdateProfileNameDTO updateProfileNameDTO = new UpdateProfileNameDTO();
                        updateProfileNameDTO.setProxy(getflowip);
                        updateProfileNameDTO.setName(username);
                        updateProfileNameDTO.setToken(atUserTokenEntity.getToken());
                        UpdateProfileNameVO updateProfileNameVO = lineService.updateProfileName(updateProfileNameDTO);
                        if (ObjectUtil.isNull(updateProfileNameVO)) {
                            latch.countDown();
                            return;
                        }
                        AtUsernameSubtaskEntity update = new AtUsernameSubtaskEntity();
                        update.setId(atUsernameSubtaskEntity.getId());
                        update.setMsg(updateProfileNameVO.getMsg());
                        if (200 == updateProfileNameVO.getCode()) {
                            update.setTaskStatus(TaskStatus.TaskStatus4.getKey());
                        }else {
                            update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                        }
                        atUsernameSubtaskEntitiesUpdate.add(update);
                    }
                    latch.countDown();
                }));
            }
            latch.await();
            synchronized (atAtUsernameSubtaskObj) {
                atUsernameSubtaskService.updateBatchById(atUsernameSubtaskEntitiesUpdate);
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task2Lock.unlock();
        }
    }


    @Autowired
    private AtUsernameTaskService atUsernameTaskService;
    @Autowired
    private AtUsernameSubtaskService atUsernameSubtaskService;

    /**
     * 获取初始化的修改图片任务
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
            //需要更换头像的子任务
            List<AtUsernameTaskEntity> atUsernameTaskEntities = atUsernameTaskService.list(new QueryWrapper<AtUsernameTaskEntity>().lambda()
                    .eq(AtUsernameTaskEntity::getTaskStatus, TaskStatus.TaskStatus1.getKey())
                    .last("limit 5")
            );
            if (CollUtil.isEmpty(atUsernameTaskEntities)) {
                log.info("UsernameTask task1 atUsernameTaskEntities isEmpty");
                return;
            }
            List<Integer> ids = atUsernameTaskEntities.stream().map(AtUsernameTaskEntity::getId).collect(Collectors.toList());
            //需要更换头像的任务
            List<AtUsernameSubtaskEntity> atUsernameSubtaskEntities = atUsernameSubtaskService.list(new QueryWrapper<AtUsernameSubtaskEntity>().lambda()
                    .eq(AtUsernameSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus1.getKey())
                    .in(AtUsernameSubtaskEntity::getUsernameTaskId,ids)
                    .last("limit 5")
            );
            if (CollUtil.isEmpty(atUsernameSubtaskEntities)) {
                log.info("UsernameTask task1 atUsernameSubtaskEntities isEmpty");

                List<AtUsernameTaskEntity> atUsernameTaskEntitiesUpdate = new ArrayList<>();
                for (AtUsernameTaskEntity atUsernameTaskEntity : atUsernameTaskEntities) {
                    AtUsernameTaskEntity update = new AtUsernameTaskEntity();
                    update.setId(atUsernameTaskEntity.getId());
                    update.setTaskStatus(TaskStatus.TaskStatus2.getKey());
                    atUsernameTaskEntitiesUpdate.add(update);
                }
                synchronized (atAtUsernameTaskEntityObj) {
                    atUsernameTaskService.updateBatchById(atUsernameTaskEntitiesUpdate);
                }
                return;
            }

            List<AtUsernameSubtaskEntity> updates = new ArrayList<>();
            for (AtUsernameSubtaskEntity atUsernameSubtaskEntity : atUsernameSubtaskEntities) {
                AtUsernameSubtaskEntity update = new AtUsernameSubtaskEntity();
                update.setId(atUsernameSubtaskEntity.getId());
                update.setTaskStatus(TaskStatus.TaskStatus2.getKey());
                updates.add(update);
            }
            synchronized (atAtUsernameSubtaskObj) {
                atUsernameSubtaskService.updateBatchById(updates);
            }

        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task1Lock.unlock();
        }

    }
}
