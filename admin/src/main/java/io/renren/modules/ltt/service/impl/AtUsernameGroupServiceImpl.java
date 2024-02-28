package io.renren.modules.ltt.service.impl;
import java.util.ArrayList;
import java.util.Date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpUtil;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.entity.AtUsernameEntity;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.service.AtUsernameService;
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
import java.util.Collection;
import java.util.List;


@Service("atUsernameGroupService")
@Game
public class AtUsernameGroupServiceImpl extends ServiceImpl<AtUsernameGroupDao, AtUsernameGroupEntity> implements AtUsernameGroupService {

    @Override
    public PageUtils<AtUsernameGroupVO> queryPage(AtUsernameGroupDTO atUsernameGroup) {
        IPage<AtUsernameGroupEntity> page = baseMapper.selectPage(
                new Query<AtUsernameGroupEntity>(atUsernameGroup).getPage(),
                new QueryWrapper<AtUsernameGroupEntity>()
        );

        return PageUtils.<AtUsernameGroupVO>page(page).setList(AtUsernameGroupConver.MAPPER.conver(page.getRecords()));
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
        String s = HttpUtil.downloadString(atUsernameGroup.getTxtUrl(), "UTF-8");
        String[] split = s.split("\n");
        Assert.isTrue(ArrayUtil.isEmpty(split),"txt不能为空");
        Assert.isTrue(split.length <= 0,"txt不能为空");

        List<AtUsernameEntity> atUsernameEntities = new ArrayList<>();
        for (String string : split) {
            AtUsernameEntity atUsername = new AtUsernameEntity();
            atUsername.setUsernameGroupId(atUsernameGroupEntity.getId());
            atUsername.setUsername(string);
            atUsername.setUseFlag(UseFlag.NO.getKey());
            atUsername.setDeleteFlag(DeleteFlag.NO.getKey());
            atUsername.setCreateTime(new Date());
            atUsernameEntities.add(atUsername);
        }
        atUsernameService.saveBatch(atUsernameEntities);
        return save;
    }

    @Override
    public boolean updateById(AtUsernameGroupDTO atUsernameGroup) {
        AtUsernameGroupEntity atUsernameGroupEntity = AtUsernameGroupConver.MAPPER.converDTO(atUsernameGroup);
        boolean flag = this.updateById(atUsernameGroupEntity);

        String s = HttpUtil.downloadString(atUsernameGroup.getTxtUrl(), "UTF-8");

        String[] split = s.split("\n");
        Assert.isTrue(ArrayUtil.isEmpty(split),"txt不能为空");
        Assert.isTrue(split.length <= 0,"txt不能为空");

        List<AtUsernameEntity> atUsernameEntities = new ArrayList<>();
        for (String string : split) {
            AtUsernameEntity atUsername = new AtUsernameEntity();
            atUsername.setUsernameGroupId(atUsernameGroupEntity.getId());
            atUsername.setUsername(string);
            atUsername.setUseFlag(UseFlag.NO.getKey());
            atUsername.setDeleteFlag(DeleteFlag.NO.getKey());
            atUsername.setCreateTime(new Date());
            atUsernameEntities.add(atUsername);
        }


        atUsernameService.saveBatch(atUsernameEntities);
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
