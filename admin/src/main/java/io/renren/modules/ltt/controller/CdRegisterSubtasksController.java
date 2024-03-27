package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.CdRegisterSubtasksDTO;
import io.renren.modules.ltt.vo.CdRegisterSubtasksVO;
import io.renren.modules.ltt.service.CdRegisterSubtasksService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:13
 */
@RestController
@RequestMapping("ltt/cdregistersubtasks")
public class CdRegisterSubtasksController {
    @Autowired
    private CdRegisterSubtasksService cdRegisterSubtasksService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:cdregistersubtasks:list")
    public R list(CdRegisterSubtasksDTO cdRegisterSubtasks){
        PageUtils page = cdRegisterSubtasksService.queryPage(cdRegisterSubtasks);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:cdregistersubtasks:info")
    public R info(@PathVariable("id") Integer id){
		CdRegisterSubtasksVO cdRegisterSubtasks = cdRegisterSubtasksService.getById(id);

        return R.ok().put("cdRegisterSubtasks", cdRegisterSubtasks);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:cdregistersubtasks:save")
    public R save(@RequestBody CdRegisterSubtasksDTO cdRegisterSubtasks){
		cdRegisterSubtasksService.save(cdRegisterSubtasks);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:cdregistersubtasks:update")
    public R update(@RequestBody CdRegisterSubtasksDTO cdRegisterSubtasks){
		cdRegisterSubtasksService.updateById(cdRegisterSubtasks);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:cdregistersubtasks:delete")
    public R delete(@RequestBody Integer[] ids){
		cdRegisterSubtasksService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
