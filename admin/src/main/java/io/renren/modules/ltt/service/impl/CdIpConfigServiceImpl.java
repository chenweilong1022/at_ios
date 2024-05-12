package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.renren.common.constant.SystemConstant;
import io.renren.common.utils.EnumUtil;
import io.renren.common.utils.PhoneUtil;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.RedisKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.CdIpConfigDao;
import io.renren.modules.ltt.entity.CdIpConfigEntity;
import io.renren.modules.ltt.dto.CdIpConfigDTO;
import io.renren.modules.ltt.vo.CdIpConfigVO;
import io.renren.modules.ltt.service.CdIpConfigService;
import io.renren.modules.ltt.conver.CdIpConfigConver;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service("cdIpConfigService")
@Game
public class CdIpConfigServiceImpl extends ServiceImpl<CdIpConfigDao, CdIpConfigEntity> implements CdIpConfigService {

    @Resource
    private SystemConstant systemConstant;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageUtils<CdIpConfigVO> queryPage(CdIpConfigDTO cdIpConfig) {
        //获取对列长度
        String regions = EnumUtil.queryValueByKey(cdIpConfig.getCountryCode(), CountryCode.values());
        Long total = redisTemplate.opsForList().size(RedisKeys.RedisKeys9.getValue(regions));
        Query<CdIpConfigEntity> cdIpConfigEntityQuery = new Query<>(cdIpConfig);
        int page = cdIpConfigEntityQuery.getCurrPage();
        int pageSize = cdIpConfigEntityQuery.getLimit();
        int start = (page - 1) * pageSize;
        int end = start + pageSize - 1;
        //获取数据
        List<String> range = redisTemplate.opsForList().range(RedisKeys.RedisKeys9.getValue(regions), start, end);
        IPage iPage = new Page(page,pageSize,total);
        List<CdIpConfigVO> collect = range.stream().map(item -> new CdIpConfigVO().setIp(item)).collect(Collectors.toList());
        return PageUtils.<CdIpConfigVO>page(iPage).setList(collect);
    }

    @Override
    public CdIpConfigVO getById(Integer id) {
        return CdIpConfigConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(CdIpConfigDTO cdIpConfig) {
        Assert.isTrue(ObjectUtil.isNull(cdIpConfig.getCountryCode()),"请选择ip的国家");
        List<String> ips = new ArrayList<>();
        String staticIps = cdIpConfig.getStaticIps();
        String[] split = staticIps.trim().split("\n");
        for (String s : split) {
            if (StrUtil.isEmpty(s)) {
                continue;
            }
            ips.add(s);
        }
        String regions = EnumUtil.queryValueByKey(cdIpConfig.getCountryCode(), CountryCode.values());
        Long l = redisTemplate.opsForList().leftPushAll(RedisKeys.RedisKeys9.getValue(regions), ips);
        if (l > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(CdIpConfigDTO cdIpConfig) {
        CdIpConfigEntity cdIpConfigEntity = CdIpConfigConver.MAPPER.converDTO(cdIpConfig);
        return this.updateById(cdIpConfigEntity);
    }

    @Override
    public boolean updateUsedCountById(Integer id) {
        return baseMapper.updateUsedCountById(id, new Date()) > 0;
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {

        return true;
    }


    @Override
    public CdIpConfigEntity getIpConfig(Integer countryCode) {
        return this.list(new QueryWrapper<CdIpConfigEntity>().lambda()
                .eq(CdIpConfigEntity::getCountryCode, countryCode)
                .lt(CdIpConfigEntity::getUsedCount, systemConstant.getIpConfigMaxUsedCount())
                .last("limit 1")
                .orderByAsc(CdIpConfigEntity::getUsedCount)).stream().findFirst().orElse(null);
    }

    @Override
    public boolean clear(CdIpConfigDTO cdIpConfig) {
        String regions = EnumUtil.queryValueByKey(cdIpConfig.getCountryCode(), CountryCode.values());
        Boolean delete = redisTemplate.delete(RedisKeys.RedisKeys9.getValue(regions));
        if (delete) {
            return true;
        }
        return false;
    }
}
