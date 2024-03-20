package io.renren.modules.ltt.dao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author huyan
 *  验活账户状态入参
 * @date 2024/3/20
 */
@Data
public class ValidateAtUserStatusParamDto implements Serializable {

    @ApiModelProperty("id集合")
    private List<Integer> ids;

    @ApiModelProperty(value="是否验活全部 true：全部")
    private Boolean validateFlag;
}
