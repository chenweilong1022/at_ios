package io.renren.modules.ltt.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.utils.EnumUtil;
import io.renren.modules.ltt.entity.CdLineRegisterEntity;
import io.renren.modules.ltt.enums.CountryCode;
import io.renren.modules.ltt.enums.PhoneStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

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
@TableName("cd_line_register")
@ApiModel("")
@Accessors(chain = true)
public class CdLineRegisterVO implements Serializable {
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
	@ApiModelProperty(required=false,value="数字代码")
	private Integer country;

	@ApiModelProperty(required=false,value="")
	private String countryCode;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String phone;
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

	/**
	 *首次取号时间
	 */
	@ApiModelProperty(required=false,value="")
	private Date firstEnterTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Date openTime;
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

	@ApiModelProperty(required=false,value="卡次数")
	private Integer registerCount;

	@ApiModelProperty(required=false,value="手机号使用状态")
	private Boolean phoneState;

	@ApiModelProperty(required=false,value="")
	private Integer phoneStatus;


	@ApiModelProperty(value = "重试次数")
	private Integer retryNum;

	private CdLineRegisterEntity cdLineRegisterEntity;

	public String getPhoneStatusStr() {
		return EnumUtil.queryValueByKey(this.phoneStatus, PhoneStatus.values());
	}

	public String getCountryCode() {
		if (StringUtils.isNotEmpty(this.countryCode)) {
			return this.countryCode;
		} else if (this.country != null) {
			return EnumUtil.queryValueByKey(this.country, CountryCode.values());
		}
		return null;
	}
}
