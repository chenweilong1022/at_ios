package io.renren.modules.client.dto;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/3/11 23:04
 */
@Data
public class UpdateProfileImageDTO {
    private String proxy;
    private String image_base_64;
    private String token;
}
