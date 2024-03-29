package io.renren.modules.ltt.enums;

import cn.hutool.core.util.ObjectUtil;
import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

/**
 * @author huyan
 * @date 2024/3/28
 */
@Getter
public enum AccountTransactionStatusEnum implements BaseEnum {
    /**
     * 账户交易状态，如1成功、0失败等
     */
    FAIL_STATUS(0, "失败"),
    SUCCESS_STATUS(1, "成功"),
    ;

    AccountTransactionStatusEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private final Integer key;
    private final String value;

    public static AccountTransactionStatusEnum checkType(Integer key) {
        if (ObjectUtil.isNull(key)) {
            return null;
        }
        for (AccountTransactionStatusEnum typeEnum : AccountTransactionStatusEnum.values()) {
            if (typeEnum.getKey().equals(key)) {
                return typeEnum;
            }
        }
        return null;
    }


    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
