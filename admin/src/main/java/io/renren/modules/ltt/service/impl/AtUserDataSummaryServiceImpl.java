package io.renren.modules.ltt.service.impl;

import io.renren.common.utils.BaseBeanUtils;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.dto.LineRegisterSummaryResultDto;
import io.renren.modules.ltt.dto.UserSummaryResultDto;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.modules.ltt.service.CdGetPhoneService;
import io.renren.modules.ltt.service.CdLineRegisterService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.AtUserDataSummaryDao;
import io.renren.modules.ltt.entity.AtUserDataSummaryEntity;
import io.renren.modules.ltt.dto.AtUserDataSummaryDTO;
import io.renren.modules.ltt.vo.AtUserDataSummaryVO;
import io.renren.modules.ltt.service.AtUserDataSummaryService;
import io.renren.modules.ltt.conver.AtUserDataSummaryConver;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service("atUserDataSummaryService")
@Game
public class AtUserDataSummaryServiceImpl extends ServiceImpl<AtUserDataSummaryDao, AtUserDataSummaryEntity> implements AtUserDataSummaryService {

    @Resource
    private CdGetPhoneService cdGetPhoneService;

    @Resource
    private AtUserService atUserService;

    @Override
    public PageUtils<AtUserDataSummaryVO> queryPage(AtUserDataSummaryDTO atUserDataSummary) {
        IPage<AtUserDataSummaryEntity> page = baseMapper.selectPage(
                new Query<AtUserDataSummaryEntity>(atUserDataSummary).getPage(),
                new QueryWrapper<AtUserDataSummaryEntity>().lambda()
                        .eq(StringUtils.isNotEmpty(atUserDataSummary.getCountryCode()),
                                AtUserDataSummaryEntity::getCountryCode, atUserDataSummary.getCountryCode())
                        .ge(atUserDataSummary.getSearchStartTime() != null,
                                AtUserDataSummaryEntity::getSummaryDate, atUserDataSummary.getSearchStartTime())
                        .le(atUserDataSummary.getSearchEndTime() != null,
                                AtUserDataSummaryEntity::getSummaryDate, atUserDataSummary.getSearchEndTime())
                        .orderByDesc(AtUserDataSummaryEntity::getSummaryDate));

        return PageUtils.<AtUserDataSummaryVO>page(page).setList(AtUserDataSummaryConver.MAPPER.conver(page.getRecords()));
    }

    /**
     * 保存数据统计记录
     *
     * @param searchTime
     */
    @Override
    public void saveAtUserDataSummary(LocalDate searchTime) {
        Map<String, AtUserDataSummaryVO> summaryVOMap = queryDataSummary(searchTime);
        //保存
        for (AtUserDataSummaryVO summaryVO : summaryVOMap.values()) {
            AtUserDataSummaryEntity summaryEntity = baseMapper.selectOne(new QueryWrapper<AtUserDataSummaryEntity>().lambda()
                    .eq(AtUserDataSummaryEntity::getCountryCode, summaryVO.getCountryCode())
                    .eq(AtUserDataSummaryEntity::getSummaryDate, searchTime));
            if (summaryEntity == null) {
                //新增
                AtUserDataSummaryEntity atUserDataSummaryEntity = BaseBeanUtils.map(summaryVO, AtUserDataSummaryEntity.class);
                atUserDataSummaryEntity.setSummaryDate(searchTime.toString());
                atUserDataSummaryEntity.setCreateTime(new Date());
                atUserDataSummaryEntity.setUpdateTime(new Date());
                this.save(atUserDataSummaryEntity);
            } else {
                //修改
                AtUserDataSummaryEntity atUserDataSummaryEntity = BaseBeanUtils.map(summaryVO, AtUserDataSummaryEntity.class);
                atUserDataSummaryEntity.setUpdateTime(new Date());
                atUserDataSummaryEntity.setId(summaryEntity.getId());
                this.updateById(atUserDataSummaryEntity);
            }
        }
    }

    /**
     * 根据条件查询对应的数据记录
     *
     * @param searchTime
     * @return
     */
    public Map<String, AtUserDataSummaryVO> queryDataSummary(LocalDate searchTime) {
        //Line信息查询
        Map<String, LineRegisterSummaryResultDto> lineSummary = cdGetPhoneService.queryLineRegisterSummary(searchTime)
                .stream().collect(Collectors.toMap(LineRegisterSummaryResultDto::getCountryCode, i -> i));

        //今日已使用数量
        Map<String, Integer> usedUserMap = atUserService.queryUsedUserSummary(searchTime);

        //当前在线数量
        Map<String, Integer> onlineUserMap = atUserService.queryOnlineUserSummary();

        Map<String, AtUserDataSummaryVO> resultMap = new HashMap<>();
        AtUserDataSummaryVO dataSummaryVO;
        List<String> countryCodeList = Arrays.asList("jp", "th");
        for (String key : countryCodeList) {
            dataSummaryVO = new AtUserDataSummaryVO();
            dataSummaryVO.setCountryCode(key);

            //账号使用数量
            dataSummaryVO.setUserUseCount(usedUserMap.get(key.toUpperCase()) != null ?
                    usedUserMap.get(key.toUpperCase()) : 0);

            //在线账号数量
            dataSummaryVO.setUserOnlineCount(onlineUserMap.get(key.toUpperCase()) != null ?
                    onlineUserMap.get(key.toUpperCase()) : 0);

            if (lineSummary.get(key) != null) {
                dataSummaryVO.setLineRegisterCount(lineSummary.get(key).getRegisterNum());
                dataSummaryVO.setLineStock(lineSummary.get(key).getRegisterStock());
            } else {
                dataSummaryVO.setLineRegisterCount(0);
                dataSummaryVO.setLineStock(0);
            }
            resultMap.put(key, dataSummaryVO);
        }
        return resultMap;
    }

    @Override
    public AtUserDataSummaryVO getById(Integer id) {
        return AtUserDataSummaryConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(AtUserDataSummaryDTO atUserDataSummary) {
        AtUserDataSummaryEntity atUserDataSummaryEntity = AtUserDataSummaryConver.MAPPER.converDTO(atUserDataSummary);
        return this.save(atUserDataSummaryEntity);
    }

    @Override
    public boolean updateById(AtUserDataSummaryDTO atUserDataSummary) {
        AtUserDataSummaryEntity atUserDataSummaryEntity = AtUserDataSummaryConver.MAPPER.converDTO(atUserDataSummary);
        return this.updateById(atUserDataSummaryEntity);
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
