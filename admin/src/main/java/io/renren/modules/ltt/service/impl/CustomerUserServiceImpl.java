package io.renren.modules.ltt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.constant.SystemConstant;
import io.renren.common.utils.BaseBeanUtils;
import io.renren.common.utils.PageUtils;
import io.renren.datasources.annotation.Game;
import io.renren.modules.ltt.dao.CustomerUserDao;
import io.renren.modules.ltt.dto.CustomerUserOperationParamDto;
import io.renren.modules.ltt.dto.CustomerUserParamDto;
import io.renren.modules.ltt.dto.CustomerUserResultDto;
import io.renren.modules.ltt.service.CustomerUserService;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserRoleService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Service
@Game
public class CustomerUserServiceImpl extends ServiceImpl<CustomerUserDao, SysUserEntity> implements CustomerUserService {

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SystemConstant systemConstant;

    @Override
    public PageUtils<CustomerUserResultDto> queryPage(CustomerUserParamDto userParamDto) {
        userParamDto.setRoleId(systemConstant.getCUSTOMER_ROLE_ID());
        userParamDto.setPageStart((userParamDto.getPage() - 1) * userParamDto.getLimit());
        Integer count = baseMapper.queryPageCount(userParamDto);
        List<CustomerUserResultDto> userList = Collections.emptyList();
        if (count > 0) {
            userList = baseMapper.queryPage(userParamDto);
        }
        return new PageUtils(userList, count, userParamDto.getLimit(), userParamDto.getPage());
    }

    @Override
    public CustomerUserResultDto getById(Long userId) {
        SysUserEntity sysUserEntity = baseMapper.selectById(userId);
        return BaseBeanUtils.map(sysUserEntity, CustomerUserResultDto.class);
    }

    @Override
    public List<CustomerUserResultDto> queryByFuzzyName(String key, Long createUserId) {
        CustomerUserParamDto userParamDto = new CustomerUserParamDto();
        userParamDto.setKey(key);
        userParamDto.setRoleId(systemConstant.getCUSTOMER_ROLE_ID());
        userParamDto.setCreateUserId(createUserId);
        return baseMapper.queryByFuzzyName(userParamDto);
    }

    @Override
    public boolean save(CustomerUserOperationParamDto sysUser) {
        SysUserEntity sysUserEntity = BaseBeanUtils.map(sysUser, SysUserEntity.class);
        sysUserEntity.setCreateTime(new Date());
        sysUserEntity.setUpdateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        sysUserEntity.setPassword(new Sha256Hash(sysUser.getPassword(), salt).toHex());
        sysUserEntity.setSalt(salt);
        sysUserEntity.setStatus(1);
        sysUserEntity.setOnlineStatus(0);
        sysUserEntity.setCreateUserId(sysUser.getCreateUserId());
        baseMapper.insert(sysUserEntity);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(sysUserEntity.getUserId(), Collections.singletonList(systemConstant.getCUSTOMER_ROLE_ID()));
        return true;
    }

    @Override
    public boolean updateById(CustomerUserOperationParamDto sysUser) {
        SysUserEntity sysUserEntity = BaseBeanUtils.map(sysUser, SysUserEntity.class);
        sysUserEntity.setUpdateTime(new Date());
        return this.updateById(sysUserEntity);
    }

    @Override
    public boolean updatePassword(CustomerUserOperationParamDto sysUser) {
        SysUserEntity sysUserEntity = BaseBeanUtils.map(sysUser, SysUserEntity.class);
        sysUserEntity.setUpdateTime(new Date());
        //sha256加密
        String salt = RandomStringUtils.randomAlphanumeric(20);
        sysUserEntity.setPassword(new Sha256Hash(sysUser.getPassword(), salt).toHex());
        sysUserEntity.setSalt(salt);
        return this.updateById(sysUserEntity);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> ids) {
        return super.removeByIds(ids);
    }

}
