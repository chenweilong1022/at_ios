package io.renren.modules.client.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.constant.SystemConstant;
import io.renren.common.utils.ConfigConstant;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.Md5Utils;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.FirefoxService;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.client.vo.CardJpGetPhoneSmsVO;
import io.renren.modules.client.vo.CardJpGetPhoneVO;
import io.renren.modules.client.vo.CardJpSFGetPhoneSmsVO;
import io.renren.modules.client.vo.GetPhoneVO;
import io.renren.modules.ltt.SfTimeZone;
import io.renren.modules.ltt.dto.CdLineIpProxyDTO;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.entity.AtUserPortEntity;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.RedisKeys;
import io.renren.modules.ltt.service.AtUserService;
import io.renren.modules.ltt.service.CdLineIpProxyService;
import io.renren.modules.ltt.vo.CdRegisterSubtasksVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.net.Proxy;
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
    @Resource(name = "caffeineCacheProjectWorkEntity")
    private Cache<String, ProjectWorkEntity> caffeineCacheProjectWorkEntity;

    @Resource(name = "cardJpSFGetPhoneSmsVOCache")
    private Cache<String, CardJpSFGetPhoneSmsVO> cardJpSFGetPhoneSmsVOCache;

    @Override
    public GetPhoneVO getPhone() {
        try {
            Queue<String> jpSfPhone = jpSfPhoneCacheListString.getIfPresent("jpSfPhone");
            if (CollectionUtil.isNotEmpty(jpSfPhone)) {
                String phone = jpSfPhone.poll();
                jpSfPhoneCacheListString.put("phone",jpSfPhone);
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

    @Resource(name = "cardJpSms")
    private Cache<String, Date> cardJpSms;
    @Autowired
    private AtUserService atUserService;
    @Autowired
    private CdLineIpProxyService cdLineIpProxyService;

    @Override
    public String getPhoneCode(String pKeys) {

        String[] split = pKeys.split("#");
        if (split.length != 3) {
            return null;
        }
        String pKey = split[0];
        String sfApi = split[1];
        String timeZone = split[2];
        //发起操作时间
        Date date = cardJpSms.getIfPresent(pKey);
        try {
            //获取缓存里面的短信
            CardJpSFGetPhoneSmsVO cardJpSFGetPhoneSmsVO = cardJpSFGetPhoneSmsVOCache.getIfPresent(pKey);
            if (ObjectUtil.isNotNull(cardJpSFGetPhoneSmsVO)) {
                Date time = cardJpSFGetPhoneSmsVO.getTime();
                if (SfTimeZone.SfTimeZone2.getKey().equals(Integer.valueOf(timeZone))) {
                    time = DateUtil.offsetHour(time,-1);
                }
                boolean before = date.before(time);
                if (cardJpSFGetPhoneSmsVO != null && before) {
                    String s = extractVerificationCode(cardJpSFGetPhoneSmsVO.getContent());
                    return s;
                }
            }

            AtUserEntity one = atUserService.getOne(new QueryWrapper<AtUserEntity>().lambda()
                    .eq(AtUserEntity::getNation,"TH")
                    .last("ORDER BY RAND() LIMIT 1")
            );

            if (ObjectUtil.isNull(one)) {
                return null;
            }

            //获取代理
            CdLineIpProxyDTO cdLineIpProxyDTO = new CdLineIpProxyDTO();
            cdLineIpProxyDTO.setTokenPhone(one.getTelephone());
            cdLineIpProxyDTO.setLzPhone(one.getTelephone());
            String proxyIp = cdLineIpProxyService.getProxyIp(cdLineIpProxyDTO);
            if (StrUtil.isEmpty(proxyIp)) {
                return null;
            }
            HttpGet request = new HttpGet(sfApi);
            request.addHeader("User-Agent", "Mozilla/5.0");
            String[] parts = proxyIp.split(":");
            if (parts.length != 2) {
                System.err.println("Invalid address format");
                return null;
            }
            String ip = parts[0];
            int port = Integer.parseInt(parts[1]);  // 将端口号的字符串转换为整数
            HttpResponse execute = HttpRequest.post(sfApi)
                    .setProxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(ip, port))).timeout(20000).execute();

            String resp = execute.body();

            log.info("CardJpSFServiceImpl_getPhoneCode_result {}", resp);

            List<CardJpSFGetPhoneSmsVO> resultList = JSON.parseArray(resp, CardJpSFGetPhoneSmsVO.class);

            if (CollectionUtil.isNotEmpty(resultList)) {
                // 根据手机号分组并保留时间最大的一个
                Map<String, CardJpSFGetPhoneSmsVO> cardJpSFGetPhoneSmsVOMap = resultList.stream()
                        .collect(Collectors.groupingBy(CardJpSFGetPhoneSmsVO::getSimnum,
                                Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(CardJpSFGetPhoneSmsVO::getTime)),
                                        Optional::get)));
                //保存
                for (String s : cardJpSFGetPhoneSmsVOMap.keySet()) {
                    cardJpSFGetPhoneSmsVOCache.put(s,cardJpSFGetPhoneSmsVOMap.get(s));
                }
                cardJpSFGetPhoneSmsVO = cardJpSFGetPhoneSmsVOMap.get(pKey);
                if (ObjectUtil.isNull(cardJpSFGetPhoneSmsVO)) {
                    return null;
                }
                Date time = cardJpSFGetPhoneSmsVO.getTime();
                if (SfTimeZone.SfTimeZone2.getKey().equals(Integer.valueOf(timeZone))) {
                    time = DateUtil.offsetHour(time,-1);
                }
                boolean before = date.before(time);
                if (cardJpSFGetPhoneSmsVO != null && before) {
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
