package io.renren.modules.ltt.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.base.dto.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户ios token数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-31 00:16:44
 */
@Data
@TableName("at_user_token_ios")
@ApiModel("用户ios token数据")
@Accessors(chain = true)
public class AtUserTokenIosDeviceResultDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * deviceId
	 */
	@ApiModelProperty(required=false,value="deviceId")
	private String deviceId;

	@ApiModelProperty(required=false,value="deviceName")
	private String deviceName;

	@ApiModelProperty(required=false,value="数量")
	private Integer deviceCount;

}
