package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.enums.RegistrationStatus;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.CdRegisterTaskDao;
import io.renren.modules.ltt.entity.CdRegisterTaskEntity;
import io.renren.modules.ltt.dto.CdRegisterTaskDTO;
import io.renren.modules.ltt.vo.CdRegisterTaskVO;
import io.renren.modules.ltt.service.CdRegisterTaskService;
import io.renren.modules.ltt.conver.CdRegisterTaskConver;

import java.io.Serializable;
import java.util.Collection;


@Service("cdRegisterTaskService")
@Game
public class CdRegisterTaskServiceImpl extends ServiceImpl<CdRegisterTaskDao, CdRegisterTaskEntity> implements CdRegisterTaskService {

    @Override
    public PageUtils<CdRegisterTaskVO> queryPage(CdRegisterTaskDTO cdRegisterTask) {
        IPage<CdRegisterTaskEntity> page = baseMapper.selectPage(
                new Query<CdRegisterTaskEntity>(cdRegisterTask).getPage(),
                new QueryWrapper<CdRegisterTaskEntity>()
        );

        return PageUtils.<CdRegisterTaskVO>page(page).setList(CdRegisterTaskConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public CdRegisterTaskVO getById(Integer id) {
        return CdRegisterTaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(CdRegisterTaskDTO cdRegisterTask) {
        cdRegisterTask.setNumberRegistered(0);
        cdRegisterTask.setNumberSuccesses(0);
        cdRegisterTask.setNumberFailures(0);
        cdRegisterTask.setRegistrationStatus(RegistrationStatus.RegistrationStatus1.getKey());
        CdRegisterTaskEntity cdRegisterTaskEntity = CdRegisterTaskConver.MAPPER.converDTO(cdRegisterTask);
        return this.save(cdRegisterTaskEntity);
    }

    @Override
    public boolean updateById(CdRegisterTaskDTO cdRegisterTask) {
        CdRegisterTaskEntity cdRegisterTaskEntity = CdRegisterTaskConver.MAPPER.converDTO(cdRegisterTask);
        return this.updateById(cdRegisterTaskEntity);
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
