

package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum IpTypeEnum implements BaseEnum {

    /**
     * ip类型 1动态ip 2固定ip
     */
    IpType1(1,"动态ip"),
    IpType2(2,"固定ip"),
    ;

    IpTypeEnum(int key, String value) {
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
