package io.renren.modules.ltt.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AtUsernameGroupUsernameCountGroupIdVO {
    /**
     * 昵称分组id
     */
    @ApiModelProperty(required=false,value="昵称分组id")
    private Integer usernameGroupId;
    /**
     * 昵称分组数量
     */
    @ApiModelProperty(required=false,value="昵称分组数量")
    private Integer usernameGroupIdCount;
}
