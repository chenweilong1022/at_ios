package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dao.UpdateAtUserCustomerParamDto;
import io.renren.modules.ltt.dao.UpdateUserGroupParamDto;
import io.renren.modules.ltt.dao.ValidateAtUserStatusParamDto;
import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.dto.UserSummaryResultDto;
import io.renren.modules.ltt.enums.UserStatus;
import io.renren.modules.ltt.vo.AtUserVO;
import io.renren.modules.ltt.entity.AtUserEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDate;
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
    PageUtils queryPage1(AtUserDTO atUser);

    /**
     * 分页查询
     * @param atUser
     * @return
     */
    PageUtils queryPageOld(AtUserDTO atUser);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    AtUserVO getById(Integer id);
//    /**
//     * 根据id查询
//     * @param ids
//     * @return
//     */
//    Map<Integer, AtUserEntity> getIds(List<Integer> ids);
    /**
     * 根据id查询
     * @param id
     * @return
     */
    boolean unlock(Integer id, UserStatus userStatus);
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
     * 养号
     * @param paramDto
     * @return
     */
    boolean maintainUser(ValidateAtUserStatusParamDto paramDto);

    /**
     * 下载账户数据
     * @param ids
     * @return
     */
    String downloadUserTokenTxt(List<Integer> ids);

    /**
     * 导出token
      * @param ids
     * @return
     */
    byte[] importToken(List<Integer> ids);

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

    /**
     * 清理封号数据
     */
    void cleanBlockData(Long sysUserId, Integer[] ids);

    /**
     * 账号使用情况汇总
     * @return
     */
    Map<String, Integer> queryUsedUserSummary(LocalDate searchTime);

    Map<String, Integer> queryOnlineUserSummary();

    /**
     * 内部使用接口，同步redis和数据库，注册次数
     * @param phone 传入则同步此phone
     */
    void syncRegisterCountTest(String phone);

    /**
     * 内部使用接口，同步注册时间
     */
    void syncPhoneRegisterTest();

    /**
     * 内部使用接口，通过红灯时间
     */
    @Async
    void syncPhoneInvalidTest(String phoneParam);
}

