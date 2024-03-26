package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dao.UpdateAtUserCustomerParamDto;
import io.renren.modules.ltt.dao.UpdateUserGroupParamDto;
import io.renren.modules.ltt.dao.ValidateAtUserStatusParamDto;
import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.vo.AtUserVO;
import io.renren.modules.ltt.entity.AtUserEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 账号数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 01:55:49
 */
public interface AtUserService extends IService<AtUserEntity> {

    /**
     * 分页查询
     * @param atUser
     * @return
     */
    PageUtils queryPage(AtUserDTO atUser);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtUserVO getById(Integer id);
    /**
     * 保存
     * @param atUser
     * @return
     */
    boolean save(AtUserDTO atUser);
    /**
     * 根据id修改
     * @param atUser
     * @return
     */
    boolean updateById(AtUserDTO atUser);

    /**
     * 更改用户分组
     * @return
     */
    boolean updateUserGroup(UpdateUserGroupParamDto paramDto);

    /**
     * 分配客服
     * @param paramDto
     * @return
     */
    boolean updateUserCustomer(UpdateAtUserCustomerParamDto paramDto);

    /**
     * 验活账号
     * @param paramDto
     * @return
     */
    boolean validateUserStatus(ValidateAtUserStatusParamDto paramDto);

    /**
     * 下载账户数据
     * @param ids
     * @return
     */
    String downloadUserTokenTxt(List<Integer> ids);

    /**
     * 用户电话
     * @param ids
     * @return
     */
    Map<Integer, String> queryTelephoneByIds(List<Integer> ids);

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

