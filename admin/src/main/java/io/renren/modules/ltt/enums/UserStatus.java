package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum UserStatus implements BaseEnum {

    UserStatus1(1,"未验证"),
    UserStatus2(2,"封号"),
    UserStatus3(3,"下线"),
    UserStatus4(4,"在线"),
    ;

    UserStatus(int key, String value) {
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
