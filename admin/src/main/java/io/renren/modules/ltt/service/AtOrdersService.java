package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.AtOrdersDTO;
import io.renren.modules.ltt.dto.AtOrdersTokenParamDTO;
import io.renren.modules.ltt.vo.AtOrdersVO;
import io.renren.modules.ltt.entity.AtOrdersEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 订单表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-29 17:15:12
 */
public interface AtOrdersService extends IService<AtOrdersEntity> {

    /**
     * 分页查询
     * @param atOrders
     * @return
     */
    PageUtils queryPage(AtOrdersDTO atOrders);
    /**
     * 根据id查询
     * @param orderId
     * @return
     */
    AtOrdersVO getById(Long orderId);

    /**
     * 生成token订单
     * @param atOrders
     * @return
     */
    boolean createOrderToken(AtOrdersTokenParamDTO atOrders);

    /**
     * 根据id修改
     * @param atOrders
     * @return
     */
    boolean updateById(AtOrdersDTO atOrders);
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

