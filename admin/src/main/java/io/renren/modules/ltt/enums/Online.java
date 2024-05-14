package io.renren.modules.ltt.enums;

import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum Online implements BaseEnum{

    YES(0,"在线"),
    NO(1,"离线"),
    ;

    Online(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private final int key;
    private final String value;

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
