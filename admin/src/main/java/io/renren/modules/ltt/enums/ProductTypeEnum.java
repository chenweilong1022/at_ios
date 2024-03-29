package io.renren.modules.ltt.enums;

import cn.hutool.core.util.ObjectUtil;
import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

/**
 * @author huyan
 * @date 2024/3/28
 */
@Getter
public enum ProductTypeEnum implements BaseEnum {
    /**
     * 商品类型
     */
    TOKEN(1, "token"),
    PORT(2, "端口"),
    PULL_GROUP(3, "拉群"),
    ;

    ProductTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private final Integer key;
    private final String value;

    public static ProductTypeEnum checkType(Integer key) {
        if (ObjectUtil.isNull(key)) {
            return null;
        }
        for (ProductTypeEnum typeEnum : ProductTypeEnum.values()) {
            if (typeEnum.getKey().equals(key)) {
                return typeEnum;
            }
        }
        return null;
    }


    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
