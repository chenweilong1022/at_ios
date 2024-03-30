package io.renren.modules.ltt.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.base.dto.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:13
 */
@Data
@TableName("cd_register_task")
@ApiModel("")
@Accessors(chain = true)
public class CdRegisterTaskOrderDTO  implements Serializable {

	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="")
	private Integer taskId;

	@ApiModelProperty(required=false,value="注册状态")
	private Integer registrationStatus;

	@ApiModelProperty(required=false,value="注册子任务id")
	private Integer subtasksId;

	@ApiModelProperty(required=false,value="line注册id")
	private Integer lineRegisterId;

	@ApiModelProperty(required=false,value="token")
	private Integer lineRegisterToken;
}
