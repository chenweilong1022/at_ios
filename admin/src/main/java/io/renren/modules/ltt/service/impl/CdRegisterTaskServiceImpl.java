package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.enums.DeleteFlag;
import io.renren.modules.ltt.enums.RealMachine;
import io.renren.modules.ltt.enums.RegistrationStatus;
import io.renren.modules.ltt.service.CdRegisterSubtasksService;
import io.renren.modules.ltt.vo.IOSTaskVO;
import org.apache.commons.lang.StringUtils;
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
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;


@Service("cdRegisterTaskService")
@Game
public class CdRegisterTaskServiceImpl extends ServiceImpl<CdRegisterTaskDao, CdRegisterTaskEntity> implements CdRegisterTaskService {

    @Resource
    private CdRegisterSubtasksService cdRegisterSubtasksService;

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

    @Override
    public boolean save(CdRegisterTaskDTO cdRegisterTask) {
        cdRegisterTask.setNumberRegistered(0);
        cdRegisterTask.setNumberSuccesses(0);
        cdRegisterTask.setNumberFailures(0);
        cdRegisterTask.setCreateTime(DateUtil.date());
        cdRegisterTask.setDeleteFlag(DeleteFlag.NO.getKey());
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
        return this.save(cdRegisterTaskEntity);
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
