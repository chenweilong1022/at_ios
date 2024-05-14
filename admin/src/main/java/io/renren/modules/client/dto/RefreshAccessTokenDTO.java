package io.renren.modules.client.dto;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/8 12:36
 */
@Data
public class RefreshAccessTokenDTO {
    private String proxy;
    private String token;
}
