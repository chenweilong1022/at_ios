package io.renren.modules.ltt.service.impl;
import java.util.*;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.dao.AtUserDao;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.Platform;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.modules.ltt.vo.AtUserVO;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.stream.Collectors;


@Service("atUserTokenService")
@Game
public class AtUserTokenServiceImpl extends ServiceImpl<AtUserTokenDao, AtUserTokenEntity> implements AtUserTokenService {


    @Resource(name = "stringListAtUserTokenEntitys")
    private Cache<Integer, AtUserTokenEntity> stringListAtUserTokenEntitys;
    @Autowired
    private AtUserDao atUserService;
    @Resource(name = "stringListAtUserEntitys")
    private Cache<Integer, AtUserEntity> stringListAtUserEntitys;

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
    public AtUserTokenEntity getByUserIdCache(Integer id) {
        AtUserEntity atUserEntity = stringListAtUserEntitys.getIfPresent(id);
        if (ObjectUtil.isNull(atUserEntity)) {
            atUserEntity = atUserService.selectById((Serializable) id);
            stringListAtUserEntitys.put(id,atUserEntity);
        }
        if (ObjectUtil.isNull(atUserEntity)) {
            return null;
        }

        AtUserTokenEntity atUserTokenEntity = stringListAtUserTokenEntitys.getIfPresent(atUserEntity.getUserTokenId());
        if (ObjectUtil.isNull(atUserTokenEntity)) {
            atUserTokenEntity = this.getById((Serializable) atUserEntity.getUserTokenId());
            stringListAtUserTokenEntitys.put(atUserEntity.getUserTokenId(),atUserTokenEntity);
        }
        return atUserTokenEntity.setTelephone(atUserEntity.getTelephone()).setNickName(atUserEntity.getNickName());
    }

//    @Override
//    public Map<Integer, AtUserTokenEntity> getIds(List<Integer> ids) {
//        Map<Integer, AtUserTokenEntity> allPresent = stringListAtUserTokenEntitys.getAllPresent(ids);
//        if (ids.size() == allPresent.keySet().size()) {
//            return allPresent;
//        }
//        List<AtUserTokenEntity> atUserTokenEntities = this.listByIds(ids);
//        Map<Integer, AtUserTokenEntity> collect = atUserTokenEntities.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, item -> item, (a, b) -> a));
//        if (ids.size() == collect.keySet().size()) {
//            stringListAtUserTokenEntitys.putAll(collect);
//            return allPresent;
//        }
//        return null;
//    }

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
    @Override
    public Map<Integer, AtUserTokenEntity> queryMapBatchIds(List<Integer> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<AtUserTokenEntity> list = baseMapper.selectBatchIds(ids);
        if (CollectionUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(AtUserTokenEntity::getId, i -> i));
    }

}
