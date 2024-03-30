package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum OrderStatus implements BaseEnum {
    /**
     * 订单状态（待处理，处理中，已完成）
     */
    OrderStatus1(1,"待处理"),
    OrderStatus2(2,"处理中"),
    OrderStatus3(3,"已完成"),
    ;

    OrderStatus(Integer key, String value) {
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
