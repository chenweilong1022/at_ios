package io.renren.modules.ltt.enums;


public enum FillUp {

    YES(0,"补充"),
    NO(1,"不补充"),
    ;

    FillUp(int key, String value) {
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
