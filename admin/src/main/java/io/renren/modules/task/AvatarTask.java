package io.renren.modules.task;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.client.LineService;
import io.renren.modules.client.dto.UpdateProfileImageDTO;
import io.renren.modules.client.dto.UpdateProfileImageResultDTO;
import io.renren.modules.client.vo.UpdateProfileImageResultVO;
import io.renren.modules.client.vo.UpdateProfileImageVO;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
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
public class AvatarTask {


    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtUserTokenService atUserTokenService;
    @Autowired
    private AtUserGroupService atUserGroupService;
    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;

    static ReentrantLock task1Lock = new ReentrantLock();
    static ReentrantLock task2Lock = new ReentrantLock();
    static ReentrantLock task3Lock = new ReentrantLock();
    static ReentrantLock task4Lock = new ReentrantLock();

    public static final Object atAtAvatarSubtaskObj = new Object();
    public static final Object atAtAvatarTaskEntityObj = new Object();
    @Autowired
    private LineService lineService;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private AtAvatarService atAvatarService;


    @Autowired
    private AtAvatarTaskService atAvatarTaskService;
    @Autowired
    private AtAvatarSubtaskService atAvatarSubtaskService;

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
            List<AtAvatarTaskEntity> atAvatarTaskEntities = atAvatarTaskService.list(new QueryWrapper<AtAvatarTaskEntity>().lambda()
                    .eq(AtAvatarTaskEntity::getTaskStatus, TaskStatus.TaskStatus2.getKey())
                    .last("limit 5")
            );
            if (CollUtil.isEmpty(atAvatarTaskEntities)) {
                log.info("AvatarTask task4 atAvatarTaskEntities isEmpty");
                return;
            }
            List<Integer> ids = atAvatarTaskEntities.stream().map(AtAvatarTaskEntity::getId).collect(Collectors.toList());
            //需要更换头像的任务
            List<AtAvatarSubtaskEntity> atAvatarSubtaskEntities = atAvatarSubtaskService.list(new QueryWrapper<AtAvatarSubtaskEntity>().lambda()
                    .in(AtAvatarSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus4.getKey(),TaskStatus.TaskStatus5.getKey())
                    .in(AtAvatarSubtaskEntity::getAvatarTaskId,ids)
            );
            if (CollUtil.isEmpty(atAvatarSubtaskEntities)) {
                log.info("AvatarTask task4 atAvatarSubtaskEntities isEmpty");
                return;
            }
            //获取子任务分组
            Map<Integer, List<AtAvatarSubtaskEntity>> integerListMap = atAvatarSubtaskEntities.stream().collect(Collectors.groupingBy(AtAvatarSubtaskEntity::getAvatarTaskId));

            List<AtAvatarTaskEntity> atAvatarTaskEntitiesUpdate = new ArrayList<>();
            for (AtAvatarTaskEntity atAvatarTaskEntity : atAvatarTaskEntities) {
                //获取所有子任务
                List<AtAvatarSubtaskEntity> avatarSubtaskEntities = integerListMap.get(atAvatarTaskEntity.getId());
                if (CollUtil.isEmpty(avatarSubtaskEntities)) {
                    continue;
                }
                //成功的任务
                long success = avatarSubtaskEntities.stream().filter(item -> TaskStatus.TaskStatus4.getKey().equals(item.getTaskStatus())).count();
                //失败的任务
                long fail = avatarSubtaskEntities.stream().filter(item -> TaskStatus.TaskStatus5.getKey().equals(item.getTaskStatus())).count();
                AtAvatarTaskEntity update = new AtAvatarTaskEntity();
                update.setSuccessfulQuantity((int) success);
                update.setFailuresQuantity((int) fail);
                update.setId(atAvatarTaskEntity.getId());
                if (success + fail == atAvatarTaskEntity.getExecutionQuantity()) {
                    update.setTaskStatus(TaskStatus.TaskStatus3.getKey());
                }
                atAvatarTaskEntitiesUpdate.add(update);
            }
            synchronized (atAtAvatarTaskEntityObj) {
                atAvatarTaskService.updateBatchById(atAvatarTaskEntitiesUpdate);
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
            //需要更换头像的任务
            List<AtAvatarSubtaskEntity> atAvatarSubtaskEntities = atAvatarSubtaskService.list(new QueryWrapper<AtAvatarSubtaskEntity>().lambda()
                    .eq(AtAvatarSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus6.getKey())
                    .last("limit 5")
            );
            if (CollUtil.isEmpty(atAvatarSubtaskEntities)) {
                log.info("AvatarTask task3 atAvatarSubtaskEntities isEmpty");
                return;
            }

            List<AtAvatarSubtaskEntity> atAvatarSubtaskEntitiesUpdate = new ArrayList<>();

            final CountDownLatch latch = new CountDownLatch(atAvatarSubtaskEntities.size());
            for (AtAvatarSubtaskEntity atAvatarSubtaskEntity : atAvatarSubtaskEntities) {
                threadPoolTaskExecutor.submit(new Thread(()->{
                    if (StrUtil.isEmpty(atAvatarSubtaskEntity.getLineTaskId())) {
                        latch.countDown();
                        return;
                    }
                    UpdateProfileImageResultDTO updateProfileImageResultDTO = new UpdateProfileImageResultDTO();
                    updateProfileImageResultDTO.setTaskId(atAvatarSubtaskEntity.getLineTaskId());
                    UpdateProfileImageResultVO updateProfileImageResult = lineService.updateProfileImageResult(updateProfileImageResultDTO);
                    AtAvatarSubtaskEntity update = new AtAvatarSubtaskEntity();
                    if (ObjectUtil.isNotNull(updateProfileImageResult)) {
                        if (200 == updateProfileImageResult.getCode()) {
                            update.setId(atAvatarSubtaskEntity.getId());
                            update.setMsg(updateProfileImageResult.getMsg() + updateProfileImageResult.getData().getRemark());
                            if (2 == updateProfileImageResult.getData().getStatus()) {
                                update.setTaskStatus(TaskStatus.TaskStatus4.getKey());
                                atAvatarSubtaskEntitiesUpdate.add(update);
                            }else if (-2 == updateProfileImageResult.getData().getStatus()) {
                                update.setTaskStatus(TaskStatus.TaskStatus2.getKey());
                                atAvatarSubtaskEntitiesUpdate.add(update);
                            }else if (-1 == updateProfileImageResult.getData().getStatus()) {
                                update.setTaskStatus(TaskStatus.TaskStatus5.getKey());
                                atAvatarSubtaskEntitiesUpdate.add(update);
                            }
                        }
                    }
                    latch.countDown();
                }));
            }
            latch.await();
            synchronized (atAtAvatarSubtaskObj) {
                atAvatarSubtaskService.updateBatchById(atAvatarSubtaskEntitiesUpdate);
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
            //需要更换头像的任务
            List<AtAvatarSubtaskEntity> atAvatarSubtaskEntities = atAvatarSubtaskService.list(new QueryWrapper<AtAvatarSubtaskEntity>().lambda()
                    .eq(AtAvatarSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus2.getKey())
                    .last("limit 5")
            );
            if (CollUtil.isEmpty(atAvatarSubtaskEntities)) {
                log.info("AvatarTask task2 atAvatarSubtaskEntities isEmpty");
                return;
            }
            //获取用户MAP
            List<Integer> userIds = atAvatarSubtaskEntities.stream().map(AtAvatarSubtaskEntity::getUserId).collect(Collectors.toList());
            List<AtUserEntity> atUserEntities = atUserService.listByIds(userIds);
            List<Integer> userTokenIds = atUserEntities.stream().map(AtUserEntity::getUserTokenId).collect(Collectors.toList());
            List<AtUserTokenEntity> tokenEntities = atUserTokenService.listByIds(userTokenIds);
            Map<Integer, AtUserTokenEntity> atUserTokenEntityMap = tokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item));
            Map<Integer, AtUserTokenEntity> userIdAtUserTokenEntityMap = atUserEntities.stream().collect(Collectors.toMap(AtUserEntity::getId, item -> atUserTokenEntityMap.get(item.getUserTokenId()).setTelephone(item.getTelephone())));
            //获取头像MAP
            List<Integer> avatarIds = atAvatarSubtaskEntities.stream().map(AtAvatarSubtaskEntity::getAvatarId).collect(Collectors.toList());
            List<AtAvatarEntity> atAvatarEntities = atAvatarService.listByIds(avatarIds);
            Map<Integer, AtAvatarEntity> atAvatarEntityMap = atAvatarEntities.stream().collect(Collectors.toMap(AtAvatarEntity::getId, item -> item));
            List<AtAvatarSubtaskEntity> atAvatarSubtaskEntitiesUpdate = new ArrayList<>();

            final CountDownLatch latch = new CountDownLatch(atAvatarSubtaskEntities.size());
            for (AtAvatarSubtaskEntity atAvatarSubtaskEntity : atAvatarSubtaskEntities) {
                threadPoolTaskExecutor.submit(new Thread(()->{
                    //获取用户token
                    AtUserTokenEntity atUserTokenEntity = userIdAtUserTokenEntityMap.get(atAvatarSubtaskEntity.getUserId());
                    if (ObjectUtil.isNull(atUserTokenEntity)) {
                        latch.countDown();
                        return;
                    }
                    AtAvatarEntity atAvatarEntity = atAvatarEntityMap.get(atAvatarSubtaskEntity.getAvatarId());
                    if (ObjectUtil.isNull(atAvatarEntity)) {
                        latch.countDown();
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
                    String avatar = atAvatarEntity.getAvatar();
                    if (StrUtil.isNotEmpty(avatar)) {
                        FastByteArrayOutputStream fastByteArrayOutputStream = new FastByteArrayOutputStream();
                        long download = HttpUtil.download(avatar, fastByteArrayOutputStream, true);
                        String linkBase64 = Base64.encode(fastByteArrayOutputStream.toByteArray());
                        UpdateProfileImageDTO updateProfileImageDTO = new UpdateProfileImageDTO();
                        updateProfileImageDTO.setProxy(proxyIp);
                        updateProfileImageDTO.setImage_base_64(linkBase64);
                        updateProfileImageDTO.setToken(atUserTokenEntity.getToken());
                        UpdateProfileImageVO updateProfileImageVO = lineService.updateProfileImage(updateProfileImageDTO);
                        if (ObjectUtil.isNotNull(updateProfileImageVO)) {
                            if (200 == updateProfileImageVO.getCode()) {
                                AtAvatarSubtaskEntity update = new AtAvatarSubtaskEntity();
                                update.setId(atAvatarSubtaskEntity.getId());
                                update.setTaskStatus(TaskStatus.TaskStatus6.getKey());
                                update.setLineTaskId(updateProfileImageVO.getData().getTaskId());
                                atAvatarSubtaskEntitiesUpdate.add(update);
                            }
                        }
                    }
                    latch.countDown();
                }));
            }
            latch.await();
            synchronized (atAtAvatarSubtaskObj) {
                atAvatarSubtaskService.updateBatchById(atAvatarSubtaskEntitiesUpdate);
            }
        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task2Lock.unlock();
        }
    }

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
            List<AtAvatarTaskEntity> atAvatarTaskEntities = atAvatarTaskService.list(new QueryWrapper<AtAvatarTaskEntity>().lambda()
                    .eq(AtAvatarTaskEntity::getTaskStatus, TaskStatus.TaskStatus1.getKey())
                    .last("limit 5")
            );
            if (CollUtil.isEmpty(atAvatarTaskEntities)) {
                log.info("AvatarTask task1 atAvatarTaskEntities isEmpty");
                return;
            }
            List<Integer> ids = atAvatarTaskEntities.stream().map(AtAvatarTaskEntity::getId).collect(Collectors.toList());
            //需要更换头像的任务
            List<AtAvatarSubtaskEntity> atAvatarSubtaskEntities = atAvatarSubtaskService.list(new QueryWrapper<AtAvatarSubtaskEntity>().lambda()
                    .eq(AtAvatarSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus1.getKey())
                    .in(AtAvatarSubtaskEntity::getAvatarTaskId,ids)
                    .last("limit 5")
            );
            if (CollUtil.isEmpty(atAvatarSubtaskEntities)) {
                log.info("AvatarTask task1 atAvatarSubtaskEntities isEmpty");

                List<AtAvatarTaskEntity> atAvatarTaskEntitiesUpdate = new ArrayList<>();
                for (AtAvatarTaskEntity atAvatarTaskEntity : atAvatarTaskEntities) {
                    AtAvatarTaskEntity update = new AtAvatarTaskEntity();
                    update.setId(atAvatarTaskEntity.getId());
                    update.setTaskStatus(TaskStatus.TaskStatus2.getKey());
                    atAvatarTaskEntitiesUpdate.add(update);
                }
                synchronized (atAtAvatarTaskEntityObj) {
                    atAvatarTaskService.updateBatchById(atAvatarTaskEntitiesUpdate);
                }
                return;
            }

            List<AtAvatarSubtaskEntity> updates = new ArrayList<>();
            for (AtAvatarSubtaskEntity atAvatarSubtaskEntity : atAvatarSubtaskEntities) {
                AtAvatarSubtaskEntity update = new AtAvatarSubtaskEntity();
                update.setId(atAvatarSubtaskEntity.getId());
                update.setTaskStatus(TaskStatus.TaskStatus2.getKey());
                updates.add(update);
            }
            synchronized (atAtAvatarSubtaskObj) {
                atAvatarSubtaskService.updateBatchById(updates);
            }

        }catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }finally {
            task1Lock.unlock();
        }

    }
}
