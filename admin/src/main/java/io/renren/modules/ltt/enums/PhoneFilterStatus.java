

package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum PhoneFilterStatus implements BaseEnum {

    PhoneFilterStatus1(1,"初始化"),
    PhoneFilterStatus2(2,"查询中"),
    PhoneFilterStatus3(3,"查询完成"),
    PhoneFilterStatus4(4,"查询失败"),
    ;

    PhoneFilterStatus(int key, String value) {
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
