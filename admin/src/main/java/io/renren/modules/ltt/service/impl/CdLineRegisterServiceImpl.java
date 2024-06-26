package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.renren.common.constant.SystemConstant;
import io.renren.common.utils.*;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.dao.AtUserDao;
import io.renren.modules.ltt.dto.*;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.enums.RedisKeys;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.modules.ltt.dao.CdLineRegisterDao;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import io.renren.modules.ltt.conver.CdLineRegisterConver;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@Service("cdLineRegisterService")
@Game
@Slf4j
public class CdLineRegisterServiceImpl extends ServiceImpl<CdLineRegisterDao, CdLineRegisterEntity> implements CdLineRegisterService {

    @Resource
    private CdGetPhoneService getPhoneService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplateObj;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisObjectTemplate;
    @Resource
    private CdGetPhoneService cdGetPhoneService;

    @Resource
    private AtUserTokenService atUserTokenService;

    @Resource
    private AtUserDao atUserDao;

    @Override
    public PageUtils<CdLineRegisterVO> queryPage(CdLineRegisterDTO cdLineRegister) {
        IPage<CdLineRegisterEntity> page = baseMapper.selectPage(
                new Query<CdLineRegisterEntity>(cdLineRegister).getPage(),
                new QueryWrapper<CdLineRegisterEntity>()
        );

        return PageUtils.<CdLineRegisterVO>page(page).setList(CdLineRegisterConver.MAPPER.conver(page.getRecords()));
    }

    @Override
    public PageUtils listByTaskId(CdLineRegisterDTO cdLineRegister) {
        IPage<CdLineRegisterVO> page = baseMapper.listPage(
                new Query<CdLineRegisterEntity>(cdLineRegister).getPage(),
                cdLineRegister
        );

        //服务器更新锁先锁住方法
        Integer mod = systemConstant.getSERVERS_MOD();
        String key = String.valueOf(mod);
        for (CdLineRegisterVO record : page.getRecords()) {
            CdLineRegisterEntity cdLineRegisterEntity = (CdLineRegisterEntity) redisTemplateObj.opsForHash().get(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(record.getId()));
            if (ObjectUtil.isNotNull(cdLineRegisterEntity)) {
                record.setCdLineRegisterEntity(cdLineRegisterEntity);
            }
            record.setRegisterCount(cdGetPhoneService.getPhoneRegisterCount(record.getPhone()));
            record.setPhoneState(cdGetPhoneService.getPhoneUseState(record.getPhone()));
        }
        return PageUtils.<CdLineRegisterVO>page(page);
    }

    @Override
    public CdLineRegisterSummaryDto listSummary(CdLineRegisterDTO cdLineRegister) {
        return baseMapper.listSummary(cdLineRegister);
    }

    @Override
    public CdLineRegisterVO getById(Integer id) {
        return CdLineRegisterConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Override
    public boolean save(CdLineRegisterDTO cdLineRegister) {
        CdLineRegisterEntity cdLineRegisterEntity = CdLineRegisterConver.MAPPER.converDTO(cdLineRegister);
        return this.save(cdLineRegisterEntity);
    }

    @Override
    public boolean insertBatch(List<CdLineRegisterEntity> cdLineRegisterEntities) {
        int count = baseMapper.insertBatch(cdLineRegisterEntities);
        return count > 0;
    }

    @Override
    public boolean updateById(CdLineRegisterDTO cdLineRegister) {
        CdLineRegisterEntity cdLineRegisterEntity = CdLineRegisterConver.MAPPER.converDTO(cdLineRegister);
        return this.updateById(cdLineRegisterEntity);
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
    public List<GetCountBySubTaskIdVO> getCountBySubTaskId(List<Integer> registerSubtasksIds) {
        return baseMapper.getCountBySubTaskId(registerSubtasksIds);
    }

    @Override
    public Integer getCountByRegisterStatus(Integer registerStatus,
                                            String countryCode) {
        Integer count = baseMapper.selectCount(new QueryWrapper<CdLineRegisterEntity>().lambda()
                .eq(CdLineRegisterEntity::getRegisterStatus, registerStatus)
                .eq(CdLineRegisterEntity::getCountryCode, countryCode));
        return count == null ? 0 : count;
    }

    @Override
    public List<CdLineRegisterVO> getListByRegisterStatus(Integer registerStatus,
                                                          String countryCode,
                                                          Integer limit) {
        List<CdLineRegisterEntity> list = baseMapper.selectList(new QueryWrapper<CdLineRegisterEntity>().lambda()
                .eq(CdLineRegisterEntity::getRegisterStatus, registerStatus)
                .eq(CdLineRegisterEntity::getCountryCode, countryCode)
                .last("limit " + limit));
        return CdLineRegisterConver.MAPPER.conver(list);
    }

    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean manualPhoneCode(String tasksId, String phoneCodes) {
        String[] split = phoneCodes.trim().split("\n");
        Map<String, String> phoneMap = new HashMap<>();
        for (String s : split) {
            try {
                String[] registerText = s.split("-");
                if (registerText.length != 2) {
                    continue;
                }
                String phone = CountryCode.CountryCode3.getKey() + registerText[0];
                String phoneCode = registerText[1];//验证码
                phoneMap.put(phone, phoneCode);
            } catch (Exception e) {
                log.error("手动接码异常 {}, {}", phoneCodes, e);
            }
        }


        CdLineRegisterDTO cdLineRegister = new CdLineRegisterDTO();
        cdLineRegister.setTaskId(tasksId);
        cdLineRegister.setPhones(phoneMap.keySet().stream().collect(Collectors.toList()));
        IPage<CdLineRegisterVO> page = baseMapper.listPage(
                new Query<CdLineRegisterEntity>(cdLineRegister).getPage(),
                cdLineRegister
        );

        List<CdGetPhoneEntity> cdGetPhoneEntityList = new ArrayList<>();
        for (CdLineRegisterVO record : page.getRecords()) {
            CdGetPhoneEntity updateCdGetPhoneEntity = new CdGetPhoneEntity();
            updateCdGetPhoneEntity.setId(record.getId());
            updateCdGetPhoneEntity.setCode(phoneMap.get(record.getPhone()));
            cdGetPhoneEntityList.add(updateCdGetPhoneEntity);
        }
        getPhoneService.updateBatchById(cdGetPhoneEntityList);
        return true;
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SystemConstant systemConstant;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean registerRetry(Integer[] ids, Boolean ipClearFlag) {
        List<CdGetPhoneEntity> cdGetPhoneList = getPhoneService.getByIds(Arrays.asList(ids));
        Assert.isTrue(CollectionUtils.isEmpty(cdGetPhoneList), "数据为空");

        return registerRetry(cdGetPhoneList, ipClearFlag, null);
    }

    public boolean registerRetry(List<CdGetPhoneEntity> phoneList,
                                 Boolean ipClearFlag,
                                 Integer retrySetNum) {
        //判断是否为改名组，改名组不允许注册
        List<String> telphoneList = phoneList.stream().map(CdGetPhoneEntity::getPhone).distinct().collect(Collectors.toList());
        List<String> changeNamePhone = atUserDao.queryByGroupType(telphoneList, AtUserGroupTypeEnum.AtUserGroupType2.getKey());
        for (String s : telphoneList) {
            Assert.isTrue(changeNamePhone.contains(s), "改名号，不允许注册");
        }

        Integer mod = systemConstant.getSERVERS_MOD();
        String key = String.valueOf(mod);

        List<CdGetPhoneEntity> updateCdGetPhoneList = new ArrayList<>();
        Integer retryNum;
        for (CdGetPhoneEntity cdGetPhone : phoneList) {
            //如果需要拉黑，去拉黑ip
            if (ipClearFlag) {
                //ip暂时拉黑
                cdLineIpProxyService.clearTokenPhone(cdGetPhone.getPhone(), CountryCode.getKeyByValue(cdGetPhone.getCountrycode()));
            }
            //删除redis的数据
            redisTemplateObj.opsForHash().delete(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhone.getId()));
            //删除redis中的验证码提交一次的数据
            redisTemplateObj.opsForHash().delete(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST_STATUS2.getValue(key), String.valueOf(cdGetPhone.getId()));
            redisTemplateObj.opsForHash().delete(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST_STATUS8.getValue(key), String.valueOf(cdGetPhone.getId()));
            //更新此条数据，发起重新注册
            CdGetPhoneEntity updateCdGetPhoneEntity = new CdGetPhoneEntity();
            updateCdGetPhoneEntity.setId(cdGetPhone.getId());
            updateCdGetPhoneEntity.setPhone(cdGetPhone.getPhone());
            updateCdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus1.getKey());
            updateCdGetPhoneEntity.setCode("");
            updateCdGetPhoneEntity.setCreateTime(new Date());
            retryNum = ObjectUtil.isNull(cdGetPhone.getRetryNum()) ? 1 : cdGetPhone.getRetryNum() + 1;
            if (retrySetNum != null) {
                retryNum = retrySetNum;
            }
            updateCdGetPhoneEntity.setRetryNum(retryNum);
            updateCdGetPhoneList.add(updateCdGetPhoneEntity);
        }

        getPhoneService.updateBatchById(updateCdGetPhoneList);
        return true;
    }



    @Override
    public boolean registerAgain(String telephone) {
        //重新注册line
        CdGetPhoneEntity cdGetPhone = getPhoneService.getOne(new QueryWrapper<CdGetPhoneEntity>().lambda()
                .eq(CdGetPhoneEntity::getPhone,telephone).last("limit 1")
        );
        if (ObjectUtil.isNotNull(cdGetPhone)) {
            return this.registerRetry(Arrays.asList(cdGetPhone),false, 0);
        }
        return false;
    }

    @Override
    public boolean registerAgains(List<String> telephoneList) {
        //重新注册line
        List<CdGetPhoneEntity> cdGetPhoneList = getPhoneService.list(new QueryWrapper<CdGetPhoneEntity>().lambda()
                .in(CdGetPhoneEntity::getPhone,telephoneList));
        if (CollectionUtils.isNotEmpty(cdGetPhoneList)) {
            return this.registerRetry(cdGetPhoneList,false, 0);
        }
        return false;
    }

    @Override
    public Integer queryLineRegisterCount(String countryCode) {
        return baseMapper.selectCount(new QueryWrapper<CdLineRegisterEntity>().lambda()
                .eq(CdLineRegisterEntity::getRegisterStatus, RegisterStatus.RegisterStatus4.getKey())
                .eq(CdLineRegisterEntity::getCountryCode, countryCode));
    }

    @Override
    public CdLineRegisterEntity queryLineRegisterByPhone(String phone) {
        return baseMapper.selectOne(new QueryWrapper<CdLineRegisterEntity>().lambda()
                .eq(CdLineRegisterEntity::getPhone, phone)
                .in(CdLineRegisterEntity::getRegisterStatus,
                        Arrays.asList(RegisterStatus.RegisterStatus4.getKey(), RegisterStatus.RegisterStatus11.getKey())));
    }


    @Override
    public CdLineRegisterEntity queryByPhone(String phone) {
        return baseMapper.selectOne(new QueryWrapper<CdLineRegisterEntity>().lambda()
                .eq(CdLineRegisterEntity::getPhone, phone));
    }

    @Override
    public Boolean invalidatePhone(Integer[] ids) {
        if (ObjectUtil.isNull(ids) || ids.length == 0) {
            return false;
        }
        List<CdGetPhoneEntity> cdGetPhoneList = getPhoneService.getByIds(Arrays.asList(ids));
        Assert.isTrue(CollectionUtils.isEmpty(cdGetPhoneList), "数据为空");

        List<CdGetPhoneEntity> phoneEntityList = new ArrayList<>();
        for (Integer id : ids) {
            CdGetPhoneEntity getPhoneEntity = new CdGetPhoneEntity();
            getPhoneEntity.setId(id);
            getPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus7.getKey());
            phoneEntityList.add(getPhoneEntity);
        }
        return getPhoneService.updateBatchById(phoneEntityList);
    }

    @Override
    public boolean saveRegisterNickname(RegisterNicknameDTO registerNicknameDTO) {
        Assert.isTrue(ObjectUtil.isNull(registerNicknameDTO.getCountryCode()), "请选择国家");
        Assert.isTrue(StringUtils.isEmpty(registerNicknameDTO.getNickname()), "请输入昵称");
        List<String> nickNameList = new ArrayList<>();
        String[] split = registerNicknameDTO.getNickname().trim().split("\n");
        for (String s : split) {
            if (StrUtil.isEmpty(s)) {
                continue;
            }
            nickNameList.add(s);
        }
        String regions = EnumUtil.queryValueByKey(registerNicknameDTO.getCountryCode(), CountryCode.values());
        Long l = redisTemplate.opsForList().rightPushAll(RedisKeys.REGISTER_NICKNAME_LIST.getValue(regions), nickNameList);
        return l > 0;
    }

    @Override
    public boolean deleteRegisterNickname(Integer countryCode) {
        Assert.isTrue(ObjectUtil.isNull(countryCode), "请选择国家");
        String regions = EnumUtil.queryValueByKey(countryCode, CountryCode.values());
        return redisTemplate.delete(RedisKeys.REGISTER_NICKNAME_LIST.getValue(regions));
    }

    @Override
    public PageUtils<String> listRegisterNickname(Integer countryCode) {
        String regions = EnumUtil.queryValueByKey(countryCode, CountryCode.values());
        String key = RedisKeys.REGISTER_NICKNAME_LIST.getValue(regions);
        Long size = redisTemplate.opsForList().size(key);
        if (size == 0) {
            return new PageUtils<>(Collections.emptyList(), Integer.valueOf(Math.toIntExact(size)), 1000, 1);
        }

        List<String> nicknameList = redisTemplate.opsForList()
                .range(RedisKeys.REGISTER_NICKNAME_LIST.getValue(regions), 0, size > 1000 ? 1000 : size);
        return new PageUtils<>(nicknameList, Integer.valueOf(Math.toIntExact(size)), 1000, 1);
    }
}
