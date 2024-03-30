package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtGroupDTO;
import io.renren.modules.ltt.vo.AtGroupVO;
import io.renren.modules.ltt.service.AtGroupService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-29 17:07:23
 */
@RestController
@RequestMapping("ltt/atgroup")
public class AtGroupController {
    @Autowired
    private AtGroupService atGroupService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atgroup:list")
    public R list(AtGroupDTO atGroup){
        PageUtils page = atGroupService.queryPage(atGroup);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atgroup:info")
    public R info(@PathVariable("id") Integer id){
		AtGroupVO atGroup = atGroupService.getById(id);

        return R.ok().put("atGroup", atGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atgroup:save")
    public R save(@RequestBody AtGroupDTO atGroup){
		atGroupService.save(atGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atgroup:update")
    public R update(@RequestBody AtGroupDTO atGroup){
		atGroupService.updateById(atGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atgroup:delete")
    public R delete(@RequestBody Integer[] ids){
		atGroupService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
