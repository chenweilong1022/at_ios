package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.client.LineService;
import io.renren.modules.client.ProxyService;
import io.renren.modules.client.dto.IssueLiffViewDTO;
import io.renren.modules.client.dto.LineTokenJson;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.entity.AtUserGroupEntity;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.enums.UserStatus;
import io.renren.modules.ltt.enums.UserStatusCode;
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

    public static final Object atUserlockObj = new Object();
    public static final Object atUserTokenlockObj = new Object();
    @Autowired
    private LineService lineService;
    @Autowired
    private ProxyService proxyService;

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;


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
