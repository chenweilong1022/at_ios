package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.renren.common.utils.DateUtils;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.dao.AtUserDao;
import io.renren.modules.ltt.dto.PortDataSummaryResultDto;
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

import static java.time.LocalTime.now;


@Service("atUserPortService")
@Game
public class AtUserPortServiceImpl extends ServiceImpl<AtUserPortDao, AtUserPortEntity> implements AtUserPortService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private AtUserDao atUserDao;

    @Override
    public PageUtils<AtUserPortVO> queryPage(AtUserPortDTO atUserPort) {
        IPage<AtUserPortEntity> page = baseMapper.selectPage(
                new Query<AtUserPortEntity>(atUserPort).getPage(),
                new QueryWrapper<AtUserPortEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(atUserPort.getSysUserId()),
                                AtUserPortEntity::getSysUserId, atUserPort.getSysUserId())
                        .orderByDesc(AtUserPortEntity::getId)
        );

        List<AtUserPortVO> converList = AtUserPortConver.MAPPER.conver(page.getRecords());

        //查询账户名称
        if (CollectionUtil.isNotEmpty(converList)) {
            List<Long> sysUserIdList = converList.stream()
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

    /**
     * 查询用户未过期端口
     *
     * @return
     */
    @Override
    public List<AtUserPortEntity> queryValidPort(Long sysUserId) {
        return baseMapper
                .selectList(new QueryWrapper<AtUserPortEntity>().lambda()
                        .eq(AtUserPortEntity::getSysUserId, sysUserId)
                        .gt(AtUserPortEntity::getExpireTime, new Date()));
    }

    @Override
    public PortDataSummaryResultDto portDataSummary(Long sysUserId) {
        //查询用户未过期端口
        List<AtUserPortEntity> portList = this.queryValidPort(sysUserId);
        Assert.isTrue(CollectionUtil.isEmpty(portList), "端口已过期");

        //端口总数
        Integer portNumTotal = portList.stream().mapToInt(AtUserPortEntity::getPortNum).sum();
        //过期时间->取最大的时间
        Date expireTime = portList.stream()
                .max(Comparator.comparing(AtUserPortEntity::getExpireTime))
                .map(AtUserPortEntity::getExpireTime).get();
        //端口已用
        Integer atUserCount = atUserDao.queryCountBySysUserId(sysUserId);
        Integer portNumSurplus = 0;
        if (portNumTotal == 0 && portNumTotal <= atUserCount) {
            portNumSurplus = 0;
        }
        if (portNumTotal > atUserCount) {
            portNumSurplus = portNumTotal - atUserCount;
        }

        //返回数据
        PortDataSummaryResultDto resultDto = new PortDataSummaryResultDto();
        resultDto.setPortNumTotal(portNumTotal);
        resultDto.setExpireTime(DateUtils.formatDate(expireTime));
        resultDto.setPortNumSurplus(portNumSurplus);
        return resultDto;
    }
}
