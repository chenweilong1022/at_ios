package io.renren.modules.ltt.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.renren.modules.sys.controller.AbstractController;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.ltt.dto.AtDataTaskDTO;
import io.renren.modules.ltt.vo.AtDataTaskVO;
import io.renren.modules.ltt.service.AtDataTaskService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 加粉任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-17 18:52:43
 */
@RestController
@RequestMapping("ltt/atdatatask")
public class AtDataTaskController extends AbstractController {
    @Autowired
    private AtDataTaskService atDataTaskService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:atdatatask:list")
    public R list(AtDataTaskDTO atDataTask){
        atDataTask.setSysUserId(getAuthUserId());
        PageUtils page = atDataTaskService.queryPage(atDataTask);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:atdatatask:info")
    public R info(@PathVariable("id") Integer id){
		AtDataTaskVO atDataTask = atDataTaskService.getById(id);

        return R.ok().put("atDataTask", atDataTask);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:atdatatask:save")
    public R save(@RequestBody AtDataTaskDTO atDataTask){
        atDataTask.setSysUserId(getAuthUserId());
		atDataTaskService.save(atDataTask);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:atdatatask:update")
    public R update(@RequestBody AtDataTaskDTO atDataTask){
		atDataTaskService.updateById(atDataTask);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:atdatatask:delete")
    public R delete(@RequestBody Integer[] ids){
		atDataTaskService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * startUp
     */
    @RequestMapping("/startUp")
    @RequiresPermissions("ltt:atdatatask:save")
    public R startUp(@RequestBody Integer[] ids){
		atDataTaskService.startUp(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 错误重试
     * @param ids
     * @return
     */
    @RequestMapping("/errRetry")
    @RequiresPermissions("ltt:atdatatask:save")
    public R errRetry(@RequestBody Integer[] ids){
        atDataTaskService.errRetry(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping("/importDataToken")
    @RequiresPermissions("ltt:atdatatask:save")
    public void importDataToken(@RequestParam Integer dataTaskId,
                                HttpServletResponse response) throws IOException {
        byte[] bytes = atDataTaskService.importDataToken(dataTaskId);
        response.reset();
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s.zip\"", java.net.URLEncoder.encode("token", "UTF-8")));
        response.addHeader("Content-Length", "" + bytes.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(bytes, response.getOutputStream());
    }

}
