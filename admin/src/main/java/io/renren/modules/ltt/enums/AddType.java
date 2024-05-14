

package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum AddType implements BaseEnum {

    AddType1(1,"搜索添加"),
    AddType2(2,"通讯录同步"),
    AddType3(3,"筛选料子"),
    AddType4(4,"mid添加"),
    ;

    AddType(int key, String value) {
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
