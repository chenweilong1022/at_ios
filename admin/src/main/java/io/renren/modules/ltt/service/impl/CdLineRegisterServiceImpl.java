package io.renren.modules.ltt.service.impl;

import cn.hutool.core.util.ObjectUtil;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import io.renren.modules.ltt.enums.PhoneStatus;
import io.renren.modules.ltt.enums.RegisterStatus;
import io.renren.modules.ltt.service.CdGetPhoneService;
import io.renren.modules.ltt.service.CdLineIpProxyService;
import io.renren.modules.ltt.vo.CdGetPhoneVO;
import io.renren.modules.ltt.vo.GetCountBySubTaskIdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.CdLineRegisterDao;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import io.renren.modules.ltt.dto.CdLineRegisterDTO;
import io.renren.modules.ltt.vo.CdLineRegisterVO;
import io.renren.modules.ltt.service.CdLineRegisterService;
import io.renren.modules.ltt.conver.CdLineRegisterConver;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Service("cdLineRegisterService")
@Game
public class CdLineRegisterServiceImpl extends ServiceImpl<CdLineRegisterDao, CdLineRegisterEntity> implements CdLineRegisterService {

    @Resource
    private CdGetPhoneService getPhoneService;

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
        return PageUtils.<CdLineRegisterVO>page(page);
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
    public boolean registerRetry(Integer id) {
        CdLineRegisterEntity cdLineRegisterEntity = baseMapper.selectById(id);
        Assert.isTrue(ObjectUtil.isNull(cdLineRegisterEntity), "数据为空");
        Assert.isTrue(ObjectUtil.isNull(cdLineRegisterEntity.getGetPhoneId()), "无对应的手机记录，请确认");
        Assert.isTrue(!RegisterStatus.RegisterStatus5.getKey().equals(cdLineRegisterEntity.getRegisterStatus()), "注册状态正常，无需重试");


        CdGetPhoneVO cdGetPhone = getPhoneService.getById(cdLineRegisterEntity.getGetPhoneId());
        Assert.isTrue(ObjectUtil.isNull(cdGetPhone), "数据为空");

        //更新此条数据，发起重新注册
        CdGetPhoneEntity updateCdGetPhoneEntity = new CdGetPhoneEntity();
        updateCdGetPhoneEntity.setId(cdGetPhone.getId());
        updateCdGetPhoneEntity.setPhoneStatus(PhoneStatus.PhoneStatus1.getKey());
        updateCdGetPhoneEntity.setCode("");
        updateCdGetPhoneEntity.setCreateTime(new Date());
        getPhoneService.updateById(updateCdGetPhoneEntity);

        //获取代理
        CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
        cdLineIpProxyDTO.setTokenPhone(cdGetPhone.getPhone());
        cdLineIpProxyDTO.setLzPhone(cdGetPhone.getPhone());
        cdLineIpProxyDTO.setNewIp(true);
        cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);

        //删除line注册此条记录
        return baseMapper.deleteById(cdLineRegisterEntity.getId()) > 0;
    }

}
