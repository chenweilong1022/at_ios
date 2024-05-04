package io.renren.modules.client.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.constant.SystemConstant;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.Md5Utils;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.FirefoxService;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.client.vo.CardJpGetPhoneSmsVO;
import io.renren.modules.client.vo.CardJpGetPhoneVO;
import io.renren.modules.client.vo.GetPhoneVO;
import io.renren.modules.ltt.enums.CountryCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 21:28
 */
@Service("cardJpSFServiceImpl")
@Game
@Slf4j
public class CardJpSFServiceImpl implements FirefoxService {

    @Resource(name = "caffeineCacheProjectWorkEntity")
    private Cache<String, ProjectWorkEntity> caffeineCacheProjectWorkEntity;

    @Resource
    private SystemConstant systemConstant;


    @EventListener
    @Order(value = 8888)
    public void test(ApplicationReadyEvent event) {
//        getPhone();
//        getPhoneCode("128590");
    }

    @Override
    public GetPhoneVO getPhone() {
        try {
            GetPhoneVO getPhoneVo = new GetPhoneVO()
                    .setPkey(UUID.randomUUID().toString())
                    .setPhone(String.format("%s%s", CountryCode.CountryCode3.getKey(), "09040029763"))
                    .setNumber("").setTime(null).setCom("").setCountry("").setCountryCode("").setOther("");
            return getPhoneVo;

        } catch (Exception e) {
            log.error("CardJpSFServiceImpl_getPhone_error {}", e);
        }
        return null;
    }

    @Override
    public String getPhoneCode(String pKey) {
        try {
            String getPhoneHttp = String.format("%s/smslist?token=%s",
                    "http://sms.szfangmm.com:3000/api/",
                    "wA54jX77SdvDSCeDkFSB6i");

            log.info("CardJpSFServiceImpl_getPhone param:{}", getPhoneHttp);
            String resp = HttpUtil.get(getPhoneHttp);
            log.info("CardJpSFServiceImpl_getPhoneCode_result {}", resp);

            return null;
        } catch (Exception e) {
            log.error("CardJpSFServiceImpl_getPhoneCode_error {}", e);
        }
        return null;
    }

    @Override
    public boolean setRel(String pKey) {
        return true;
    }

    @Override
    public boolean withBlackMobile(String pKey) {
        return true;
    }

}
