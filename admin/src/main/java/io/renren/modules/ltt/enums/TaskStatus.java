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
    TaskStatus7(7,"通过手机号搜索成功"),
    TaskStatus8(8,"通过mid添加成功"),
    TaskStatus9(9,"同步通讯录发起"),
    TaskStatus10(10,"通讯录同步成功"),
    TaskStatus11(11,"拉群成功"),
    TaskStatus12(12,"料子分组完成"),
    TaskStatus13(13,"暂停执行"),
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
