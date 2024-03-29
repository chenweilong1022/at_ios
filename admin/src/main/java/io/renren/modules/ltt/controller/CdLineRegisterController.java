package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @RequiresPermissions("ltt:cdlineregister:list")
    public R list(CdLineRegisterDTO cdLineRegister){
        PageUtils page = cdLineRegisterService.queryPage(cdLineRegister);

        return R.ok().put("page", page);
    }

    /**
     * 根据taskid查询
     */
    @RequestMapping("/listByTaskId")
    @RequiresPermissions("ltt:cdlineregister:list")
    public R listByTaskId(CdLineRegisterDTO cdLineRegister){
        PageUtils page = cdLineRegisterService.listByTaskId(cdLineRegister);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:cdlineregister:info")
    public R info(@PathVariable("id") Integer id){
		CdLineRegisterVO cdLineRegister = cdLineRegisterService.getById(id);

        return R.ok().put("cdLineRegister", cdLineRegister);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:cdlineregister:save")
    public R save(@RequestBody CdLineRegisterDTO cdLineRegister){
		cdLineRegisterService.save(cdLineRegister);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:cdlineregister:update")
    public R update(@RequestBody CdLineRegisterDTO cdLineRegister){
		cdLineRegisterService.updateById(cdLineRegister);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:cdlineregister:delete")
    public R delete(@RequestBody Integer[] ids){
		cdLineRegisterService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
