package io.renren.common.utils;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huyan
 * @date 2024/5/10
 */
public class StrTextUtil {


    /**
     * 校验是否是验证码
     * @param smsText
     * @return
     */
    public static Boolean verificationCodeFlag(String smsText) {
        if (StringUtils.isEmpty(smsText)) {
            return false;
        }
        // 使用正则表达式匹配短信内容中的验证码
        Pattern pattern = Pattern.compile("\\d{6}"); // 此处使用六位数字作为验证码的示例
        Matcher matcher = pattern.matcher(smsText);
        if (matcher.find() && StringUtils.isNotEmpty(matcher.group())) {
            return true;
        }
        return false;
    }
}
