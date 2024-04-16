package io.renren.modules.client.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 23:19
 */
@Data
@Accessors(chain = true)
public class LineRegisterVO implements Serializable {
    private String msg;
    private long code;
    private DataLineRegisterVO data;
}
