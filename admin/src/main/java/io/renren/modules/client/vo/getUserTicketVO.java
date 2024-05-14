package io.renren.modules.client.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/4/15 00:25
 */
@Data
@Accessors(chain = true)
public class getUserTicketVO {
    private String msg;
    private long code;
    private Data data;

    @lombok.Data
    public static class Data {
        private long expirationTime;
        private long maxUseCount;
        private String id;

    }
}
