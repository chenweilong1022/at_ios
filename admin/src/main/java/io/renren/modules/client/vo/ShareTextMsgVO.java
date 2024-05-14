package io.renren.modules.client.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/1/29 22:46
 */
@Data
@Accessors(chain = true)
public class ShareTextMsgVO {
    private String msg;
    private long code;
    private Data data;

    @lombok.Data
    public static class Data {
        private long appExtensionType;
        private long relatedMessageServiceCode;
        private String relatedMessageId;
        private List<String> chunks;
        private String to;
        private String fromMid;
        private long contentType;
        private long messageRelationType;
    }
}
