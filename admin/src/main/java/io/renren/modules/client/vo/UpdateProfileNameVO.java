package io.renren.modules.client.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/3/20 22:57
 */
@Data
@Accessors(chain = true)
public class UpdateProfileNameVO {
    private String msg;
    private long code;
}
