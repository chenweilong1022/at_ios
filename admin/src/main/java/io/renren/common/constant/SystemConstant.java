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

    /**
     * 当前服务器
     */
    @Value(value = "${servers.mod}")
    private Integer SERVERS_MOD;
    /**
     * 总服务器数量
     */
    @Value(value = "${servers.total-mod}")
    private Integer SERVERS_TOTAL_MOD;

    @Value(value = "${magicServer.url}")
    private String MAGIC_SERVER_URL;

    @Value(value = "${jpSmsConfig.userCode}")
    private String jpSmsConfigUserCode;

    @Value(value = "${jpSmsConfig.interfaceKey}")
    private String jpSmsConfigInterfaceKey;

    @Value(value = "${jpSmsConfig.interfaceUrl}")
    private String jpSmsConfigInterfaceUrl;

    @Value(value = "${ipConfig.maxUsedCount}")
    private Integer ipConfigMaxUsedCount;


}
