package io.renren.modules.ltt.controller;

import java.util.Arrays;

import io.renren.modules.ltt.dto.LineRegisterSummaryResultDto;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.ltt.dto.CdLineRegisterDTO;
import io.renren.modules.ltt.vo.CdLineRegisterVO;
import io.renren.modules.ltt.service.CdLineRegisterService;
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
@RequestMapping("ltt/cdlineregister")
public class CdLineRegisterController {
    @Autowired
    private CdLineRegisterService cdLineRegisterService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:cdregistertask:list")
    public R list(CdLineRegisterDTO cdLineRegister){
        PageUtils page = cdLineRegisterService.queryPage(cdLineRegister);

        return R.ok().put("page", page);
    }

    /**
     * 根据taskid查询
     */
    @RequestMapping("/listByTaskId")
    @RequiresPermissions("ltt:cdregistertask:list")
    public R listByTaskId(CdLineRegisterDTO cdLineRegister){
        PageUtils page = cdLineRegisterService.listByTaskId(cdLineRegister);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:cdregistertask:info")
    public R info(@PathVariable("id") Integer id){
		CdLineRegisterVO cdLineRegister = cdLineRegisterService.getById(id);

        return R.ok().put("cdLineRegister", cdLineRegister);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:cdregistertask:save")
    public R save(@RequestBody CdLineRegisterDTO cdLineRegister){
		cdLineRegisterService.save(cdLineRegister);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:cdregistertask:update")
    public R update(@RequestBody CdLineRegisterDTO cdLineRegister){
		cdLineRegisterService.updateById(cdLineRegister);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:cdregistertask:delete")
    public R delete(@RequestBody Integer[] ids){
		cdLineRegisterService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 注册重试
     */
    @RequestMapping("/registerRetry/{id}")
    @RequiresPermissions("ltt:cdregistertask:info")
    public R registerRetry(@PathVariable("id") Integer id){
        boolean b = cdLineRegisterService.registerRetry(id);

        return R.ok();
    }

    /**
     * 查询line注册数量
     * @param countryCode
     * @return
     */
    @RequestMapping("/queryLineRegisterCount")
    @RequiresPermissions("ltt:cdregistertask:info")
    public R queryLineRegisterCount(@RequestParam("countryCode") String countryCode){
        Integer count = cdLineRegisterService.queryLineRegisterCount(countryCode);

        return R.ok().put("lineRegisterCount", count);
    }

    /**
     * 注册数据汇总
     *
     */
    @RequestMapping("/queryLineRegisterSummary")
    @RequiresPermissions("ltt:cdregistertask:info")
    public R queryLineRegisterSummary() {
        LineRegisterSummaryResultDto resultDto = cdLineRegisterService.queryLineRegisterSummary();

        return R.ok().put("lineRegisterSummary", resultDto);
    }
}
