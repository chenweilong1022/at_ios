package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtGroupDao;
import io.renren.modules.ltt.entity.AtGroupEntity;
import io.renren.modules.ltt.dto.AtGroupDTO;
import io.renren.modules.ltt.vo.AtGroupVO;
import io.renren.modules.ltt.service.AtGroupService;
import io.renren.modules.ltt.conver.AtGroupConver;

import java.io.Serializable;
import java.util.Collection;


@Service("atGroupService")
@Game
public class AtGroupServiceImpl extends ServiceImpl<AtGroupDao, AtGroupEntity> implements AtGroupService {

    @Override
    public PageUtils<AtGroupVO> queryPage(AtGroupDTO atGroup) {
        IPage<AtGroupEntity> page = baseMapper.selectPage(
                new Query<AtGroupEntity>(atGroup).getPage(),
                new QueryWrapper<AtGroupEntity>()
        );

        return PageUtils.<AtGroupVO>page(page).setList(AtGroupConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtGroupVO getById(Integer id) {
        return AtGroupConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtGroupDTO atGroup) {
        AtGroupEntity atGroupEntity = AtGroupConver.MAPPER.converDTO(atGroup);
        return this.save(atGroupEntity);
    }

    @Override
    public boolean updateById(AtGroupDTO atGroup) {
        AtGroupEntity atGroupEntity = AtGroupConver.MAPPER.converDTO(atGroup);
        return this.updateById(atGroupEntity);
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
