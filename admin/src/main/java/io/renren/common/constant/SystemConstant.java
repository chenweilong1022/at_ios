package io.renren.common.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author huyan
 * @deprecated  常量池
 * @date 2024/3/18
 */
@Getter
@Component
public class SystemConstant {

    @Value(value = "${customer.roleId}")
    private Long CUSTOMER_ROLE_ID;

    @Value(value = "${magicServer.url}")
    private String MAGIC_SERVER_URL;

    @Value(value = "${jpSmsConfig.userCode}")
    private String jpSmsConfigUserCode;

    @Value(value = "${jpSmsConfig.interfaceKey}")
    private String jpSmsConfigInterfaceKey;

    @Value(value = "${jpSmsConfig.interfaceUrl}")
    private String jpSmsConfigInterfaceUrl;


}
