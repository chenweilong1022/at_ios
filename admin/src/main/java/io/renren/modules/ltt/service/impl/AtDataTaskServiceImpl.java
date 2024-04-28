package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import io.renren.common.utils.EnumUtil;
import io.renren.common.utils.PhoneUtil;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.vo.GroupCountByDataTaskIdVO;
import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.entity.*;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.AtDataService;
import io.renren.modules.ltt.service.AtDataSubtaskService;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.modules.ltt.vo.AtDataSubtaskVO;
import io.renren.modules.ltt.vo.AtUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtDataTaskDao;
import io.renren.modules.ltt.dto.AtDataTaskDTO;
import io.renren.modules.ltt.vo.AtDataTaskVO;
import io.renren.modules.ltt.service.AtDataTaskService;
import io.renren.modules.ltt.conver.AtDataTaskConver;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service("atDataTaskService")
@Game
@Slf4j
public class AtDataTaskServiceImpl extends ServiceImpl<AtDataTaskDao, AtDataTaskEntity> implements AtDataTaskService {


    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtDataService atDataService;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;

    @Override
    public PageUtils<AtDataTaskVO> queryPage(AtDataTaskDTO atDataTask) {
        IPage<AtDataTaskEntity> page = baseMapper.selectPage(
                new Query<AtDataTaskEntity>(atDataTask).getPage(),
                new QueryWrapper<AtDataTaskEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(atDataTask.getSysUserId()), AtDataTaskEntity::getSysUserId, atDataTask.getSysUserId())
                        .orderByDesc(AtDataTaskEntity::getId)
        );

        return PageUtils.<AtDataTaskVO>page(page).setList(AtDataTaskConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtDataTaskVO getById(Integer id) {
        return AtDataTaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(AtDataTaskDTO atDataTask) {
        atDataTask.setCreateTime(DateUtil.date());
        atDataTask.setDeleteFlag(DeleteFlag.NO.getKey());
        atDataTask.setAddTotalQuantity(0);
        atDataTask.setSuccessfulQuantity(0);
        atDataTask.setFailuresQuantity(0);
        atDataTask.setUpdateTime(DateUtil.date());
        atDataTask.setTaskStatus(TaskStatus.TaskStatus0.getKey());
        AtDataTaskEntity atDataTaskEntity = AtDataTaskConver.MAPPER.converDTO(atDataTask);

        AtUserDTO atUserDTO = new AtUserDTO();
        atUserDTO.setSysUserId(atDataTask.getSysUserId());
        String regions = EnumUtil.queryValueByKey(atDataTask.getCountryCode(), CountryCode.values());
        atUserDTO.setNation(regions.toUpperCase());
        atUserDTO.setUserGroupId(atDataTask.getUserGroupId());
        atUserDTO.setStatus(UserStatus.UserStatus4.getKey());
        atUserDTO.setUserSource(AtUserSourceEnum.AtUserSource1.getKey());
        atUserDTO.setLimit(5000);
        //获取符合账号的号码
        PageUtils pageUtils = atUserService.queryPageOld(atUserDTO);



        //获取分组下所有的用户
        List<AtUserVO> atUserEntities = pageUtils.getList();
        List<AtUserEntity> atUserEntitiesUpdates = new ArrayList<>();
        Assert.isTrue(CollUtil.isEmpty(atUserEntities),String.format("分组下%s国家账号不足",regions));
        Integer addQuantityLimit = atDataTask.getAddQuantityLimit();
        Integer limit = addQuantityLimit * atUserEntities.size();
        List<AtDataEntity> atDataEntities = atDataService.list(new QueryWrapper<AtDataEntity>().lambda()
                .eq(AtDataEntity::getDataGroupId,atDataTask.getDataGroupId())
                .eq(AtDataEntity::getUseFlag, UseFlag.NO.getKey())
                .last("limit " + limit)
        );
//        Assert.isTrue(atDataEntities.size() < limit,"数据不够用户分配，请补充");
        //设置总拉粉人数，并且去保存
        atDataTaskEntity.setAddTotalQuantity(atDataEntities.size());
        boolean save = this.save(atDataTaskEntity);

        List<List<AtDataEntity>> partitions = Lists.partition(atDataEntities, addQuantityLimit);
        List<AtDataSubtaskEntity> atDataSubtaskEntities = new ArrayList<>();
        List<AtDataEntity> updates = new ArrayList<>();
        int addTotalQuantity = 0;
        for (int i = 0; i < partitions.size(); i++) {
            //获取分组后的datalist
            List<AtDataEntity> atDataEntityList = partitions.get(i);
            //获取用户
            AtUserVO atUserEntity = atUserEntities.get(i);
            if (addQuantityLimit > atDataEntityList.size()) {
                break;
            }
            AtUserEntity atUserEntityUpdate = new AtUserEntity();
            atUserEntityUpdate.setId(atUserEntity.getId());
            atUserEntityUpdate.setStatus(UserStatus.UserStatus6.getKey());
            atUserEntitiesUpdates.add(atUserEntityUpdate);
            addTotalQuantity = addTotalQuantity + atDataEntityList.size();
            for (AtDataEntity atDataEntity : atDataEntityList) {
                String data = atDataEntity.getData();
                //修改data为已经使用
                AtDataEntity update = new AtDataEntity();
                update.setId(atDataEntity.getId());
                update.setUseFlag(UseFlag.YES.getKey());
                updates.add(update);
                //添加到加粉子任务去
                AtDataSubtaskEntity atDataSubtaskEntity = new AtDataSubtaskEntity();
                atDataSubtaskEntity.setGroupType(atDataTaskEntity.getGroupType());
                atDataSubtaskEntity.setDataTaskId(atDataTaskEntity.getId());
                atDataSubtaskEntity.setUserId(atUserEntity.getId());
                atDataSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus1.getKey());
                if (GroupType.GroupType1.getKey().equals(atDataTaskEntity.getGroupType())) {
                    atDataSubtaskEntity.setContactKey(data);
                }else if (GroupType.GroupType2.getKey().equals(atDataTaskEntity.getGroupType())) {
                    atDataSubtaskEntity.setMid(data);
                }else if (GroupType.GroupType3.getKey().equals(atDataTaskEntity.getGroupType())) {
                    atDataSubtaskEntity.setMid(data);
                }else if (GroupType.GroupType4.getKey().equals(atDataTaskEntity.getGroupType())) {
                    atDataSubtaskEntity.setMid(data);
                }else if (GroupType.GroupType5.getKey().equals(atDataTaskEntity.getGroupType())) {
                    atDataSubtaskEntity.setContactKey(data);
                    checkCountry(atDataEntity, atUserEntity);
                }
                atDataSubtaskEntity.setSysUserId(atDataTask.getSysUserId());
                atDataSubtaskEntity.setDeleteFlag(DeleteFlag.NO.getKey());
                atDataSubtaskEntity.setCreateTime(DateUtil.date());
                atDataSubtaskEntities.add(atDataSubtaskEntity);
            }
        }
        Assert.isTrue(addTotalQuantity <= 0,"数据不足，请添加数据");
        AtDataTaskEntity update = new AtDataTaskEntity();
        update.setId(atDataTaskEntity.getId());
        update.setAddTotalQuantity(addTotalQuantity);
        this.updateById(update);
        atDataSubtaskService.saveBatch(atDataSubtaskEntities);
        atDataService.updateBatchById(updates);
        atUserService.updateBatchById(atUserEntitiesUpdates);
        return save;
    }

    private static void checkCountry(AtDataEntity atDataEntity, AtUserVO atUserEntity) {
        boolean flag = false;
        try {
            String data = atDataEntity.getData();
            String telephone = atUserEntity.getTelephone();
            long countryCodeData = PhoneUtil.getPhoneNumberInfo("+" + data.trim()).getCountryCode();
            PhoneCountryVO phoneNumberInfo = PhoneUtil.getPhoneNumberInfo("+" + telephone.trim());
            long countryCodeTelephone = phoneNumberInfo.getCountryCode();
            flag = countryCodeData != countryCodeTelephone;
            log.info("flag = {}",flag);
        } catch (Exception e) {
            log.error("err = {}",e.getMessage());
        }
        Assert.isTrue(flag,"通讯录模式，协议号和料子国家必须相同");
    }

    @Override
    public boolean updateById(AtDataTaskDTO atDataTask) {
        AtDataTaskEntity atDataTaskEntity = AtDataTaskConver.MAPPER.converDTO(atDataTask);
        return this.updateById(atDataTaskEntity);
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
    public void startUp(Collection<? extends Serializable> ids) {
        List<AtDataTaskEntity> atDataTaskEntities = this.listByIds(ids);
        List<AtDataTaskEntity> updates = new ArrayList<>();
        for (AtDataTaskEntity atDataTaskEntity : atDataTaskEntities) {
            AtDataTaskEntity update = new AtDataTaskEntity();
            update.setId(atDataTaskEntity.getId());
            update.setTaskStatus(TaskStatus.TaskStatus1.getKey());
            updates.add(update);
        }
        this.updateBatchById(updates);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void errRetry(List<Integer> ids) {
        //获取头像任务所有
        List<AtDataTaskEntity> taskEntities = this.listByIds(ids);

        for (AtDataTaskEntity taskEntity : taskEntities) {
            taskEntity.setTaskStatus(TaskStatus.TaskStatus2.getKey());
            taskEntity.setFailuresQuantity(0);
            //获取所有子任务
            List<AtDataSubtaskEntity> list = atDataSubtaskService.list(new QueryWrapper<AtDataSubtaskEntity>().lambda()
                    .eq(AtDataSubtaskEntity::getDataTaskId,taskEntity.getId())
                    .eq(AtDataSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus5.getKey())
            );

            for (AtDataSubtaskEntity atAvatarSubtaskEntity : list) {
                atAvatarSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus2.getKey());
            }
            if (CollUtil.isNotEmpty(list)) {
                atDataSubtaskService.updateBatchById(list);
            }
        }
        if (CollUtil.isNotEmpty(taskEntities)) {
            this.updateBatchById(taskEntities);
        }
    }

    @Override
    public List<GroupCountByDataTaskIdVO> groupCountByDataTaskId() {
        return baseMapper.groupCountByDataTaskId();
    }

    @Override
    public byte[] importDataToken(Integer dataTaskId) {
        List<AtDataSubtaskVO> subtaskVOList = atDataSubtaskService.getByDataTaskIds(dataTaskId);
        Assert.isTrue(CollUtil.isEmpty(subtaskVOList), "数据为空");

        List<Integer> userIdList = subtaskVOList.stream().filter(i -> ObjectUtil.isNotNull(i.getUserId()))
                .map(AtDataSubtaskVO::getUserId).distinct().collect(Collectors.toList());
        Assert.isTrue(CollUtil.isEmpty(userIdList), "数据为空");

        byte[] bytes = atUserService.importToken(userIdList);
        return bytes;
    }

}
