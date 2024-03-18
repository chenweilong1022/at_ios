package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.CustomerUserOperationParamDto;
import io.renren.modules.ltt.dto.CustomerUserParamDto;
import io.renren.modules.ltt.dto.CustomerUserResultDto;
import io.renren.modules.sys.dto.SysUserDto;
import io.renren.modules.sys.entity.SysUserEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 系统用户
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-18 12:08:34
 */
public interface CustomerUserService extends IService<SysUserEntity> {

    /**
     * 分页查询
     * @param sysUser
     * @return
     */
    PageUtils queryPage(CustomerUserParamDto sysUser);
    /**
     * 根据id查询
     * @param userId
     * @return
     */
    CustomerUserResultDto getById(Long userId);
    /**
     * 保存
     * @param sysUser
     * @return
     */
    boolean save(CustomerUserOperationParamDto sysUser);
    /**
     * 根据id修改
     * @param sysUser
     * @return
     */
    boolean updateById(CustomerUserOperationParamDto sysUser);
    /**
     * 根据id修改密码
     * @param sysUser
     * @return
     */
    boolean updatePassword(CustomerUserOperationParamDto sysUser);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @Override
    boolean removeByIds(Collection<? extends Serializable> ids);
}

