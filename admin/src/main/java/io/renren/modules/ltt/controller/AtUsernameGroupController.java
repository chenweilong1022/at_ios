package io.renren.modules.ltt.controller;

import java.util.Arrays;

import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtUsernameGroupDTO;
import io.renren.modules.ltt.vo.AtUsernameGroupVO;
import io.renren.modules.ltt.service.AtUsernameGroupService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 昵称分组
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-28 15:19:44
 */
@RestController
@RequestMapping("ltt/atusernamegroup")
public class AtUsernameGroupController extends AbstractController {
    @Autowired
    private AtUsernameGroupService atUsernameGroupService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atusernamegroup:list")
    public R list(AtUsernameGroupDTO atUsernameGroup){
        atUsernameGroup.setSysUserId(getAuthUserId());
        PageUtils page = atUsernameGroupService.queryPage(atUsernameGroup);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atusernamegroup:info")
    public R info(@PathVariable("id") Integer id){
		AtUsernameGroupVO atUsernameGroup = atUsernameGroupService.getById(id);

        return R.ok().put("atUsernameGroup", atUsernameGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atusernamegroup:save")
    public R save(@RequestBody AtUsernameGroupDTO atUsernameGroup){
        atUsernameGroup.setSysUserId(getUserId());
		atUsernameGroupService.save(atUsernameGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atusernamegroup:update")
    public R update(@RequestBody AtUsernameGroupDTO atUsernameGroup){
		atUsernameGroupService.updateById(atUsernameGroup);

        return R.ok();
    }

    /**
     * 批量更改昵称
     */
    @RequestMapping("/updateBatchAtUsername")
    @RequiresPermissions("ltt:atusernamegroup:update")
    public R updateBatchAtUsername(@RequestBody AtUsernameGroupDTO atUsernameGroup){
        atUsernameGroup.setSysUserId(getUserId());
		atUsernameGroupService.updateBatchAtUsername(atUsernameGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atusernamegroup:delete")
    public R delete(@RequestBody Integer[] ids){
		atUsernameGroupService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
