package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import io.renren.common.utils.PhoneUtil;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.entity.*;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.GroupType;
import io.renren.modules.ltt.enums.TaskStatus;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.service.AtDataService;
import io.renren.modules.ltt.service.AtDataSubtaskService;
import io.renren.modules.ltt.service.AtUserService;
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


@Service("atDataTaskService")
@Game
@Slf4j
public class AtDataTaskServiceImpl extends ServiceImpl<AtDataTaskDao, AtDataTaskEntity> implements AtDataTaskService {

    @Override
    public PageUtils<AtDataTaskVO> queryPage(AtDataTaskDTO atDataTask) {
        IPage<AtDataTaskEntity> page = baseMapper.selectPage(
                new Query<AtDataTaskEntity>(atDataTask).getPage(),
                new QueryWrapper<AtDataTaskEntity>()
        );

        return PageUtils.<AtDataTaskVO>page(page).setList(AtDataTaskConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtDataTaskVO getById(Integer id) {
        return AtDataTaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtDataService atDataService;
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;
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
        //获取分组下所有的用户
        List<AtUserEntity> atUserEntities = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
                .eq(AtUserEntity::getUserGroupId,atDataTask.getUserGroupId())
        );
        Assert.isTrue(CollUtil.isEmpty(atUserEntities),"用户分组不能为空");
        Integer addQuantityLimit = atDataTask.getAddQuantityLimit();
        Integer limit = addQuantityLimit * atUserEntities.size();
        List<AtDataEntity> atDataEntities = atDataService.list(new QueryWrapper<AtDataEntity>().lambda()
                .eq(AtDataEntity::getDataGroupId,atDataTask.getDataGroupId())
                .eq(AtDataEntity::getUseFlag, UseFlag.NO.getKey())
                .last("limit " + limit)
        );
        Assert.isTrue(atDataEntities.size() < limit,"数据不够用户分配，请补充");
        //设置总拉粉人数，并且去保存
        atDataTaskEntity.setAddTotalQuantity(atDataEntities.size());
        boolean save = this.save(atDataTaskEntity);

        List<List<AtDataEntity>> partitions = Lists.partition(atDataEntities, addQuantityLimit);
        List<AtDataSubtaskEntity> atDataSubtaskEntities = new ArrayList<>();
        List<AtDataEntity> updates = new ArrayList<>();
        for (int i = 0; i < partitions.size(); i++) {
            //获取分组后的datalist
            List<AtDataEntity> atDataEntityList = partitions.get(i);
            //获取用户
            AtUserEntity atUserEntity = atUserEntities.get(i);
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
                }else if (GroupType.GroupType4.getKey().equals(atDataTaskEntity.getGroupType())) {
                    atDataSubtaskEntity.setMid(data);
                }else if (GroupType.GroupType5.getKey().equals(atDataTaskEntity.getGroupType())) {
                    atDataSubtaskEntity.setContactKey(data);
                    checkCountry(atDataEntity, atUserEntity);
                }
                atDataSubtaskEntity.setDeleteFlag(DeleteFlag.NO.getKey());
                atDataSubtaskEntity.setCreateTime(DateUtil.date());
                atDataSubtaskEntities.add(atDataSubtaskEntity);
            }
        }
        atDataSubtaskService.saveBatch(atDataSubtaskEntities);
        atDataService.updateBatchById(updates);
        return save;
    }

    private static void checkCountry(AtDataEntity atDataEntity, AtUserEntity atUserEntity) {
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

}
