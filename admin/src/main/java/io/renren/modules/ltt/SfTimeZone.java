

package io.renren.modules.ltt;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum SfTimeZone implements BaseEnum {

    SfTimeZone1(1,"中国时间"),
    SfTimeZone2(2,"日本时间"),
    ;

    SfTimeZone(int key, String value) {
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
