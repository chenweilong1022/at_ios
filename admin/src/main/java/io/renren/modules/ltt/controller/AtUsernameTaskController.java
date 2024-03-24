package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtUsernameTaskDTO;
import io.renren.modules.ltt.vo.AtUsernameTaskVO;
import io.renren.modules.ltt.service.AtUsernameTaskService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 昵称任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-20 21:46:41
 */
@RestController
@RequestMapping("ltt/atusernametask")
public class AtUsernameTaskController {
    @Autowired
    private AtUsernameTaskService atUsernameTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atusernametask:list")
    public R list(AtUsernameTaskDTO atUsernameTask){
        PageUtils page = atUsernameTaskService.queryPage(atUsernameTask);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atusernametask:info")
    public R info(@PathVariable("id") Integer id){
		AtUsernameTaskVO atUsernameTask = atUsernameTaskService.getById(id);

        return R.ok().put("atUsernameTask", atUsernameTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atusernametask:save")
    public R save(@RequestBody AtUsernameTaskDTO atUsernameTask){
		atUsernameTaskService.save(atUsernameTask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atusernametask:update")
    public R update(@RequestBody AtUsernameTaskDTO atUsernameTask){
		atUsernameTaskService.updateById(atUsernameTask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atusernametask:delete")
    public R delete(@RequestBody Integer[] ids){
		atUsernameTaskService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 错误重试
     * @param ids
     * @return
     */
    @RequestMapping("/errRetry")
    @RequiresPermissions("ltt:atusernametask:delete")
    public R errRetry(@RequestBody Integer[] ids){
        atUsernameTaskService.errRetry(Arrays.asList(ids));

        return R.ok();
    }
}
