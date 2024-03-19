package io.renren.modules.ltt.controller;

import java.util.Arrays;
import java.util.List;

import io.renren.modules.ltt.entity.AtAvatarGroupEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.ltt.dto.AtAvatarGroupDTO;
import io.renren.modules.ltt.vo.AtAvatarGroupVO;
import io.renren.modules.ltt.service.AtAvatarGroupService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 头像分组
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-28 22:45:51
 */
@RestController
@RequestMapping("ltt/atavatargroup")
public class AtAvatarGroupController {
    @Autowired
    private AtAvatarGroupService atAvatarGroupService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atavatargroup:list")
    public R list(AtAvatarGroupDTO atAvatarGroup){
        PageUtils page = atAvatarGroupService.queryPage(atAvatarGroup);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atavatargroup:info")
    public R info(@PathVariable("id") Integer id){
		AtAvatarGroupVO atAvatarGroup = atAvatarGroupService.getById(id);

        return R.ok().put("atAvatarGroup", atAvatarGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atavatargroup:save")
    public R save(@RequestBody AtAvatarGroupDTO atAvatarGroup){
		atAvatarGroupService.save(atAvatarGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atavatargroup:update")
    public R update(@RequestBody AtAvatarGroupDTO atAvatarGroup){
		atAvatarGroupService.updateById(atAvatarGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atavatargroup:delete")
    public R delete(@RequestBody Integer[] ids){
		atAvatarGroupService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 模糊检索根据分组名称
     */
    @RequestMapping("/queryByFuzzyName")
    @RequiresPermissions("ltt:atavatargroup:list")
    public R queryByFuzzyName(@RequestParam String searchWord){
        List<AtAvatarGroupEntity> groupList = atAvatarGroupService.queryByFuzzyName(searchWord);
        return R.ok().put("groupList", groupList);
    }

}
