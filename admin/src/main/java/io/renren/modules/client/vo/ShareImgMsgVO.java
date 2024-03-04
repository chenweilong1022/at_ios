package io.renren.modules.client.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/1/30 01:54
 */
@Data
@Accessors(chain = true)
public class ShareImgMsgVO {
    private String msg;
    private long code;
}
