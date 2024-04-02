package io.renren.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * @author huyan
 * @date 2024/4/2
 */
@Slf4j
public class Md5Utils {

    public static String generateMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("generateMD5_error {}", e);
            return null;
        }
    }

}
