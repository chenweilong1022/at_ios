package io.renren.modules.ltt.controller;

import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtMessageRecordDTO;
import io.renren.modules.ltt.vo.AtMessageRecordVO;
import io.renren.modules.ltt.service.AtMessageRecordService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 消息记录
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-21 14:41:06
 */
@RestController
@RequestMapping("ltt/atmessagerecord")
public class AtMessageRecordController {
    @Autowired
    private AtMessageRecordService atMessageRecordService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atmessagerecord:list")
    public R list(AtMessageRecordDTO atMessageRecord){
        PageUtils page = atMessageRecordService.queryPage(atMessageRecord);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atmessagerecord:info")
    public R info(@PathVariable("id") Integer id){
		AtMessageRecordVO atMessageRecord = atMessageRecordService.getById(id);

        return R.ok().put("atMessageRecord", atMessageRecord);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atmessagerecord:save")
    public R save(@RequestBody AtMessageRecordDTO atMessageRecord){
		atMessageRecordService.save(atMessageRecord);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atmessagerecord:update")
    public R update(@RequestBody AtMessageRecordDTO atMessageRecord){
		atMessageRecordService.updateById(atMessageRecord);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atmessagerecord:delete")
    public R delete(@RequestBody Integer[] ids){
		atMessageRecordService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
