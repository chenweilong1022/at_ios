package io.renren.modules.ltt.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.utils.EnumUtil;
import io.renren.modules.ltt.enums.RegistrationStatus;
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
public class CdRegisterTaskVO implements Serializable {
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
	 * 代理模式
	 */
	@ApiModelProperty(required=false,value="")
	private Integer proxyIp;
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
	private Integer fillUpRegisterTaskId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Date createTime;
	public String getRegistrationStatusStr() {
		return EnumUtil.queryValueByKey(this.registrationStatus, RegistrationStatus.values());
	}

	public String getScheduleFloat() {
		double v = (double) (numberSuccesses + 0) / totalAmount * 100;
		return String.format("%.2f", v);
	}
}
