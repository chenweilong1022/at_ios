package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtDataTaskDTO;
import io.renren.modules.ltt.vo.AtDataTaskVO;
import io.renren.modules.ltt.service.AtDataTaskService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 加粉任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-17 18:52:43
 */
@RestController
@RequestMapping("ltt/atdatatask")
public class AtDataTaskController {
    @Autowired
    private AtDataTaskService atDataTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atdatatask:list")
    public R list(AtDataTaskDTO atDataTask){
        PageUtils page = atDataTaskService.queryPage(atDataTask);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atdatatask:info")
    public R info(@PathVariable("id") Integer id){
		AtDataTaskVO atDataTask = atDataTaskService.getById(id);

        return R.ok().put("atDataTask", atDataTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atdatatask:save")
    public R save(@RequestBody AtDataTaskDTO atDataTask){
		atDataTaskService.save(atDataTask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atdatatask:update")
    public R update(@RequestBody AtDataTaskDTO atDataTask){
		atDataTaskService.updateById(atDataTask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atdatatask:delete")
    public R delete(@RequestBody Integer[] ids){
		atDataTaskService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * startUp
     */
    @RequestMapping("/startUp")
    @RequiresPermissions("ltt:atdatatask:delete")
    public R startUp(@RequestBody Integer[] ids){
		atDataTaskService.startUp(Arrays.asList(ids));
        return R.ok();
    }

}
