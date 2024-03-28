package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum LockMapKeyResource implements BaseEnum {

    LockMapKeyResource1(1,"CdRegisterTaskEntity"),
    LockMapKeyResource2(2,"CdRegisterSubtasksEntity"),
    LockMapKeyResource3(3,"CdLineIpProxyEntity"),
    LockMapKeyResource4(4,"CdGetPhoneEntity"),
    ;

    LockMapKeyResource(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private final int key;
    private final String value;


    public Integer getKey() {
        return key;
    }


    public static String getKeyByResource(LockMapKeyResource lockMapKeyResource,Integer id) {
        return String.format("%s_%s",lockMapKeyResource.getValue(),id);
    }

    public String getValue() {
        return value;
    }
}
