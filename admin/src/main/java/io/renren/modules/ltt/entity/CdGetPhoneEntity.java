package io.renren.modules.ltt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 12:36:12
 */

@TableName("cd_get_phone")
@ApiModel("")
@Accessors(chain = true)
@Getter
@Setter
public class CdGetPhoneEntity implements Serializable {
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
	private String number;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String pkey;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String time;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String country;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String countrycode;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String other;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String com;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String phone;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String code;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer phoneStatus;
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
	/**
	 *首次取号时间
	 */
	@ApiModelProperty(required=false,value="")
	private Date firstEnterTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer subtasksId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer lineRegisterId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String sfApi;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer timeZone;

	@ApiModelProperty(value = "重试次数")
	private Integer retryNum;
}
