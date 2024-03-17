package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.enums.DeleteFlag;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUserPortDao;
import io.renren.modules.ltt.entity.AtUserPortEntity;
import io.renren.modules.ltt.dto.AtUserPortDTO;
import io.renren.modules.ltt.vo.AtUserPortVO;
import io.renren.modules.ltt.service.AtUserPortService;
import io.renren.modules.ltt.conver.AtUserPortConver;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


@Service("atUserPortService")
@Game
public class AtUserPortServiceImpl extends ServiceImpl<AtUserPortDao, AtUserPortEntity> implements AtUserPortService {

    @Override
    public PageUtils<AtUserPortVO> queryPage(AtUserPortDTO atUserPort) {
        IPage<AtUserPortEntity> page = baseMapper.selectPage(
                new Query<AtUserPortEntity>(atUserPort).getPage(),
                new QueryWrapper<AtUserPortEntity>()
        );

        return PageUtils.<AtUserPortVO>page(page).setList(AtUserPortConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtUserPortVO getById(Integer id) {
        return AtUserPortConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtUserPortDTO atUserPort) {
        AtUserPortEntity atUserPortEntity = AtUserPortConver.MAPPER.converDTO(atUserPort);
        atUserPortEntity.setCreateTime(new Date());
        atUserPortEntity.setDeleteFlag(DeleteFlag.NO.getKey());
        return this.save(atUserPortEntity);
    }

    @Override
    public boolean updateById(AtUserPortDTO atUserPort) {
        AtUserPortEntity atUserPortEntity = AtUserPortConver.MAPPER.converDTO(atUserPort);
        atUserPortEntity.setCreateTime(new Date());
        atUserPortEntity.setDeleteFlag(DeleteFlag.NO.getKey());
        return this.updateById(atUserPortEntity);
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
