package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import io.renren.common.utils.PhoneUtil;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.enums.PhoneFilterStatus;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("cdPhoneFilterService")
@Game
public class CdPhoneFilterServiceImpl extends ServiceImpl<CdPhoneFilterDao, CdPhoneFilterEntity> implements CdPhoneFilterService {

    @Override
    public PageUtils<CdPhoneFilterVO> queryPage(CdPhoneFilterDTO cdPhoneFilter) {
        IPage<CdPhoneFilterEntity> page = baseMapper.selectPage(
                new Query<CdPhoneFilterEntity>(cdPhoneFilter).getPage(),
                new QueryWrapper<CdPhoneFilterEntity>()
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
                phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(s);
            } catch (Exception e) {

            }
            if (ObjectUtil.isNotNull(phoneNumberInfo)) {
                CdPhoneFilterEntity cdPhoneFilterEntity = new CdPhoneFilterEntity();
                cdPhoneFilterEntity.setContactKey(s);
                cdPhoneFilterEntity.setTaskStatus(PhoneFilterStatus.PhoneFilterStatus1.getKey());
                cdPhoneFilterEntities.add(cdPhoneFilterEntity);
            }
        }

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
    public byte[] exportSJ(CdPhoneFilterDTO cdPhoneFilter) {
        //料子数据
        String materialText = HttpUtil.downloadString(cdPhoneFilter.getTextUrl(), "UTF-8");
        String[] materialTextSplit = materialText.split("\n");
        Assert.isTrue(ArrayUtil.isEmpty(materialTextSplit),"txt不能为空");

        List<CdPhoneFilterEntity> list = this.list(new QueryWrapper<CdPhoneFilterEntity>().lambda()
                .eq(CdPhoneFilterEntity::getTaskStatus, PhoneFilterStatus.PhoneFilterStatus3.getKey())
        );
        Map<String, CdPhoneFilterEntity> collect = list.stream().collect(Collectors.toMap(CdPhoneFilterEntity::getContactKey, item -> item));

        List<String> newJS = new ArrayList<>();
        for (String s : materialTextSplit) {
            if (StrUtil.isEmpty(s)) {
                newJS.add(s);
                continue;
            }


            PhoneCountryVO phoneNumberInfo = null;
            try {
                phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(s);
                CdPhoneFilterEntity cdPhoneFilterEntity = collect.get(s);
                if (ObjectUtil.isNotNull(cdPhoneFilterEntity)) {
                    String trim = StrUtil.trim(cdPhoneFilterEntity.getContactKey().trim());
                    String format = String.format("%s   %s  %s", trim.replace("\r","").replace("\n",""), cdPhoneFilterEntity.getMid(), cdPhoneFilterEntity.getDisplayName());
                    newJS.add(format);
                }else {
                    newJS.add(s);
                }
            } catch (Exception e) {
                CdPhoneFilterEntity cdPhoneFilterEntity = collect.get(s);
                if (ObjectUtil.isNotNull(cdPhoneFilterEntity)) {
                    String trim = StrUtil.trim(cdPhoneFilterEntity.getContactKey().trim());
                    String format = String.format("%s   %s  %s", trim.replace("\r","").replace("\n",""), cdPhoneFilterEntity.getMid(), cdPhoneFilterEntity.getDisplayName());
                    newJS.add(format);
                }else {
                    newJS.add(s);
                }
            }
        }
        String newStr = newJS.stream().map(phone -> phone + "\n").collect(Collectors.joining());
        return StrUtil.bytes(newStr);
    }

}
