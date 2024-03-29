package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AccountDetailsDTO;
import io.renren.modules.ltt.vo.AccountDetailsVO;
import io.renren.modules.ltt.entity.AccountDetailsEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 流水明细表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-28 11:39:25
 */
public interface AccountDetailsService extends IService<AccountDetailsEntity> {

    /**
     * 分页查询
     * @param accountDetails
     * @return
     */
    PageUtils queryPage(AccountDetailsDTO accountDetails);
    /**
     * 根据id查询
     * @param transactionId
     * @return
     */
    AccountDetailsVO getById(Integer transactionId);
    /**
     * 保存
     * @param accountDetails
     * @return
     */
    boolean save(AccountDetailsDTO accountDetails);
    /**
     * 根据id修改
     * @param accountDetails
     * @return
     */
    boolean updateById(AccountDetailsDTO accountDetails);
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

