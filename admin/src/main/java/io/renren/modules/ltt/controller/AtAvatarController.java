package io.renren.modules.ltt.controller;

import java.util.Arrays;

import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtAvatarDTO;
import io.renren.modules.ltt.vo.AtAvatarVO;
import io.renren.modules.ltt.service.AtAvatarService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 头像
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-29 17:21:50
 */
@RestController
@RequestMapping("ltt/atavatar")
public class AtAvatarController extends AbstractController {
    @Autowired
    private AtAvatarService atAvatarService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atavatar:list")
    public R list(AtAvatarDTO atAvatar){
        atAvatar.setSysUserId(getAuthUserId());
        PageUtils page = atAvatarService.queryPage(atAvatar);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atavatar:info")
    public R info(@PathVariable("id") Integer id){
		AtAvatarVO atAvatar = atAvatarService.getById(id);

        return R.ok().put("atAvatar", atAvatar);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atavatar:save")
    public R save(@RequestBody AtAvatarDTO atAvatar){
        atAvatar.setSysUserId(getUserId());
		atAvatarService.save(atAvatar);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atavatar:update")
    public R update(@RequestBody AtAvatarDTO atAvatar){
		atAvatarService.updateById(atAvatar);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atavatar:delete")
    public R delete(@RequestBody Integer[] ids){
		atAvatarService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
