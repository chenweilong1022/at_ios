

package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum ProxyStatus implements BaseEnum {

    ProxyStatus1(1,"lunaproxy"),
    ProxyStatus2(2,"ip2world"),
    ProxyStatus3(3,"静态代理"),
    ;

    ProxyStatus(int key, String value) {
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
