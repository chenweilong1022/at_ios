package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.vo.AtUsernameGroupUsernameCountGroupIdVO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUsernameDao;
import io.renren.modules.ltt.entity.AtUsernameEntity;
import io.renren.modules.ltt.dto.AtUsernameDTO;
import io.renren.modules.ltt.vo.AtUsernameVO;
import io.renren.modules.ltt.service.AtUsernameService;
import io.renren.modules.ltt.conver.AtUsernameConver;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Service("atUsernameService")
@Game
public class AtUsernameServiceImpl extends ServiceImpl<AtUsernameDao, AtUsernameEntity> implements AtUsernameService {

    @Override
    public PageUtils<AtUsernameVO> queryPage(AtUsernameDTO atUsername) {
        IPage<AtUsernameEntity> page = baseMapper.selectPage(
                new Query<AtUsernameEntity>(atUsername).getPage(),
                new QueryWrapper<AtUsernameEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(atUsername.getSysUserId()), AtUsernameEntity::getSysUserId, atUsername.getSysUserId())
                        .orderByDesc(AtUsernameEntity::getId)
        );

        return PageUtils.<AtUsernameVO>page(page).setList(AtUsernameConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtUsernameVO getById(Integer id) {
        return AtUsernameConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtUsernameDTO atUsername) {
        AtUsernameEntity atUsernameEntity = AtUsernameConver.MAPPER.converDTO(atUsername);
        return this.save(atUsernameEntity);
    }

    @Override
    public boolean updateById(AtUsernameDTO atUsername) {
        AtUsernameEntity atUsernameEntity = AtUsernameConver.MAPPER.converDTO(atUsername);
        return this.updateById(atUsernameEntity);
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
    public List<AtUsernameGroupUsernameCountGroupIdVO> usernameCountGroupId() {
        return baseMapper.usernameCountGroupId();
    }

}
