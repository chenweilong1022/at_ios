package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.CdIpConfigDTO;
import io.renren.modules.ltt.vo.CdIpConfigVO;
import io.renren.modules.ltt.service.CdIpConfigService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-05-12 20:47:51
 */
@RestController
@RequestMapping("ltt/cdipconfig")
public class CdIpConfigController {
    @Autowired
    private CdIpConfigService cdIpConfigService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:cdipconfig:list")
    public R list(CdIpConfigDTO cdIpConfig){
        PageUtils page = cdIpConfigService.queryPage(cdIpConfig);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:cdipconfig:info")
    public R info(@PathVariable("id") Integer id){
		CdIpConfigVO cdIpConfig = cdIpConfigService.getById(id);

        return R.ok().put("cdIpConfig", cdIpConfig);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:cdipconfig:save")
    public R save(@RequestBody CdIpConfigDTO cdIpConfig){
		cdIpConfigService.save(cdIpConfig);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:cdipconfig:update")
    public R update(@RequestBody CdIpConfigDTO cdIpConfig){
		cdIpConfigService.updateById(cdIpConfig);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:cdipconfig:delete")
    public R delete(@RequestBody CdIpConfigDTO cdIpConfig){
        return R.data(cdIpConfigService.clear(cdIpConfig));
    }

}
