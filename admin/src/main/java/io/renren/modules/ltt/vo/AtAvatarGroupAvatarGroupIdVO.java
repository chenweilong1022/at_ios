package io.renren.modules.ltt.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AtAvatarGroupAvatarGroupIdVO {

    @ApiModelProperty(value = "可用数量")
    private Integer avatarGroupIdCount;
    private Integer avatarGroupId;
}
