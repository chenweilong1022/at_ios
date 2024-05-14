package io.renren.modules.ltt.controller;

import java.util.Arrays;

import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.LttUserDTO;
import io.renren.modules.ltt.vo.LttUserVO;
import io.renren.modules.ltt.service.LttUserService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 用户表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2022-08-02 14:05:38
 */
@RestController
@RequestMapping("ltt/lttuser")
public class LttUserController extends AbstractController {
    @Autowired
    private LttUserService lttUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:lttuser:list")
    public R list(LttUserDTO lttUser){
        PageUtils page = lttUserService.queryPage(lttUser);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:lttuser:info")
    public R info(@PathVariable("id") Long id){
		LttUserVO lttUser = lttUserService.getById(id);

        return R.ok().put("lttUser", lttUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:lttuser:save")
    public R save(@RequestBody LttUserDTO lttUser){
		lttUserService.save(lttUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:lttuser:update")
    public R update(@RequestBody LttUserDTO lttUser){
		lttUserService.updateById(lttUser);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:lttuser:delete")
    public R delete(@RequestBody Long[] ids){
		lttUserService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
