package io.renren.modules.ltt.controller;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import io.renren.modules.ltt.dao.UpdateAtUserCustomerParamDto;
import io.renren.modules.ltt.dao.UpdateUserGroupParamDto;
import io.renren.modules.ltt.dao.ValidateAtUserStatusParamDto;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.ltt.dto.AtUserDTO;
import io.renren.modules.ltt.vo.AtUserVO;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 账号数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 01:55:49
 */
@RestController
@RequestMapping("ltt/atuser")
public class AtUserController extends AbstractController {
    @Autowired
    private AtUserService atUserService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atuser:list")
    public R list(AtUserDTO atUser){
        atUser.setSysUserId(getAuthUserId());
        PageUtils page = atUserService.queryPage(atUser);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atuser:info")
    public R info(@PathVariable("id") Integer id){
		AtUserVO atUser = atUserService.getById(id);

        return R.ok().put("atUser", atUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atuser:save")
    public R save(@RequestBody AtUserDTO atUser){
        atUser.setSysUserId(getUserId());
		atUserService.save(atUser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atuser:update")
    public R update(@RequestBody AtUserDTO atUser){
		atUserService.updateById(atUser);

        return R.ok();
    }

    /**
     * 更改用户分组
     */
    @RequestMapping("/updateUserGroup")
    @RequiresPermissions("ltt:atuser:update")
    public R updateUserGroup(@RequestBody UpdateUserGroupParamDto paramDto){
		atUserService.updateUserGroup(paramDto);

        return R.ok();
    }

    /**
     * 分配客服
     */
    @RequestMapping("/updateUserCustomer")
    @RequiresPermissions("ltt:atuser:update")
    public R updateUserCustomer(@RequestBody UpdateAtUserCustomerParamDto paramDto){
		atUserService.updateUserCustomer(paramDto);

        return R.ok();
    }

    /**
     * 验活账号
     */
    @RequestMapping("/validateUserStatus")
    @RequiresPermissions("ltt:atuser:update")
    public R validateUserStatus(@RequestBody ValidateAtUserStatusParamDto paramDto){
		atUserService.validateUserStatus(paramDto);

        return R.ok();
    }

    @RequestMapping("/downloadUserTokenTxt")
    @RequiresPermissions("ltt:atuser:save")
    public R downloadUserTokenTxt(@RequestBody List<Integer> ids) {
        String fileUrl = atUserService.downloadUserTokenTxt(ids);
        return R.ok().put("fileUrl", fileUrl);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atuser:delete")
    public R delete(@RequestBody Integer[] ids){
		atUserService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
