package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.dto.IosTokenDTO;
import io.renren.modules.ltt.entity.CdRegisterTaskEntity;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.service.CdRegisterTaskService;
import io.renren.modules.ltt.vo.IOSTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUserTokenIosDao;
import io.renren.modules.ltt.entity.AtUserTokenIosEntity;
import io.renren.modules.ltt.dto.AtUserTokenIosDTO;
import io.renren.modules.ltt.vo.AtUserTokenIosVO;
import io.renren.modules.ltt.service.AtUserTokenIosService;
import io.renren.modules.ltt.conver.AtUserTokenIosConver;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;


@Service("atUserTokenIosService")
@Game
@Slf4j
public class AtUserTokenIosServiceImpl extends ServiceImpl<AtUserTokenIosDao, AtUserTokenIosEntity> implements AtUserTokenIosService {

    @Autowired
    private CdRegisterTaskService cdRegisterTaskService;

    @Override
    public PageUtils<AtUserTokenIosVO> queryPage(AtUserTokenIosDTO atUserTokenIos) {
        IPage<AtUserTokenIosEntity> page = baseMapper.selectPage(
                new Query<AtUserTokenIosEntity>(atUserTokenIos).getPage(),
                new QueryWrapper<AtUserTokenIosEntity>().lambda()
                        .eq(AtUserTokenIosEntity::getDeleteFlag, DeleteFlag.NO.getKey())
                        .eq(StringUtils.isNotEmpty(atUserTokenIos.getCountry()), AtUserTokenIosEntity::getCountry, atUserTokenIos.getCountry())
                        .eq(StringUtils.isNotEmpty(atUserTokenIos.getUserName()), AtUserTokenIosEntity::getUserName, atUserTokenIos.getUserName())
                        .eq(ObjectUtil.isNotNull(atUserTokenIos.getReductionFlag()), AtUserTokenIosEntity::getReductionFlag, atUserTokenIos.getReductionFlag())
                        .orderByDesc(AtUserTokenIosEntity::getId)
        );

        return PageUtils.<AtUserTokenIosVO>page(page).setList(AtUserTokenIosConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtUserTokenIosVO getById(Integer id) {
        return AtUserTokenIosConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtUserTokenIosDTO atUserTokenIos) {
        AtUserTokenIosEntity atUserTokenIosEntity = AtUserTokenIosConver.MAPPER.converDTO(atUserTokenIos);
        return this.save(atUserTokenIosEntity);
    }

    @Override
    public boolean updateById(AtUserTokenIosDTO atUserTokenIos) {
        AtUserTokenIosEntity atUserTokenIosEntity = AtUserTokenIosConver.MAPPER.converDTO(atUserTokenIos);
        return this.updateById(atUserTokenIosEntity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncAppToken(IosTokenDTO map) {
        AtUserTokenIosEntity atUserTokenIosEntity = new AtUserTokenIosEntity();
        atUserTokenIosEntity.setCountry(map.getCountry());
        atUserTokenIosEntity.setBundleId(map.getBundleId());
        atUserTokenIosEntity.setAppUserId(map.getAppUserId());
        atUserTokenIosEntity.setUserName(map.getUserName());
        String phoneNumber = StrUtil.cleanBlank(map.getToken().getPhoneNumber());
        atUserTokenIosEntity.setPhoneNumber(phoneNumber);
        atUserTokenIosEntity.setMid(map.getToken().getMid());
        atUserTokenIosEntity.setIosToken(JSONUtil.toJsonStr(map.getToken()));
        atUserTokenIosEntity.setUseFlag(UseFlag.NO.getKey());
        atUserTokenIosEntity.setDeleteFlag(DeleteFlag.NO.getKey());
        atUserTokenIosEntity.setCreateTime(DateUtil.date());
        atUserTokenIosEntity.setDeviceId(map.getDeviceId());
        AtUserTokenIosEntity one = this.getOne(new QueryWrapper<AtUserTokenIosEntity>().lambda()
                .eq(AtUserTokenIosEntity::getPhoneNumber,phoneNumber)
                .last("limit 1")
        );
        //获取注册任务id，当无任务，或者任务已完成时，返回空
        Integer taskId = this.getRegisterTaskId();
        if (ObjectUtil.isNull(taskId)) {
            log.warn("syncAppToken 真机注册任务已完成");
            return;
        }
        atUserTokenIosEntity.setTaskId(taskId);

        if (ObjectUtil.isNull(one)) {
            this.save(atUserTokenIosEntity);
        }else {
            atUserTokenIosEntity.setId(one.getId());
            this.updateById(atUserTokenIosEntity);
        }

    }

    /**
     * 获取注册任务id
     * @return 当无任务，或者任务已完成时，返回空
     */
    private Integer getRegisterTaskId() {
        CdRegisterTaskEntity registerTaskEntity = cdRegisterTaskService.queryRealMachineRegister();
        if (ObjectUtil.isNull(registerTaskEntity)) {
            return null;
        }
        Integer registerCount = baseMapper.selectCount(new QueryWrapper<AtUserTokenIosEntity>().lambda()
                .eq(AtUserTokenIosEntity::getTaskId, registerTaskEntity.getId()));
        if (registerCount >= registerTaskEntity.getTotalAmount()) {
            cdRegisterTaskService.removeByIds(Collections.singletonList(registerTaskEntity.getId()));
            return null;
        }
        return registerTaskEntity.getId();
    }

    @Resource(name = "stringQueueCacheIOSTaskVO")
    private Cache<String, Queue<IOSTaskVO>> stringQueueCacheIOSTaskVO;

    @Override
    public void taskIosFind(Integer[] ids) {
        for (Integer id : ids) {

            AtUserTokenIosEntity atUserTokenIosEntity = this.getById((Serializable) id);
            IOSTaskVO iosTaskVO = new IOSTaskVO();
            iosTaskVO.setTaskType("find");
            iosTaskVO.setPhone(atUserTokenIosEntity.getPhoneNumber().replace("+",""));

            String deviceId = atUserTokenIosEntity.getDeviceId();
            Queue<IOSTaskVO> cacheIOSTaskVOIfPresent = stringQueueCacheIOSTaskVO.getIfPresent(deviceId);
            if (CollUtil.isEmpty(cacheIOSTaskVOIfPresent) || cacheIOSTaskVOIfPresent.isEmpty()) {
                cacheIOSTaskVOIfPresent = new LinkedList<>();
            }
            cacheIOSTaskVOIfPresent.offer(iosTaskVO);
            stringQueueCacheIOSTaskVO.put(deviceId,cacheIOSTaskVOIfPresent);

            //更新状态为已还原
            AtUserTokenIosEntity updateEntity = new AtUserTokenIosEntity();
            updateEntity.setId(atUserTokenIosEntity.getId());
            updateEntity.setReductionFlag(0);
            this.updateById(updateEntity);
        }
    }

}
