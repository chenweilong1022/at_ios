package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum GroupType implements BaseEnum {

    GroupType1(1,"搜索手机号加粉模式"),
    GroupType2(2,"日本，台湾专用uid模式1（加分号必须是日本）"),
    GroupType3(3,"无限加粉模式（加粉号专用）"),
    GroupType4(4,"日本，台湾专用uid模式2（加分号必须是日本）"),
    GroupType5(5,"同步通讯录模式（加粉号和数据必须是相同国家）"),
    GroupType6(6,"泰国号拉群，日本号同步通讯录合群"),
    ;

    GroupType(int key, String value) {
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

    public static GroupType getGroupTypeByKey(Integer key) {
        for (GroupType groupType : values()) {
            if (groupType.getKey().equals(key)) {
                return groupType;
            }
        }
        return null;
    }
}
