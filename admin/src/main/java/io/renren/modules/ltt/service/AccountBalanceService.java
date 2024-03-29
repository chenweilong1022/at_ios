package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AccountBalanceDTO;
import io.renren.modules.ltt.vo.AccountBalanceVO;
import io.renren.modules.ltt.entity.AccountBalanceEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 账户余额表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-28 11:39:25
 */
public interface AccountBalanceService extends IService<AccountBalanceEntity> {

    /**
     * 分页查询
     * @param accountBalance
     * @return
     */
    PageUtils queryPage(AccountBalanceDTO accountBalance);
    /**
     * 根据id查询
     * @param accountId
     * @return
     */
    AccountBalanceVO getById(Integer accountId);

    AccountBalanceVO getBySysUserId(Long sysUserId);

    /**
     * 保存账户资金变动
     * @param accountBalance
     * @return
     */
    boolean changeAccount(AccountBalanceDTO accountBalance);
    /**
     * 根据id修改
     * @param accountBalance
     * @return
     */
    boolean updateById(AccountBalanceDTO accountBalance);
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

