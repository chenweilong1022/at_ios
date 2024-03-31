package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.dto.IosTokenDTO;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.UseFlag;
import io.renren.modules.ltt.vo.IOSTaskVO;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUserTokenIosDao;
import io.renren.modules.ltt.entity.AtUserTokenIosEntity;
import io.renren.modules.ltt.dto.AtUserTokenIosDTO;
import io.renren.modules.ltt.vo.AtUserTokenIosVO;
import io.renren.modules.ltt.service.AtUserTokenIosService;
import io.renren.modules.ltt.conver.AtUserTokenIosConver;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;


@Service("atUserTokenIosService")
@Game
public class AtUserTokenIosServiceImpl extends ServiceImpl<AtUserTokenIosDao, AtUserTokenIosEntity> implements AtUserTokenIosService {

    @Override
    public PageUtils<AtUserTokenIosVO> queryPage(AtUserTokenIosDTO atUserTokenIos) {
        IPage<AtUserTokenIosEntity> page = baseMapper.selectPage(
                new Query<AtUserTokenIosEntity>(atUserTokenIos).getPage(),
                new QueryWrapper<AtUserTokenIosEntity>()
        );

        return PageUtils.<AtUserTokenIosVO>page(page).setList(AtUserTokenIosConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public AtUserTokenIosVO getById(Integer id) {
        return AtUserTokenIosConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtUserTokenIosDTO atUserTokenIos) {
        AtUserTokenIosEntity atUserTokenIosEntity = AtUserTokenIosConver.MAPPER.converDTO(atUserTokenIos);
        return this.save(atUserTokenIosEntity);
    }

    @Override
    public boolean updateById(AtUserTokenIosDTO atUserTokenIos) {
        AtUserTokenIosEntity atUserTokenIosEntity = AtUserTokenIosConver.MAPPER.converDTO(atUserTokenIos);
        return this.updateById(atUserTokenIosEntity);
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
    @Transactional(rollbackFor = Exception.class)
    public void syncAppToken(IosTokenDTO map) {
        AtUserTokenIosEntity atUserTokenIosEntity = new AtUserTokenIosEntity();
        atUserTokenIosEntity.setCountry(map.getCountry());
        atUserTokenIosEntity.setBundleId(map.getBundleId());
        atUserTokenIosEntity.setAppUserId(map.getAppUserId());
        atUserTokenIosEntity.setUserName(map.getUserName());
        String phoneNumber = StrUtil.cleanBlank(map.getToken().getPhoneNumber());
        atUserTokenIosEntity.setPhoneNumber(phoneNumber);
        atUserTokenIosEntity.setMid(map.getToken().getMid());
        atUserTokenIosEntity.setIosToken(JSONUtil.toJsonStr(map.getToken()));
        atUserTokenIosEntity.setUseFlag(UseFlag.NO.getKey());
        atUserTokenIosEntity.setDeleteFlag(DeleteFlag.NO.getKey());
        atUserTokenIosEntity.setCreateTime(DateUtil.date());
        atUserTokenIosEntity.setDeviceId(map.getDeviceId());
        AtUserTokenIosEntity one = this.getOne(new QueryWrapper<AtUserTokenIosEntity>().lambda()
                .eq(AtUserTokenIosEntity::getPhoneNumber,phoneNumber)
                .last("limit 1")
        );
        if (ObjectUtil.isNull(one)) {
            this.save(atUserTokenIosEntity);
        }else {
            atUserTokenIosEntity.setId(one.getId());
            this.updateById(atUserTokenIosEntity);
        }
    }

    @Resource(name = "stringQueueCacheIOSTaskVO")
    private Cache<String, Queue<IOSTaskVO>> stringQueueCacheIOSTaskVO;

    @Override
    public void taskIosFind(Integer[] ids) {
        for (Integer id : ids) {

            AtUserTokenIosEntity atUserTokenIosEntity = this.getById((Serializable) id);
            IOSTaskVO iosTaskVO = new IOSTaskVO();
            iosTaskVO.setTaskType("find");
            iosTaskVO.setPhone(atUserTokenIosEntity.getPhoneNumber().replace("+",""));

            String deviceId = atUserTokenIosEntity.getDeviceId();
            Queue<IOSTaskVO> cacheIOSTaskVOIfPresent = stringQueueCacheIOSTaskVO.getIfPresent(deviceId);
            if (CollUtil.isEmpty(cacheIOSTaskVOIfPresent) || cacheIOSTaskVOIfPresent.isEmpty()) {
                cacheIOSTaskVOIfPresent = new LinkedList<>();
            }
            cacheIOSTaskVOIfPresent.offer(iosTaskVO);
            stringQueueCacheIOSTaskVO.put(deviceId,cacheIOSTaskVOIfPresent);
        }
    }

}
