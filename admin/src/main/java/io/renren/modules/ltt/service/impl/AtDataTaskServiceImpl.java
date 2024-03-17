package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
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

import java.io.Serializable;
import java.util.Collection;


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
    public boolean save(AtDataTaskDTO atDataTask) {
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

}
