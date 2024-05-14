package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.FirefoxService;
import io.renren.modules.client.vo.GetPhoneVO;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.PhoneStatus;
import io.renren.modules.ltt.enums.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
import java.util.List;
import java.util.stream.Collectors;


@Service("cdGetPhoneService")
@Game
@Slf4j
public class CdGetPhoneServiceImpl extends ServiceImpl<CdGetPhoneDao, CdGetPhoneEntity> implements CdGetPhoneService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
    public List<CdGetPhoneVO> getByIds(List<Integer> ids) {
        return CdGetPhoneConver.MAPPER.conver(baseMapper.selectBatchIds(ids));
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

    @Resource(name = "cardJpSmsOver")
    private Cache<String, String> cardJpSmsOver;

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
                if (cardJpSmsOver.getIfPresent("jpSmsOverFlag") != null) {
                    break;
                }
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
                List<String> pkeys = phone.getPkeys();
                List<String> phones = phone.getPhones();

                if (CollUtil.isNotEmpty(pkeys)) {
                    for (int i1 = 0; i1 < pkeys.size(); i1++) {
                        String pkey = pkeys.get(i1);
                        String phoneNumber = phones.get(i1);

                        CdGetPhoneEntity cdGetPhoneEntity = new CdGetPhoneEntity();
                        cdGetPhoneEntity.setNumber(phone.getNumber());
                        cdGetPhoneEntity.setPkey(pkey);
                        cdGetPhoneEntity.setTime(phone.getTime());
                        cdGetPhoneEntity.setCountry(cdGetPhone.getCountrycodeKey().toString());
                        cdGetPhoneEntity.setCountrycode(cdGetPhone.getCountrycode());
                        cdGetPhoneEntity.setOther(phone.getOther());
                        cdGetPhoneEntity.setCom(phone.getCom());
                        cdGetPhoneEntity.setPhone(phoneNumber);
                        cdGetPhoneEntity.setDeleteFlag(DeleteFlag.NO.getKey());
                        cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus1.getKey());
                        cdGetPhoneEntity.setCreateTime(DateUtil.date());
                        cdGetPhoneEntity.setSubtasksId(cdGetPhone.getSubtasksId());
                        cdGetPhoneEntities.add(cdGetPhoneEntity);
                    }
                }else {
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
                }
            }else {
                break;
            }
        }

        //如果获取了足够的数量 保存记录
        if (CollUtil.isNotEmpty(cdGetPhoneEntities)) {
            //查询是否重复
            List<String> phone = cdGetPhoneEntities.stream()
                    .map(CdGetPhoneEntity::getPhone).collect(Collectors.toList());

            //数据库已存在的手机号
            List<String> repeatPhoneList = baseMapper.selectList(new QueryWrapper<CdGetPhoneEntity>()
                            .lambda().in(CdGetPhoneEntity::getPhone, phone)).stream()
                    .map(CdGetPhoneEntity::getPhone).collect(Collectors.toList());

            log.info("拿三方手机号，与数据库重复 {}, 重复手机号：{}", cdGetPhone, repeatPhoneList);
            cdGetPhoneEntities = cdGetPhoneEntities.stream()
                    .filter(i -> !repeatPhoneList.contains(i.getPhone())).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(cdGetPhoneEntities)) {
                this.saveBatch(cdGetPhoneEntities);
            }
        }
        return cdGetPhoneEntities;
    }

    /**
     * 查询手机号注册次数
     */
    @Override
    public Integer getPhoneRegisterCount(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return 0;
        }
        try {
            Object object = stringRedisTemplate.opsForHash()
                    .get(RedisKeys.RedisKeys10.getValue(), phone);
            if (object != null) {
                return Integer.valueOf(String.valueOf(object));
            }
        } catch (Exception e) {
            log.error("查询手机号注册次数异常 {}, {}", phone, e);
        }
        return 0;
    }

    /**
     * 查询手机号是否可用
     * @return true:代表手机号可用可购买
     */
    @Override
    public Boolean getPhoneUseState(String phone) {
        try {
            if (StringUtils.isNotEmpty(phone)) {
                Object object = stringRedisTemplate.opsForValue()
                        .get(RedisKeys.RedisKeys12.getValue(phone));
                if (object != null) {
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("查询手机号注册次数异常 {}, {}", phone, e);
        }
        return true;
    }
}
