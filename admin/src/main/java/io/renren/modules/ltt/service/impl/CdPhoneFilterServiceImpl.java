package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import io.renren.common.utils.PhoneUtil;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.dto.SearchPhoneDTO;
import io.renren.modules.client.dto.SearchUserIdDTO;
import io.renren.modules.client.vo.SearchPhoneVO;
import io.renren.modules.client.vo.SearchUserIdVO;
import io.renren.modules.client.vo.The818051863582;
import io.renren.modules.ltt.conver.CdPhoneFilterRecordConver;
import io.renren.modules.ltt.dao.CdPhoneFilterRecordDao;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.dto.CdPhoneFilterRecordDTO;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.entity.CdPhoneFilterRecordEntity;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.PhoneFilterStatus;
import io.renren.modules.ltt.enums.UserStatus;
import io.renren.modules.ltt.service.CdPhoneFilterRecordService;
import io.renren.modules.ltt.vo.CdPhoneFilterRecordVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.CdPhoneFilterDao;
import io.renren.modules.ltt.entity.CdPhoneFilterEntity;
import io.renren.modules.ltt.dto.CdPhoneFilterDTO;
import io.renren.modules.ltt.vo.CdPhoneFilterVO;
import io.renren.modules.ltt.service.CdPhoneFilterService;
import io.renren.modules.ltt.conver.CdPhoneFilterConver;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@Service("cdPhoneFilterService")
@Game
public class CdPhoneFilterServiceImpl extends ServiceImpl<CdPhoneFilterDao, CdPhoneFilterEntity> implements CdPhoneFilterService {

    @Resource
    private CdPhoneFilterRecordService cdPhoneFilterRecordService;

    @Resource
    private CdPhoneFilterRecordDao cdPhoneFilterRecordDao;

    @Override
    public PageUtils<CdPhoneFilterVO> queryPage(CdPhoneFilterDTO cdPhoneFilter) {
        IPage<CdPhoneFilterEntity> page = baseMapper.selectPage(
                new Query<CdPhoneFilterEntity>(cdPhoneFilter).getPage(),
                new QueryWrapper<CdPhoneFilterEntity>().lambda()
                        .eq(CdPhoneFilterEntity::getRecordId, cdPhoneFilter.getRecordId())
                        .eq(ObjectUtil.isNotNull(cdPhoneFilter.getTaskStatus()), CdPhoneFilterEntity::getTaskStatus, cdPhoneFilter.getTaskStatus())
                        .orderByDesc(CdPhoneFilterEntity::getRecordId)
        );

        return PageUtils.<CdPhoneFilterVO>page(page).setList(CdPhoneFilterConver.MAPPER.conver(page.getRecords()));
    }

    @Override
    public CdPhoneFilterVO getById(Integer id) {
        return CdPhoneFilterConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(CdPhoneFilterDTO cdPhoneFilter) {

        //料子数据
        String materialText = HttpUtil.downloadString(cdPhoneFilter.getTextUrl(), "UTF-8");
        String[] materialTextSplit = materialText.split("\n");
        Assert.isTrue(ArrayUtil.isEmpty(materialTextSplit),"txt不能为空");


        List<CdPhoneFilterEntity> cdPhoneFilterEntities = new ArrayList<>();
        for (String s : materialTextSplit) {
            if (StrUtil.isEmpty(s)) {
                continue;
            }
            PhoneCountryVO phoneNumberInfo = null;
            try {
                s = StrUtil.cleanBlank(s);
                s = s.replace("-","");
                phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(s);
            } catch (Exception e) {

            }
            if (ObjectUtil.isNotNull(phoneNumberInfo)) {
                CdPhoneFilterEntity cdPhoneFilterEntity = new CdPhoneFilterEntity();
                cdPhoneFilterEntity.setContactKey(s);
                cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus2.getKey());
                cdPhoneFilterEntities.add(cdPhoneFilterEntity);
            }
        }

        Assert.isTrue(CollectionUtil.isEmpty(cdPhoneFilterEntities), "上传数据不能为空");

        //数据记录总表
        CdPhoneFilterRecordEntity recordEntity = new CdPhoneFilterRecordEntity();
        recordEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus2.getKey());
        recordEntity.setCreateTime(new Date());
        recordEntity.setTotalCount((long)cdPhoneFilterEntities.size());
        recordEntity.setSuccessCount(0l);
        recordEntity.setFailCount(0l);
        cdPhoneFilterRecordService.save(recordEntity);

        //数据记录
        cdPhoneFilterEntities.forEach(i->{
            i.setRecordId(recordEntity.getRecordId());
            i.setCreateTime(new Date());
            i.setDeleteFlag(DeleteFlag.NO.getKey());
        });
        boolean b = this.saveBatch(cdPhoneFilterEntities, cdPhoneFilterEntities.size() + 1);
        return b;
    }

    @Override
    public boolean updateById(CdPhoneFilterDTO cdPhoneFilter) {
        CdPhoneFilterEntity cdPhoneFilterEntity = CdPhoneFilterConver.MAPPER.converDTO(cdPhoneFilter);
        return this.updateById(cdPhoneFilterEntity);
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
    public byte[] exportSJ(Long recordId) {
        CdPhoneFilterRecordEntity recordEntity = cdPhoneFilterRecordService.getById(recordId);
        Assert.isTrue(ObjectUtil.isNull(recordEntity),"数据为空，请刷新重试");

        List<CdPhoneFilterEntity> list = baseMapper.selectList(new QueryWrapper<CdPhoneFilterEntity>().lambda()
                .eq(CdPhoneFilterEntity::getRecordId, recordEntity.getRecordId())
        );
        Map<String, CdPhoneFilterEntity> collect = list.stream()
                .collect(Collectors.toMap(CdPhoneFilterEntity::getContactKey, item -> item,(s1,s2) -> s1));

        List<String> newJS = new ArrayList<>();
        for (CdPhoneFilterEntity cdPhoneFilterEntity : list) {
//            PhoneCountryVO phoneNumberInfo = null;
            try {
//                phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(s);
                String trim = StrUtil.trim(cdPhoneFilterEntity.getContactKey().trim());
                if (PhoneFilterStatus.PhoneFilterStatus3.getKey().equals(cdPhoneFilterEntity.getTaskStatus())) {
                    String format = String.format("%s   %s  %s", trim.replace("\r","").replace("\n",""), cdPhoneFilterEntity.getMid(), cdPhoneFilterEntity.getDisplayName());
                    newJS.add(format);
                }else {
                    newJS.add(cdPhoneFilterEntity.getContactKey());
                }
            } catch (Exception e) {
                newJS.add(cdPhoneFilterEntity.getContactKey());
            }
        }
        String newStr = newJS.stream().map(phone -> phone + "\n").collect(Collectors.joining());
        return StrUtil.bytes(newStr);
    }

}
