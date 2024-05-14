package io.renren.modules.client.dto;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/1/29 22:02
 */
@Data
public class CreateThreadDTO {
    private String toMid;
    private String token;
    private String proxy;
}
