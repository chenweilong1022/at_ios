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

}