package io.renren.modules.ltt.controller;

import java.util.Arrays;

import cn.hutool.core.util.ObjectUtil;
import io.renren.common.utils.Constant;
import io.renren.common.validator.Assert;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AccountDetailsDTO;
import io.renren.modules.ltt.vo.AccountDetailsVO;
import io.renren.modules.ltt.service.AccountDetailsService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * 流水明细表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-28 11:39:25
 */
@RestController
@RequestMapping("ltt/accountdetails")
public class AccountDetailsController extends AbstractController {
    @Autowired
    private AccountDetailsService accountDetailsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:accountdetails:list")
    public R list(AccountDetailsDTO accountDetails) {
        Long userId = getUserId();

        //不是管理员，不可以查询其他用户数据
        if (userId != Constant.SUPER_ADMIN) {
            if (ObjectUtil.isNull(accountDetails.getSysUserId())) {
                return R.error("无权限查询");
            }
            if (!userId.equals(accountDetails.getSysUserId())) {
                return R.error("无权限查询");
            }
        }

        PageUtils page = accountDetailsService.queryPage(accountDetails);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{transactionId}")
    @RequiresPermissions("ltt:accountdetails:info")
    public R info(@PathVariable("transactionId") Integer transactionId) {
        AccountDetailsVO accountDetails = accountDetailsService.getById(transactionId);

        return R.ok().put("accountDetails", accountDetails);
    }

//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    @RequiresPermissions("ltt:accountdetails:save")
//    public R save(@RequestBody AccountDetailsDTO accountDetails){
//		accountDetailsService.save(accountDetails);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    @RequiresPermissions("ltt:accountdetails:update")
//    public R update(@RequestBody AccountDetailsDTO accountDetails){
//		accountDetailsService.updateById(accountDetails);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    @RequiresPermissions("ltt:accountdetails:delete")
//    public R delete(@RequestBody Integer[] transactionIds){
//		accountDetailsService.removeByIds(Arrays.asList(transactionIds));
//
//        return R.ok();
//    }

}
