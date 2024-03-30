package io.renren.modules.ltt.controller;

import java.util.Arrays;

import io.renren.modules.ltt.entity.ProductInfoEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.ltt.dto.ProductInfoDTO;
import io.renren.modules.ltt.vo.ProductInfoVO;
import io.renren.modules.ltt.service.ProductInfoService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * 商品信息表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-28 11:39:25
 */
@RestController
@RequestMapping("ltt/productinfo")
public class ProductInfoController {
    @Autowired
    private ProductInfoService productInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:productinfo:list")
    public R list(ProductInfoDTO productInfo) {
        PageUtils page = productInfoService.queryPage(productInfo);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{productId}")
    @RequiresPermissions("ltt:productinfo:info")
    public R info(@PathVariable("productId") Integer productId) {
        ProductInfoVO productInfo = productInfoService.getById(productId);

        return R.ok().put("productInfo", productInfo);
    }

    /**
     * 获取商品信息
     */
    @RequestMapping("/queryOnlyProduct")
    @RequiresPermissions("ltt:productinfo:info")
    public R queryOnlyProduct(@RequestParam("productType") Integer productType,
                              @RequestParam("countryCode") String countryCode,
                              @RequestParam(value = "status", required = false) Integer status) {
        ProductInfoEntity productInfo = productInfoService.queryOnlyProduct(productType, countryCode, status);

        return R.ok().put("productInfo", productInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:productinfo:save")
    public R save(@RequestBody ProductInfoDTO productInfo) {
        productInfoService.save(productInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:productinfo:update")
    public R update(@RequestBody ProductInfoDTO productInfo) {
        productInfoService.updateById(productInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:productinfo:delete")
    public R delete(@RequestBody Integer[] productIds) {
        productInfoService.removeByIds(Arrays.asList(productIds));

        return R.ok();
    }

}
