package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUserDao;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.vo.AtUserVO;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.modules.ltt.conver.AtUserConver;

import java.io.Serializable;
import java.util.Collection;


@Service("atUserService")
@Game
public class AtUserServiceImpl extends ServiceImpl<AtUserDao, AtUserEntity> implements AtUserService {

    @Override
    public PageUtils<AtUserVO> queryPage(AtUserDTO atUser) {
        IPage<AtUserEntity> page = baseMapper.selectPage(
                new Query<AtUserEntity>(atUser).getPage(),
                new QueryWrapper<AtUserEntity>()
        );

        return PageUtils.<AtUserVO>page(page).setList(AtUserConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtUserVO getById(Integer id) {
        return AtUserConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtUserDTO atUser) {
        AtUserEntity atUserEntity = AtUserConver.MAPPER.converDTO(atUser);
        return this.save(atUserEntity);
    }

    @Override
    public boolean updateById(AtUserDTO atUser) {
        AtUserEntity atUserEntity = AtUserConver.MAPPER.converDTO(atUser);
        return this.updateById(atUserEntity);
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
