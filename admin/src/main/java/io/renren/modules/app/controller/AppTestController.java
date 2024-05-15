package io.renren.modules.app.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.base.vo.EnumVo;
import io.renren.modules.ltt.dto.CdRegisterRedisDto;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import io.renren.modules.ltt.enums.*;
import io.renren.common.utils.EnumUtil;
import io.renren.common.utils.R;
import io.renren.common.validator.AssertI18n;
import io.renren.modules.app.code.UserCode;
import io.renren.modules.app.dto.UserUpdateDTO;
import io.renren.modules.ltt.service.CdGetPhoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

/**
 * APP测试接口
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:47
 */
@RestController
@RequestMapping("/app")
@Validated
@Slf4j
public class AppTestController {

    @Resource(name = "jpSfPhoneCacheListString")
    private Cache<String, Queue<String>> jpSfPhoneCacheListString;


    @GetMapping("update")
    public R update(@Valid UserUpdateDTO updateDTO) {
        log.info("[update][updateDTO: {}]", updateDTO);
        AssertI18n.isTrue(updateDTO.getId() < 10, UserCode.USER_ID_MIN_ERROR);
        return R.ok();
    }

    @GetMapping("enums/countryCodes")
    public R update() {
        List<EnumVo> enumVos = new ArrayList<>();
        for (CountryCode value : CountryCode.values()) {
            enumVos.add(new EnumVo().setKey(value.getKey()).setValue(value.getValue()).setValue2(value.getValue2()));
        }
        return R.data(enumVos);
    }

    @GetMapping("enums/accountTransactionTypeCodes")
    public R accountTransactionTypeCodes() {
        List<EnumVo> enumVos = EnumUtil.enumToVo(AccountTransactionTypeEnum.values());
        return R.data(enumVos);
    }

    @GetMapping("enums/proxyStatus")
    public R proxyStatus() {
        List<EnumVo> enumVos = EnumUtil.enumToVo(ProxyStatus.values());
        return R.data(enumVos);
    }

    @GetMapping("enums/accountTransactionStatusCodes")
    public R accountTransactionStatusCodes() {
        List<EnumVo> enumVos = EnumUtil.enumToVo(AccountTransactionStatusEnum.values());
        return R.data(enumVos);
    }

    @GetMapping("enums/getProductTypeCodes")
    public R getProductTypeCodes() {
        List<EnumVo> enumVos = EnumUtil.enumToVo(ProductTypeEnum.values());
        return R.data(enumVos);
    }

    @GetMapping("enums/getOpenApps")
    public R getOpenApps() {
        List<EnumVo> enumVos = EnumUtil.enumToVo(OpenApp.values());
        return R.data(enumVos);
    }

    @GetMapping("enums/getOrderStatus")
    public R getOrderStatus() {
        List<EnumVo> enumVos = EnumUtil.enumToVo(OrderStatus.values());
        return R.data(enumVos);
    }

    @GetMapping("enums/getAtUserSource")
    public R getAtUserSource() {
        List<EnumVo> enumVos = EnumUtil.enumToVo(AtUserSourceEnum.values());
        return R.data(enumVos);
    }

    @GetMapping("enums/getGroupType")
    public R getGroupType() {
        List<EnumVo> enumVos = EnumUtil.enumToVo(GroupType.values());
        return R.data(enumVos);
    }

    @GetMapping("enums/getGroupStatus")
    public R getGroupStatus() {
        List<EnumVo> enumVos = EnumUtil.enumToVo(GroupStatus.values());
        return R.data(enumVos);
    }

    @GetMapping("enums/getRegisterStatus")
    public R getRegisterStatus() {
        List<EnumVo> enumVos = EnumUtil.enumToVo(RegisterStatus.values());
        return R.data(enumVos);
    }

    @GetMapping("enums/getOpenStatus")
    public R getOpenStatus() {
        List<EnumVo> enumVos = EnumUtil.enumToVo(OpenStatus.values());
        return R.data(enumVos);
    }

    @PostMapping("setJpSfPhoneCache")
    public R setJpSfPhoneCache(@RequestBody LinkedList<String> phoneList) {
        jpSfPhoneCacheListString.put("jpSfPhone",phoneList);
        return R.data(true);
    }

    @Resource
    private CdGetPhoneService cdGetPhoneService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @PostMapping("test")
    public R test() {
        List<CdGetPhoneEntity> list = cdGetPhoneService.list(new QueryWrapper<CdGetPhoneEntity>().lambda()
                .eq(CdGetPhoneEntity::getCountry, "81").last("limit 10"));

        cdGetPhoneService.saveWaitRegisterPhone(list);
        List<Object> waitRegisterList = stringRedisTemplate.opsForHash().values(RedisKeys.WAIT_START_REGISTER_PHONE.getValue()).subList(0, 100);


        for (Object object : waitRegisterList) {
            log.info(JSON.toJSONString(object));
            CdRegisterRedisDto cdGetPhoneEntity =  JSON.parseObject((String) object, CdRegisterRedisDto.class);
            log.info(JSON.toJSONString(cdGetPhoneEntity));
        }

        return R.data(true);
    }
//
//
//    /**
//     * 更新手机号注册次数
//     */
//    private Integer savePhoneRegisterCount(String phone) {
//        try {
//            Integer registerCount = cdGetPhoneService.getPhoneRegisterCount(phone) + 1;
//
//            log.error("更新手机号注册次数 {}, 次数：{}", phone, registerCount);
//            redisTemplate.opsForHash().put(RedisKeys.RedisKeys10.getValue(), phone, String.valueOf(registerCount));
//
//            //大于等于3次的卡，与前两次的做对比，超过24小时，才为可用状态
//            if (registerCount >= 3) {
//                redisTemplate.opsForValue().set(RedisKeys.RedisKeys12.getValue(phone), String.valueOf(registerCount), (24 * 60) + 30, TimeUnit.MINUTES);
//            }
//            return registerCount;
//        } catch (Exception e) {
//            log.error("更新手机号注册次数异常 {}, {}", phone, e);
//        }
//        return 0;
//    }

}
