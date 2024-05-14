package io.renren.modules.ltt.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huyan
 * @date 2024/4/25
 */
@Data
public class LineRegisterSummaryResultDto implements Serializable {
    @ApiModelProperty(required=false,value="国家")
    private String countryCode;

    @ApiModelProperty("库存")
    private Integer registerStock;

    @ApiModelProperty("今日注册数量")
    private Integer registerNum;
}
