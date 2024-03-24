package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.entity.*;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.TaskStatus;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.modules.ltt.service.AtUsernameService;
import io.renren.modules.ltt.service.AtUsernameSubtaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUsernameTaskDao;
import io.renren.modules.ltt.dto.AtUsernameTaskDTO;
import io.renren.modules.ltt.vo.AtUsernameTaskVO;
import io.renren.modules.ltt.service.AtUsernameTaskService;
import io.renren.modules.ltt.conver.AtUsernameTaskConver;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service("atUsernameTaskService")
@Game
public class AtUsernameTaskServiceImpl extends ServiceImpl<AtUsernameTaskDao, AtUsernameTaskEntity> implements AtUsernameTaskService {

    @Override
    public PageUtils<AtUsernameTaskVO> queryPage(AtUsernameTaskDTO atUsernameTask) {
        IPage<AtUsernameTaskEntity> page = baseMapper.selectPage(
                new Query<AtUsernameTaskEntity>(atUsernameTask).getPage(),
                new QueryWrapper<AtUsernameTaskEntity>()
        );

        return PageUtils.<AtUsernameTaskVO>page(page).setList(AtUsernameTaskConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtUsernameTaskVO getById(Integer id) {
        return AtUsernameTaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtUsernameService atUsernameService;
    @Autowired
    private AtUsernameSubtaskService atUsernameSubtaskService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(AtUsernameTaskDTO atUsernameTask) {
        atUsernameTask.setExecutionQuantity(0);
        atUsernameTask.setFailuresQuantity(0);
        atUsernameTask.setSuccessfulQuantity(0);
        atUsernameTask.setCreateTime(DateUtil.date());
        atUsernameTask.setDeleteFlag(DeleteFlag.NO.getKey());
        atUsernameTask.setTaskStatus(TaskStatus.TaskStatus1.getKey());
        AtUsernameTaskEntity atUsernameTaskEntity = AtUsernameTaskConver.MAPPER.converDTO(atUsernameTask);
        //获取分组下所有的用户
        List<AtUserEntity> atUserEntities = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
                .eq(AtUserEntity::getUserGroupId,atUsernameTask.getUserGroupId())
        );
        //获取未使用用户nicename
        List<AtUsernameEntity> atUsernameEntities = atUsernameService.list(new QueryWrapper<AtUsernameEntity>().lambda()
                .eq(AtUsernameEntity::getUsernameGroupId,atUsernameTask.getUsernameGroupId())
                .eq(AtUsernameEntity::getUseFlag, UseFlag.NO.getKey())
                .last("limit " + atUserEntities.size())
        );
        Assert.isTrue(atUsernameEntities.size() < atUserEntities.size(),"昵称不够用户分配，请补充");


        //修改任务数量
        atUsernameTaskEntity.setExecutionQuantity(atUserEntities.size());
        boolean save = this.save(atUsernameTaskEntity);
        List<AtUsernameSubtaskEntity> atUsernameSubtaskEntities = new ArrayList<>();
        List<AtUsernameEntity> atUsernameEntityListUpdate = new ArrayList<>();
        for (int i = 0; i < atUserEntities.size(); i++) {
            AtUserEntity atUserEntity = atUserEntities.get(i);
            AtUsernameEntity atUsernameEntity = atUsernameEntities.get(i);

            AtUsernameEntity update = new AtUsernameEntity();
            update.setId(atUsernameEntity.getId());
            update.setUseFlag(UseFlag.YES.getKey());
            atUsernameEntityListUpdate.add(update);

            AtUsernameSubtaskEntity atUsernameSubtaskEntity = new AtUsernameSubtaskEntity();
            atUsernameSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus1.getKey());
            atUsernameSubtaskEntity.setUsernameTaskId(atUsernameTaskEntity.getId());
            atUsernameSubtaskEntity.setUserGroupId(atUsernameTaskEntity.getUserGroupId());
            atUsernameSubtaskEntity.setUsernameGroupId(atUsernameTaskEntity.getUsernameGroupId());
            atUsernameSubtaskEntity.setUserId(atUserEntity.getId());
            atUsernameSubtaskEntity.setUsernameId(atUsernameEntity.getId());
            atUsernameSubtaskEntity.setDeleteFlag(DeleteFlag.NO.getKey());
            atUsernameSubtaskEntity.setCreateTime(DateUtil.date());
            atUsernameSubtaskEntities.add(atUsernameSubtaskEntity);
        }
        //昵称使用修改
        atUsernameService.updateBatchById(atUsernameEntityListUpdate);
        //任务新增
        atUsernameSubtaskService.saveBatch(atUsernameSubtaskEntities);
        return save;
    }

    @Override
    public boolean updateById(AtUsernameTaskDTO atUsernameTask) {
        AtUsernameTaskEntity atUsernameTaskEntity = AtUsernameTaskConver.MAPPER.converDTO(atUsernameTask);
        return this.updateById(atUsernameTaskEntity);
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
    public void errRetry(List<Integer> ids) {
        //获取头像任务所有
        List<AtUsernameTaskEntity> taskEntities = this.listByIds(ids);

        for (AtUsernameTaskEntity taskEntity : taskEntities) {
            taskEntity.setTaskStatus(TaskStatus.TaskStatus2.getKey());
            taskEntity.setFailuresQuantity(0);
            //获取所有子任务
            List<AtUsernameSubtaskEntity> list = atUsernameSubtaskService.list(new QueryWrapper<AtUsernameSubtaskEntity>().lambda()
                    .eq(AtUsernameSubtaskEntity::getUsernameTaskId,taskEntity.getId())
                    .eq(AtUsernameSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus5.getKey())
            );

            for (AtUsernameSubtaskEntity subtaskEntity : list) {
                subtaskEntity.setTaskStatus(TaskStatus.TaskStatus2.getKey());
            }
            if (CollUtil.isNotEmpty(list)) {
                atUsernameSubtaskService.updateBatchById(list);
            }
        }
        if (CollUtil.isNotEmpty(taskEntities)) {
            this.updateBatchById(taskEntities);
        }
    }

}
