package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.renren.common.constant.SystemConstant;
import io.renren.common.utils.*;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.dto.CdLineRegisterSummaryDto;
import io.renren.modules.ltt.dto.LineRegisterSummaryResultDto;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.PhoneStatus;
import io.renren.modules.ltt.enums.RedisKeys;
import io.renren.modules.ltt.enums.RegisterStatus;
import io.renren.modules.ltt.service.*;
import io.renren.modules.ltt.vo.GetCountBySubTaskIdVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.modules.ltt.dao.CdLineRegisterDao;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import io.renren.modules.ltt.dto.CdLineRegisterDTO;
import io.renren.modules.ltt.vo.CdLineRegisterVO;
import io.renren.modules.ltt.conver.CdLineRegisterConver;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        Integer mod = systemConstant.getSERVERS_MOD();
        String key = String.valueOf(mod);

        List<CdGetPhoneEntity> updateCdGetPhoneList = new ArrayList<>();
        Integer retryNum;//{"@class":"io.renren.modules.ltt.entity.CdLineRegisterEntity","id":null,"ab":"2024.307.2034","appVersion":"14.3.1","countryCode":"th","phone":"66858523701","proxy":"socks5://chenweilong122-zone-resi-region-th-session-110w8x2h9abj-sessTime-5:ch1433471850@4a6974acaeab2113.us.ip2world.vip:6001","proxyStatus":1,"txtToken":"81f9933e3a434a1aaf7af09893937fd0","taskId":"4a959394-3617-4c1f-ba0f-9942e23f1c0f","smsCode":null,"registerStatus":1,"deleteFlag":1,"createTime":["cn.hutool.core.date.DateTime",1716286708812],"openTime":null,"token":null,"getPhoneId":413332,"pkey":"C4AAE53ACE3A55E4910E9E93B1678E17E2B7AFA9DA17C060","subtasksId":2160,"groupTaskId":null,"openStatus":null,"accountStatus":null,"groupCount":null,"accountExistStatus":null,"errMsg":null,"exportStatus":null}
        for (CdGetPhoneEntity cdGetPhone : cdGetPhoneList) {
            //删除redis的数据
            redisTemplateObj.opsForHash().delete(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST.getValue(key), String.valueOf(cdGetPhone.getId()));
            //删除redis中的验证码提交一次的数据
            redisTemplateObj.opsForHash().delete(RedisKeys.CDLINEREGISTERENTITY_SAVE_LIST_STATUS2.getValue(key), String.valueOf(cdGetPhone.getId()));
            //更新此条数据，发起重新注册
            CdGetPhoneEntity updateCdGetPhoneEntity = new CdGetPhoneEntity();
            updateCdGetPhoneEntity.setId(cdGetPhone.getId());
            updateCdGetPhoneEntity.setPhone(cdGetPhone.getPhone());
            updateCdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus1.getKey());
            updateCdGetPhoneEntity.setCode("");
            updateCdGetPhoneEntity.setCreateTime(new Date());
            retryNum = ObjectUtil.isNull(cdGetPhone.getRetryNum()) ? 1 : cdGetPhone.getRetryNum() + 1;
            updateCdGetPhoneEntity.setRetryNum(retryNum);
            updateCdGetPhoneList.add(updateCdGetPhoneEntity);
        }

        getPhoneService.updateBatchById(updateCdGetPhoneList);
        //redis注册流程改为：待处理
        this.registerRetryRedis(cdGetPhoneList);
        return true;
    }

    /**
     * 错误重试
     * @param phoneEntityList 必须是实体类全部的值
     */
    private void registerRetryRedis(List<CdGetPhoneEntity> phoneEntityList) {
        phoneEntityList.stream().forEach(i -> {
            i.setPhoneStatus(PhoneStatus.PhoneStatus1.getKey());
            i.setCode("");
            i.setCreateTime(new Date());
        });
        //删除注册流程，回到待处理
        List<String> phoneList = phoneEntityList.stream().map(CdGetPhoneEntity::getPhone).distinct().collect(Collectors.toList());
        for (String phone : phoneList) {
            redisObjectTemplate.opsForHash().delete(RedisKeys.REGISTER_TASK.getValue(), phone);
        }

        //保存redis
        cdGetPhoneService.saveWaitRegisterPhone(phoneEntityList);
    }

    @Override
    public boolean registerAgain(String telephone) {
        CdLineRegisterEntity cdLineRegisterEntity = this.getOne(new QueryWrapper<CdLineRegisterEntity>().lambda()
                .eq(CdLineRegisterEntity::getPhone,telephone)
                .orderByDesc(CdLineRegisterEntity::getId)
                .last("limit 1")
        );
        if (ObjectUtil.isNotNull(cdLineRegisterEntity)) {
            CdGetPhoneEntity cdGetPhone = getPhoneService.queryById(cdLineRegisterEntity.getGetPhoneId());
            if (ObjectUtil.isNotNull(cdGetPhone)) {
                //ip暂时拉黑
                this.clearTokenPhone(cdGetPhone);

                //更新此条数据，发起重新注册
                CdGetPhoneEntity updateCdGetPhoneEntity = new CdGetPhoneEntity();
                updateCdGetPhoneEntity.setId(cdGetPhone.getId());
                updateCdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus1.getKey());
                updateCdGetPhoneEntity.setCode("");
                updateCdGetPhoneEntity.setCreateTime(new Date());
                //重试次数，更新为0，从头计数
                updateCdGetPhoneEntity.setRetryNum(0);
                getPhoneService.updateById(updateCdGetPhoneEntity);
                //redis注册流程改为：待处理
                this.registerRetryRedis(Arrays.asList(cdGetPhone));
            }
            baseMapper.deleteById(cdLineRegisterEntity.getId());
        }

        return false;
    }

    private void clearTokenPhone(CdGetPhoneEntity cdGetPhone) {
        //ip暂时拉黑
        if (StringUtils.isEmpty(cdGetPhone.getCode())
                || Boolean.FALSE.equals(StrTextUtil.verificationCodeFlag(cdGetPhone.getCode()))) {
            cdLineIpProxyService.clearTokenPhone(cdGetPhone.getPhone(), CountryCode.getKeyByValue(cdGetPhone.getCountrycode()));
        }
    }

    @Override
    public Integer queryLineRegisterCount(String countryCode) {
        return baseMapper.selectCount(new QueryWrapper<CdLineRegisterEntity>().lambda()
                .eq(CdLineRegisterEntity::getRegisterStatus, RegisterStatus.RegisterStatus4.getKey())
                .eq(CdLineRegisterEntity::getCountryCode, countryCode));
    }

    @Override
    public List<LineRegisterSummaryResultDto> queryLineRegisterSummary(LocalDate searchTime) {
        LocalTime localTime = LocalTime.of(8, 0, 0);

        LocalDateTime searchStartTime = searchTime.atTime(localTime);
        LocalDateTime searchEndTime = searchTime.plusDays(1).atTime(localTime);
        List<LineRegisterSummaryResultDto> summaryResultDtos = baseMapper.queryLineRegisterSummary(searchStartTime, searchEndTime);
        return summaryResultDtos;
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
    @Async
    public void saveAtUserToken(CdGetPhoneEntity cdGetPhoneEntityUpdate,
                                String token) {
        if (!PhoneStatus.PhoneStatus8.getKey().equals(cdGetPhoneEntityUpdate.getPhoneStatus())
                || StringUtils.isEmpty(token)) {
            return;
        }
        //修改
        CdGetPhoneEntity updateCdGetPhoneEntity = new CdGetPhoneEntity();
        updateCdGetPhoneEntity.setId(cdGetPhoneEntityUpdate.getId());
        updateCdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus9.getKey());
        boolean b = cdGetPhoneService.updateById(updateCdGetPhoneEntity);
        if (Boolean.TRUE.equals(b)) {
            //生成token
            AtUserTokenEntity userTokenEntity = new AtUserTokenEntity();
            userTokenEntity.setToken(token);
            userTokenEntity.setUserGroupId(null);
            atUserTokenService.save(userTokenEntity);
        }
    }

}
