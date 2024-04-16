package io.renren.modules.client.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/1 02:16
 */
@Data
@Accessors(chain = true)
public class RegisterResultVO {
    private Long code;
    private String msg;
    private RegisterResultVOData data;
}
