package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUserTokenDao;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.dto.AtUserTokenDTO;
import io.renren.modules.ltt.vo.AtUserTokenVO;
import io.renren.modules.ltt.service.AtUserTokenService;
import io.renren.modules.ltt.conver.AtUserTokenConver;

import java.io.Serializable;
import java.util.Collection;


@Service("atUserTokenService")
@Game
public class AtUserTokenServiceImpl extends ServiceImpl<AtUserTokenDao, AtUserTokenEntity> implements AtUserTokenService {

    @Override
    public PageUtils<AtUserTokenVO> queryPage(AtUserTokenDTO atUserToken) {
        IPage<AtUserTokenEntity> page = baseMapper.selectPage(
                new Query<AtUserTokenEntity>(atUserToken).getPage(),
                new QueryWrapper<AtUserTokenEntity>()
        );

        return PageUtils.<AtUserTokenVO>page(page).setList(AtUserTokenConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtUserTokenVO getById(Integer id) {
        return AtUserTokenConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtUserTokenDTO atUserToken) {
        AtUserTokenEntity atUserTokenEntity = AtUserTokenConver.MAPPER.converDTO(atUserToken);
        return this.save(atUserTokenEntity);
    }

    @Override
    public boolean updateById(AtUserTokenDTO atUserToken) {
        AtUserTokenEntity atUserTokenEntity = AtUserTokenConver.MAPPER.converDTO(atUserToken);
        return this.updateById(atUserTokenEntity);
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
