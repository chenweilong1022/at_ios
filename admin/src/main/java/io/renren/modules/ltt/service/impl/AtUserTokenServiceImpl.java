package io.renren.modules.ltt.service.impl;
import java.util.Date;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.Platform;
import io.renren.modules.ltt.enums.UseFlag;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUserTokenDao;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.dto.AtUserTokenDTO;
import io.renren.modules.ltt.vo.AtUserTokenVO;
import io.renren.modules.ltt.service.AtUserTokenService;
import io.renren.modules.ltt.conver.AtUserTokenConver;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service("atUserTokenService")
@Game
public class AtUserTokenServiceImpl extends ServiceImpl<AtUserTokenDao, AtUserTokenEntity> implements AtUserTokenService {

    @Override
    public PageUtils<AtUserTokenVO> queryPage(AtUserTokenDTO atUserToken) {
        IPage<AtUserTokenEntity> page = baseMapper.selectPage(
                new Query<AtUserTokenEntity>(atUserToken).getPage(),
                new QueryWrapper<AtUserTokenEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(atUserToken.getSysUserId()),
                                AtUserTokenEntity::getSysUserId, atUserToken.getSysUserId())
                        .orderByDesc(AtUserTokenEntity::getId)
        );

        return PageUtils.<AtUserTokenVO>page(page).setList(AtUserTokenConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtUserTokenVO getById(Integer id) {
        return AtUserTokenConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(AtUserTokenDTO atUserToken) {
        String s = HttpUtil.downloadString(atUserToken.getTxtUrl(), "UTF-8");
        String[] split = s.split("\n");
        Assert.isTrue(ArrayUtil.isEmpty(split),"txt不能为空");
        Assert.isTrue(split.length <= 0,"txt不能为空");
        List<AtUserTokenEntity> atUserTokenEntities = new ArrayList<>();
        for (String string : split) {
            AtUserTokenEntity userTokenEntity = new AtUserTokenEntity();
            userTokenEntity.setToken(string);
            userTokenEntity.setUseFlag(UseFlag.NO.getKey());
            userTokenEntity.setDeleteFlag(DeleteFlag.NO.getKey());
            userTokenEntity.setCreateTime(new Date());
            userTokenEntity.setPlatform(atUserToken.getPlatform());
            userTokenEntity.setUserGroupId(atUserToken.getUserGroupId());
            userTokenEntity.setSysUserId(atUserToken.getSysUserId());
            atUserTokenEntities.add(userTokenEntity);
        }
        return this.saveBatch(atUserTokenEntities);
    }

    @Override
    public boolean saveUserTokenBatch(List<AtUserTokenEntity>  userTokenEntityList) {
        for (AtUserTokenEntity userTokenEntity : userTokenEntityList) {
            userTokenEntity.setUseFlag(UseFlag.NO.getKey());
            userTokenEntity.setDeleteFlag(DeleteFlag.NO.getKey());
            userTokenEntity.setCreateTime(new Date());
            userTokenEntity.setPlatform(Platform.IOS.getKey());
        }
        return this.saveBatch(userTokenEntityList);
    }

    @Override
    public boolean updateById(AtUserTokenDTO atUserToken) {
        AtUserTokenEntity atUserTokenEntity = AtUserTokenConver.MAPPER.converDTO(atUserToken);
        return this.updateById(atUserTokenEntity);
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
    public List<AtUserTokenEntity> selectBatchIds(List<Integer> ids) {
        return baseMapper.selectBatchIds(ids);
    }

}
