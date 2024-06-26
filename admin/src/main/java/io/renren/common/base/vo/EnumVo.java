package io.renren.common.base.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 枚举类输出前端的实体类
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2019/6/18
 */
@Data
@Accessors(chain = true)
public class EnumVo {
    private Integer key;
    private String value;
    private String value2;
    private List<EnumVo> children;
}
