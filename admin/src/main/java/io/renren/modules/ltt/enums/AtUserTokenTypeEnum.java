package io.renren.modules.ltt.enums;

import cn.hutool.core.util.ObjectUtil;
import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

/**
 * @author huyan
 * @date 2024/3/28
 */
@Getter
public enum AtUserTokenTypeEnum implements BaseEnum {
    /**
     * token类型 1协议token 2真机token
     */
    AtUserTokenType1(1, "协议token"),
    AtUserTokenType2(2, "真机token"),
    ;

    AtUserTokenTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private final Integer key;
    private final String value;

    public static AtUserTokenTypeEnum checkType(Integer key) {
        if (ObjectUtil.isNull(key)) {
            return null;
        }
        for (AtUserTokenTypeEnum typeEnum : AtUserTokenTypeEnum.values()) {
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
