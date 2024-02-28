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

import io.renren.modules.ltt.dao.AtUsernameGroupDao;
import io.renren.modules.ltt.entity.AtUsernameGroupEntity;
import io.renren.modules.ltt.dto.AtUsernameGroupDTO;
import io.renren.modules.ltt.vo.AtUsernameGroupVO;
import io.renren.modules.ltt.service.AtUsernameGroupService;
import io.renren.modules.ltt.conver.AtUsernameGroupConver;

import java.io.Serializable;
import java.util.Collection;


@Service("atUsernameGroupService")
@Game
public class AtUsernameGroupServiceImpl extends ServiceImpl<AtUsernameGroupDao, AtUsernameGroupEntity> implements AtUsernameGroupService {

    @Override
    public PageUtils<AtUsernameGroupVO> queryPage(AtUsernameGroupDTO atUsernameGroup) {
        IPage<AtUsernameGroupEntity> page = baseMapper.selectPage(
                new Query<AtUsernameGroupEntity>(atUsernameGroup).getPage(),
                new QueryWrapper<AtUsernameGroupEntity>()
        );

        return PageUtils.<AtUsernameGroupVO>page(page).setList(AtUsernameGroupConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtUsernameGroupVO getById(Integer id) {
        return AtUsernameGroupConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtUsernameGroupDTO atUsernameGroup) {
        atUsernameGroup.setCreateTime(DateUtil.date());
        atUsernameGroup.setDeleteFlag(DeleteFlag.NO.getKey());
        AtUsernameGroupEntity atUsernameGroupEntity = AtUsernameGroupConver.MAPPER.converDTO(atUsernameGroup);
        return this.save(atUsernameGroupEntity);
    }

    @Override
    public boolean updateById(AtUsernameGroupDTO atUsernameGroup) {
        AtUsernameGroupEntity atUsernameGroupEntity = AtUsernameGroupConver.MAPPER.converDTO(atUsernameGroup);
        return this.updateById(atUsernameGroupEntity);
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
