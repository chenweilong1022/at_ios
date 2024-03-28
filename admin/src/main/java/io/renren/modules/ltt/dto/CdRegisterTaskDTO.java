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
public class CdRegisterTaskDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="")
	private Integer id;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer totalAmount;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer numberThreads;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer numberRegistered;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer numberSuccesses;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer numberFailures;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer countryCode;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer fillUp;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer registrationStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer deleteFlag;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Date createTime;

}
