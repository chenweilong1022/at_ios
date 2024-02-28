package io.renren.modules.ltt.enums;


public enum UseFlag {

    NO(1,"未使用"),
    YES(0,"已使用"),
    ;

    UseFlag(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private final int key;
    private final String value;


    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
