package io.renren.modules.ltt.dao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huyan
 * @date 2024/3/20
 */
@Data
public class UpdateUserGroupParamDto implements Serializable {

    @ApiModelProperty("id集合")
    private List<Integer> ids;

    @ApiModelProperty("账户分组")
    private Integer userGroupId;

    @ApiModelProperty(required=false,value="分组名称")
    private String userGroupName;
}
