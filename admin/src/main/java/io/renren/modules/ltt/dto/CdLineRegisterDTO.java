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
import java.util.List;

/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:13
 */
@Data
@TableName("cd_line_register")
@ApiModel("")
@Accessors(chain = true)
public class CdLineRegisterDTO extends PageParam implements Serializable {
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
	private String ab;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String appVersion;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String countryCode;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String phone;
	@ApiModelProperty(required=false,value="")
	private List<String> phones;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String proxy;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer proxyStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String txtToken;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String taskId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String smsCode;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer registerStatus;
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
	@ApiModelProperty(required=false,value="")
	private String createStartTime;
	@ApiModelProperty(required=false,value="")
	private String createEndTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String token;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer getPhoneId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String pkey;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer subtasksId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer tasksId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer groupTaskId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer openStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer accountStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer groupCount;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer accountExistStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String errMsg;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer exportStatus;

}
