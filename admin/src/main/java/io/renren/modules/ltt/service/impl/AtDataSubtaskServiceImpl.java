package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.dto.AtDataSubtaskParamPageDTO;
import io.renren.modules.ltt.dto.AtDataSubtaskResultDto;
import io.renren.modules.ltt.dto.CustomerUserResultDto;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtDataSubtaskDao;
import io.renren.modules.ltt.entity.AtDataSubtaskEntity;
import io.renren.modules.ltt.dto.AtDataSubtaskDTO;
import io.renren.modules.ltt.vo.AtDataSubtaskVO;
import io.renren.modules.ltt.service.AtDataSubtaskService;
import io.renren.modules.ltt.conver.AtDataSubtaskConver;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Service("atDataSubtaskService")
@Game
public class AtDataSubtaskServiceImpl extends ServiceImpl<AtDataSubtaskDao, AtDataSubtaskEntity> implements AtDataSubtaskService {

    @Override
    public PageUtils<AtDataSubtaskResultDto> queryPage(AtDataSubtaskParamPageDTO atDataSubtask) {
        atDataSubtask.setPageStart((atDataSubtask.getPage() - 1) * atDataSubtask.getLimit());
        Integer count = baseMapper.queryPageCount(atDataSubtask);
        List<AtDataSubtaskResultDto> resultList = Collections.emptyList();
        if (count > 0) {
            resultList = baseMapper.queryPage(atDataSubtask);
        }
        return new PageUtils(resultList, count, atDataSubtask.getLimit(), atDataSubtask.getPage());
    }

    @Override
    public AtDataSubtaskVO getById(Integer id) {
        return AtDataSubtaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtDataSubtaskDTO atDataSubtask) {
        AtDataSubtaskEntity atDataSubtaskEntity = AtDataSubtaskConver.MAPPER.converDTO(atDataSubtask);
        return this.save(atDataSubtaskEntity);
    }

    @Override
    public boolean updateById(AtDataSubtaskDTO atDataSubtask) {
        AtDataSubtaskEntity atDataSubtaskEntity = AtDataSubtaskConver.MAPPER.converDTO(atDataSubtask);
        return this.updateById(atDataSubtaskEntity);
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
    public PageUtils listFriend(AtDataSubtaskParamPageDTO atDataSubtask) {
        IPage<AtDataSubtaskEntity> page = baseMapper.selectPage(
                new Query<AtDataSubtaskEntity>(atDataSubtask).getPage(),
                new QueryWrapper<AtDataSubtaskEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(atDataSubtask.getSysUserId()), AtDataSubtaskEntity::getSysUserId, atDataSubtask.getSysUserId())
                        .isNotNull(AtDataSubtaskEntity::getMid)
                        .isNotNull(AtDataSubtaskEntity::getType)
                        .orderByDesc(AtDataSubtaskEntity::getId)
        );

        return PageUtils.<AtDataSubtaskVO>page(page).setList(AtDataSubtaskConver.MAPPER.conver(page.getRecords()));
    }

    @Override
    public List<AtDataSubtaskVO> groupByUserId(AtDataSubtaskEntity dto) {
        return baseMapper.groupByUserId(dto);
    }

    @Override
    public void saveBatchOnMe(List<AtDataSubtaskEntity> atDataSubtaskEntityList) {
        baseMapper.saveBatchOnMe(atDataSubtaskEntityList);
    }

}
