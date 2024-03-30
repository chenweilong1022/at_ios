package io.renren.modules.ltt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.ltt.dto.ProductInfoDTO;
import io.renren.modules.ltt.vo.ProductInfoVO;
import io.renren.modules.ltt.entity.ProductInfoEntity;

import java.io.Serializable;
import java.util.Collection;


/**
 * 商品信息表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-28 11:39:25
 */
public interface ProductInfoService extends IService<ProductInfoEntity> {

    /**
     * 分页查询
     * @param productInfo
     * @return
     */
    PageUtils queryPage(ProductInfoDTO productInfo);
    /**
     * 根据id查询
     * @param productId
     * @return
     */
    ProductInfoVO getById(Integer productId);
    /**
     * 保存
     * @param productInfo
     * @return
     */
    boolean save(ProductInfoDTO productInfo);
    /**
     * 根据id修改
     * @param productInfo
     * @return
     */
    boolean updateById(ProductInfoDTO productInfo);

    /**
     * 获取商品信息
     * @param productType
     * @param countryCode
     * @param status
     * @return
     */
    ProductInfoEntity queryOnlyProduct(Integer productType,
                                       Integer countryCode,
                                       Integer status);

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

