package io.renren.modules.ltt.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.conver.AtUsernameGroupConver;
import io.renren.modules.ltt.entity.AtDataEntity;
import io.renren.modules.ltt.entity.AtUsernameEntity;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.service.AtDataService;
import io.renren.modules.ltt.vo.AtDataGroupVODataCountGroupIdVO;
import io.renren.modules.ltt.vo.AtUsernameGroupUsernameCountGroupIdVO;
import io.renren.modules.ltt.vo.AtUsernameGroupVO;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;
import java.util.stream.Collectors;


@Service("atDataGroupService")
@Game
public class AtDataGroupServiceImpl extends ServiceImpl<AtDataGroupDao, AtDataGroupEntity> implements AtDataGroupService {

    @Override
    public PageUtils<AtDataGroupVO> queryPage(AtDataGroupDTO atDataGroup) {
        IPage<AtDataGroupEntity> page = baseMapper.selectPage(
                new Query<AtDataGroupEntity>(atDataGroup).getPage(),
                new QueryWrapper<AtDataGroupEntity>()
        );

        //根据分组id转化为map
        List<AtDataGroupVODataCountGroupIdVO> atDataGroupVODataCountGroupIdVOS = atDataService.dataCountGroupId();

        Map<Integer, Integer> integerIntegerMap = atDataGroupVODataCountGroupIdVOS.stream().collect(Collectors.toMap(AtDataGroupVODataCountGroupIdVO::getDataGroupId, AtDataGroupVODataCountGroupIdVO::getDataGroupIdCount));
        //设置分组数量
        List<AtDataGroupVO> records = AtDataGroupConver.MAPPER.conver(page.getRecords());
        for (AtDataGroupVO record : records) {
            record.setDataGroupIdCount(0);
            Integer count = integerIntegerMap.get(record.getId());
            if (ObjectUtil.isNotNull(count)) {
                record.setDataGroupIdCount(count);
            }
        }

        return PageUtils.<AtDataGroupVO>page(page).setList(records);
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

    @Autowired
    private AtDataService atDataService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(AtDataGroupDTO atDataGroup) {
        AtDataGroupEntity atDataGroupEntity = AtDataGroupConver.MAPPER.converDTO(atDataGroup);
        boolean flag = this.updateById(atDataGroupEntity);
        String s = HttpUtil.downloadString(atDataGroup.getTxtUrl(), "UTF-8");
        String[] split = s.split("\n");
        Assert.isTrue(ArrayUtil.isEmpty(split),"txt不能为空");
        Assert.isTrue(split.length <= 0,"txt不能为空");

        List<AtDataEntity> atDataEntities = new ArrayList<>();
        for (String string : split) {
            AtDataEntity atDataEntity = new AtDataEntity();
            atDataEntity.setDataGroupId(atDataGroup.getId());
            atDataEntity.setData(string);
            atDataEntity.setUseFlag(UseFlag.NO.getKey());
            atDataEntity.setDeleteFlag(DeleteFlag.NO.getKey());
            atDataEntity.setCreateTime(new Date());
            atDataEntities.add(atDataEntity);
        }

        atDataService.saveBatch(atDataEntities);
        return flag;
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        boolean flag = super.removeByIds(ids);

        //删除用户分组下的昵称
        atDataService.remove(new QueryWrapper<AtDataEntity>().lambda()
                .in(AtDataEntity::getDataGroupId,ids)
        );
        return flag;
    }

}