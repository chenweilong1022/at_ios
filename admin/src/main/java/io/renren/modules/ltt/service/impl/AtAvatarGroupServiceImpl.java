package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.entity.AtAvatarEntity;
import io.renren.modules.ltt.entity.AtUsernameEntity;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.service.AtAvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtAvatarGroupDao;
import io.renren.modules.ltt.entity.AtAvatarGroupEntity;
import io.renren.modules.ltt.dto.AtAvatarGroupDTO;
import io.renren.modules.ltt.vo.AtAvatarGroupVO;
import io.renren.modules.ltt.service.AtAvatarGroupService;
import io.renren.modules.ltt.conver.AtAvatarGroupConver;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Service("atAvatarGroupService")
@Game
public class AtAvatarGroupServiceImpl extends ServiceImpl<AtAvatarGroupDao, AtAvatarGroupEntity> implements AtAvatarGroupService {

    @Override
    public PageUtils<AtAvatarGroupVO> queryPage(AtAvatarGroupDTO atAvatarGroup) {
        IPage<AtAvatarGroupEntity> page = baseMapper.selectPage(
                new Query<AtAvatarGroupEntity>(atAvatarGroup).getPage(),
                new QueryWrapper<AtAvatarGroupEntity>()
        );

        return PageUtils.<AtAvatarGroupVO>page(page).setList(AtAvatarGroupConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtAvatarGroupVO getById(Integer id) {
        return AtAvatarGroupConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtAvatarGroupDTO atAvatarGroup) {
        atAvatarGroup.setCreateTime(DateUtil.date());
        atAvatarGroup.setDeleteFlag(DeleteFlag.NO.getKey());
        AtAvatarGroupEntity atAvatarGroupEntity = AtAvatarGroupConver.MAPPER.converDTO(atAvatarGroup);
        return this.save(atAvatarGroupEntity);
    }

    @Autowired
    private AtAvatarService atAvatarService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(AtAvatarGroupDTO atAvatarGroup) {
        AtAvatarGroupEntity atAvatarGroupEntity = AtAvatarGroupConver.MAPPER.converDTO(atAvatarGroup);
        boolean flag = this.updateById(atAvatarGroupEntity);

        List<String> avatarList = atAvatarGroup.getAvatarList();
        Assert.isTrue(CollUtil.isEmpty(avatarList),"头像不能为空");

        List<AtAvatarEntity> atUsernameEntities = new ArrayList<>();
        for (String string : avatarList) {
            AtAvatarEntity atUsername = new AtAvatarEntity();
            atUsername.setAvatarGroupId(atAvatarGroupEntity.getId());
            atUsername.setAvatar(string);
            atUsername.setUseFlag(UseFlag.NO.getKey());
            atUsername.setDeleteFlag(DeleteFlag.NO.getKey());
            atUsername.setCreateTime(new Date());
            atUsernameEntities.add(atUsername);
        }
        atAvatarService.saveBatch(atUsernameEntities);
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
