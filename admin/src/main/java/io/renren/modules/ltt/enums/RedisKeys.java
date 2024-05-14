

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
    /**
     * 卡次数
     */
    RedisKeys10(10,"PHONE_REGISTER"),
    /**
     * 水军
     */
    RedisKeys11(11,"PHONE_REGISTER"),

    /**
     * 暂时不可用手机号池
     */
    RedisKeys12(12,"PHONE_INVALID"),
    /**
     * 用户任务池
     */
    RedisKeys13(13,"USER_TASKS_WORK"),
    /**
     * 加粉任务词
     */
    RedisKeys14(14,"USER_TASKS_WORK_FINISH"),
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
