package io.renren.modules.task;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.modules.ltt.entity.AtOrdersEntity;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.AtOrdersService;
import io.renren.modules.ltt.service.AtUserTokenService;
import io.renren.modules.ltt.service.CdLineRegisterService;
import io.renren.modules.ltt.vo.CdLineRegisterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/2 00:45
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private AtOrdersService atOrdersService;
    @Autowired
    private CdLineRegisterService cdLineRegisterService;

    @Autowired
    private AtUserTokenService atUserTokenService;
    @Autowired
    private ConcurrentHashMap<String, Lock> lockMap;

    @Resource(name = "poolExecutor")
    private ThreadPoolTaskExecutor poolExecutor;

    /**
     * token订单处理
     */
    @Scheduled(fixedDelay = 1000*1000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void tokenOrderRegister() {
        List<AtOrdersEntity> list = atOrdersService.list(new QueryWrapper<AtOrdersEntity>()
                .lambda().in(AtOrdersEntity::getOrderStatus,
                        Arrays.asList(OrderStatus.OrderStatus1.getKey(), OrderStatus.OrderStatus2.getKey()))
                .eq(AtOrdersEntity::getProductType, ProductTypeEnum.TOKEN.getKey()).last("limit 10"));
        if (CollUtil.isEmpty(list)) {
            log.info("tokenOrderRegister list isEmpty");
            return;
        }
        for (AtOrdersEntity atOrdersEntity : list) {
            poolExecutor.execute(() -> {
                String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource6, atOrdersEntity.getOrderId());
                Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());
                boolean triedLock = lock.tryLock();
                log.info("tokenOrderRegister keyByResource = {} 获取的锁为 = {}", keyByResource, triedLock);
                if (triedLock) {
                    try {
                        Integer orderNumber = atOrdersEntity.getOrderNumber() != null ? atOrdersEntity.getOrderNumber() : 0;
                        Integer successNumber = atOrdersEntity.getSuccessNumber() != null ? atOrdersEntity.getSuccessNumber() : 0;
                        Integer pendingCount = orderNumber - successNumber;//待处理数量

                        if (pendingCount <= 0) {
                            //订单已处理完成,更改订单状态
                            this.updateAtOrder(atOrdersEntity, 0);
                            return;
                        }

                        //查询符合条件的数据
                        List<CdLineRegisterVO> registerList = cdLineRegisterService.getListByRegisterStatus(RegisterStatus.RegisterStatus4.getKey(), atOrdersEntity.getCountryCode(), pendingCount);
                        if (CollUtil.isEmpty(registerList)) {
                            log.info("tokenOrderRegister registerList = {}", registerList.size());
                            return;
                        }

                        //插入AtUserTokenEntity
                        this.handleRegisterData(registerList, atOrdersEntity.getSysUserId());

                        //更新订单表
                        this.updateAtOrder(atOrdersEntity, registerList.size());
                    } finally {
                        lock.unlock();
                    }
                } else {
                    log.info("keyByResource = {} 在执行", keyByResource);
                }
            });
        }

    }

    private void updateAtOrder(AtOrdersEntity atOrdersEntity, Integer successNumber) {
        AtOrdersEntity updateOrdersEntity = new AtOrdersEntity();
        updateOrdersEntity.setOrderId(atOrdersEntity.getOrderId());
        updateOrdersEntity.setSuccessNumber(atOrdersEntity.getSuccessNumber() + successNumber);
        updateOrdersEntity.setUpdateTime(new Date());
        if (updateOrdersEntity.getSuccessNumber() >= atOrdersEntity.getOrderNumber()) {
            //订单完成
            updateOrdersEntity.setOrderStatus(OrderStatus.OrderStatus3.getKey());
        } else {
            //订单继续处理
            updateOrdersEntity.setOrderStatus(OrderStatus.OrderStatus2.getKey());
        }
        atOrdersService.updateById(updateOrdersEntity);
    }


    private void handleRegisterData(List<CdLineRegisterVO> registerList, Long sysUserId) {
        AtUserTokenEntity userTokenEntity = null;
        List<AtUserTokenEntity> userTokenEntityList = new ArrayList<>(registerList.size());

        CdLineRegisterEntity updateLineRegisterDto = null;
        List<CdLineRegisterEntity> updateLineRegisterList = new ArrayList<>(registerList.size());

        for (CdLineRegisterVO cdLineRegisterVO : registerList) {
            //生成token
            userTokenEntity = new AtUserTokenEntity();
            userTokenEntity.setToken(cdLineRegisterVO.getToken());
            userTokenEntity.setUseFlag(UseFlag.NO.getKey());
            userTokenEntity.setDeleteFlag(DeleteFlag.NO.getKey());
            userTokenEntity.setCreateTime(new Date());
            userTokenEntity.setPlatform(Platform.IOS.getKey());
            userTokenEntity.setUserGroupId(null);
            userTokenEntity.setSysUserId(sysUserId);
            userTokenEntityList.add(userTokenEntity);

            //修改
            updateLineRegisterDto = new CdLineRegisterEntity();
            updateLineRegisterDto.setId(cdLineRegisterVO.getId());
            updateLineRegisterDto.setRegisterStatus(RegisterStatus.RegisterStatus11.getKey());
            updateLineRegisterList.add(updateLineRegisterDto);
        }
        atUserTokenService.saveBatch(userTokenEntityList);
        cdLineRegisterService.updateBatchById(updateLineRegisterList);
    }

}
