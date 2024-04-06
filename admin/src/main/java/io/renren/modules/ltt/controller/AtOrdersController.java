package io.renren.modules.ltt.controller;

import java.util.Arrays;

import io.renren.modules.ltt.dto.AtOrdersTokenParamDTO;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.ltt.dto.AtOrdersDTO;
import io.renren.modules.ltt.vo.AtOrdersVO;
import io.renren.modules.ltt.service.AtOrdersService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 订单表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-29 17:15:12
 */
@RestController
@RequestMapping("ltt/atorders")
public class AtOrdersController extends AbstractController {
    @Autowired
    private AtOrdersService atOrdersService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atorders:list")
    public R list(AtOrdersDTO atOrders){
        atOrders.setSysUserId(getAuthUserId());
        PageUtils page = atOrdersService.queryPage(atOrders);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{orderId}")
    @RequiresPermissions("ltt:atorders:info")
    public R info(@PathVariable("orderId") Long orderId){
		AtOrdersVO atOrders = atOrdersService.getById(orderId);

        return R.ok().put("atOrders", atOrders);
    }

    /**
     * 生成token订单
     */
    @PostMapping("/createOrderToken")
    @RequiresPermissions("ltt:atorders:save")
    public R createOrderToken(@RequestBody AtOrdersTokenParamDTO atOrders){
        atOrders.setSysUserId(getAuthUserId());
		atOrdersService.createOrderToken(atOrders);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atorders:update")
    public R update(@RequestBody AtOrdersDTO atOrders){
		atOrdersService.updateById(atOrders);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atorders:delete")
    public R delete(@RequestBody Long[] orderIds){
		atOrdersService.removeByIds(Arrays.asList(orderIds));

        return R.ok();
    }

}
