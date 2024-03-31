package io.renren.modules.app.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.base.vo.EnumVo;
import io.renren.common.utils.EnumUtil;
import io.renren.common.utils.R;
import io.renren.common.validator.AssertI18n;
import io.renren.modules.app.code.UserCode;
import io.renren.modules.app.dto.UserUpdateDTO;
import io.renren.modules.ltt.dto.IosTokenDTO;
import io.renren.modules.ltt.enums.AccountTransactionStatusEnum;
import io.renren.modules.ltt.enums.AccountTransactionTypeEnum;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.ProductTypeEnum;
import io.renren.modules.ltt.service.AtUserTokenIosService;
import io.renren.modules.ltt.vo.IOSTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * APP测试接口
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:47
 */
@RestController
@RequestMapping("/app")
public class AppTokensController {

    @Autowired
    private AtUserTokenIosService atUserTokenIosService;


    @PostMapping("/token/syncAppToken")
    public R syncAppToken(@RequestBody IosTokenDTO map, @RequestParam("deviceId") String deviceId) {
        map.setDeviceId(deviceId);
        atUserTokenIosService.syncAppToken(map);
        return R.ok();
    }

    @Resource(name = "stringQueueCacheIOSTaskVO")
    private Cache<String, Queue<IOSTaskVO>> stringQueueCacheIOSTaskVO;

    @PostMapping("/task/getTask")
    public R getTask( @RequestParam("deviceId") String deviceId) {
        Queue<IOSTaskVO> cacheIOSTaskVOIfPresent = stringQueueCacheIOSTaskVO.getIfPresent(deviceId);
        if (CollUtil.isEmpty(cacheIOSTaskVOIfPresent)) {
            //判断是否存在注册任务
            Queue<IOSTaskVO> cacheIOSTaskVOIfPresent1 = stringQueueCacheIOSTaskVO.getIfPresent("register");
            if (CollUtil.isEmpty(cacheIOSTaskVOIfPresent1)) {
                return R.data("");
            }
            LinkedList<IOSTaskVO> iosTaskVOS = new LinkedList<>(cacheIOSTaskVOIfPresent1);
            return R.data(iosTaskVOS.poll());
        }
        if (cacheIOSTaskVOIfPresent.isEmpty()) {
            //判断是否存在注册任务
            Queue<IOSTaskVO> cacheIOSTaskVOIfPresent1 = stringQueueCacheIOSTaskVO.getIfPresent("register");
            if (CollUtil.isEmpty(cacheIOSTaskVOIfPresent1)) {
                return R.data("");
            }
            LinkedList<IOSTaskVO> iosTaskVOS = new LinkedList<>(cacheIOSTaskVOIfPresent1);
            return R.data(iosTaskVOS.poll());
        }
        IOSTaskVO poll = cacheIOSTaskVOIfPresent.poll();
        stringQueueCacheIOSTaskVO.put(deviceId,cacheIOSTaskVOIfPresent);
        return R.data(poll);
    }

}
