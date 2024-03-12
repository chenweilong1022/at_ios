package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtAvatarTaskDTO;
import io.renren.modules.ltt.vo.AtAvatarTaskVO;
import io.renren.modules.ltt.service.AtAvatarTaskService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 头像任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-11 14:32:25
 */
@RestController
@RequestMapping("ltt/atavatartask")
public class AtAvatarTaskController {
    @Autowired
    private AtAvatarTaskService atAvatarTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atavatartask:list")
    public R list(AtAvatarTaskDTO atAvatarTask){
        PageUtils page = atAvatarTaskService.queryPage(atAvatarTask);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atavatartask:info")
    public R info(@PathVariable("id") Integer id){
		AtAvatarTaskVO atAvatarTask = atAvatarTaskService.getById(id);

        return R.ok().put("atAvatarTask", atAvatarTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atavatartask:save")
    public R save(@RequestBody AtAvatarTaskDTO atAvatarTask){
		atAvatarTaskService.save(atAvatarTask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atavatartask:update")
    public R update(@RequestBody AtAvatarTaskDTO atAvatarTask){
		atAvatarTaskService.updateById(atAvatarTask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/errRetry")
    @RequiresPermissions("ltt:atavatartask:delete")
    public R errRetry(@RequestBody Integer[] ids){
        atAvatarTaskService.errRetry(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atavatartask:delete")
    public R delete(@RequestBody Integer[] ids){
		atAvatarTaskService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
