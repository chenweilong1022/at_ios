package io.renren.modules.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/12 15:11
 */
@Data
public class AutoAssignGroupsDTO {
    /**
     * 拉群限制
     */
    @ApiModelProperty(required=false,value="")
    private int accountGroupRestrictions;
    /**
     * 单个群需要多少人
     */
    @ApiModelProperty(required=false,value="")
    private int numberSingleGroups;
    /**
     * 拉群的国家账号限制
     */
    @ApiModelProperty(required=false,value="")
    private String countryCode;
    /**
     * 拉群id
     */
    Integer id;
}
