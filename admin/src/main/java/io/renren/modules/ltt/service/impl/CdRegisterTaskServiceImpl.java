package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.utils.PhoneUtil;
import io.renren.common.utils.vo.PhoneCountryVO;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.SfTimeZone;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import io.renren.modules.ltt.entity.CdRegisterSubtasksEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.modules.ltt.service.CdGetPhoneService;
import io.renren.modules.ltt.service.CdRegisterSubtasksService;
import io.renren.modules.ltt.vo.IOSTaskVO;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.CdRegisterTaskDao;
import io.renren.modules.ltt.entity.CdRegisterTaskEntity;
import io.renren.modules.ltt.dto.CdRegisterTaskDTO;
import io.renren.modules.ltt.vo.CdRegisterTaskVO;
import io.renren.modules.ltt.service.CdRegisterTaskService;
import io.renren.modules.ltt.conver.CdRegisterTaskConver;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Service("cdRegisterTaskService")
@Game
public class CdRegisterTaskServiceImpl extends ServiceImpl<CdRegisterTaskDao, CdRegisterTaskEntity> implements CdRegisterTaskService {

    @Resource
    private CdRegisterSubtasksService cdRegisterSubtasksService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageUtils<CdRegisterTaskVO> queryPage(CdRegisterTaskDTO cdRegisterTask) {
        IPage<CdRegisterTaskEntity> page = baseMapper.selectPage(
                new Query<CdRegisterTaskEntity>(cdRegisterTask).getPage(),
                new QueryWrapper<CdRegisterTaskEntity>().lambda()
                        .lt(CdRegisterTaskEntity::getFillUpRegisterTaskId, 0)
                        .eq(ObjectUtil.isNotNull(cdRegisterTask.getCountryCode()),CdRegisterTaskEntity::getCountryCode, cdRegisterTask.getCountryCode() )
                        .eq(ObjectUtil.isNotNull(cdRegisterTask.getRegistrationStatus()),CdRegisterTaskEntity::getRegistrationStatus, cdRegisterTask.getRegistrationStatus() )
                        .orderByDesc(CdRegisterTaskEntity::getId)
        );

        return PageUtils.<CdRegisterTaskVO>page(page).setList(CdRegisterTaskConver.MAPPER.conver(page.getRecords()));
    }

    @Override
    public CdRegisterTaskVO getById(Integer id) {
        return CdRegisterTaskConver.MAPPER.conver(baseMapper.selectById(id));
    }

    @Resource(name = "stringQueueCacheIOSTaskVO")
    private Cache<String, Queue<IOSTaskVO>> stringQueueCacheIOSTaskVO;
    @Autowired
    private CdGetPhoneService cdGetPhoneService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(CdRegisterTaskDTO cdRegisterTask) {
        cdRegisterTask.setNumberRegistered(0);
        cdRegisterTask.setNumberSuccesses(0);
        cdRegisterTask.setNumberFailures(0);
        cdRegisterTask.setCreateTime(DateUtil.date());
        cdRegisterTask.setDeleteFlag(DeleteFlag.NO.getKey());
        cdRegisterTask.setFillUpRegisterTaskId(-1);
        cdRegisterTask.setRegistrationStatus(RegistrationStatus.RegistrationStatus1.getKey());
        //真机
        if (RealMachine.RealMachine2.getKey().equals(cdRegisterTask.getRealMachine())) {
            cdRegisterTask.setRegistrationStatus(RegistrationStatus.RegistrationStatus9.getKey());
            IOSTaskVO iosTaskVO = new IOSTaskVO();
            iosTaskVO.setTaskType("register");

            Queue<IOSTaskVO> cacheIOSTaskVOIfPresent = stringQueueCacheIOSTaskVO.getIfPresent("register");
            if (CollUtil.isEmpty(cacheIOSTaskVOIfPresent) || cacheIOSTaskVOIfPresent.isEmpty()) {
                cacheIOSTaskVOIfPresent = new LinkedList<>();
            }
            cacheIOSTaskVOIfPresent.offer(iosTaskVO);
            stringQueueCacheIOSTaskVO.put("register", cacheIOSTaskVOIfPresent);
        }
        CdRegisterTaskEntity cdRegisterTaskEntity = CdRegisterTaskConver.MAPPER.converDTO(cdRegisterTask);
        boolean save = this.save(cdRegisterTaskEntity);
//        proxyIp

//        redisTemplate.opsForValue().set("","");

        //获取国家 如果是四方
        Integer countryCode = cdRegisterTask.getCountryCode();
        List<String> phones = new ArrayList<>();
        String urlString = "";
        if (CountryCode.CountryCode8.getKey().equals(countryCode)) {
            String sfData = cdRegisterTask.getSfData();
            String[] split = sfData.trim().split("\n");
            for (String s : split) {
                try {
                    PhoneCountryVO phoneNumberInfo = PhoneUtil.getPhoneNumberInfo(CountryCode.CountryCode3.getKey() + s);
                    phones.add(s);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    if (s.contains("http")) {
                        urlString = getSmsListApi(s);
                    }
                }
            }

            //获取子任务数量
            int count = cdRegisterSubtasksService.count(new QueryWrapper<CdRegisterSubtasksEntity>().lambda()
                    .eq(CdRegisterSubtasksEntity::getTaskId, cdRegisterTaskEntity.getId())
            );
            //查询子任务是否在注册
            if (count == 0) {
                List<CdRegisterSubtasksEntity> cdRegisterSubtasksEntities = new ArrayList<>();
                boolean flag = true;
                while (flag) {
                    //总数量
                    Integer totalAmount = cdRegisterTaskEntity.getTotalAmount();
                    //线程数
                    Integer numberThreads = cdRegisterTaskEntity.getNumberThreads();
                    //注册数量 0
                    Integer numberRegistered = cdRegisterTaskEntity.getNumberRegistered();
                    //剩余注册数量
                    Integer newNumberRegistrations = totalAmount - numberRegistered;
                    if (newNumberRegistrations > numberThreads) {
                        newNumberRegistrations = numberThreads;
                    }else{
                        flag = false;
                        cdRegisterTaskEntity.setRegistrationStatus(RegistrationStatus.RegistrationStatus2.getKey());
                    }
                    CdRegisterSubtasksEntity cdRegisterSubtasksEntity = new CdRegisterSubtasksEntity();
                    cdRegisterSubtasksEntity.setTaskId(cdRegisterTaskEntity.getId());
                    if (cdRegisterTaskEntity.getFillUpRegisterTaskId() > 0) {
                        cdRegisterSubtasksEntity.setTaskId(cdRegisterTaskEntity.getFillUpRegisterTaskId());
                    }
                    cdRegisterSubtasksEntity.setCountryCode(cdRegisterTaskEntity.getCountryCode());
                    cdRegisterSubtasksEntity.setNumberRegistrations(newNumberRegistrations > 0 ? newNumberRegistrations : numberRegistered);
                    cdRegisterSubtasksEntity.setNumberSuccesses(0);
                    cdRegisterSubtasksEntity.setNumberFailures(0);
                    cdRegisterSubtasksEntity.setRegistrationStatus(RegistrationStatus.RegistrationStatus6.getKey());
                    cdRegisterSubtasksEntity.setCreateTime(DateUtil.date());
                    cdRegisterSubtasksEntity.setDeleteFlag(DeleteFlag.NO.getKey());
                    cdRegisterSubtasksEntities.add(cdRegisterSubtasksEntity);
                    //设置主表注册数量
                    cdRegisterTaskEntity.setNumberRegistered(cdRegisterTaskEntity.getNumberRegistered() + newNumberRegistrations);
                }
                //保存子任务
                cdRegisterSubtasksService.saveBatch(cdRegisterSubtasksEntities,cdRegisterSubtasksEntities.size());
                //修改状态
                this.updateById(cdRegisterTaskEntity);

                Queue<String> queue = new LinkedList<>(phones);
                List<CdGetPhoneEntity> cdGetPhoneEntities = new ArrayList<>();
                for (CdRegisterSubtasksEntity cdRegisterSubtasksEntity : cdRegisterSubtasksEntities) {
                    Integer numberRegistrations = cdRegisterSubtasksEntity.getNumberRegistrations();
                    for (Integer i = 0; i < numberRegistrations; i++) {
                        String poll = queue.poll();
                        Assert.isTrue(StrUtil.isEmpty(poll),"注册数据小于注册数量");
                        CdGetPhoneEntity cdGetPhoneEntity = new CdGetPhoneEntity();
                        cdGetPhoneEntity.setNumber(poll);
                        cdGetPhoneEntity.setPkey(poll);
                        cdGetPhoneEntity.setTime("");
                        cdGetPhoneEntity.setCountry(cdRegisterSubtasksEntity.getCountryCode().toString());
                        cdGetPhoneEntity.setCountrycode(CountryCode.getValueByKey(cdRegisterSubtasksEntity.getCountryCode()));
                        cdGetPhoneEntity.setOther("");
                        cdGetPhoneEntity.setCom("");
                        cdGetPhoneEntity.setPhone(CountryCode.CountryCode3.getKey() + poll);
                        cdGetPhoneEntity.setDeleteFlag(DeleteFlag.NO.getKey());
                        cdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus1.getKey());
                        cdGetPhoneEntity.setCreateTime(DateUtil.date());
                        cdGetPhoneEntity.setSubtasksId(cdRegisterSubtasksEntity.getId());
                        cdGetPhoneEntity.setSfApi(urlString);
                        cdGetPhoneEntity.setTimeZone(SfTimeZone.SfTimeZone2.getKey());
                        if (urlString.contains("vip")) {
                            cdGetPhoneEntity.setTimeZone(SfTimeZone.SfTimeZone1.getKey());
                        }
                        cdGetPhoneEntities.add(cdGetPhoneEntity);
                    }
                }
                cdGetPhoneService.saveBatch(cdGetPhoneEntities);
            }
        }
        return save;
    }

    public String getSmsListApi(String urlString) {
        try {
            URL url = new URL(urlString.trim());
            String format = String.format("http://%s:%s/api/smslist?token=%s", url.getHost(), url.getPort(), url.getPath().replace("/", ""));
            return format;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            String urlString = "http://sms.newszfang.vip:3000/yTrfmbhtu85fa28BVEWCSP";
            URL url = new URL(urlString);
            String host = url.getHost();

            // 解析主机名获取IP地址
            InetAddress address = InetAddress.getByName(host);
            String ipAddress = address.getHostAddress();

            System.out.println("IP Address: " + ipAddress);

        } catch (MalformedURLException | UnknownHostException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void createRegisterTask(Integer registerCount, Integer countryCode) {
        CdRegisterTaskDTO registerTaskDTO = new CdRegisterTaskDTO();
        registerTaskDTO.setTotalAmount(registerCount);
        registerTaskDTO.setNumberThreads(50);
        registerTaskDTO.setFillUp(1);
        registerTaskDTO.setCountryCode(countryCode);
        registerTaskDTO.setCreateTime(new Date());
        registerTaskDTO.setDeleteFlag(DeleteFlag.NO.getKey());
        this.save(registerTaskDTO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean stopRegisterTask(Integer taskId) {
        CdRegisterTaskEntity cdRegisterTaskEntity = new CdRegisterTaskEntity();
        cdRegisterTaskEntity.setId(taskId);
        cdRegisterTaskEntity.setRegistrationStatus(RegistrationStatus.RegistrationStatus3.getKey());
        this.updateById(cdRegisterTaskEntity);

        //更新子表
        return cdRegisterSubtasksService.updateStatusByTaskId(taskId, RegistrationStatus.RegistrationStatus3.getKey());
    }

    @Override
    public boolean updateById(CdRegisterTaskDTO cdRegisterTask) {
        CdRegisterTaskEntity cdRegisterTaskEntity = CdRegisterTaskConver.MAPPER.converDTO(cdRegisterTask);
        return this.updateById(cdRegisterTaskEntity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        //真机注册清除
        Queue<IOSTaskVO> cacheIOSTaskVOIfPresent = new LinkedList<>();
        stringQueueCacheIOSTaskVO.put("register", cacheIOSTaskVOIfPresent);
        return super.removeByIds(ids);
    }

    @Override
    public Integer sumByTaskId(Integer id) {
        return baseMapper.sumByTaskId(id);
    }

    @Override
    public CdRegisterTaskEntity queryRealMachineRegister() {
        return baseMapper.selectList(new QueryWrapper<CdRegisterTaskEntity>().lambda()
                .eq(CdRegisterTaskEntity::getRegistrationStatus, RegistrationStatus.RegistrationStatus9.getKey())).stream().findFirst().orElse(null);
    }

}
