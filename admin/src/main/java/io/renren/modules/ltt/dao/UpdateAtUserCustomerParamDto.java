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
public class UpdateAtUserCustomerParamDto implements Serializable {

    @ApiModelProperty("id集合")
    private List<Integer> ids;

    /**
     * 所属客服id
     */
    @ApiModelProperty(required=false,value="所属客服id")
    private Integer customerServiceId;
    /**
     * 所属客服
     */
    @ApiModelProperty(required=false,value="所属客服")
    private String customerService;
}
