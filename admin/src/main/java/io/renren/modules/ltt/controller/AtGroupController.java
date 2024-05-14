package io.renren.modules.ltt.controller;

import java.io.IOException;
import java.util.Arrays;

import io.renren.modules.client.dto.ImportZipDTO;
import io.renren.modules.sys.controller.AbstractController;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.AtGroupDTO;
import io.renren.modules.ltt.vo.AtGroupVO;
import io.renren.modules.ltt.service.AtGroupService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-29 17:07:23
 */
@RestController
@RequestMapping("ltt/atgroup")
public class AtGroupController extends AbstractController {
    @Autowired
    private AtGroupService atGroupService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atgrouptask:list")
    public R list(@RequestBody AtGroupDTO atGroup){
        PageUtils page = atGroupService.queryPage(atGroup);

        return R.ok().put("page", page);
    }





    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atgrouptask:info")
    public R info(@PathVariable("id") Integer id){
		AtGroupVO atGroup = atGroupService.getById(id);

        return R.ok().put("atGroup", atGroup);
    }
    /**
     * 错误重试
     */
    @RequestMapping("/errRetryGroup/{id}")
    @RequiresPermissions("ltt:atgrouptask:update")
    public R errRetryGroup(@PathVariable("id") Integer id){
		Boolean flag = atGroupService.errRetryGroup(id);

        return R.ok().put("resultFlag", flag);
    }

    /**
     * 开始拉群
     */
    @RequestMapping("/startGroup")
    @RequiresPermissions("ltt:atgrouptask:update")
    public R startGroup(@RequestBody Integer[] ids){
        Boolean flag = atGroupService.startGroup(Arrays.asList(ids));
        return R.data(flag);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atgrouptask:save")
    public R save(@RequestBody AtGroupDTO atGroup){
		atGroupService.save(atGroup);

        return R.ok();
    }

    /**
     * 导出zip
     */
    @RequestMapping("importZip")
    @RequiresPermissions("ltt:atgrouptask:save")
    public void importZip(ImportZipDTO importZipDTO, HttpServletResponse response) throws IOException {
        byte[] bytes = atGroupService.importZip(importZipDTO);

        response.reset();
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s.zip\"",java.net.URLEncoder.encode(importZipDTO.getZipName(),"UTF-8")));
        response.addHeader("Content-Length", "" + bytes.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(bytes, response.getOutputStream());
    }

    /**
     * 更换token
     */
    @RequestMapping("reallocateToken")
    @RequiresPermissions("ltt:atgrouptask:save")
    public R reallocateToken(@RequestBody AtGroupDTO atGroup) {
        atGroup.setSysUserId(getAuthUserId());
        atGroupService.reallocateToken(atGroup);
        return R.ok();
    }

    /**
     * 启动任务
     */
    @RequestMapping("startTask")
    @RequiresPermissions("ltt:atgrouptask:save")
    public R startTask(@RequestBody AtGroupDTO atGroup) {
        atGroup.setSysUserId(getAuthUserId());
        atGroupService.startTask(atGroup);
        return R.ok();
    }

    /**
     * 修改群名
     */
    @RequestMapping("updateGroupName")
    @RequiresPermissions("ltt:atgrouptask:save")
    public R updateGroupName(@RequestBody AtGroupDTO atGroup) {
        atGroup.setSysUserId(getAuthUserId());
        Integer successCount = atGroupService.updateGroupName(atGroup);
        return R.ok().put("successCount", successCount);
    }

    /**
     * 获取真实名称
     */
    @RequestMapping("getRealGroupName")
    @RequiresPermissions("ltt:atgrouptask:save")
    public R getRealGroupName(@RequestBody AtGroupDTO atGroup) {
        atGroup.setSysUserId(getAuthUserId());
        atGroupService.getRealGroupName(atGroup);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atgrouptask:update")
    public R update(@RequestBody AtGroupDTO atGroup){
		atGroupService.updateById(atGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atgrouptask:delete")
    public R delete(@RequestBody Integer[] ids){
		atGroupService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
