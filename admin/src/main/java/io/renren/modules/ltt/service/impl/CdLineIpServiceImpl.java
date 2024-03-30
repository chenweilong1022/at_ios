package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.CdLineIpDao;
import io.renren.modules.ltt.entity.CdLineIpEntity;
import io.renren.modules.ltt.dto.CdLineIpDTO;
import io.renren.modules.ltt.vo.CdLineIpVO;
import io.renren.modules.ltt.service.CdLineIpService;
import io.renren.modules.ltt.conver.CdLineIpConver;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


@Service("cdLineIpService")
@Game
public class CdLineIpServiceImpl extends ServiceImpl<CdLineIpDao, CdLineIpEntity> implements CdLineIpService {

    @Override
    public PageUtils<CdLineIpVO> queryPage(CdLineIpDTO cdLineIp) {
        IPage<CdLineIpEntity> page = baseMapper.selectPage(
                new Query<CdLineIpEntity>(cdLineIp).getPage(),
                new QueryWrapper<CdLineIpEntity>()
        );

        return PageUtils.<CdLineIpVO>page(page).setList(CdLineIpConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public CdLineIpVO getById(Integer id) {
        return CdLineIpConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(CdLineIpDTO cdLineIp) {
        CdLineIpEntity one = this.getOne(new QueryWrapper<CdLineIpEntity>().lambda()
                .eq(CdLineIpEntity::getIp,cdLineIp.getIp())
                .last("limit 1")
        );
        if (ObjectUtil.isNull(one)) {
            cdLineIp.setCreateTime(new Date());
            CdLineIpEntity cdLineIpEntity = CdLineIpConver.MAPPER.converDTO(cdLineIp);
            return this.save(cdLineIpEntity);
        }
        return false;
    }

    @Override
    public boolean updateById(CdLineIpDTO cdLineIp) {
        CdLineIpEntity cdLineIpEntity = CdLineIpConver.MAPPER.converDTO(cdLineIp);
        return this.updateById(cdLineIpEntity);
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
