package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtUserGroupDTO;
import io.renren.modules.ltt.vo.AtUserGroupVO;
import io.renren.modules.ltt.service.AtUserGroupService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 用户分组表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-27 17:18:34
 */
@RestController
@RequestMapping("ltt/atusergroup")
public class AtUserGroupController {
    @Autowired
    private AtUserGroupService atUserGroupService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atusergroup:list")
    public R list(AtUserGroupDTO atUserGroup){
        PageUtils page = atUserGroupService.queryPage(atUserGroup);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atusergroup:info")
    public R info(@PathVariable("id") Integer id){
		AtUserGroupVO atUserGroup = atUserGroupService.getById(id);

        return R.ok().put("atUserGroup", atUserGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atusergroup:save")
    public R save(@RequestBody AtUserGroupDTO atUserGroup){
		atUserGroupService.save(atUserGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atusergroup:update")
    public R update(@RequestBody AtUserGroupDTO atUserGroup){
		atUserGroupService.updateById(atUserGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atusergroup:delete")
    public R delete(@RequestBody Integer[] ids){
		atUserGroupService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
