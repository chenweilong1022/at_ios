

package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum RedisKeys implements BaseEnum {

    RedisKeys1(1,"IP_POOLS"),
    RedisKeys2(2,"PHONE_POOLS"),
    RedisKeys3(3,"IP_BLOCK_POOLS"),
    ;

    RedisKeys(int key, String value) {
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

    public String getValue(String country) {
        return value + country;
    }
}
