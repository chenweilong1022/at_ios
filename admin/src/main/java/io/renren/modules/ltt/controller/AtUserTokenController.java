package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtUserTokenDTO;
import io.renren.modules.ltt.vo.AtUserTokenVO;
import io.renren.modules.ltt.service.AtUserTokenService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 用户token数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 01:54:42
 */
@RestController
@RequestMapping("ltt/atusertoken")
public class AtUserTokenController {
    @Autowired
    private AtUserTokenService atUserTokenService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atusertoken:list")
    public R list(AtUserTokenDTO atUserToken){
        PageUtils page = atUserTokenService.queryPage(atUserToken);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atusertoken:info")
    public R info(@PathVariable("id") Integer id){
		AtUserTokenVO atUserToken = atUserTokenService.getById(id);

        return R.ok().put("atUserToken", atUserToken);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atusertoken:save")
    public R save(@RequestBody AtUserTokenDTO atUserToken){
		atUserTokenService.save(atUserToken);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atusertoken:update")
    public R update(@RequestBody AtUserTokenDTO atUserToken){
		atUserTokenService.updateById(atUserToken);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atusertoken:delete")
    public R delete(@RequestBody Integer[] ids){
		atUserTokenService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
