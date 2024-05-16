package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum LockMapKeyResource implements BaseEnum {

    LockMapKeyResource1(1,"CdRegisterTaskEntity"),
    LockMapKeyResource2(2,"CdRegisterSubtasksEntity"),
    LockMapKeyResource3(3,"CdLineIpProxyEntity"),
    LockMapKeyResource4(4,"CdGetPhoneEntity"),
    LockMapKeyResource5(5,"CdLineRegisterEntity"),
    LockMapKeyResource6(6,"AtOrdersEntity"),
    LockMapKeyResource7(7,"AtDataSubtaskEntity"),
    LockMapKeyResource8(8,"AtGroupEntity"),
    LockMapKeyResource9(9,"AtUserEntity"),
    LockMapKeyResource10(10,"AtUserTokenEntity"),
    LockMapKeyResource11(11,"AtUserDataSummaryEntity"),
    LockMapKeyResource12(12,"AtGroupEntityUpdateName"),
    LockMapKeyResource13(13,"AtGroupChangeEntity"),
    LockMapKeyResource14(14,"CdLineIp"),
    LockMapKeyResource15(15,"HTTP_LOCK"),
    SAVE_WAIT_SMS_PHONE(16,"SAVE_WAIT_SMS_PHONE"),
    SAVE_WAIT_REGISTER_PHONE(17,"SAVE_WAIT_REGISTER_PHONE"),
    SAVE_REGISTER_FINISH_PHONE(18,"SAVE_REGISTER_FINISH_PHONE"),
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

    public static String getKeyByResource(LockMapKeyResource lockMapKeyResource,String id) {
        return String.format("%s_%s",lockMapKeyResource.getValue(),id);
    }

    public String getValue() {
        return value;
    }
}
