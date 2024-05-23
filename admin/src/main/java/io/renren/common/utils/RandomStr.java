package io.renren.common.utils;

import cn.hutool.core.util.RandomUtil;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/5/23 17:12
 */
public class RandomStr {
    private static final String BASE_NUMBER = "0123456789";
    /** 用于随机选的字符 */
    private static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyz";
    private static final String BASE_CHAR_UP = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /** 用于随机选的字符和数字 */
    private static final String BASE_CHAR_NUMBER = BASE_CHAR + BASE_NUMBER + BASE_CHAR_UP;
    public static String randomString(int length) {
        return RandomUtil.randomString(BASE_CHAR_NUMBER,length);
    }
}
