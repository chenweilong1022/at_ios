package io.renren.modules.ltt.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:13
 */
@Data
@Accessors(chain = true)
public class CdLineRegisterSummaryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ApiModelProperty(required = false, value = "注册成功数量（可购买的）")
    private Integer successRegisterCount;
    /**
     *
     */
    @ApiModelProperty(required = false, value = "注册中数量（待处理、发起注册、验证码）")
    private String waitRegisterCount;

}
