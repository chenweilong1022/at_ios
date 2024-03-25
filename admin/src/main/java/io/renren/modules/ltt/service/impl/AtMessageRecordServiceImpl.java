package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtMessageRecordDao;
import io.renren.modules.ltt.entity.AtMessageRecordEntity;
import io.renren.modules.ltt.dto.AtMessageRecordDTO;
import io.renren.modules.ltt.vo.AtMessageRecordVO;
import io.renren.modules.ltt.service.AtMessageRecordService;
import io.renren.modules.ltt.conver.AtMessageRecordConver;

import java.io.Serializable;
import java.util.Collection;


@Service("atMessageRecordService")
@Game
public class AtMessageRecordServiceImpl extends ServiceImpl<AtMessageRecordDao, AtMessageRecordEntity> implements AtMessageRecordService {

    @Override
    public PageUtils<AtMessageRecordVO> queryPage(AtMessageRecordDTO atMessageRecord) {
        IPage<AtMessageRecordEntity> page = baseMapper.selectPage(
                new Query<AtMessageRecordEntity>(atMessageRecord).getPage(),
                new QueryWrapper<AtMessageRecordEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(atMessageRecord.getSysUserId()),
                                AtMessageRecordEntity::getSysUserId, atMessageRecord.getSysUserId())
                        .orderByDesc(AtMessageRecordEntity::getId)
        );

        return PageUtils.<AtMessageRecordVO>page(page).setList(AtMessageRecordConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtMessageRecordVO getById(Integer id) {
        return AtMessageRecordConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtMessageRecordDTO atMessageRecord) {
        AtMessageRecordEntity atMessageRecordEntity = AtMessageRecordConver.MAPPER.converDTO(atMessageRecord);
        return this.save(atMessageRecordEntity);
    }

    @Override
    public boolean updateById(AtMessageRecordDTO atMessageRecord) {
        AtMessageRecordEntity atMessageRecordEntity = AtMessageRecordConver.MAPPER.converDTO(atMessageRecord);
        return this.updateById(atMessageRecordEntity);
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
