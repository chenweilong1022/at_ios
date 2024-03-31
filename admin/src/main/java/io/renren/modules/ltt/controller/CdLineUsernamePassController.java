package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.CdLineUsernamePassDTO;
import io.renren.modules.ltt.vo.CdLineUsernamePassVO;
import io.renren.modules.ltt.service.CdLineUsernamePassService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-31 02:37:15
 */
@RestController
@RequestMapping("ltt/cdlineusernamepass")
public class CdLineUsernamePassController {
    @Autowired
    private CdLineUsernamePassService cdLineUsernamePassService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:cdlineusernamepass:list")
    public R list(CdLineUsernamePassDTO cdLineUsernamePass){
        PageUtils page = cdLineUsernamePassService.queryPage(cdLineUsernamePass);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:cdlineusernamepass:info")
    public R info(@PathVariable("id") Integer id){
		CdLineUsernamePassVO cdLineUsernamePass = cdLineUsernamePassService.getById(id);

        return R.ok().put("cdLineUsernamePass", cdLineUsernamePass);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CdLineUsernamePassDTO cdLineUsernamePass){
		cdLineUsernamePassService.save(cdLineUsernamePass);
        return R.data(cdLineUsernamePass);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CdLineUsernamePassDTO cdLineUsernamePass){
		cdLineUsernamePassService.updateById(cdLineUsernamePass);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:cdlineusernamepass:delete")
    public R delete(@RequestBody Integer[] ids){
		cdLineUsernamePassService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
