package io.renren.modules.ltt.service.impl;

import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.CdGetPhoneDao;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import io.renren.modules.ltt.dto.CdGetPhoneDTO;
import io.renren.modules.ltt.vo.CdGetPhoneVO;
import io.renren.modules.ltt.service.CdGetPhoneService;
import io.renren.modules.ltt.conver.CdGetPhoneConver;

import java.io.Serializable;
import java.util.Collection;


@Service("cdGetPhoneService")
@Game
public class CdGetPhoneServiceImpl extends ServiceImpl<CdGetPhoneDao, CdGetPhoneEntity> implements CdGetPhoneService {

    @Override
    public PageUtils<CdGetPhoneVO> queryPage(CdGetPhoneDTO cdGetPhone) {
        IPage<CdGetPhoneEntity> page = baseMapper.selectPage(
                new Query<CdGetPhoneEntity>(cdGetPhone).getPage(),
                new QueryWrapper<CdGetPhoneEntity>()
        );

        return PageUtils.<CdGetPhoneVO>page(page).setList(CdGetPhoneConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public CdGetPhoneVO getById(Integer id) {
        return CdGetPhoneConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(CdGetPhoneDTO cdGetPhone) {
        CdGetPhoneEntity cdGetPhoneEntity = CdGetPhoneConver.MAPPER.converDTO(cdGetPhone);
        return this.save(cdGetPhoneEntity);
    }

    @Override
    public boolean updateById(CdGetPhoneDTO cdGetPhone) {
        CdGetPhoneEntity cdGetPhoneEntity = CdGetPhoneConver.MAPPER.converDTO(cdGetPhone);
        return this.updateById(cdGetPhoneEntity);
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
