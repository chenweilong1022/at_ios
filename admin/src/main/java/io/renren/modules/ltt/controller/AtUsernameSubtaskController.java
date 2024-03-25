package io.renren.modules.ltt.controller;

import java.util.Arrays;

import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtUsernameSubtaskDTO;
import io.renren.modules.ltt.vo.AtUsernameSubtaskVO;
import io.renren.modules.ltt.service.AtUsernameSubtaskService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 昵称任务子任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-20 21:46:41
 */
@RestController
@RequestMapping("ltt/atusernamesubtask")
public class AtUsernameSubtaskController extends AbstractController {
    @Autowired
    private AtUsernameSubtaskService atUsernameSubtaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atusernamesubtask:list")
    public R list(AtUsernameSubtaskDTO atUsernameSubtask){
        atUsernameSubtask.setSysUserId(getAuthUserId());
        PageUtils page = atUsernameSubtaskService.queryPage(atUsernameSubtask);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atusernamesubtask:info")
    public R info(@PathVariable("id") Integer id){
		AtUsernameSubtaskVO atUsernameSubtask = atUsernameSubtaskService.getById(id);

        return R.ok().put("atUsernameSubtask", atUsernameSubtask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atusernamesubtask:save")
    public R save(@RequestBody AtUsernameSubtaskDTO atUsernameSubtask){
        atUsernameSubtask.setSysUserId(getUserId());
		atUsernameSubtaskService.save(atUsernameSubtask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atusernamesubtask:update")
    public R update(@RequestBody AtUsernameSubtaskDTO atUsernameSubtask){
		atUsernameSubtaskService.updateById(atUsernameSubtask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atusernamesubtask:delete")
    public R delete(@RequestBody Integer[] ids){
		atUsernameSubtaskService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
