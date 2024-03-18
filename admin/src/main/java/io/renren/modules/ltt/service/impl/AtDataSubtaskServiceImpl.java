package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtDataSubtaskDao;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import io.renren.modules.ltt.dto.AtDataSubtaskDTO;
import io.renren.modules.ltt.vo.AtDataSubtaskVO;
import io.renren.modules.ltt.service.AtDataSubtaskService;
import io.renren.modules.ltt.conver.AtDataSubtaskConver;

import java.io.Serializable;
import java.util.Collection;


@Service("atDataSubtaskService")
@Game
public class AtDataSubtaskServiceImpl extends ServiceImpl<AtDataSubtaskDao, AtDataSubtaskEntity> implements AtDataSubtaskService {

    @Override
    public PageUtils<AtDataSubtaskVO> queryPage(AtDataSubtaskDTO atDataSubtask) {
        IPage<AtDataSubtaskEntity> page = baseMapper.selectPage(
                new Query<AtDataSubtaskEntity>(atDataSubtask).getPage(),
                new QueryWrapper<AtDataSubtaskEntity>()
        );

        return PageUtils.<AtDataSubtaskVO>page(page).setList(AtDataSubtaskConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtDataSubtaskVO getById(Integer id) {
        return AtDataSubtaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtDataSubtaskDTO atDataSubtask) {
        AtDataSubtaskEntity atDataSubtaskEntity = AtDataSubtaskConver.MAPPER.converDTO(atDataSubtask);
        return this.save(atDataSubtaskEntity);
    }

    @Override
    public boolean updateById(AtDataSubtaskDTO atDataSubtask) {
        AtDataSubtaskEntity atDataSubtaskEntity = AtDataSubtaskConver.MAPPER.converDTO(atDataSubtask);
        return this.updateById(atDataSubtaskEntity);
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
