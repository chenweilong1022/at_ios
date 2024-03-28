package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum RegisterStatus implements BaseEnum {

    RegisterStatus1(1,"发起注册"),
    RegisterStatus2(2,"等待验证码"),
    RegisterStatus3(3,"提交验证码"),
    RegisterStatus4(4,"注册成功"),
    RegisterStatus5(5,"注册出现问题"),
    RegisterStatus7(7,"已经拉过群了"),
    RegisterStatus8(8,"账号被二次接码"),
    RegisterStatus9(9,"真机注册"),
    RegisterStatus10(10,"账号锁了"),
    ;

    RegisterStatus(Integer key, String value) {
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
