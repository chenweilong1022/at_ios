package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum UserStatusCode implements BaseEnum {

    UserStatusCode1(1,"E2EE_GROUP_TOO_MANY_MEMBERS"),
    UserStatusCode2(2,"Expected protocol id 82 but got 1f"),
    UserStatusCode3(3,"DB_FAILED"),
    UserStatusCode4(4,"Access token refresh"),
    UserStatusCode5(5,"unknown result"),
    UserStatusCode6(6,"Protocol.LineContact"),
    UserStatusCode7(7,"ABUSE_BLOCK"),
    UserStatusCode8(8,"EXCESSIVE_ACCESS"),
    UserStatusCode9(9,"Internal Server Error"),
    UserStatusCode10(10,"AUTHENTICATION_FAILED"),
    UserStatusCode11(11,"INVALID_STATE"),
    UserStatusCode12(12,"NOT_FOUND Reason"),
    UserStatusCode13(13,"issueLiffView failed: unknown result"),
    UserStatusCode14(14,"authentication error"),
    ;

    UserStatusCode(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private final int key;
    private final String value;


    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
