package io.renren.modules.ltt.controller;

import java.util.Arrays;

import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtDataDTO;
import io.renren.modules.ltt.vo.AtDataVO;
import io.renren.modules.ltt.service.AtDataService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 01:14:06
 */
@RestController
@RequestMapping("ltt/atdata")
public class AtDataController extends AbstractController {
    @Autowired
    private AtDataService atDataService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atdata:list")
    public R list(AtDataDTO atData){
        atData.setSysUserId(getAuthUserId());
        PageUtils page = atDataService.queryPage(atData);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atdata:info")
    public R info(@PathVariable("id") Integer id){
		AtDataVO atData = atDataService.getById(id);

        return R.ok().put("atData", atData);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atdata:save")
    public R save(@RequestBody AtDataDTO atData){
        atData.setSysUserId(getUserId());
		atDataService.save(atData);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atdata:update")
    public R update(@RequestBody AtDataDTO atData){
		atDataService.updateById(atData);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atdata:delete")
    public R delete(@RequestBody Integer[] ids){
		atDataService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
