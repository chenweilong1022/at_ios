

package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum CountryCode implements BaseEnum {

    CountryCode1(66,"th"),
    CountryCode2(886,"tw"),
    CountryCode3(81,"jp"),
    CountryCode4(82,"kr"),
    CountryCode5(852,"hk"),
    CountryCode6(855,"kh"),
    ;

    CountryCode(int key, String value) {
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
