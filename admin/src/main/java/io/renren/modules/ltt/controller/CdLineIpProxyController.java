package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.vo.CdLineIpProxyVO;
import io.renren.modules.ltt.service.CdLineIpProxyService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 17:49:13
 */
@RestController
@RequestMapping("ltt/cdlineipproxy")
public class CdLineIpProxyController {
    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:cdlineipproxy:list")
    public R list(CdLineIpProxyDTO cdLineIpProxy){
        PageUtils page = cdLineIpProxyService.queryPage(cdLineIpProxy);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:cdlineipproxy:info")
    public R info(@PathVariable("id") Integer id){
		CdLineIpProxyVO cdLineIpProxy = cdLineIpProxyService.getById(id);

        return R.ok().put("cdLineIpProxy", cdLineIpProxy);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:cdlineipproxy:save")
    public R save(@RequestBody CdLineIpProxyDTO cdLineIpProxy){
		cdLineIpProxyService.save(cdLineIpProxy);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:cdlineipproxy:update")
    public R update(@RequestBody CdLineIpProxyDTO cdLineIpProxy){
		cdLineIpProxyService.updateById(cdLineIpProxy);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:cdlineipproxy:delete")
    public R delete(@RequestBody Integer[] ids){
		cdLineIpProxyService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
