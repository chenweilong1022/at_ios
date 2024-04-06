package io.renren.modules.ltt.controller;

import java.util.Arrays;

import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtDataGroupDTO;
import io.renren.modules.ltt.vo.AtDataGroupVO;
import io.renren.modules.ltt.service.AtDataGroupService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 数据分组
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 00:27:43
 */
@RestController
@RequestMapping("ltt/atdatagroup")
public class AtDataGroupController extends AbstractController {
    @Autowired
    private AtDataGroupService atDataGroupService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atdatagroup:list")
    public R list(AtDataGroupDTO atDataGroup){
        atDataGroup.setSysUserId(getAuthUserId());
        PageUtils page = atDataGroupService.queryPage(atDataGroup);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atdatagroup:info")
    public R info(@PathVariable("id") Integer id){
		AtDataGroupVO atDataGroup = atDataGroupService.getById(id);

        return R.ok().put("atDataGroup", atDataGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atdatagroup:save")
    public R save(@RequestBody AtDataGroupDTO atDataGroup){
        atDataGroup.setSysUserId(getAuthUserId());
		atDataGroupService.save(atDataGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atdatagroup:update")
    public R update(@RequestBody AtDataGroupDTO atDataGroup){
		atDataGroupService.updateById(atDataGroup);

        return R.ok();
    }

    /**
     * 批量修改昵称
     */
    @RequestMapping("/updateBatchGroup")
    @RequiresPermissions("ltt:atdatagroup:update")
    public R updateBatchGroup(@RequestBody AtDataGroupDTO atDataGroup){
        atDataGroup.setSysUserId(getAuthUserId());
		atDataGroupService.updateBatchGroup(atDataGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atdatagroup:delete")
    public R delete(@RequestBody Integer[] ids){
		atDataGroupService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
