package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.CdRegisterSubtasksDao;
import io.renren.modules.ltt.entity.CdRegisterSubtasksEntity;
import io.renren.modules.ltt.dto.CdRegisterSubtasksDTO;
import io.renren.modules.ltt.vo.CdRegisterSubtasksVO;
import io.renren.modules.ltt.service.CdRegisterSubtasksService;
import io.renren.modules.ltt.conver.CdRegisterSubtasksConver;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Service("cdRegisterSubtasksService")
@Game
public class CdRegisterSubtasksServiceImpl extends ServiceImpl<CdRegisterSubtasksDao, CdRegisterSubtasksEntity> implements CdRegisterSubtasksService {

    @Override
    public PageUtils<CdRegisterSubtasksVO> queryPage(CdRegisterSubtasksDTO cdRegisterSubtasks) {
        IPage<CdRegisterSubtasksEntity> page = baseMapper.selectPage(
                new Query<CdRegisterSubtasksEntity>(cdRegisterSubtasks).getPage(),
                new QueryWrapper<CdRegisterSubtasksEntity>()
        );

        return PageUtils.<CdRegisterSubtasksVO>page(page).setList(CdRegisterSubtasksConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public CdRegisterSubtasksVO getById(Integer id) {
        return CdRegisterSubtasksConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(CdRegisterSubtasksDTO cdRegisterSubtasks) {
        CdRegisterSubtasksEntity cdRegisterSubtasksEntity = CdRegisterSubtasksConver.MAPPER.converDTO(cdRegisterSubtasks);
        return this.save(cdRegisterSubtasksEntity);
    }

    @Override
    public boolean updateById(CdRegisterSubtasksDTO cdRegisterSubtasks) {
        CdRegisterSubtasksEntity cdRegisterSubtasksEntity = CdRegisterSubtasksConver.MAPPER.converDTO(cdRegisterSubtasks);
        return this.updateById(cdRegisterSubtasksEntity);
    }

    @Override
    public boolean updateStatusByTaskId(Integer taskId, Integer registrationStatus) {
        CdRegisterSubtasksEntity cdRegisterSubtasksEntity = new CdRegisterSubtasksEntity();
        cdRegisterSubtasksEntity.setTaskId(taskId);
        cdRegisterSubtasksEntity.setRegistrationStatus(registrationStatus);
        return baseMapper.update(cdRegisterSubtasksEntity, new QueryWrapper<CdRegisterSubtasksEntity>().lambda()
                .eq(CdRegisterSubtasksEntity::getTaskId, taskId)) > 0;
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
    public List<CdRegisterSubtasksVO> groupByTaskId() {
        return baseMapper.groupByTaskId();
    }

    @Override
    public List<CdRegisterSubtasksEntity> queryByTaskId(Integer taskId) {
        return baseMapper.selectList(new QueryWrapper<CdRegisterSubtasksEntity>().lambda()
                .eq(CdRegisterSubtasksEntity::getTaskId, taskId));
    }
}
