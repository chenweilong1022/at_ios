package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum AuditStatus implements BaseEnum {

    AuditStatus1(1,"审核中"),
    AuditStatus2(2,"审核失败"),
    AuditStatu3(3,"审核成功"),
    ;

    AuditStatus(int key, String value) {
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
