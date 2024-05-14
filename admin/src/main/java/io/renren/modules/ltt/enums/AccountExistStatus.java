

package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum AccountExistStatus implements BaseEnum {

    AccountExistStatus1(1,"首次卡"),
    AccountExistStatus2(2,"二次卡"),
    ;

    AccountExistStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private final Integer key;
    private final String value;


    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
