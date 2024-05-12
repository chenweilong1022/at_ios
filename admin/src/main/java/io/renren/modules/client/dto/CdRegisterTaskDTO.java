package io.renren.modules.client.dto;

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
 * @date 2023-12-02 00:31:50
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

	@ApiModelProperty(required=false,value="任务名称")
	private String taskName;
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
