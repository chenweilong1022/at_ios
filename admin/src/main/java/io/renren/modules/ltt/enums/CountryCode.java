

package io.renren.modules.ltt.enums;


import cn.hutool.core.util.ObjectUtil;
import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

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

    public static Integer getKeyByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        for (CountryCode typeEnum : CountryCode.values()) {
            if (typeEnum.getValue().equals(value)) {
                return typeEnum.getKey();
            }
        }
        return null;
    }

    public static String getValueByKey(Integer key) {
        if (ObjectUtil.isNull(key)) {
            return null;
        }
        for (CountryCode typeEnum : CountryCode.values()) {
            if (typeEnum.getKey().equals(key)) {
                return typeEnum.getValue();
            }
        }
        return null;
    }

    public static CountryCode getEnumByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        for (CountryCode typeEnum : CountryCode.values()) {
            if (typeEnum.getValue().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
