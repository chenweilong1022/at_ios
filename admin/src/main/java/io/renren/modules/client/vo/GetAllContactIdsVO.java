package io.renren.modules.client.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/3/20 15:20
 */
@Data
@Accessors(chain = true)
public class GetAllContactIdsVO {
//    {"code":200,"msg":"","data":["ua1a39c9a55b897a525821529f67e2c0f"]}
    private String msg;
    private long code;
    private List<String> data;
}
