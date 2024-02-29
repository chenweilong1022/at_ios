package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtAvatarDao;
import io.renren.modules.ltt.entity.AtAvatarEntity;
import io.renren.modules.ltt.dto.AtAvatarDTO;
import io.renren.modules.ltt.vo.AtAvatarVO;
import io.renren.modules.ltt.service.AtAvatarService;
import io.renren.modules.ltt.conver.AtAvatarConver;

import java.io.Serializable;
import java.util.Collection;


@Service("atAvatarService")
@Game
public class AtAvatarServiceImpl extends ServiceImpl<AtAvatarDao, AtAvatarEntity> implements AtAvatarService {

    @Override
    public PageUtils<AtAvatarVO> queryPage(AtAvatarDTO atAvatar) {
        IPage<AtAvatarEntity> page = baseMapper.selectPage(
                new Query<AtAvatarEntity>(atAvatar).getPage(),
                new QueryWrapper<AtAvatarEntity>()
        );

        return PageUtils.<AtAvatarVO>page(page).setList(AtAvatarConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtAvatarVO getById(Integer id) {
        return AtAvatarConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtAvatarDTO atAvatar) {
        AtAvatarEntity atAvatarEntity = AtAvatarConver.MAPPER.converDTO(atAvatar);
        return this.save(atAvatarEntity);
    }

    @Override
    public boolean updateById(AtAvatarDTO atAvatar) {
        AtAvatarEntity atAvatarEntity = AtAvatarConver.MAPPER.converDTO(atAvatar);
        return this.updateById(atAvatarEntity);
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
