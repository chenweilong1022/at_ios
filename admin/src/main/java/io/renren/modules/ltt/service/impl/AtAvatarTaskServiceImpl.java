package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.entity.AtAvatarEntity;
import io.renren.modules.ltt.entity.AtAvatarSubtaskEntity;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.TaskStatus;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtAvatarTaskDao;
import io.renren.modules.ltt.entity.AtAvatarTaskEntity;
import io.renren.modules.ltt.dto.AtAvatarTaskDTO;
import io.renren.modules.ltt.vo.AtAvatarTaskVO;
import io.renren.modules.ltt.conver.AtAvatarTaskConver;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("atAvatarTaskService")
@Game
public class AtAvatarTaskServiceImpl extends ServiceImpl<AtAvatarTaskDao, AtAvatarTaskEntity> implements AtAvatarTaskService {

    @Resource
    private AtUserGroupService atUserGroupService;

    @Resource
    private AtAvatarGroupService avatarGroupService;


    @Autowired
    private AtUserService atUserService;
    @Autowired
    private AtAvatarService atAvatarService;
    @Autowired
    private AtAvatarSubtaskService atAvatarSubtaskService;

    @Override
    public PageUtils<AtAvatarTaskVO> queryPage(AtAvatarTaskDTO atAvatarTask) {
        IPage<AtAvatarTaskEntity> page = baseMapper.selectPage(
                new Query<AtAvatarTaskEntity>(atAvatarTask).getPage(),
                new QueryWrapper<AtAvatarTaskEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(atAvatarTask.getSysUserId()), AtAvatarTaskEntity::getSysUserId, atAvatarTask.getSysUserId())
                        .orderByDesc(AtAvatarTaskEntity::getId)
        );
        List<AtAvatarTaskVO> resultList = AtAvatarTaskConver.MAPPER.conver(page.getRecords());
        if (CollectionUtil.isEmpty(resultList)) {
            return PageUtils.<AtAvatarTaskVO>page(page).setList(resultList);
        }

        //查询账户分组
        List<Integer> userGroupIdList = resultList.stream().filter(i -> i.getUserGroupId() != null)
                .map(AtAvatarTaskVO::getUserGroupId).distinct().collect(Collectors.toList());
        Map<Integer, String> userGroupIdMap = atUserGroupService.getMapByIds(userGroupIdList);

        //查询头像分组
        List<Integer> avatarGroupIdList = resultList.stream().filter(i -> i.getAvatarGroupId() != null)
                .map(AtAvatarTaskVO::getAvatarGroupId).distinct().collect(Collectors.toList());
        Map<Integer, String> avatarGroupIdMap = avatarGroupService.getMapByIds(avatarGroupIdList);


        for (AtAvatarTaskVO atAvatarTaskVO : resultList) {
            if (atAvatarTaskVO.getUserGroupId() != null){
                atAvatarTaskVO.setUserGroupName(userGroupIdMap.get(atAvatarTaskVO.getUserGroupId()));
            }
            if (atAvatarTaskVO.getAvatarGroupId() != null){
                atAvatarTaskVO.setAvatarGroupName(avatarGroupIdMap.get(atAvatarTaskVO.getAvatarGroupId()));
            }
        }
        return PageUtils.<AtAvatarTaskVO>page(page).setList(resultList);
    }
    @Override
    public AtAvatarTaskVO getById(Integer id) {
        return AtAvatarTaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(AtAvatarTaskDTO atAvatarTask) {
        atAvatarTask.setExecutionQuantity(0);
        atAvatarTask.setFailuresQuantity(0);
        atAvatarTask.setSuccessfulQuantity(0);
        atAvatarTask.setCreateTime(DateUtil.date());
        atAvatarTask.setDeleteFlag(DeleteFlag.NO.getKey());
        atAvatarTask.setTaskStatus(TaskStatus.TaskStatus1.getKey());
        AtAvatarTaskEntity atAvatarTaskEntity = AtAvatarTaskConver.MAPPER.converDTO(atAvatarTask);

        //获取分组下所有的用户
        List<AtUserEntity> atUserEntities = atUserService.list(new QueryWrapper<AtUserEntity>().lambda()
                .eq(AtUserEntity::getUserGroupId,atAvatarTask.getUserGroupId())
        );
        //获取未使用用户头像
        List<AtAvatarEntity> atAvatarEntities = atAvatarService.list(new QueryWrapper<AtAvatarEntity>().lambda()
                .eq(AtAvatarEntity::getAvatarGroupId,atAvatarTask.getAvatarGroupId())
                .eq(AtAvatarEntity::getUseFlag, UseFlag.NO.getKey())
                .last("limit " + atUserEntities.size())
        );
        Assert.isTrue(atAvatarEntities.size() < atUserEntities.size(),"头像不够用户分配，请补充");

        //修改任务数量
        atAvatarTaskEntity.setExecutionQuantity(atUserEntities.size());
        boolean save = this.save(atAvatarTaskEntity);
        List<AtAvatarSubtaskEntity> atAvatarSubtaskEntities = new ArrayList<>();
        List<AtAvatarEntity> atAvatarEntityListUpdate = new ArrayList<>();
        for (int i = 0; i < atUserEntities.size(); i++) {
            AtUserEntity atUserEntity = atUserEntities.get(i);
            AtAvatarEntity atAvatarEntity = atAvatarEntities.get(i);

            AtAvatarEntity update = new AtAvatarEntity();
            update.setId(atAvatarEntity.getId());
            update.setUseFlag(UseFlag.YES.getKey());
            atAvatarEntityListUpdate.add(update);

            AtAvatarSubtaskEntity atAvatarSubtaskEntity = new AtAvatarSubtaskEntity();
            atAvatarSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus1.getKey());
            atAvatarSubtaskEntity.setAvatarTaskId(atAvatarTaskEntity.getId());
            atAvatarSubtaskEntity.setUserGroupId(atAvatarTask.getUserGroupId());
            atAvatarSubtaskEntity.setAvatarGroupId(atAvatarTask.getAvatarGroupId());
            atAvatarSubtaskEntity.setUserId(atUserEntity.getId());
            atAvatarSubtaskEntity.setAvatarId(atAvatarEntity.getId());
            atAvatarSubtaskEntity.setDeleteFlag(DeleteFlag.NO.getKey());
            atAvatarSubtaskEntity.setCreateTime(DateUtil.date());
            atAvatarSubtaskEntity.setSysUserId(atAvatarTask.getSysUserId());
            atAvatarSubtaskEntities.add(atAvatarSubtaskEntity);
        }
        //图片使用修改
        atAvatarService.updateBatchById(atAvatarEntityListUpdate);
        //任务新增
        atAvatarSubtaskService.saveBatch(atAvatarSubtaskEntities);
        return save;
    }

    @Override
    public boolean updateById(AtAvatarTaskDTO atAvatarTask) {
        AtAvatarTaskEntity atAvatarTaskEntity = AtAvatarTaskConver.MAPPER.converDTO(atAvatarTask);
        return this.updateById(atAvatarTaskEntity);
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
        List<AtAvatarTaskEntity> taskEntities = this.listByIds(ids);

        for (AtAvatarTaskEntity taskEntity : taskEntities) {
            taskEntity.setTaskStatus(TaskStatus.TaskStatus2.getKey());
            taskEntity.setFailuresQuantity(0);
            //获取所有子任务
            List<AtAvatarSubtaskEntity> list = atAvatarSubtaskService.list(new QueryWrapper<AtAvatarSubtaskEntity>().lambda()
                    .eq(AtAvatarSubtaskEntity::getAvatarTaskId,taskEntity.getId())
                    .eq(AtAvatarSubtaskEntity::getTaskStatus,TaskStatus.TaskStatus5.getKey())
            );

            for (AtAvatarSubtaskEntity atAvatarSubtaskEntity : list) {
                atAvatarSubtaskEntity.setTaskStatus(TaskStatus.TaskStatus2.getKey());
            }
            if (CollUtil.isNotEmpty(list)) {
                atAvatarSubtaskService.updateBatchById(list);
            }
        }
        if (CollUtil.isNotEmpty(taskEntities)) {
            this.updateBatchById(taskEntities);
        }
    }

}
