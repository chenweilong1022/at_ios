package io.renren.modules.client.dto;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/1/30 01:53
 */
@Data
public class ShareImgMsgDTO {
    private String proxy;
    private String imgData;
    private String encryptedAccessToken;
    private String toMid;
    private String token;
}
