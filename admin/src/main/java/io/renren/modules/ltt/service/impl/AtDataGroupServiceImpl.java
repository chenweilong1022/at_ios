package io.renren.modules.ltt.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpUtil;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.enums.DeleteFlag;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtDataGroupDao;
import io.renren.modules.ltt.entity.AtDataGroupEntity;
import io.renren.modules.ltt.dto.AtDataGroupDTO;
import io.renren.modules.ltt.vo.AtDataGroupVO;
import io.renren.modules.ltt.service.AtDataGroupService;
import io.renren.modules.ltt.conver.AtDataGroupConver;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;


@Service("atDataGroupService")
@Game
public class AtDataGroupServiceImpl extends ServiceImpl<AtDataGroupDao, AtDataGroupEntity> implements AtDataGroupService {

    @Override
    public PageUtils<AtDataGroupVO> queryPage(AtDataGroupDTO atDataGroup) {
        IPage<AtDataGroupEntity> page = baseMapper.selectPage(
                new Query<AtDataGroupEntity>(atDataGroup).getPage(),
                new QueryWrapper<AtDataGroupEntity>()
        );

        return PageUtils.<AtDataGroupVO>page(page).setList(AtDataGroupConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtDataGroupVO getById(Integer id) {
        return AtDataGroupConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtDataGroupDTO atDataGroup) {
        atDataGroup.setCreateTime(DateUtil.date());
        atDataGroup.setDeleteFlag(DeleteFlag.NO.getKey());
        AtDataGroupEntity atDataGroupEntity = AtDataGroupConver.MAPPER.converDTO(atDataGroup);
        return this.save(atDataGroupEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(AtDataGroupDTO atDataGroup) {
        AtDataGroupEntity atDataGroupEntity = AtDataGroupConver.MAPPER.converDTO(atDataGroup);
        boolean flag = this.updateById(atDataGroupEntity);
        String s = HttpUtil.downloadString(atDataGroup.getTxtUrl(), "UTF-8");
        String[] split = s.split("\n");
        Assert.isTrue(ArrayUtil.isEmpty(split),"txt不能为空");
        Assert.isTrue(split.length <= 0,"txt不能为空");


        return flag;
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
