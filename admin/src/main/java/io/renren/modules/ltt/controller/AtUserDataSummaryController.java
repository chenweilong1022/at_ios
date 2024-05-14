package io.renren.modules.ltt.controller;

import java.time.LocalDate;
import java.util.Arrays;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.ltt.dto.AtUserDataSummaryDTO;
import io.renren.modules.ltt.vo.AtUserDataSummaryVO;
import io.renren.modules.ltt.service.AtUserDataSummaryService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * 数据汇总-定时更新
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-05-06 13:15:52
 */
@RestController
@RequestMapping("ltt/atuserdatasummary")
public class AtUserDataSummaryController {
    @Autowired
    private AtUserDataSummaryService atUserDataSummaryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(AtUserDataSummaryDTO atUserDataSummary) {
        PageUtils page = atUserDataSummaryService.queryPage(atUserDataSummary);

        return R.ok().put("page", page);
    }

    /**
     * 保存数据统计记录
     *
     * @param searchTime
     */
    @RequestMapping("/saveAtUserDataSummary")
    public R saveAtUserDataSummary(@RequestParam String searchTime) {
        atUserDataSummaryService.saveAtUserDataSummary(LocalDate.parse(searchTime));
        return R.ok().put("flag", true);
    }
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atuserdatasummary:info")
    public R info(@PathVariable("id") Integer id) {
        AtUserDataSummaryVO atUserDataSummary = atUserDataSummaryService.getById(id);

        return R.ok().put("atUserDataSummary", atUserDataSummary);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atuserdatasummary:save")
    public R save(@RequestBody AtUserDataSummaryDTO atUserDataSummary) {
        atUserDataSummaryService.save(atUserDataSummary);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atuserdatasummary:update")
    public R update(@RequestBody AtUserDataSummaryDTO atUserDataSummary) {
        atUserDataSummaryService.updateById(atUserDataSummary);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atuserdatasummary:delete")
    public R delete(@RequestBody Integer[] ids) {
        atUserDataSummaryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
