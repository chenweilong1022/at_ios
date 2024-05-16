package io.renren.modules.ltt.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.modules.ltt.entity.CdGetPhoneEntity;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:12
 */
@TableName("cd_get_phone")
@ApiModel("")
@Accessors(chain = true)
public class CdRegisterRedisDto implements Serializable {

    private String telPhone;

    private CdGetPhoneEntity phoneEntity;

    private CdLineRegisterEntity lineRegister;

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public CdGetPhoneEntity getPhoneEntity() {
        return phoneEntity;
    }

    public void setPhoneEntity(CdGetPhoneEntity phoneEntity) {
        this.phoneEntity = phoneEntity;
    }

    public CdLineRegisterEntity getLineRegister() {
        return lineRegister;
    }

    public void setLineRegister(CdLineRegisterEntity lineRegister) {
        this.lineRegister = lineRegister;
    }
}
