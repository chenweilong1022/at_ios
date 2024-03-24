package io.renren.modules.ltt.service.impl;
import java.util.*;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.entity.AtUsernameEntity;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.service.AtUsernameService;
import io.renren.modules.ltt.vo.AtUsernameGroupUsernameCountGroupIdVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUsernameGroupDao;
import io.renren.modules.ltt.entity.AtUsernameGroupEntity;
import io.renren.modules.ltt.dto.AtUsernameGroupDTO;
import io.renren.modules.ltt.vo.AtUsernameGroupVO;
import io.renren.modules.ltt.service.AtUsernameGroupService;
import io.renren.modules.ltt.conver.AtUsernameGroupConver;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.stream.Collectors;


@Service("atUsernameGroupService")
@Game
public class AtUsernameGroupServiceImpl extends ServiceImpl<AtUsernameGroupDao, AtUsernameGroupEntity> implements AtUsernameGroupService {

    @Override
    public PageUtils<AtUsernameGroupVO> queryPage(AtUsernameGroupDTO atUsernameGroup) {
        IPage<AtUsernameGroupEntity> page = baseMapper.selectPage(
                new Query<AtUsernameGroupEntity>(atUsernameGroup).getPage(),
                new QueryWrapper<AtUsernameGroupEntity>().lambda()
                        .orderByDesc(AtUsernameGroupEntity::getId)
        );
        //根据分组id转化为map
        List<AtUsernameGroupUsernameCountGroupIdVO> atUsernameGroupUsernameCountGroupIdVOS = atUsernameService.usernameCountGroupId();
        Map<Integer, Integer> integerIntegerMap = atUsernameGroupUsernameCountGroupIdVOS.stream().collect(Collectors.toMap(AtUsernameGroupUsernameCountGroupIdVO::getUsernameGroupId, AtUsernameGroupUsernameCountGroupIdVO::getUsernameGroupIdCount));
        //设置分组数量
        //ToDo bug
        List<AtUsernameGroupVO> records = AtUsernameGroupConver.MAPPER.conver(page.getRecords());
        for (AtUsernameGroupVO record : records) {
            record.setUsernameGroupIdCount(0);
            Integer count = integerIntegerMap.get(record.getId());
            if (ObjectUtil.isNotNull(count)) {
                record.setUsernameGroupIdCount(count);
            }
        }
        return PageUtils.<AtUsernameGroupVO>page(page).setList(records);
    }
    @Override
    public AtUsernameGroupVO getById(Integer id) {
        return AtUsernameGroupConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Autowired
    private AtUsernameService atUsernameService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(AtUsernameGroupDTO atUsernameGroup) {
        atUsernameGroup.setCreateTime(DateUtil.date());
        atUsernameGroup.setDeleteFlag(DeleteFlag.NO.getKey());
        AtUsernameGroupEntity atUsernameGroupEntity = AtUsernameGroupConver.MAPPER.converDTO(atUsernameGroup);
        boolean save = this.save(atUsernameGroupEntity);
        return save;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(AtUsernameGroupDTO atUsernameGroup) {
        AtUsernameGroupEntity atUsernameGroupEntity = AtUsernameGroupConver.MAPPER.converDTO(atUsernameGroup);
        return this.updateById(atUsernameGroupEntity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchAtUsername(AtUsernameGroupDTO atUsernameGroup) {
        Assert.isTrue(StringUtils.isEmpty(atUsernameGroup.getTxtUrl()),"数据不能为空");

        String s = HttpUtil.downloadString(atUsernameGroup.getTxtUrl(), "UTF-8");

        String[] split = s.split("\n");
        Assert.isTrue(ArrayUtil.isEmpty(split),"txt不能为空");
        Assert.isTrue(split.length <= 0,"txt不能为空");

        List<AtUsernameEntity> atUsernameEntities = new ArrayList<>();
        for (String string : split) {
            AtUsernameEntity atUsername = new AtUsernameEntity();
            atUsername.setUsernameGroupId(atUsernameGroup.getId());
            atUsername.setUsername(string);
            atUsername.setUseFlag(UseFlag.NO.getKey());
            atUsername.setDeleteFlag(DeleteFlag.NO.getKey());
            atUsername.setCreateTime(new Date());
            atUsernameEntities.add(atUsername);
        }
        return atUsernameService.saveBatch(atUsernameEntities);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        //删除用户分组
        boolean flag = super.removeByIds(ids);
        //删除用户分组下的昵称
        atUsernameService.remove(new QueryWrapper<AtUsernameEntity>().lambda()
                .in(AtUsernameEntity::getUsernameGroupId,ids)
        );
        return flag;
    }

}
