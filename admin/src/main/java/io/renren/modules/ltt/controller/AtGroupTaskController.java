package io.renren.modules.ltt.controller;

import io.renren.modules.ltt.vo.OnGroupPreVO;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtGroupTaskDTO;
import io.renren.modules.ltt.vo.AtGroupTaskVO;
import io.renren.modules.ltt.service.AtGroupTaskService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import java.util.Arrays;
import java.util.List;



/**
 * 拉群任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-29 17:07:23
 */
@RestController
@RequestMapping("ltt/atgrouptask")
public class AtGroupTaskController extends AbstractController {
    @Autowired
    private AtGroupTaskService atGroupTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atgrouptask:list")
    public R list(AtGroupTaskDTO atGroupTask){
        PageUtils page = atGroupTaskService.queryPage(atGroupTask);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/getGroupNameList")
    @RequiresPermissions("ltt:atgrouptask:list")
    public R getGroupNameList(AtGroupTaskDTO atGroupTask){
        List<String> stringList = atGroupTaskService.getGroupNameList(atGroupTask);
        return R.data(stringList);
    }

    /**
     * 列表
     */
    @RequestMapping("/onGroupPre")
    @RequiresPermissions("ltt:atgrouptask:list")
    public R onGroupPre(@RequestBody AtGroupTaskDTO atGroupTask){
        List<OnGroupPreVO> onGroupPreVOS = atGroupTaskService.onGroupPre(atGroupTask);

        return R.ok().put("onGroupPreVOS",onGroupPreVOS).put("remaining",atGroupTask.getRemaining());
    }

    /**
     * 拉群开始
     */
    @RequestMapping("/onGroupStart")
    @RequiresPermissions("ltt:atgrouptask:list")
    public R onGroupStart(@RequestBody AtGroupTaskDTO atGroupTask){
        atGroupTask.setSysUserId(getUserId());
        atGroupTaskService.onGroupStart(atGroupTask);
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atgrouptask:info")
    public R info(@PathVariable("id") Integer id){
		AtGroupTaskVO atGroupTask = atGroupTaskService.getById(id);

        return R.ok().put("atGroupTask", atGroupTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atgrouptask:save")
    public R save(@RequestBody AtGroupTaskDTO atGroupTask){
		atGroupTaskService.save(atGroupTask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atgrouptask:update")
    public R update(@RequestBody AtGroupTaskDTO atGroupTask){
		atGroupTaskService.updateById(atGroupTask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atgrouptask:delete")
    public R delete(@RequestBody Integer[] ids){
		atGroupTaskService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
