package io.renren.modules.ltt.controller;

import java.util.Arrays;
import java.util.List;

import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class AtUserGroupController extends AbstractController {
    @Autowired
    private AtUserGroupService atUserGroupService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atusergroup:list")
    public R list(AtUserGroupDTO atUserGroup){
        atUserGroup.setSysUserId(getAuthUserId());
        PageUtils page = atUserGroupService.queryPage(atUserGroup);

        return R.ok().put("page", page);
    }

    /**
     * 模糊检索根据分组名称
     */
    @GetMapping("/queryByFuzzyName")
    @RequiresPermissions("ltt:atusergroup:list")
    public R queryByFuzzyName(@RequestParam(required = false) String searchWord){
        List<AtUserGroupVO> groupList = atUserGroupService.queryByFuzzyName(searchWord, getAuthUserId());
        return R.ok().put("groupList", groupList);
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
        atUserGroup.setSysUserId(getUserId());
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
