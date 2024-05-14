package io.renren.modules.ltt.controller;

import java.util.Arrays;
import java.util.Queue;

import com.github.benmanes.caffeine.cache.Cache;
import io.renren.modules.ltt.dto.AtUserTokenIosDeviceParamDTO;
import io.renren.modules.ltt.vo.IOSTaskVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtUserTokenIosDTO;
import io.renren.modules.ltt.vo.AtUserTokenIosVO;
import io.renren.modules.ltt.service.AtUserTokenIosService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.annotation.Resource;


/**
 * 用户ios token数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-31 00:16:44
 */
@RestController
@RequestMapping("ltt/atusertokenios")
public class AtUserTokenIosController {
    @Autowired
    private AtUserTokenIosService atUserTokenIosService;

    /**
     * 设备分页查询
     */
    @RequestMapping("/queryDevicePage")
//    @RequiresPermissions("ltt:atusertokenios:list")
    public R queryDevicePage(AtUserTokenIosDeviceParamDTO paramDTO){
        PageUtils page = atUserTokenIosService.queryDevicePage(paramDTO);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("ltt:atusertokenios:list")
    public R list(AtUserTokenIosDTO atUserTokenIos){
        PageUtils page = atUserTokenIosService.queryPage(atUserTokenIos);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
//    @RequiresPermissions("ltt:atusertokenios:info")
    public R info(@PathVariable("id") Integer id){
		AtUserTokenIosVO atUserTokenIos = atUserTokenIosService.getById(id);

        return R.ok().put("atUserTokenIos", atUserTokenIos);
    }




    /**
     * 去找手机
     */
    @RequestMapping("/taskIosFind")
//    @RequiresPermissions("ltt:atusertokenios:save")
    public R taskIosFind(@RequestBody Integer[] ids){
        atUserTokenIosService.taskIosFind(ids);
        return R.ok();
    }

    /**
     * 备份
     */
    @RequestMapping("/backUp")
//    @RequiresPermissions("ltt:atusertokenios:save")
    public R backUp(@RequestBody AtUserTokenIosDTO atUserTokenIos){
        atUserTokenIosService.backUp(atUserTokenIos);
        return R.ok();
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("ltt:atusertokenios:save")
    public R save(@RequestBody AtUserTokenIosDTO atUserTokenIos){
		atUserTokenIosService.save(atUserTokenIos);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("ltt:atusertokenios:update")
    public R update(@RequestBody AtUserTokenIosDTO atUserTokenIos){
		atUserTokenIosService.updateById(atUserTokenIos);

        return R.ok();
    }

    /**
     * 更改设备名称
     */
    @RequestMapping("/updateDeviceName")
//    @RequiresPermissions("ltt:atusertokenios:update")
    public R updateDeviceName(@RequestBody AtUserTokenIosDeviceParamDTO atUserTokenIos){
		atUserTokenIosService.updateDeviceName(atUserTokenIos);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("ltt:atusertokenios:delete")
    public R delete(@RequestBody Integer[] ids){
		atUserTokenIosService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
