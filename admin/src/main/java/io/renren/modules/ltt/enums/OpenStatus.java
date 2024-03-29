

package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum OpenStatus implements BaseEnum {

    OpenStatus1(1,"未打开"),
    OpenStatus2(2,"打开中"),
    OpenStatus3(3,"已打开"),
    OpenStatus4(4,"打开失败"),
    OpenStatus5(5,"需要更新token"),
    OpenStatus6(6,"二次失败"),
    ;

    OpenStatus(int key, String value) {
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
