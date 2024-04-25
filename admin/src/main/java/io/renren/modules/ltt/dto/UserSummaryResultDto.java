package io.renren.modules.ltt.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author huyan
 * @date 2024/4/25
 */
@Data
public class UserSummaryResultDto implements Serializable {

    @ApiModelProperty("今日已使用数量")
    private Integer usedUserStock;

    @ApiModelProperty("今日在线数量")
    private Integer onlineUserNum;

}
