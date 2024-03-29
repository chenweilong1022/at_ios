package io.renren.modules.ltt.controller;

import java.util.Arrays;

import io.renren.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AccountBalanceDTO;
import io.renren.modules.ltt.vo.AccountBalanceVO;
import io.renren.modules.ltt.service.AccountBalanceService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 账户余额表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-28 11:39:25
 */
@RestController
@RequestMapping("ltt/accountbalance")
public class AccountBalanceController extends AbstractController {
    @Autowired
    private AccountBalanceService accountBalanceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:accountbalance:list")
    public R list(AccountBalanceDTO accountBalance){
        PageUtils page = accountBalanceService.queryPage(accountBalance);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{accountId}")
    @RequiresPermissions("ltt:accountbalance:info")
    public R info(@PathVariable("accountId") Integer accountId){
		AccountBalanceVO accountBalance = accountBalanceService.getById(accountId);

        return R.ok().put("accountBalance", accountBalance);
    }



    /**
     * 信息
     */
    @RequestMapping("/getBySysUserId/{sysUserId}")
    @RequiresPermissions("ltt:accountbalance:info")
    public R getBySysUserId(@PathVariable("sysUserId") Long sysUserId){
		AccountBalanceVO accountBalance = accountBalanceService.getBySysUserId(sysUserId);

        return R.ok().put("balance", accountBalance);
    }

    /**
     * 保存账户资金变动
     */
    @RequestMapping("/changeAccount")
    @RequiresPermissions("ltt:accountbalance:save")
    public R changeAccount(@RequestBody AccountBalanceDTO accountBalance){
        accountBalance.setOperationUserId(getUserId());
		accountBalanceService.changeAccount(accountBalance);
        return R.ok();
    }

//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    @RequiresPermissions("ltt:accountbalance:update")
//    public R update(@RequestBody AccountBalanceDTO accountBalance){
//		accountBalanceService.updateById(accountBalance);
//
//        return R.ok();
//    }

//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    @RequiresPermissions("ltt:accountbalance:delete")
//    public R delete(@RequestBody Integer[] accountIds){
//		accountBalanceService.removeByIds(Arrays.asList(accountIds));
//
//        return R.ok();
//    }

}
