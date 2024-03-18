package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum TaskStatus implements BaseEnum {

    TaskStatus0(0,"任务未启动"),
    TaskStatus1(1,"初始化任务"),
    TaskStatus2(2,"任务进行中"),
    TaskStatus3(3,"任务完成"),
    TaskStatus4(4,"子任务成功"),
    TaskStatus5(5,"子任务失败"),
    TaskStatus6(6,"提交头像修改"),
    ;

    TaskStatus(int key, String value) {
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
