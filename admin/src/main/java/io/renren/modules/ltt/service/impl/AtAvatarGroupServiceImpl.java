package io.renren.modules.ltt.service.impl;

import cn.hutool.core.date.DateUtil;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.enums.DeleteFlag;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtAvatarGroupDao;
import io.renren.modules.ltt.entity.AtAvatarGroupEntity;
import io.renren.modules.ltt.dto.AtAvatarGroupDTO;
import io.renren.modules.ltt.vo.AtAvatarGroupVO;
import io.renren.modules.ltt.service.AtAvatarGroupService;
import io.renren.modules.ltt.conver.AtAvatarGroupConver;

import java.io.Serializable;
import java.util.Collection;


@Service("atAvatarGroupService")
@Game
public class AtAvatarGroupServiceImpl extends ServiceImpl<AtAvatarGroupDao, AtAvatarGroupEntity> implements AtAvatarGroupService {

    @Override
    public PageUtils<AtAvatarGroupVO> queryPage(AtAvatarGroupDTO atAvatarGroup) {
        IPage<AtAvatarGroupEntity> page = baseMapper.selectPage(
                new Query<AtAvatarGroupEntity>(atAvatarGroup).getPage(),
                new QueryWrapper<AtAvatarGroupEntity>()
        );

        return PageUtils.<AtAvatarGroupVO>page(page).setList(AtAvatarGroupConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtAvatarGroupVO getById(Integer id) {
        return AtAvatarGroupConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtAvatarGroupDTO atAvatarGroup) {
        atAvatarGroup.setCreateTime(DateUtil.date());
        atAvatarGroup.setDeleteFlag(DeleteFlag.NO.getKey());
        AtAvatarGroupEntity atAvatarGroupEntity = AtAvatarGroupConver.MAPPER.converDTO(atAvatarGroup);
        return this.save(atAvatarGroupEntity);
    }

    @Override
    public boolean updateById(AtAvatarGroupDTO atAvatarGroup) {
        AtAvatarGroupEntity atAvatarGroupEntity = AtAvatarGroupConver.MAPPER.converDTO(atAvatarGroup);
        return this.updateById(atAvatarGroupEntity);
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
