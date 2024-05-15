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
@Data
@TableName("cd_get_phone")
@ApiModel("")
@Accessors(chain = true)
public class CdRegisterRedisDto implements Serializable {

    private String telPhone;

    /**
     * 验证码是否发送成功
     */
    private Boolean smsFlag;

    private CdGetPhoneEntity phoneEntity;

    private CdLineRegisterEntity lineRegister;

}
