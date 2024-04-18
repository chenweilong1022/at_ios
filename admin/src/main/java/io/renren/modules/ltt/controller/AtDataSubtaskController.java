package io.renren.modules.ltt.controller;

import java.util.Arrays;

import io.renren.modules.ltt.dto.AtDataSubtaskParamPageDTO;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtDataSubtaskDTO;
import io.renren.modules.ltt.vo.AtDataSubtaskVO;
import io.renren.modules.ltt.service.AtDataSubtaskService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 加粉任务子任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-18 10:53:33
 */
@RestController
@RequestMapping("ltt/atdatasubtask")
public class AtDataSubtaskController extends AbstractController {
    @Autowired
    private AtDataSubtaskService atDataSubtaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atdatasubtask:list")
    public R list(@RequestBody AtDataSubtaskParamPageDTO atDataSubtask){
        atDataSubtask.setSysUserId(getAuthUserId());
        PageUtils page = atDataSubtaskService.queryPage(atDataSubtask);

        return R.ok().put("page", page);
    }

    /**
     * 获取朋友列表
     */
    @RequestMapping("/listFriend")
    @RequiresPermissions("ltt:atdatasubtask:list")
    public R listFriend(AtDataSubtaskParamPageDTO atDataSubtask){
        atDataSubtask.setSysUserId(getAuthUserId());
        PageUtils page = atDataSubtaskService.listFriend(atDataSubtask);
        return R.ok().put("page", page);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atdatasubtask:info")
    public R info(@PathVariable("id") Integer id){
		AtDataSubtaskVO atDataSubtask = atDataSubtaskService.getById(id);

        return R.ok().put("atDataSubtask", atDataSubtask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atdatasubtask:save")
    public R save(@RequestBody AtDataSubtaskDTO atDataSubtask){
        atDataSubtask.setSysUserId(getAuthUserId());
		atDataSubtaskService.save(atDataSubtask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atdatasubtask:update")
    public R update(@RequestBody AtDataSubtaskDTO atDataSubtask){
		atDataSubtaskService.updateById(atDataSubtask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atdatasubtask:delete")
    public R delete(@RequestBody Integer[] ids){
		atDataSubtaskService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
