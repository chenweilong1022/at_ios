package io.renren.modules.ltt.controller;

import java.util.Arrays;

import io.renren.common.utils.Constant;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtUserPortDTO;
import io.renren.modules.ltt.vo.AtUserPortVO;
import io.renren.modules.ltt.service.AtUserPortService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * 账户端口管理
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-17 18:25:29
 */
@RestController
@RequestMapping("ltt/atuserport")
public class AtUserPortController extends AbstractController {
    @Autowired
    private AtUserPortService atUserPortService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atuserport:list")
    public R list(AtUserPortDTO atUserPort) {
        //如果不是超级管理员，则只查询自己所拥有的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
            atUserPort.setSysUserId(getUserId());
        }
        PageUtils page = atUserPortService.queryPage(atUserPort);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atuserport:info")
    public R info(@PathVariable("id") Integer id) {
        AtUserPortVO atUserPort = atUserPortService.getById(id);

        return R.ok().put("atUserPort", atUserPort);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atuserport:save")
    public R save(@RequestBody AtUserPortDTO atUserPort) {
        atUserPort.setSysUserId(getUserId());
        atUserPortService.save(atUserPort);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atuserport:update")
    public R update(@RequestBody AtUserPortDTO atUserPort){
		atUserPortService.updateById(atUserPort);

        return R.ok();
    }

    /**
     * 删除
     */
//    @RequestMapping("/delete")
//    @RequiresPermissions("ltt:atuserport:delete")
//    public R delete(@RequestBody Integer[] ids){
//		atUserPortService.removeByIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
