package io.renren.modules.client.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/1/29 21:53
 */
@Data
public class CanSendListByGroupTaskIdVO {
    /**
     *
     */
    @ApiModelProperty(required=false,value="")
    private Integer groupTaskId;
    /**
     *
     */
    @ApiModelProperty(required=false,value="")
    private Integer materialPhoneId;
}
