package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.CdLineIpDTO;
import io.renren.modules.ltt.vo.CdLineIpVO;
import io.renren.modules.ltt.service.CdLineIpService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-30 21:39:27
 */
@RestController
@RequestMapping("ltt/cdlineip")
public class CdLineIpController {
    @Autowired
    private CdLineIpService cdLineIpService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(CdLineIpDTO cdLineIp){
        PageUtils page = cdLineIpService.queryPage(cdLineIp);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		CdLineIpVO cdLineIp = cdLineIpService.getById(id);

        return R.ok().put("cdLineIp", cdLineIp);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CdLineIpDTO cdLineIp){
        return R.data(cdLineIpService.save(cdLineIp));
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:cdlineip:update")
    public R update(@RequestBody CdLineIpDTO cdLineIp){
		cdLineIpService.updateById(cdLineIp);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:cdlineip:delete")
    public R delete(@RequestBody Integer[] ids){
		cdLineIpService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
