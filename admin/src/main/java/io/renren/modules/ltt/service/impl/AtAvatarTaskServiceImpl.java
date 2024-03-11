package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
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
import io.renren.modules.ltt.service.AtAvatarTaskService;
import io.renren.modules.ltt.conver.AtAvatarTaskConver;

import java.io.Serializable;
import java.util.Collection;


@Service("atAvatarTaskService")
@Game
public class AtAvatarTaskServiceImpl extends ServiceImpl<AtAvatarTaskDao, AtAvatarTaskEntity> implements AtAvatarTaskService {

    @Override
    public PageUtils<AtAvatarTaskVO> queryPage(AtAvatarTaskDTO atAvatarTask) {
        IPage<AtAvatarTaskEntity> page = baseMapper.selectPage(
                new Query<AtAvatarTaskEntity>(atAvatarTask).getPage(),
                new QueryWrapper<AtAvatarTaskEntity>()
        );

        return PageUtils.<AtAvatarTaskVO>page(page).setList(AtAvatarTaskConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtAvatarTaskVO getById(Integer id) {
        return AtAvatarTaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtAvatarTaskDTO atAvatarTask) {
        AtAvatarTaskEntity atAvatarTaskEntity = AtAvatarTaskConver.MAPPER.converDTO(atAvatarTask);
        return this.save(atAvatarTaskEntity);
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

}
