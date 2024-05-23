package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.enums.DeleteFlag;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUserGroupDao;
import io.renren.modules.ltt.entity.AtUserGroupEntity;
import io.renren.modules.ltt.dto.AtUserGroupDTO;
import io.renren.modules.ltt.vo.AtUserGroupVO;
import io.renren.modules.ltt.service.AtUserGroupService;
import io.renren.modules.ltt.conver.AtUserGroupConver;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("atUserGroupService")
@Game
public class AtUserGroupServiceImpl extends ServiceImpl<AtUserGroupDao, AtUserGroupEntity> implements AtUserGroupService {

    @Override
    public PageUtils<AtUserGroupVO> queryPage(AtUserGroupDTO atUserGroup) {
        IPage<AtUserGroupEntity> page = baseMapper.selectPage(
                new Query<AtUserGroupEntity>(atUserGroup).getPage(),
                new QueryWrapper<AtUserGroupEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(atUserGroup.getSysUserId()), AtUserGroupEntity::getSysUserId, atUserGroup.getSysUserId())
                        .like(ObjectUtil.isNotNull(atUserGroup.getName()), AtUserGroupEntity::getName, atUserGroup.getName())
                        .orderByDesc(AtUserGroupEntity::getId)
        );

        return PageUtils.<AtUserGroupVO>page(page).setList(AtUserGroupConver.MAPPER.conver(page.getRecords()));
    }

    @Override
    public List<AtUserGroupVO> queryByFuzzyName(String userGroupName, Long sysUserId) {
        List<AtUserGroupEntity> list = this.list(new QueryWrapper<AtUserGroupEntity>().lambda()
                .likeRight(StringUtils.isNotEmpty(userGroupName), AtUserGroupEntity::getName, userGroupName)
                .eq(ObjectUtil.isNotNull(sysUserId) ,AtUserGroupEntity::getSysUserId, sysUserId)
                .orderByDesc(AtUserGroupEntity::getId)
                .last("limit " + 20));
        return AtUserGroupConver.MAPPER.conver(list);
    }

    @Override
    public AtUserGroupVO getById(Integer id) {
        return AtUserGroupConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public List<AtUserGroupVO> getByIds(List<Integer> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<AtUserGroupEntity> list = baseMapper.selectBatchIds(ids);
        return AtUserGroupConver.MAPPER.conver(list);
    }

    @Override
    public Map<Integer, String> getMapByIds(List<Integer> ids) {
        List<AtUserGroupVO> list = getByIds(ids);
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(AtUserGroupVO::getId, AtUserGroupVO::getName));
    }

    @Override
    public boolean save(AtUserGroupDTO atUserGroup) {
        atUserGroup.setCreateTime(DateUtil.date());
        atUserGroup.setDeleteFlag(DeleteFlag.NO.getKey());
        AtUserGroupEntity atUserGroupEntity = AtUserGroupConver.MAPPER.converDTO(atUserGroup);
        return this.save(atUserGroupEntity);
    }

    @Override
    public boolean updateById(AtUserGroupDTO atUserGroup) {
        AtUserGroupEntity atUserGroupEntity = AtUserGroupConver.MAPPER.converDTO(atUserGroup);
        return this.updateById(atUserGroupEntity);
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
