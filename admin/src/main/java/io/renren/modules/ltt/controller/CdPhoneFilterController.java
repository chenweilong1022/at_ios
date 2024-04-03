package io.renren.modules.ltt.controller;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.ltt.dto.CdPhoneFilterDTO;
import io.renren.modules.ltt.vo.CdPhoneFilterVO;
import io.renren.modules.ltt.service.CdPhoneFilterService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 手机号筛选
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-25 00:55:11
 */
@RestController
@RequestMapping("ltt/cdphonefilter")
public class CdPhoneFilterController {
    @Autowired
    private CdPhoneFilterService cdPhoneFilterService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("ltt:cdphonefilter:list")
    public R list(CdPhoneFilterDTO cdPhoneFilter){
        PageUtils page = cdPhoneFilterService.queryPage(cdPhoneFilter);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("ltt:cdphonefilter:info")
    public R info(@PathVariable("id") Integer id){
        CdPhoneFilterVO cdPhoneFilter = cdPhoneFilterService.getById(id);

        return R.ok().put("cdPhoneFilter", cdPhoneFilter);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("ltt:cdphonefilter:save")
    public R save(@RequestBody CdPhoneFilterDTO cdPhoneFilter){
        cdPhoneFilterService.save(cdPhoneFilter);

        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/exportSJ")
    @RequiresPermissions("ltt:cdphonefilter:save")
    public void exportSJ(CdPhoneFilterDTO cdPhoneFilter, HttpServletResponse response) throws IOException {
        byte[] bytes = cdPhoneFilterService.exportSJ(cdPhoneFilter);
        response.reset();
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s.txt\"",java.net.URLEncoder.encode("充值","UTF-8")));
        response.addHeader("Content-Length", "" + bytes.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(bytes, response.getOutputStream());
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("ltt:cdphonefilter:update")
    public R update(@RequestBody CdPhoneFilterDTO cdPhoneFilter){
        cdPhoneFilterService.updateById(cdPhoneFilter);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("ltt:cdphonefilter:delete")
    public R delete(@RequestBody Integer[] ids){
        cdPhoneFilterService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
