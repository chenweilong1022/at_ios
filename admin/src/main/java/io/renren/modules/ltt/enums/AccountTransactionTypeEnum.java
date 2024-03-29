package io.renren.modules.ltt.enums;

import cn.hutool.core.util.ObjectUtil;
import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

/**
 * @author huyan
 * @date 2024/3/28
 */
@Getter
public enum AccountTransactionTypeEnum implements BaseEnum {
    /**
     * 账户交易类型：1充值
     */
    RECHARGE(1, "充值"),
    //todo 暂时未定义具体消费项目，以后再改
    CONSUMPTION(2, "消费"),
    ;

    AccountTransactionTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private final Integer key;
    private final String value;

    public static AccountTransactionTypeEnum checkType(Integer key) {
        if (ObjectUtil.isNull(key)) {
            return null;
        }
        for (AccountTransactionTypeEnum typeEnum : AccountTransactionTypeEnum.values()) {
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
