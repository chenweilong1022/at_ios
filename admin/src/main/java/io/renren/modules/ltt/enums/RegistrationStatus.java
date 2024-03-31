package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum RegistrationStatus implements BaseEnum {

    RegistrationStatus1(1,"新注册"),
    RegistrationStatus2(2,"注册中"),
    RegistrationStatus3(3,"暂停注册"),
    RegistrationStatus4(4,"注册异常"),
    RegistrationStatus5(5,"验证码提交完成"),
    RegistrationStatus6(6,"子任务保存完成"),
    RegistrationStatus7(7,"注册完成"),
    RegistrationStatus9(9,"真机注册任务"),
    ;

    RegistrationStatus(int key, String value) {
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
