package io.renren.modules.client.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @date 2023-11-30 22:33:44
 */
@Data
@TableName("cd_get_phone")
@ApiModel("")
@Accessors(chain = true)
public class CdGetPhoneVO implements Serializable {
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
	private Integer subtasksId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Date createTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer lineRegisterId;

}
