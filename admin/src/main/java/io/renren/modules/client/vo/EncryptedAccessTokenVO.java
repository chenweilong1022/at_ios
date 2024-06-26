package io.renren.modules.client.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/1/30 01:49
 */
@Data
@Accessors(chain = true)
public class EncryptedAccessTokenVO {
    private String msg;
    private long code;
    private Data data;
    @lombok.Data
    public static class Data {
        private String encryptedAccessToken;
    }
}
