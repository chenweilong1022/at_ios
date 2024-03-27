package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.CdGetPhoneDTO;
import io.renren.modules.ltt.vo.CdGetPhoneVO;
import io.renren.modules.ltt.service.CdGetPhoneService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:12
 */
@RestController
@RequestMapping("ltt/cdgetphone")
public class CdGetPhoneController {
    @Autowired
    private CdGetPhoneService cdGetPhoneService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:cdgetphone:list")
    public R list(CdGetPhoneDTO cdGetPhone){
        PageUtils page = cdGetPhoneService.queryPage(cdGetPhone);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:cdgetphone:info")
    public R info(@PathVariable("id") Integer id){
		CdGetPhoneVO cdGetPhone = cdGetPhoneService.getById(id);

        return R.ok().put("cdGetPhone", cdGetPhone);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:cdgetphone:save")
    public R save(@RequestBody CdGetPhoneDTO cdGetPhone){
		cdGetPhoneService.save(cdGetPhone);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:cdgetphone:update")
    public R update(@RequestBody CdGetPhoneDTO cdGetPhone){
		cdGetPhoneService.updateById(cdGetPhone);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:cdgetphone:delete")
    public R delete(@RequestBody Integer[] ids){
		cdGetPhoneService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
