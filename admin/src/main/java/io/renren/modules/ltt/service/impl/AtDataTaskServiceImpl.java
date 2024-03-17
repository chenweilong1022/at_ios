package io.renren.modules.ltt.service.impl;

import cn.hutool.core.date.DateUtil;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.TaskStatus;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtDataTaskDao;
import io.renren.modules.ltt.entity.AtDataTaskEntity;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(AtDataTaskDTO atDataTask) {
        atDataTask.setCreateTime(DateUtil.date());
        atDataTask.setDeleteFlag(DeleteFlag.NO.getKey());
        atDataTask.setAddTotalQuantity(0);
        atDataTask.setSuccessfulQuantity(0);
        atDataTask.setFailuresQuantity(0);
        atDataTask.setAddQuantityLimit(0);
        atDataTask.setUpdateTime(DateUtil.date());
        atDataTask.setTaskStatus(TaskStatus.TaskStatus0.getKey());
        AtDataTaskEntity atDataTaskEntity = AtDataTaskConver.MAPPER.converDTO(atDataTask);
        return this.save(atDataTaskEntity);
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
            update.setTaskStatus(TaskStatus.TaskStatus2.getKey());
            updates.add(update);
        }
        this.updateBatchById(updates);
    }

}
