

package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum MaterialPhoneType implements BaseEnum {

    MaterialType1(1,"拉群料子"),
    MaterialType2(2,"私信料子"),
    ;

    MaterialPhoneType(int key, String value) {
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
