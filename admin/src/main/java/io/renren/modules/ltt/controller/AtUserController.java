package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.vo.AtUserVO;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 账号数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 01:44:22
 */
@RestController
@RequestMapping("ltt/atuser")
public class AtUserController {
    @Autowired
    private AtUserService atUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atuser:list")
    public R list(AtUserDTO atUser){
        PageUtils page = atUserService.queryPage(atUser);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atuser:info")
    public R info(@PathVariable("id") Integer id){
		AtUserVO atUser = atUserService.getById(id);

        return R.ok().put("atUser", atUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atuser:save")
    public R save(@RequestBody AtUserDTO atUser){
		atUserService.save(atUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atuser:update")
    public R update(@RequestBody AtUserDTO atUser){
		atUserService.updateById(atUser);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atuser:delete")
    public R delete(@RequestBody Integer[] ids){
		atUserService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
