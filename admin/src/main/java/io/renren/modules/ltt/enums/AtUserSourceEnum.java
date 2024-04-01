package io.renren.modules.ltt.enums;

import cn.hutool.core.util.ObjectUtil;
import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

/**
 * @author huyan
 * @date 2024/3/28
 */
@Getter
public enum AtUserSourceEnum implements BaseEnum {
    /**
     * 账号来源协议 1协议 2真机
     */
    AtUserSource1(1, "协议"),
    AtUserSource2(2, "真机"),
    ;

    AtUserSourceEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private final Integer key;
    private final String value;

    public static AtUserSourceEnum checkType(Integer key) {
        if (ObjectUtil.isNull(key)) {
            return null;
        }
        for (AtUserSourceEnum typeEnum : AtUserSourceEnum.values()) {
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
