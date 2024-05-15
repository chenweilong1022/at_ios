

package io.renren.modules.ltt.enums;


import io.renren.common.base.interfaces.BaseEnum;
import lombok.Getter;

@Getter
public enum RedisKeys implements BaseEnum {

    RedisKeys1(1,"IP_POOLS"),
    RedisKeys2(2,"PHONE_POOLS"),
    RedisKeys3(3,"IP_BLOCK_POOLS"),
    /**
     * ip失效
     */
    RedisKeys4(4,"IP_INVALID_POOLS"),
    /**
     * 注册代理类型
     */
    RedisKeys5(5,"REGISTER_PROXY_TYPE"),
    /**
     * 取ip的NX
     */
    RedisKeys6(6,"POPIP_NX"),
    /**
     * 存ip的NX
     */
    RedisKeys7(7,"PUSHIP_NX"),
    /**
     * 动态ip池
     */
    RedisKeys8(8,"DYNAMIC_IP_LIST"),
    /**
     * 静态ip池
     */
    RedisKeys9(9,"STATIC_IP_LIST"),
    /**
     * 卡次数
     */
    RedisKeys10(10,"PHONE_REGISTER"),
    /**
     * 水军
     */
    RedisKeys11(11,"PHONE_REGISTER"),

    /**
     * 暂时不可用手机号池
     */
    RedisKeys12(12,"PHONE_INVALID"),
    /**
     * 用户任务池
     */
    USER_TASKS_POOL(13,"USER_TASKS_POOL"),
    /**
     * 加粉结束
     */
    USER_TASKS_WORK_FINISH(14,"USER_TASKS_WORK_FINISH"),
    /**
     * 加粉任务池子进行中
     */
    USER_TASKS_WORKING(15,"USER_TASKS_WORKING"),
    /**
     * 加粉任务锁{"@class":"io.renren.modules.ltt.entity.AtDataSubtaskEntity","id":1029455,"dataTaskId":11596,"userId":26790,"changeUserId":null,"taskStatus":5,"groupType":2,"luid":null,"contactType":null,"contactKey":"66648959184","mid":"ub2a3ced178a6aee28878b749d1f10714","createdTime":null,"type":null,"status":null,"relation":null,"displayName":"Emily-Binance","phoneticName":null,"pictureStatus":null,"thumbnailUrl":null,"statusMessage":null,"displayNameOverridden":null,"favoriteTime":null,"capableVoiceCall":null,"capableVideoCall":null,"capableMyhome":null,"capableBuddy":null,"attributes":null,"settings":null,"picturePath":null,"recommendpArams":null,"friendRequestStatus":null,"musicProfile":null,"videoProfile":null,"deleteFlag":1,"createTime":["cn.hutool.core.date.DateTime",1715701804353],"updateTime":null,"lineTaskId":null,"msg":"添加好友异常: TalkException({Code:AUTHENTICATION_FAILED Reason: ParameterMap:map[]})","refreshContactStatus":null,"sysUserId":null,"dataType":2,"groupId":15387,"recordId":null,"openApp":1,"mod":null,"totalMod":null}
     */
    USER_TASKS_WORKING_NX(16,"USER_TASKS_WORKING_NX"),
    /**
     * 加粉任务状态队列
     */
    ATDATATASKENTITY_LIST(17,"ATDATATASKENTITY_LIST"),
    /**
     * 群任务队列
     */
    ATGROUPENTITY_LIST(18,"ATGROUPENTITY_LIST"),
    /**
     * 用户任务队列
     */
    ATUSERENTITY_LIST(19,"ATUSERENTITY_LIST"),
    /**
     * 加粉任务结束锁
     */
    USER_TASKS_WORK_FINISH_NX(20,"USER_TASKS_WORK_FINISH_NX"),
    /**
     * 任务服务器锁
     */
    MOD_NX(21,"MOD_NX"),
    /**
     * 清理任务锁
     */
    USER_TASKS_WORKING_CLEAN_NX(22,"USER_TASKS_WORKING_CLEAN_NX"),
    /**
     * 待发起注册手机号
     */
    WAIT_START_REGISTER_PHONE(23,"WAIT_START_REGISTER_PHONE"),
    /**
     * 待获取验证码手机号
     */
    SAVE_WAIT_SMS_PHONE(24,"SAVE_WAIT_SMS_PHONE"),
    /**
     * 待获取验证码手机号
     */
    WAIT_SMS_PHONE(25,"WAIT_SMS_PHONE"),
    /**
     * 保存已获取到验证码数据
     */
    SAVE_WAIT_REGISTER_PHONE(26,"SAVE_WAIT_REGISTER_PHONE"),
    /**
     * 已获取到验证码，发起注册数据
     */
    WAIT_REGISTER_PHONE(27,"WAIT_REGISTER_PHONE"),
    ;

    RedisKeys(int key, String value) {
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

    public String getValue(String country) {
        return value + country;
    }
}
