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
public class ConversionAppTokenVO {
    private String msg;
    private long code;
    private String token;
}
