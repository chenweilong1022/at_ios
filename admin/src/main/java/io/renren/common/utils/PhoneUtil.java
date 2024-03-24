package io.renren.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
import io.renren.common.utils.vo.PhoneCountryVO;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mao, hongtu1993@sina.cn
 * @version Enter version here..., 5:47 PM 2019/12/16
 */
public class PhoneUtil {
    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();


    /**
     * 根据国家代码和手机号  判断手机号是否有效
     * @param phoneNumber 手机号码
     * @param countryCode 国号(区号)
     * @return true / false
     */
    public static boolean checkPhoneNumber(long phoneNumber, int countryCode) {
        PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(countryCode);
        pn.setNationalNumber(phoneNumber);
        return phoneNumberUtil.isValidNumber(pn);
    }

    private static Pattern phoneReg = Pattern.compile("\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$");

    public static PhoneCountryVO getPhoneNumberInfo(String phoneNumber) throws Exception {
        // 正则校验
        Matcher matcher = phoneReg.matcher(phoneNumber);
        if (!matcher.find()) {
            throw new Exception("The phone number maybe like:" + "+[National Number][Phone number], but got " + phoneNumber);
        }

        Phonenumber.PhoneNumber referencePhonenumber;
        try {
            String language = "CN";
            referencePhonenumber = phoneNumberUtil.parse(phoneNumber, language);
        } catch (NumberParseException e) {
            throw new Exception(e.getMessage());
        }
        String regionCodeForNumber = phoneNumberUtil.getRegionCodeForNumber(referencePhonenumber);

        if (regionCodeForNumber == null) {
            throw new Exception("Missing region code by phone number " + phoneNumber);
        }

        boolean checkSuccess = PhoneUtil.checkPhoneNumber(referencePhonenumber.getNationalNumber(), referencePhonenumber.getCountryCode());
        if (!checkSuccess) {
//            throw new Exception("Not an active number:" + phoneNumber);
        }

        String description = geocoder.getDescriptionForNumber(referencePhonenumber, Locale.CHINA);

        int countryCode = referencePhonenumber.getCountryCode();
        long nationalNumber = referencePhonenumber.getNationalNumber();
        JSONObject resultObject = new JSONObject();
        // 区域编码 Locale : HK, US, CN ...
        resultObject.put("regionCode", regionCodeForNumber);
        // 国号: 86, 1, 852 ... @link: https://blog.csdn.net/wzygis/article/details/45073327
        resultObject.put("countryCode", countryCode);
        // 去掉+号 和 国号/区号 后的实际号码
        resultObject.put("nationalNumber", nationalNumber);
        // 所在地区描述信息
        resultObject.put("description", description);
        // 去掉+号后的号码 (用于阿里云发送短信)
        resultObject.put("number", String.valueOf(countryCode) + nationalNumber);
        resultObject.put("fullNumber", phoneNumber);

        PhoneCountryVO phoneCountryVO = new PhoneCountryVO();
        phoneCountryVO.setNumber(String.valueOf(countryCode) + nationalNumber);
        phoneCountryVO.setRegionCode(regionCodeForNumber);
        phoneCountryVO.setNationalNumber(nationalNumber);
        phoneCountryVO.setCountryCode(countryCode);
        phoneCountryVO.setDescription(description);
        phoneCountryVO.setFullNumber(phoneNumber);

        return phoneCountryVO;

    }

    public static void main(String[] args) throws Exception {

        // {"number":"85268476749","regionCode":"HK","nationalNumber":68476749,"countryCode":852,"description":"香港","fullNumber":"+85268476749"}

        PhoneCountryVO phoneNumberInfo = PhoneUtil.getPhoneNumberInfo("+66 83 209 2640");
        System.out.println(phoneNumberInfo);

        // {"number":"16467879865","regionCode":"US","nationalNumber":6467879865,"countryCode":1,"description":"美国","fullNumber":"+16467879865"}
//        System.out.println(PhoneUtil.getPhoneNumberInfo("+16467879865").toJSONString());

        // The phone number maybe like:+[National Number][Phone number], but got +86155xxxx331
//        System.out.println(PhoneUtil.getPhoneNumberInfo("+86155xxxx331").toJSONString());
    }
}
