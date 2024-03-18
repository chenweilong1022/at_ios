package io.renren.modules.ltt.controller;

import io.renren.common.constant.SystemConstant;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.ltt.dao.CustomerUserDao;
import io.renren.modules.ltt.dto.CustomerUserOperationParamDto;
import io.renren.modules.ltt.dto.CustomerUserParamDto;
import io.renren.modules.ltt.dto.CustomerUserResultDto;
import io.renren.modules.ltt.service.CustomerUserService;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;


/**
 * 系统用户
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-18 12:08:34
 */
@RestController
@RequestMapping("ltt/customeruser")
public class CustomerUserController extends AbstractController {
    @Autowired
    private CustomerUserService sysUserService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:customeruser:list")
    public R list(CustomerUserParamDto customeruser) {
        customeruser.setCreateUserId(getUserId());
        PageUtils page = sysUserService.queryPage(customeruser);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("ltt:customeruser:info")
    public R info(@PathVariable("userId") Long userId) {
        CustomerUserResultDto customeruser = sysUserService.getById(userId);

        return R.ok().put("customeruser", customeruser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:customeruser:save")
    public R save(@RequestBody CustomerUserOperationParamDto customeruser) {
        customeruser.setCreateUserId(getUserId());
        sysUserService.save(customeruser);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:customeruser:update")
    public R update(@RequestBody CustomerUserOperationParamDto customeruser) {
        sysUserService.updateById(customeruser);

        return R.ok();
    }

    /**
     * 修改密码
     */
    @RequestMapping("/updatePassword")
    @RequiresPermissions("ltt:customeruser:update")
    public R updatePassword(@RequestBody CustomerUserOperationParamDto customeruser) {
        sysUserService.updatePassword(customeruser);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:customeruser:delete")
    public R delete(@RequestBody Long[] userIds) {
        sysUserService.removeByIds(Arrays.asList(userIds));

        return R.ok();
    }

}
