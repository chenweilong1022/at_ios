package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.vo.AtDataGroupVODataCountGroupIdVO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtDataDao;
import io.renren.modules.ltt.entity.AtDataEntity;
import io.renren.modules.ltt.dto.AtDataDTO;
import io.renren.modules.ltt.vo.AtDataVO;
import io.renren.modules.ltt.service.AtDataService;
import io.renren.modules.ltt.conver.AtDataConver;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Service("atDataService")
@Game
public class AtDataServiceImpl extends ServiceImpl<AtDataDao, AtDataEntity> implements AtDataService {

    @Override
    public PageUtils<AtDataVO> queryPage(AtDataDTO atData) {
        IPage<AtDataEntity> page = baseMapper.selectPage(
                new Query<AtDataEntity>(atData).getPage(),
                new QueryWrapper<AtDataEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(atData.getSysUserId()), AtDataEntity::getSysUserId, atData.getSysUserId())
                        .orderByDesc(AtDataEntity::getId)
        );

        return PageUtils.<AtDataVO>page(page).setList(AtDataConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtDataVO getById(Integer id) {
        return AtDataConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtDataDTO atData) {
        AtDataEntity atDataEntity = AtDataConver.MAPPER.converDTO(atData);
        return this.save(atDataEntity);
    }

    @Override
    public boolean updateById(AtDataDTO atData) {
        AtDataEntity atDataEntity = AtDataConver.MAPPER.converDTO(atData);
        return this.updateById(atDataEntity);
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
    public List<AtDataGroupVODataCountGroupIdVO> dataCountGroupId(List<Integer> dataGroupIdList) {
        return baseMapper.dataCountGroupId(dataGroupIdList);
    }

}
