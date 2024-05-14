package io.renren.modules.ltt.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huyan
 * @date 2024/4/18
 */
@Data
public class CdPhoneFilterStatusDto implements Serializable {

    @ApiModelProperty(value = "成功数量")
    private Long successCount;

    @ApiModelProperty(value = "失败数量")
    private Long failCount;
}
