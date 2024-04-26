package io.renren.modules.ltt.service.impl;

import io.renren.common.constant.SystemConstant;
import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.CdIpConfigDao;
import io.renren.modules.ltt.entity.CdIpConfigEntity;
import io.renren.modules.ltt.dto.CdIpConfigDTO;
import io.renren.modules.ltt.vo.CdIpConfigVO;
import io.renren.modules.ltt.service.CdIpConfigService;
import io.renren.modules.ltt.conver.CdIpConfigConver;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


@Service("cdIpConfigService")
@Game
public class CdIpConfigServiceImpl extends ServiceImpl<CdIpConfigDao, CdIpConfigEntity> implements CdIpConfigService {

    @Resource
    private SystemConstant systemConstant;

    @Override
    public PageUtils<CdIpConfigVO> queryPage(CdIpConfigDTO cdIpConfig) {
        IPage<CdIpConfigEntity> page = baseMapper.selectPage(
                new Query<CdIpConfigEntity>(cdIpConfig).getPage(),
                new QueryWrapper<CdIpConfigEntity>()
        );

        return PageUtils.<CdIpConfigVO>page(page).setList(CdIpConfigConver.MAPPER.conver(page.getRecords()));
    }

    @Override
    public CdIpConfigVO getById(Integer id) {
        return CdIpConfigConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(CdIpConfigDTO cdIpConfig) {
        CdIpConfigEntity cdIpConfigEntity = CdIpConfigConver.MAPPER.converDTO(cdIpConfig);
        return this.save(cdIpConfigEntity);
    }

    @Override
    public boolean updateById(CdIpConfigDTO cdIpConfig) {
        CdIpConfigEntity cdIpConfigEntity = CdIpConfigConver.MAPPER.converDTO(cdIpConfig);
        return this.updateById(cdIpConfigEntity);
    }

    @Override
    public boolean updateUsedCountById(Integer id) {
        return baseMapper.updateUsedCountById(id, new Date()) > 0;
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
    public CdIpConfigEntity getIpConfig(Integer countryCode) {
        return this.list(new QueryWrapper<CdIpConfigEntity>().lambda()
                .eq(CdIpConfigEntity::getCountryCode, countryCode)
                .lt(CdIpConfigEntity::getUsedCount, systemConstant.getIpConfigMaxUsedCount())
                .last("limit 1")
                .orderByAsc(CdIpConfigEntity::getUsedCount)).stream().findFirst().orElse(null);
    }
}
