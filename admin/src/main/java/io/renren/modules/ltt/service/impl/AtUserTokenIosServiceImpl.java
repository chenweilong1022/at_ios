package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.conver.AtUserTokenIosConver;
import io.renren.modules.ltt.dao.AtUserTokenIosDao;
import io.renren.modules.ltt.dto.AtUserTokenIosDTO;
import io.renren.modules.ltt.dto.AtUserTokenIosDeviceParamDTO;
import io.renren.modules.ltt.dto.AtUserTokenIosDeviceResultDTO;
import io.renren.modules.ltt.dto.IosTokenDTO;
import io.renren.modules.ltt.entity.AtUserTokenIosEntity;
import io.renren.modules.ltt.entity.CdRegisterTaskEntity;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.service.AtUserTokenIosService;
import io.renren.modules.ltt.service.CdRegisterTaskService;
import io.renren.modules.ltt.vo.AtUserTokenIosVO;
import io.renren.modules.ltt.vo.IOSTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;


@Service("atUserTokenIosService")
@Game
@Slf4j
public class AtUserTokenIosServiceImpl extends ServiceImpl<AtUserTokenIosDao, AtUserTokenIosEntity> implements AtUserTokenIosService {

    @Autowired
    private CdRegisterTaskService cdRegisterTaskService;

    @Override
    public PageUtils<AtUserTokenIosDeviceResultDTO> queryDevicePage(AtUserTokenIosDeviceParamDTO paramDTO) {
        paramDTO.setPageStart((paramDTO.getPage() - 1) * paramDTO.getLimit());
        Integer count = baseMapper.queryDevicePageCount(paramDTO);
        List<AtUserTokenIosDeviceResultDTO> resultList = Collections.emptyList();
        if (count > 0) {
            resultList = baseMapper.queryDevicePage(paramDTO);
        }
        return new PageUtils(resultList, count, paramDTO.getLimit(), paramDTO.getPage());
    }

    @Override
    public PageUtils<AtUserTokenIosVO> queryPage(AtUserTokenIosDTO atUserTokenIos) {
        IPage<AtUserTokenIosEntity> page = baseMapper.selectPage(
                new Query<AtUserTokenIosEntity>(atUserTokenIos).getPage(),
                new QueryWrapper<AtUserTokenIosEntity>().lambda()
                        .eq(AtUserTokenIosEntity::getDeleteFlag, DeleteFlag.NO.getKey())
                        .eq(StringUtils.isNotEmpty(atUserTokenIos.getDeviceId()), AtUserTokenIosEntity::getDeviceId, atUserTokenIos.getDeviceId())
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
    public boolean updateDeviceName(AtUserTokenIosDeviceParamDTO paramDTO) {
        Assert.isTrue(StringUtils.isEmpty(paramDTO.getDeviceId()), "设备id不能为空");
        Assert.isTrue(StringUtils.isEmpty(paramDTO.getDeviceName()), "设备名称不能为空");

        AtUserTokenIosEntity updateEntity = new AtUserTokenIosEntity();
        updateEntity.setDeviceName(paramDTO.getDeviceName());

        return this.update(updateEntity, new QueryWrapper<AtUserTokenIosEntity>().lambda()
                .eq(AtUserTokenIosEntity::getDeviceId, paramDTO.getDeviceId()));
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
        atUserTokenIosEntity.setIosToken(map.getBody());
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

        }else {
            atUserTokenIosEntity.setTaskId(taskId);
        }

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

    @Override
    public void backUp(AtUserTokenIosDTO atUserTokenIos) {
        if (CollUtil.isNotEmpty(atUserTokenIos.getIds())) {
            List<AtUserTokenIosEntity> atUserTokenIosEntities = this.listByIds(atUserTokenIos.getIds());
            for (AtUserTokenIosEntity atUserTokenIosEntity : atUserTokenIosEntities) {
                IOSTaskVO iosTaskVO = new IOSTaskVO();
                iosTaskVO.setTaskType("backup");
                iosTaskVO.setPhone(atUserTokenIosEntity.getPhoneNumber().replace("+",""));

                String deviceId = atUserTokenIosEntity.getDeviceId();
                Queue<IOSTaskVO> cacheIOSTaskVOIfPresent = stringQueueCacheIOSTaskVO.getIfPresent(deviceId);
                if (CollUtil.isEmpty(cacheIOSTaskVOIfPresent) || cacheIOSTaskVOIfPresent.isEmpty()) {
                    cacheIOSTaskVOIfPresent = new LinkedList<>();
                }
                cacheIOSTaskVOIfPresent.offer(iosTaskVO);
                stringQueueCacheIOSTaskVO.put(deviceId,cacheIOSTaskVOIfPresent);
            }
            return;
        }
        IOSTaskVO iosTaskVO = new IOSTaskVO();
        iosTaskVO.setTaskType("backup");
        iosTaskVO.setPhone(atUserTokenIos.getPhoneNumber().replace("+",""));

        String deviceId = atUserTokenIos.getDeviceId();
        Queue<IOSTaskVO> cacheIOSTaskVOIfPresent = stringQueueCacheIOSTaskVO.getIfPresent(deviceId);
        if (CollUtil.isEmpty(cacheIOSTaskVOIfPresent) || cacheIOSTaskVOIfPresent.isEmpty()) {
            cacheIOSTaskVOIfPresent = new LinkedList<>();
        }
        cacheIOSTaskVOIfPresent.offer(iosTaskVO);
        stringQueueCacheIOSTaskVO.put(deviceId,cacheIOSTaskVOIfPresent);
    }

}
