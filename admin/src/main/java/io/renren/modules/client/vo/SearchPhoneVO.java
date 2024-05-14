package io.renren.modules.client.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/3 16:35
 */
@Data
@Accessors(chain = true)
public class SearchPhoneVO {
    private String msg;
    private long code;
    private Map<String, The818051863582> data;
}
