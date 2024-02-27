package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.LttUserDao;
import io.renren.modules.ltt.entity.LttUserEntity;
import io.renren.modules.ltt.dto.LttUserDTO;
import io.renren.modules.ltt.vo.LttUserVO;
import io.renren.modules.ltt.service.LttUserService;
import io.renren.modules.ltt.conver.LttUserConver;

import java.io.Serializable;
import java.util.Collection;


@Service("lttUserService")
@Game
public class LttUserServiceImpl extends ServiceImpl<LttUserDao, LttUserEntity> implements LttUserService {

    @Override
    public PageUtils<LttUserVO> queryPage(LttUserDTO lttUser) {
        IPage<LttUserEntity> page = baseMapper.selectPage(
                new Query<LttUserEntity>(lttUser).getPage(),
                new QueryWrapper<LttUserEntity>()
        );

        return PageUtils.<LttUserVO>page(page).setList(LttUserConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public LttUserVO getById(Long id) {
        return LttUserConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(LttUserDTO lttUser) {
        LttUserEntity lttUserEntity = LttUserConver.MAPPER.converDTO(lttUser);
        return this.save(lttUserEntity);
    }

    @Override
    public boolean updateById(LttUserDTO lttUser) {
        LttUserEntity lttUserEntity = LttUserConver.MAPPER.converDTO(lttUser);
        return this.updateById(lttUserEntity);
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
