package io.renren.modules.app.controller;


import cn.hutool.json.JSONUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

}
