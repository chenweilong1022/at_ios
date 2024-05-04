package io.renren.modules.client.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
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
import io.renren.modules.client.vo.CardJpSFGetPhoneSmsVO;
import io.renren.modules.client.vo.GetPhoneVO;
import io.renren.modules.ltt.entity.AtUserPortEntity;
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

import static io.renren.modules.client.impl.CardJpServiceImpl.extractVerificationCode;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 21:28
 */
@Service("cardJpSFServiceImpl")
@Game
@Slf4j
public class CardJpSFServiceImpl implements FirefoxService {

    @Resource(name = "jpSfPhoneCacheListString")
    private Cache<String, Queue<String>> jpSfPhoneCacheListString;

    @Override
    public GetPhoneVO getPhone() {
        try {
            Queue<String> jpSfPhone = jpSfPhoneCacheListString.getIfPresent("jpSfPhone");
            if (CollectionUtil.isNotEmpty(jpSfPhone)) {
                String phone = jpSfPhone.poll();
                if (StrUtil.isNotEmpty(phone)) {
                    GetPhoneVO getPhoneVo = new GetPhoneVO()
                            .setPkey(phone)
                            .setPhone(String.format("%s%s", CountryCode.CountryCode3.getKey(), phone))
                            .setNumber("").setTime(null).setCom("").setCountry("").setCountryCode("").setOther("");
                    return getPhoneVo;
                }
            }
            return null;
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

            List<CardJpSFGetPhoneSmsVO> resultList = JSON.parseArray(resp, CardJpSFGetPhoneSmsVO.class);

            if (CollectionUtil.isNotEmpty(resultList)) {
                CardJpSFGetPhoneSmsVO cardJpSFGetPhoneSmsVO = resultList.stream()
                        .filter(i -> pKey.equals(i.getSimnum()))
                        .max(Comparator.comparing(CardJpSFGetPhoneSmsVO::getTime)).orElse(null);
                if (cardJpSFGetPhoneSmsVO != null) {
                    String s = extractVerificationCode(cardJpSFGetPhoneSmsVO.getContent());
                    return s;
                }
            }
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

    public static void main(String[] args) {
        String result = "[\n" +
                "    {\n" +
                "        \"id\": 50,\n" +
                "        \"content\": \"認証番号「155646」をLINEで入力して下さい。他人には教えないで下さい。30分間有効です。\",\n" +
                "        \"time\": \"2024-05-04 20:59:48\",\n" +
                "        \"simnum\": \"09040029763\"\n" +
                "    },\n" +
                "   {\n" +
                "        \"id\": 51,\n" +
                "        \"content\": \"認証番号「155647」をLINEで入力して下さい。他人には教えないで下さい。30分間有効です。\",\n" +
                "        \"time\": \"2024-05-04 20:59:58\",\n" +
                "        \"simnum\": \"09040029763\"\n" +
                "    }\n" +
                "]";
        String phone = "09040029763";
        List<CardJpSFGetPhoneSmsVO> resultList = JSON.parseArray(result, CardJpSFGetPhoneSmsVO.class);
        log.info("CardJpServiceImpl_getPhoneCode_result {}", resultList);

        if (CollectionUtil.isNotEmpty(resultList)) {
            CardJpSFGetPhoneSmsVO cardJpSFGetPhoneSmsVO = resultList.stream()
                    .filter(i -> phone.equals(i.getSimnum()))
                    .max(Comparator.comparing(CardJpSFGetPhoneSmsVO::getTime)).orElse(null);
            if (cardJpSFGetPhoneSmsVO != null) {
                String s = extractVerificationCode(cardJpSFGetPhoneSmsVO.getContent());
                System.out.println(s);
            }
        }


    }
}
