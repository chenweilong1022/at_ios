package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.renren.datasources.annotation.Game;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.CdPhoneFilterRecordDao;
import io.renren.modules.ltt.entity.CdPhoneFilterRecordEntity;
import io.renren.modules.ltt.dto.CdPhoneFilterRecordDTO;
import io.renren.modules.ltt.vo.CdPhoneFilterRecordVO;
import io.renren.modules.ltt.service.CdPhoneFilterRecordService;
import io.renren.modules.ltt.conver.CdPhoneFilterRecordConver;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


@Service("cdPhoneFilterRecordService")
@Game
public class CdPhoneFilterRecordServiceImpl extends ServiceImpl<CdPhoneFilterRecordDao, CdPhoneFilterRecordEntity> implements CdPhoneFilterRecordService {

    @Override
    public PageUtils<CdPhoneFilterRecordVO> queryPage(CdPhoneFilterRecordDTO paramDto) {
        IPage<CdPhoneFilterRecordEntity> page = baseMapper.selectPage(
                new Query<CdPhoneFilterRecordEntity>(paramDto).getPage(),
                new QueryWrapper<CdPhoneFilterRecordEntity>().lambda()
                        .eq(ObjectUtil.isNotNull(paramDto.getTaskStatus()), CdPhoneFilterRecordEntity::getTaskStatus, paramDto.getTaskStatus())
                        .orderByDesc(CdPhoneFilterRecordEntity::getRecordId)
        );

        List<CdPhoneFilterRecordVO> resultList = CdPhoneFilterRecordConver.MAPPER.conver(page.getRecords());

        return PageUtils.<CdPhoneFilterRecordVO>page(page).setList(resultList);
    }
    @Override
    public CdPhoneFilterRecordVO getById(Integer recordId) {
        return CdPhoneFilterRecordConver.MAPPER.conver(baseMapper.selectById(recordId));
    }
    @Override
    public List<CdPhoneFilterRecordVO> getByIds(List<Integer> recordIdList) {
        return CdPhoneFilterRecordConver.MAPPER.conver(baseMapper.selectBatchIds(recordIdList));
    }

    @Override
    public boolean save(CdPhoneFilterRecordDTO cdPhoneFilterRecord) {
        CdPhoneFilterRecordEntity cdPhoneFilterRecordEntity = CdPhoneFilterRecordConver.MAPPER.converDTO(cdPhoneFilterRecord);
        return this.save(cdPhoneFilterRecordEntity);
    }

    @Override
    public boolean updateById(CdPhoneFilterRecordDTO cdPhoneFilterRecord) {
        CdPhoneFilterRecordEntity cdPhoneFilterRecordEntity = CdPhoneFilterRecordConver.MAPPER.converDTO(cdPhoneFilterRecord);
        return this.updateById(cdPhoneFilterRecordEntity);
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
