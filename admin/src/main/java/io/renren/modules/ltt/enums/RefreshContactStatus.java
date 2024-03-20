package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum RefreshContactStatus implements BaseEnum {

    RefreshContactStatus1(1,"未刷新联系人"),
    RefreshContactStatus2(2,"刷新中"),
    RefreshContactStatus3(3,"已刷新"),
    ;

    RefreshContactStatus(int key, String value) {
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
