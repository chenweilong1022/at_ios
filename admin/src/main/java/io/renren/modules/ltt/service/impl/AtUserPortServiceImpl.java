package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.sys.service.SysUserService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUserPortDao;
import io.renren.modules.ltt.entity.AtUserPortEntity;
import io.renren.modules.ltt.dto.AtUserPortDTO;
import io.renren.modules.ltt.vo.AtUserPortVO;
import io.renren.modules.ltt.service.AtUserPortService;
import io.renren.modules.ltt.conver.AtUserPortConver;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@Service("atUserPortService")
@Game
public class AtUserPortServiceImpl extends ServiceImpl<AtUserPortDao, AtUserPortEntity> implements AtUserPortService {

    @Resource
    private SysUserService sysUserService;
    @Override
    public PageUtils<AtUserPortVO> queryPage(AtUserPortDTO atUserPort) {
        IPage<AtUserPortEntity> page = baseMapper.selectPage(
                new Query<AtUserPortEntity>(atUserPort).getPage(),
                new QueryWrapper<AtUserPortEntity>().lambda()
                        .orderByDesc(AtUserPortEntity::getCreateTime)
        );

        List<AtUserPortVO> converList = AtUserPortConver.MAPPER.conver(page.getRecords());

        //查询账户名称
        if (CollectionUtil.isNotEmpty(converList)) {
            List<Long> sysUserIdList = converList.stream()
                    .filter(i -> i.getSysUserId() != null)
                    .map(AtUserPortVO::getSysUserId).collect(Collectors.toList());
            Map<Long, String> sysUserMap = sysUserService.queryUserNameByUserIdList(sysUserIdList);
            for (AtUserPortVO atUserPortVO : converList) {
                atUserPortVO.setSysUserName(sysUserMap.get(atUserPortVO.getSysUserId()));
            }
        }

        return PageUtils.<AtUserPortVO>page(page).setList(converList);
    }
    @Override
    public AtUserPortVO getById(Integer id) {
        AtUserPortVO atUserPortVO = AtUserPortConver.MAPPER.conver(baseMapper.selectById(id));
        Map<Long, String> sysUserMap = sysUserService
                .queryUserNameByUserIdList(Collections.singletonList(atUserPortVO.getSysUserId()));
        atUserPortVO.setSysUserName(sysUserMap.get(atUserPortVO.getSysUserId()));
        return atUserPortVO;
    }

    @Override
    public boolean save(AtUserPortDTO atUserPort) {
        AtUserPortEntity atUserPortEntity = AtUserPortConver.MAPPER.converDTO(atUserPort);
        atUserPortEntity.setCreateTime(new Date());
        atUserPortEntity.setDeleteFlag(DeleteFlag.NO.getKey());
        return this.save(atUserPortEntity);
    }

    @Override
    public boolean updateById(AtUserPortDTO atUserPort) {
        AtUserPortEntity atUserPortEntity = AtUserPortConver.MAPPER.converDTO(atUserPort);
        atUserPortEntity.setCreateTime(new Date());
        atUserPortEntity.setDeleteFlag(DeleteFlag.NO.getKey());
        return this.updateById(atUserPortEntity);
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
