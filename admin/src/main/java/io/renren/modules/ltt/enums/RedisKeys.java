

package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum RedisKeys implements BaseEnum {

    RedisKeys1(1,"IP_POOLS"),
    RedisKeys2(2,"PHONE_POOLS"),
    RedisKeys3(3,"IP_BLOCK_POOLS"),
    /**
     * ip失效
     */
    RedisKeys4(4,"IP_INVALID_POOLS"),
    /**
     * 注册代理类型
     */
    RedisKeys5(5,"REGISTER_PROXY_TYPE"),
    /**
     * 取ip的NX
     */
    RedisKeys6(6,"POPIP_NX"),
    /**
     * 存ip的NX
     */
    RedisKeys7(7,"PUSHIP_NX"),
    /**
     * 动态ip池
     */
    RedisKeys8(8,"DYNAMIC_IP_LIST"),
    /**
     * 静态ip池
     */
    RedisKeys9(9,"STATIC_IP_LIST"),
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
