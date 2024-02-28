package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtUsernameDTO;
import io.renren.modules.ltt.vo.AtUsernameVO;
import io.renren.modules.ltt.service.AtUsernameService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 昵称
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-28 22:56:04
 */
@RestController
@RequestMapping("ltt/atusername")
public class AtUsernameController {
    @Autowired
    private AtUsernameService atUsernameService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atusername:list")
    public R list(AtUsernameDTO atUsername){
        PageUtils page = atUsernameService.queryPage(atUsername);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atusername:info")
    public R info(@PathVariable("id") Integer id){
		AtUsernameVO atUsername = atUsernameService.getById(id);

        return R.ok().put("atUsername", atUsername);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atusername:save")
    public R save(@RequestBody AtUsernameDTO atUsername){
		atUsernameService.save(atUsername);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atusername:update")
    public R update(@RequestBody AtUsernameDTO atUsername){
		atUsernameService.updateById(atUsername);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atusername:delete")
    public R delete(@RequestBody Integer[] ids){
		atUsernameService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
