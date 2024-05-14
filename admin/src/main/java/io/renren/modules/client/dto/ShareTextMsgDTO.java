package io.renren.modules.client.dto;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/1/29 22:46
 */
@Data
public class ShareTextMsgDTO {
    private String proxy;
    private String msgText;
    private String negotiatePublicKeyBase64;
    private long negotiatePublicKeyId;
    private String toMid;
    private String token;
}
