package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtAvatarSubtaskDTO;
import io.renren.modules.ltt.vo.AtAvatarSubtaskVO;
import io.renren.modules.ltt.service.AtAvatarSubtaskService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 头像任务子任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-11 14:32:25
 */
@RestController
@RequestMapping("ltt/atavatarsubtask")
public class AtAvatarSubtaskController {
    @Autowired
    private AtAvatarSubtaskService atAvatarSubtaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atavatarsubtask:list")
    public R list(AtAvatarSubtaskDTO atAvatarSubtask){
        PageUtils page = atAvatarSubtaskService.queryPage(atAvatarSubtask);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atavatarsubtask:info")
    public R info(@PathVariable("id") Integer id){
		AtAvatarSubtaskVO atAvatarSubtask = atAvatarSubtaskService.getById(id);

        return R.ok().put("atAvatarSubtask", atAvatarSubtask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atavatarsubtask:save")
    public R save(@RequestBody AtAvatarSubtaskDTO atAvatarSubtask){
		atAvatarSubtaskService.save(atAvatarSubtask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atavatarsubtask:update")
    public R update(@RequestBody AtAvatarSubtaskDTO atAvatarSubtask){
		atAvatarSubtaskService.updateById(atAvatarSubtask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atavatarsubtask:delete")
    public R delete(@RequestBody Integer[] ids){
		atAvatarSubtaskService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
