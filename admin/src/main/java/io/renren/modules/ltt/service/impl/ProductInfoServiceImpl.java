package io.renren.modules.ltt.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.renren.common.validator.Assert;
import io.renren.datasources.annotation.Game;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.ltt.dao.ProductInfoDao;
import io.renren.modules.ltt.entity.ProductInfoEntity;
import io.renren.modules.ltt.dto.ProductInfoDTO;
import io.renren.modules.ltt.vo.ProductInfoVO;
import io.renren.modules.ltt.service.ProductInfoService;
import io.renren.modules.ltt.conver.ProductInfoConver;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service("productInfoService")
@Game
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoDao, ProductInfoEntity> implements ProductInfoService {

    @Override
    public PageUtils<ProductInfoVO> queryPage(ProductInfoDTO productInfo) {
        IPage<ProductInfoEntity> page = baseMapper.selectPage(
                new Query<ProductInfoEntity>(productInfo).getPage(),
                new QueryWrapper<ProductInfoEntity>().lambda()
                        .likeRight(StringUtils.isNotEmpty(productInfo.getProductName()),
                                ProductInfoEntity::getProductName, productInfo.getProductName())
                        .orderByDesc(ProductInfoEntity::getProductId)
        );

        return PageUtils.<ProductInfoVO>page(page).setList(ProductInfoConver.MAPPER.conver(page.getRecords()));
    }

    @Override
    public ProductInfoVO getById(Integer productId) {
        return ProductInfoConver.MAPPER.conver(baseMapper.selectById(productId));
    }

    @Override
    public boolean save(ProductInfoDTO productInfo) {
        this.checkParam(productInfo);//校验入参

        ProductInfoEntity productInfoEntity = ProductInfoConver.MAPPER.converDTO(productInfo);
        productInfoEntity.setCreateTime(new Date());
        productInfoEntity.setUpdateTime(new Date());
        productInfoEntity.setStatus(0);//先下架
        return this.save(productInfoEntity);
    }

    @Override
    public boolean updateById(ProductInfoDTO productInfo) {
        this.checkParam(productInfo);//校验入参

        ProductInfoEntity productInfoEntity = ProductInfoConver.MAPPER.converDTO(productInfo);
        return this.updateById(productInfoEntity);
    }

    private Boolean checkParam(ProductInfoDTO productInfo) {
        //商品类型+地区唯一
        List<ProductInfoEntity> productList = baseMapper
                .selectList(new QueryWrapper<ProductInfoEntity>().lambda()
                        .eq(ProductInfoEntity::getProductType, productInfo.getProductType())
                        .eq(ProductInfoEntity::getCountryCode, productInfo.getCountryCode()));

        if (ObjectUtil.isNull(productInfo.getProductId())) {
            //新增
            Assert.isTrue(CollectionUtil.isNotEmpty(productList), "商品类型加国号，此组合商品已存在，不可新增");
        } else {
            //修改
            if (CollectionUtil.isNotEmpty(productList)) {
                List<Integer> productIdList = productList.stream().map(ProductInfoEntity::getProductId).collect(Collectors.toList());
                Assert.isTrue(!productIdList.contains(productInfo.getProductId()), "商品类型加国号，此组合商品已存在，不可修改");
            }
        }
        return true;
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
