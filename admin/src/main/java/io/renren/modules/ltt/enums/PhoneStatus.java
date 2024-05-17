package io.renren.modules.ltt.enums;


public enum PhoneStatus {

    PhoneStatus1(1,"未使用"),
    PhoneStatus2(2,"line使用"),
    PhoneStatus3(3,"收到验证码"),
    PhoneStatus4(4,"line提交了验证码"),
    PhoneStatus5(5,"需要释放手机号"),
    PhoneStatus6(6,"释放完成"),
    PhoneStatus7(7,"作废"),
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
