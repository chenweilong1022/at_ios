package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum PhoneStatus implements BaseEnum {

    PhoneStatus1(1,"待处理"),
    PhoneStatus2(2,"发起注册"),
    PhoneStatus3(3,"收到验证码"),
    PhoneStatus4(4,"提交验证码"),
    PhoneStatus5(5,"需要释放手机号"),
    PhoneStatus6(6,"注册出现问题"),
    PhoneStatus7(7,"作废"),
    PhoneStatus8(8,"注册完成"),
    PhoneStatus9(9,"已购买"),
    ;

    PhoneStatus(int key, String value) {
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
