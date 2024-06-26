package io.renren.modules.client.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.constant.SystemConstant;
import io.renren.common.utils.ConfigConstant;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.Md5Utils;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.FirefoxService;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.client.vo.*;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 21:28
 */
@Service("cardJpServiceImpl")
@Game
@Slf4j
public class CardJpServiceImpl implements FirefoxService {

    @Resource(name = "caffeineCacheProjectWorkEntity")
    private Cache<String, ProjectWorkEntity> caffeineCacheProjectWorkEntity;

    @Resource
    private SystemConstant systemConstant;

    @Resource(name = "cardJpSms")
    private Cache<String, Date> cardJpSms;

    @Resource(name = "cardJpSmsOver")
    private Cache<String, String> cardJpSmsOver;


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisObjectTemplate;

    @EventListener
    @Order(value = 8888)
    public void test(ApplicationReadyEvent event) {
//        getPhone();
//        getPhoneCode("128590");
    }

    //{"code":1,"msg":"SUCCESS","time":"1712051689","data":[{"take_id":128590,"phone_number":"07026371784"}]}
    //{"code":1,"msg":"SUCCESS","time":"1712060836","data":[{"take_id":129101,"phone_number":"08092924050"}]}
    @Override
    public GetPhoneVO getPhone() {
        GetPhoneVO phone = getPhoneBatch(100);
        if (phone == null) {
            phone = getPhoneBatch(100);
        } else if (phone.getMaxGetPhoneCount() != null) {
            phone = getPhoneBatch(phone.getMaxGetPhoneCount());
        }
        if (phone != null) {
            return phone;
        }
        return null;
    }


    public GetPhoneVO getPhoneBatch(Integer count) {
        try {
            if (cardJpSmsOver.getIfPresent("jpSmsOverFlag") != null) {
                return null;
            }

            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("user_code", systemConstant.getJpSmsConfigUserCode());//必填，用户号
            paramMap.put("platform_id", "4");//必填，平台ID {"platform_id":4,"platform_name":"line","price":50,"repeat_price":0}
            paramMap.put("take_count", String.valueOf(count));//必填，取号数量
            paramMap.put("notify_url", "123");//必填，回调地址,取号成功将会回调该地址
            paramMap.put("timestamp", DateUtils.getTimestampMillis());//必填，请求时间戳(秒)
            paramMap.put("sign", getSign(paramMap));//必填，签名

            String paramStr = JSONUtil.toJsonStr(paramMap);
            log.info("CardJpServiceImpl_getPhone_param {}", paramStr);

            String url = String.format("%s/TakePhoneNumberEx", systemConstant.getJpSmsConfigInterfaceUrl());
            String resp = HttpUtil.post(url, paramStr);
            log.info("CardJpServiceImpl_getPhone_result {}", resp);

            CardJpGetPhoneVO resultDto = JSON.parseObject(resp, CardJpGetPhoneVO.class);

            if (StringUtils.isNotEmpty(resultDto.getMsg()) && resultDto.getMsg().contains("API超限")) {
                cardJpSmsOver.put("jpSmsOverFlag", "true");
                return null;
            }

            if (StringUtils.isNotEmpty(resultDto.getMsg()) && resultDto.getMsg().contains("本次最多只能")) {
                Integer maxGetPhoneCount = getNumberByText(resultDto.getMsg());
                if (maxGetPhoneCount != null) {
                    GetPhoneVO getPhoneVo = new GetPhoneVO().setMaxGetPhoneCount(maxGetPhoneCount) ;
                    return getPhoneVo;
                }
            }

            if (ObjectUtil.isNotNull(resultDto)
                    && CollectionUtil.isNotEmpty(resultDto.getData())) {

                List<String> phones = new ArrayList<>();
                List<String> pkeys = new ArrayList<>();

                for (CardJpGetPhoneVO.Data datum : resultDto.getData()) {
                    String format = String.format("%s%s", CountryCode.CountryCode3.getKey(), datum.getPhone_number());
                    phones.add(format);
                    pkeys.add(datum.getTake_id());
                }
                GetPhoneVO getPhoneVo = new GetPhoneVO()
                        .setPkeys(pkeys)
                        .setPhones(phones)
                        .setNumber("").setTime(null).setCom("").setCountry("").setCountryCode("").setOther("");
                return getPhoneVo;
            }
        } catch (Exception e) {
            log.error("CardJpServiceImpl_getPhone_error {}", e);
        }
        return null;
    }

//    public static void main(String[] args) {
//        String resp = "{\"code\":1,\"msg\":\"SUCCESS\",\"time\":\"1712394634\",\"data\":{\"ret\":[{\"take_id\":146107,\"state\":1,\"phone_number\":\"08013881651\",\"take_time\":1712391241,\"sms\":[{\"recv_time\":1712393514,\"content\":\"認証番号「255982」をLINEで入力して下で下さい。30分間有効です。\"},{\"recv_time\":1712394272,\"content\":\"認証番号「514325」をLINEで入力して下さい。\\n他人には教えないで下さい。30分間有効です。\"}]}]}}";
//        CardJpGetPhoneSmsVO resultDto = JSON.parseObject(resp, CardJpGetPhoneSmsVO.class);
//        log.info("CardJpServiceImpl_getPhoneCode_resultDto {}", resultDto);
//
//        if (resultDto.getCode() != 1 || resultDto.getData() == null) {
//            log.error("CardJpServiceImpl_getPhoneCode_error {}, result :{}", resultDto);
//            System.out.println("null");
//        }
//        List<CardJpGetPhoneSmsVO.Data.Ret> ret = resultDto.getData().getRet();
//        if (CollectionUtil.isNotEmpty(ret)) {
//            CardJpGetPhoneSmsVO.Data.Ret ret1 = ret.stream().filter(i -> CollectionUtil.isNotEmpty(i.getSms())).findFirst().orElse(null);
//            if (ObjectUtil.isNotNull(ret1) && CollectionUtil.isNotEmpty(ret1.getSms())) {
//                CardJpGetPhoneSmsVO.Data.Ret.Sm sms = ret1.getSms().stream()
//                        .filter(i -> DateUtils.comparisonTime(i.getRecvTime(), "1712394271"))
//                        .findFirst().orElse(null);
//                System.out.println("ObjectUtil.isNotNull(sms)"+ObjectUtil.isNotNull(sms));
//                System.out.println(sms);
//                System.out.println(sms.getContent());
//                System.out.println(extractVerificationCode(sms.getContent()));
//            }
//        }
//    }


    @Override
    public Map<Long,CardJpGetPhoneSmsVO.Data.Ret.Sm> getPhoneCodes(String pKeys) {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("user_code", systemConstant.getJpSmsConfigUserCode());//必填，用户号
        paramMap.put("take_ids", pKeys);//必填，取号ID,多个ID请用英文半角逗号分隔
        String currentTime = DateUtils.getTimestampMillis();
        paramMap.put("timestamp", currentTime);//必填，请求时间戳(秒)
        paramMap.put("sign", getSign(paramMap));//必填，签名

        String paramStr = JSONUtil.toJsonStr(paramMap);
        log.info("CardJpServiceImpl_getPhoneCode_param {}", paramStr);

        String url = String.format("%s/GetState", systemConstant.getJpSmsConfigInterfaceUrl());
        String resp = HttpUtil.post(url, paramStr);
        //{"code":1,"msg":"SUCCESS","time":"1712064278","data":{"ret":[{"take_id":129216,"state":1,"phone_number":"08023755245","take_time":1712064202,"sms":[]}]}}
        log.info("CardJpServiceImpl_getPhoneCode_result {}", resp);

        CardJpGetPhoneSmsVO resultDto = JSON.parseObject(resp, CardJpGetPhoneSmsVO.class);
        log.info("CardJpServiceImpl_getPhoneCode_resultDto {}", resultDto);

        if (StringUtils.isNotEmpty(resultDto.getMsg()) && resultDto.getMsg().contains("API超限")) {
            cardJpSmsOver.put("jpSmsOverFlag", "true");
            return null;
        }

        if (resultDto.getCode() != 1 || resultDto.getData() == null) {
            log.error("CardJpServiceImpl_getPhoneCode_error {}, result :{}", paramStr, resultDto);
            return null;
        }
        List<CardJpGetPhoneSmsVO.Data.Ret> ret = resultDto.getData().getRet();
        Map<Long,CardJpGetPhoneSmsVO.Data.Ret.Sm> map = new HashMap();
        try{
            for (CardJpGetPhoneSmsVO.Data.Ret ret1 : ret) {
                List<CardJpGetPhoneSmsVO.Data.Ret.Sm> sms = ret1.getSms();
                for (CardJpGetPhoneSmsVO.Data.Ret.Sm sm : sms) {
                    long timestamp = Long.valueOf(sm.getRecvTime());  // Your Unix timestamp
//                LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("Asia/Shanghai"));
//                ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
//                Date recvTime = Date.from(zonedDateTime.toInstant());
                    sm.setTime(timestamp);
                }
                if (CollUtil.isEmpty(sms)) {
                    continue;
                }
                CardJpGetPhoneSmsVO.Data.Ret.Sm max = Collections.max(sms, Comparator.comparingLong(CardJpGetPhoneSmsVO.Data.Ret.Sm::getTime));
                if (ObjectUtil.isNotNull(max)) {
                    map.put(ret1.getTakeid(),max);
                }
            }
        }catch (Exception e) {
            log.error("e = ",e.getMessage());
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public String getPhoneCode(String pKey) {
        try {
            //发起操作时间
            Date date = cardJpSms.getIfPresent(pKey);
            log.info("CardJpServiceImpl_getPhoneCode_createTimestamp {}", date);

            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("user_code", systemConstant.getJpSmsConfigUserCode());//必填，用户号
            paramMap.put("take_ids", pKey);//必填，取号ID,多个ID请用英文半角逗号分隔
            String currentTime = DateUtils.getTimestampMillis();
            paramMap.put("timestamp", currentTime);//必填，请求时间戳(秒)
            paramMap.put("sign", getSign(paramMap));//必填，签名

            String paramStr = JSONUtil.toJsonStr(paramMap);
            log.info("CardJpServiceImpl_getPhoneCode_param {}", paramStr);

            String url = String.format("%s/GetState", systemConstant.getJpSmsConfigInterfaceUrl());
            String resp = HttpUtil.post(url, paramStr);
            //{"code":1,"msg":"SUCCESS","time":"1712064278","data":{"ret":[{"take_id":129216,"state":1,"phone_number":"08023755245","take_time":1712064202,"sms":[]}]}}
            log.info("CardJpServiceImpl_getPhoneCode_result {}", resp);

            CardJpGetPhoneSmsVO resultDto = JSON.parseObject(resp, CardJpGetPhoneSmsVO.class);
            log.info("CardJpServiceImpl_getPhoneCode_resultDto {}", resultDto);

            if (StringUtils.isNotEmpty(resultDto.getMsg()) && resultDto.getMsg().contains("API超限")) {
                cardJpSmsOver.put("jpSmsOverFlag", "true");
                return null;
            }

            if (resultDto.getCode() != 1 || resultDto.getData() == null) {
                log.error("CardJpServiceImpl_getPhoneCode_error {}, result :{}", paramStr, resultDto);
                return null;
            }
            List<CardJpGetPhoneSmsVO.Data.Ret> ret = resultDto.getData().getRet();
            if (CollectionUtil.isNotEmpty(ret)) {
                for (CardJpGetPhoneSmsVO.Data.Ret ret1 : ret) {
                    if (ObjectUtil.isNotNull(ret1) && CollectionUtil.isNotEmpty(ret1.getSms())) {
                        List<CardJpGetPhoneSmsVO.Data.Ret.Sm> sms = ret1.getSms();
                        for (CardJpGetPhoneSmsVO.Data.Ret.Sm sm : sms) {
                            long timestamp = Long.valueOf(sm.getRecvTime());  // Your Unix timestamp
                            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.of("Asia/Shanghai"));
                            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
                            Date recvTime = Date.from(zonedDateTime.toInstant());
                            boolean before = date.before(recvTime);
                            if (before) {
                                return extractVerificationCode(sm.getContent());
                            }
                        }
                    }
                }
            }
            return null;
        } catch (Exception e) {
            log.error("CardJpServiceImpl_getPhoneCode_error {}", e);
        }
        return null;
    }

    @Override
    public boolean setRel(String pKey) {
//        try {
//            HashMap<String, String> paramMap = new HashMap<>();
//            paramMap.put("user_code", systemConstant.getJpSmsConfigUserCode());//必填，用户号
//            paramMap.put("take_ids", pKey);//必填，取号ID,多个ID请用英文半角逗号分隔
//            paramMap.put("timestamp", DateUtils.getTimestampMillis());//必填，请求时间戳(秒)
//            paramMap.put("sign", getSign(paramMap));//必填，签名
//
//            String paramStr = JSONUtil.toJsonStr(paramMap);
//            log.info("CardJpServiceImpl_setRel_param {}", paramStr);
//
//            String url = String.format("%s/CancelTake", systemConstant.getJpSmsConfigInterfaceUrl());
//            String resp = HttpUtil.post(url, paramStr);
//            log.info("CardJpServiceImpl_setRel_result {}", resp);
//
//            CardJpGetPhoneCancelVO resultDto = JSON.parseObject(resp, CardJpGetPhoneCancelVO.class);
//            log.error("CardJpServiceImpl_setRel_resultDto {}", resultDto);
//
//            if (resultDto.getCode() != 1 || resultDto.getData() == null) {
//                log.error("CardJpServiceImpl_setRel_error {}, result :{}", paramStr, resultDto);
//                return false;
//            }
//            List<String> takeIds = resultDto.getData().getTake_ids();
//            if (CollectionUtil.isNotEmpty(takeIds) && takeIds.contains(pKey)) {
//                return true;
//            }
//        } catch (Exception e) {
//            log.error("CardJpServiceImpl_setRel_error {}", e);
//        }
        return true;
    }

    @Override
    public boolean withBlackMobile(String pKey) {
//        try {
//            HashMap<String, String> paramMap = new HashMap<>();
//            paramMap.put("user_code", systemConstant.getJpSmsConfigUserCode());//必填，用户号
//            paramMap.put("take_ids", pKey);//必填，取号ID,多个ID请用英文半角逗号分隔
//            paramMap.put("timestamp", DateUtils.getTimestampMillis());//必填，请求时间戳(秒)
//            paramMap.put("sign", getSign(paramMap));//必填，签名
//
//            String paramStr = JSONUtil.toJsonStr(paramMap);
//            log.info("CardJpServiceImpl_withBlackMobile_param {}", paramStr);
//
//            String url = String.format("%s/Blocking", systemConstant.getJpSmsConfigInterfaceUrl());
//            String resp = HttpUtil.post(url, paramStr);
//            log.info("CardJpServiceImpl_withBlackMobile_result {}", resp);
//
//            CardJpGetPhoneCancelVO resultDto = JSON.parseObject(resp, CardJpGetPhoneCancelVO.class);
//            log.error("CardJpServiceImpl_withBlackMobile_resultDto {}", resultDto);
//
//            if (resultDto.getCode() != 1 || resultDto.getData() == null) {
//                log.error("CardJpServiceImpl_withBlackMobile_error {}, result :{}", paramStr, resultDto);
//                return false;
//            }
//            List<String> takeIds = resultDto.getData().getTake_ids();
//            if (CollectionUtil.isNotEmpty(takeIds) && takeIds.contains(pKey)) {
//                return true;
//            }
//        } catch (Exception e) {
//            log.error("CardJpServiceImpl_setRel_error {}", e);
//        }
        return true;
    }

    /**
     * 获取平台信息
     *
     * @return
     */
//    @EventListener
//    @Order(value = 8888)
    public void getPlatformInfo(ApplicationReadyEvent event) {
        try {
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("user_code", systemConstant.getJpSmsConfigUserCode());//必填，用户号
            paramMap.put("timestamp", DateUtils.getTimestampMillis());//必填，请求时间戳(秒)
            paramMap.put("sign", getSign(paramMap));//必填签名
            String paramStr = JSONUtil.toJsonStr(paramMap);

            String url = String.format("%s/GetPlatformInfo", systemConstant.getJpSmsConfigInterfaceUrl());
            String resp = HttpUtil.post(url, paramStr);
        } catch (Exception e) {
            log.error("getPlatformInfo_error {}", e);
        }
    }


    public static String extractVerificationCode(String smsText) {
        if (StringUtils.isEmpty(smsText)) {
            return null;
        }
        // 使用正则表达式匹配短信内容中的验证码
        Pattern pattern = Pattern.compile("\\d{6}"); // 此处使用六位数字作为验证码的示例
        Matcher matcher = pattern.matcher(smsText);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }


    public static Integer getNumberByText(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        // 使用正则表达式匹配数字
        Pattern pattern = Pattern.compile("\\d+"); // 匹配一个或多个数字
        Matcher matcher = pattern.matcher(text);

        // 循环匹配并输出结果
        while (matcher.find()) {
            String number = matcher.group(); // 获取匹配到的数字
            return Integer.valueOf(number);
        }
        return null;
    }


    /**
     * 获取秘钥
     */
    private String getSign(Map<String, String> paramMap) {
        //按照参数名ASCII码从小到大排序
        String param = generateSign(paramMap);
        //拼接成的字符串stringA再与商户密钥拼接成新的字符串stringB，并对stringB进行md5运算，得出签名sign
        String signParam = param + systemConstant.getJpSmsConfigInterfaceKey(); // 拼接商户密钥
        return Md5Utils.generateMD5(signParam); // 生成MD5签名
    }

    public String generateSign(Map<String, String> paramMap) {
        // 过滤非空参数值，并按照ASCII字典序排序
        return paramMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null) // 过滤非空值
                .sorted(Map.Entry.comparingByKey()) // 按键（参数名）排序
                .map(entry -> entry.getKey() + "=" + entry.getValue()) // 转换成key=value格式
                .collect(Collectors.joining("&")); // 使用&连接成一个字符串
    }
}
