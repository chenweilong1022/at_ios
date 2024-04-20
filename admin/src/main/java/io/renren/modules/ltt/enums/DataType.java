

package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum DataType implements BaseEnum {

    DataType1(1,"料子"),
    DataType2(2,"水军"),
    DataType3(3,"加粉号"),
    DataType4(4,"筛水军"),
    ;

    DataType(int key, String value) {
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
