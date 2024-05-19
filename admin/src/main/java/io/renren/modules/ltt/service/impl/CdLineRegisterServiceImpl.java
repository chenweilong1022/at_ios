package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.renren.common.constant.SystemConstant;
import io.renren.common.utils.*;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.dto.CdLineRegisterSummaryDto;
import io.renren.modules.ltt.dto.LineRegisterSummaryResultDto;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.PhoneStatus;
import io.renren.modules.ltt.enums.RedisKeys;
import io.renren.modules.ltt.enums.RegisterStatus;
import io.renren.modules.ltt.service.CdGetPhoneService;
import io.renren.modules.ltt.service.CdLineIpProxyService;
import io.renren.modules.ltt.vo.GetCountBySubTaskIdVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.modules.ltt.dao.CdLineRegisterDao;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import io.renren.modules.ltt.dto.CdLineRegisterDTO;
import io.renren.modules.ltt.vo.CdLineRegisterVO;
import io.renren.modules.ltt.service.CdLineRegisterService;
import io.renren.modules.ltt.conver.CdLineRegisterConver;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service("cdLineRegisterService")
@Game
@Slf4j
public class CdLineRegisterServiceImpl extends ServiceImpl<CdLineRegisterDao, CdLineRegisterEntity> implements CdLineRegisterService {

    @Resource
    private CdGetPhoneService getPhoneService;


    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisObjectTemplate;
    @Resource
    private CdGetPhoneService cdGetPhoneService;

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
        for (CdLineRegisterVO record : page.getRecords()) {
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

        Map<Integer, Integer> lineRegisterMap = baseMapper.selectList(new QueryWrapper<CdLineRegisterEntity>().lambda()
                        .in(CdLineRegisterEntity::getGetPhoneId, Arrays.asList(ids))).stream()
                .collect(Collectors.toMap(CdLineRegisterEntity::getGetPhoneId, CdLineRegisterEntity::getId,(a,b)->b));

        List<CdGetPhoneEntity> updateCdGetPhoneList = new ArrayList<>();
        List<Integer> lineRegisterIds = new ArrayList<>();
        Integer retryNum;
        for (CdGetPhoneEntity cdGetPhone : cdGetPhoneList) {
            Integer lineRegisterId = lineRegisterMap.get(cdGetPhone.getId());
            if (lineRegisterId != null) {
                //删除line注册此条记录
                lineRegisterIds.add(lineRegisterId);
                if (Boolean.TRUE.equals(ipClearFlag)) {
                    //ip暂时拉黑
                    this.clearTokenPhone(cdGetPhone);
                }
                //清除注册任务表
                stringRedisTemplate.opsForSet().remove(RedisKeys.REGISTER_TASK_GET_PHONE_IDS.getValue(String.valueOf(systemConstant.getSERVERS_MOD()+PhoneStatus.PhoneStatus2.getKey())), String.valueOf(cdGetPhone.getId()));
                stringRedisTemplate.opsForSet().remove(RedisKeys.REGISTER_TASK_GET_PHONE_IDS.getValue(String.valueOf(systemConstant.getSERVERS_MOD()+RegisterStatus.RegisterStatus4.getKey())), String.valueOf(lineRegisterId));
            }else {
                stringRedisTemplate.opsForSet().remove(RedisKeys.REGISTER_TASK_GET_PHONE_IDS.getValue(String.valueOf(systemConstant.getSERVERS_MOD()+PhoneStatus.PhoneStatus2.getKey())), String.valueOf(cdGetPhone.getId()));
            }
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

        //删除line注册此条记录
        if (CollectionUtils.isNotEmpty(lineRegisterIds)) {
            baseMapper.deleteBatchIds(lineRegisterIds);
        }

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
        List<LineRegisterSummaryResultDto> summaryResultDtos = baseMapper.queryLineRegisterSummary(searchTime);
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
        getPhoneService.updateBatchById(phoneEntityList);

        List<CdLineRegisterEntity> lineRegisterList = baseMapper.selectList(new QueryWrapper<CdLineRegisterEntity>().lambda()
                .in(CdLineRegisterEntity::getGetPhoneId, Arrays.asList(ids))).stream().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(lineRegisterList)) {
            List<CdLineRegisterEntity> updateLineRegisterList = new ArrayList<>();
            for (CdLineRegisterEntity registerEntity : lineRegisterList) {
                CdLineRegisterEntity updateLineRegister = new CdLineRegisterEntity();
                updateLineRegister.setId(registerEntity.getId());
                updateLineRegister.setRegisterStatus(RegisterStatus.RegisterStatus11.getKey());
                updateLineRegisterList.add(updateLineRegister);
            }
            this.updateBatchById(updateLineRegisterList);
        }

        return true;
    }

}
