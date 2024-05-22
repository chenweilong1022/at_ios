package io.renren.modules.ltt.enums;

import cn.hutool.core.util.ObjectUtil;
import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

/**
 * @author huyan
 * @date 2024/3/28
 */
@Getter
public enum AtUserGroupTypeEnum implements BaseEnum {
    /**
     * 账号分组类型
     */
    AtUserGroupType1(1, "普通组"),
    AtUserGroupType2(2, "改名组"),
    AtUserGroupType3(3, "终结组"),
    ;

    AtUserGroupTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    private final Integer key;
    private final String value;

    public static AtUserGroupTypeEnum checkType(Integer key) {
        if (ObjectUtil.isNull(key)) {
            return null;
        }
        for (AtUserGroupTypeEnum typeEnum : AtUserGroupTypeEnum.values()) {
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
