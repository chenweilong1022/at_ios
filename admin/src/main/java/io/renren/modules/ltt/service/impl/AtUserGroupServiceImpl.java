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

import io.renren.modules.ltt.dao.AtUserGroupDao;
import io.renren.modules.ltt.entity.AtUserGroupEntity;
import io.renren.modules.ltt.dto.AtUserGroupDTO;
import io.renren.modules.ltt.vo.AtUserGroupVO;
import io.renren.modules.ltt.service.AtUserGroupService;
import io.renren.modules.ltt.conver.AtUserGroupConver;

import java.io.Serializable;
import java.util.Collection;


@Service("atUserGroupService")
@Game
public class AtUserGroupServiceImpl extends ServiceImpl<AtUserGroupDao, AtUserGroupEntity> implements AtUserGroupService {

    @Override
    public PageUtils<AtUserGroupVO> queryPage(AtUserGroupDTO atUserGroup) {
        IPage<AtUserGroupEntity> page = baseMapper.selectPage(
                new Query<AtUserGroupEntity>(atUserGroup).getPage(),
                new QueryWrapper<AtUserGroupEntity>()
        );

        return PageUtils.<AtUserGroupVO>page(page).setList(AtUserGroupConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtUserGroupVO getById(Integer id) {
        return AtUserGroupConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtUserGroupDTO atUserGroup) {
        atUserGroup.setCreateTime(DateUtil.date());
        atUserGroup.setDeleteFlag(DeleteFlag.NO.getKey());
        AtUserGroupEntity atUserGroupEntity = AtUserGroupConver.MAPPER.converDTO(atUserGroup);
        return this.save(atUserGroupEntity);
    }

    @Override
    public boolean updateById(AtUserGroupDTO atUserGroup) {
        AtUserGroupEntity atUserGroupEntity = AtUserGroupConver.MAPPER.converDTO(atUserGroup);
        return this.updateById(atUserGroupEntity);
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
