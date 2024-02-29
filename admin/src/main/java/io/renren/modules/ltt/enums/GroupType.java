package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum GroupType implements BaseEnum {

    GroupType1(1,"手机号"),
    GroupType2(2,"不封控地区普通uid模式"),
    GroupType3(3,"自定义id"),
    GroupType4(4,"日本，台湾专用uid模式"),
    ;

    GroupType(int key, String value) {
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
