package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.FirefoxService;
import io.renren.modules.client.vo.GetPhoneVO;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.PhoneStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.CdGetPhoneDao;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import io.renren.modules.ltt.dto.CdGetPhoneDTO;
import io.renren.modules.ltt.vo.CdGetPhoneVO;
import io.renren.modules.ltt.service.CdGetPhoneService;
import io.renren.modules.ltt.conver.CdGetPhoneConver;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Service("cdGetPhoneService")
@Game
@Slf4j
public class CdGetPhoneServiceImpl extends ServiceImpl<CdGetPhoneDao, CdGetPhoneEntity> implements CdGetPhoneService {

    @Override
    public PageUtils<CdGetPhoneVO> queryPage(CdGetPhoneDTO cdGetPhone) {
        IPage<CdGetPhoneEntity> page = baseMapper.selectPage(
                new Query<CdGetPhoneEntity>(cdGetPhone).getPage(),
                new QueryWrapper<CdGetPhoneEntity>()
        );

        return PageUtils.<CdGetPhoneVO>page(page).setList(CdGetPhoneConver.MAPPER.conver(page.getRecords()));
    }
    @Override
    public CdGetPhoneVO getById(Integer id) {
        return CdGetPhoneConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(CdGetPhoneDTO cdGetPhone) {
        CdGetPhoneEntity cdGetPhoneEntity = CdGetPhoneConver.MAPPER.converDTO(cdGetPhone);
        return this.save(cdGetPhoneEntity);
    }

    @Override
    public boolean updateById(CdGetPhoneDTO cdGetPhone) {
        CdGetPhoneEntity cdGetPhoneEntity = CdGetPhoneConver.MAPPER.converDTO(cdGetPhone);
        return this.updateById(cdGetPhoneEntity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

    @Resource(name = "cardMeServiceImpl")
    private FirefoxService cardMeService;

    @Resource(name = "cardJpServiceImpl")
    private FirefoxService cardJpService;

    @Resource(name = "cardJpSFServiceImpl")
    private FirefoxService cardJpSFService;

    @Resource(name = "firefoxServiceImpl")
    private FirefoxService firefoxService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CdGetPhoneEntity> addCount(CdGetPhoneDTO cdGetPhone){
        Integer count = cdGetPhone.getCount();
        List<CdGetPhoneEntity> cdGetPhoneEntities = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                log.error("err = {}", e);
                continue;
            }

            GetPhoneVO phone = null;
            if (CountryCode.CountryCode3.getKey().equals(cdGetPhone.getCountrycodeKey()) && CountryCode.CountryCode3.getValue().equals(cdGetPhone.getCountrycode())) {
                //日本
                phone = cardJpService.getPhone();
            }else if (CountryCode.CountryCode8.getKey().equals(cdGetPhone.getCountrycodeKey()) && CountryCode.CountryCode8.getValue().equals(cdGetPhone.getCountrycode())) {
                phone = cardJpSFService.getPhone();
            } else if (CountryCode.CountryCode5.getValue().equals(cdGetPhone.getCountrycode())) {
                //香港
                phone = firefoxService.getPhone();
            } else {
                phone = cardMeService.getPhone();
            }

            //获取到一个算一个，如果获取不到，直接返回
            if (ObjectUtil.isNotNull(phone)) {
                CdGetPhoneEntity cdGetPhoneEntity = new CdGetPhoneEntity();
                cdGetPhoneEntity.setNumber(phone.getNumber());
                cdGetPhoneEntity.setPkey(phone.getPkey());
                cdGetPhoneEntity.setTime(phone.getTime());
                cdGetPhoneEntity.setCountry(cdGetPhone.getCountrycodeKey().toString());
                cdGetPhoneEntity.setCountrycode(cdGetPhone.getCountrycode());
                cdGetPhoneEntity.setOther(phone.getOther());
                cdGetPhoneEntity.setCom(phone.getCom());
                cdGetPhoneEntity.setPhone(phone.getPhone());
                cdGetPhoneEntity.setDeleteFlag(DeleteFlag.NO.getKey());
                cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus1.getKey());
                cdGetPhoneEntity.setCreateTime(DateUtil.date());
                cdGetPhoneEntity.setSubtasksId(cdGetPhone.getSubtasksId());
                cdGetPhoneEntities.add(cdGetPhoneEntity);
            }else {
                break;
            }
        }

        //如果获取了足够的数量 保存记录
        if (CollUtil.isNotEmpty(cdGetPhoneEntities)) {
            this.saveBatch(cdGetPhoneEntities);
        }
        return cdGetPhoneEntities;
    }
}
