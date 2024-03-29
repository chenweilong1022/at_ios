package io.renren.modules.ltt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 账户余额表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-28 13:19:55
 */
@Data
@TableName("account_balance")
@ApiModel("账户余额表")
@Accessors(chain = true)
public class AccountBalanceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 账户ID，唯一标识每个账户
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="账户ID，唯一标识每个账户")
	private Integer accountId;
	/**
	 * 用户ID，标识账户所属的用户
	 */
	@ApiModelProperty(required=false,value="用户ID，标识账户所属的用户")
	private Long sysUserId;
	/**
	 * 可用余额
	 */
	@ApiModelProperty(required=false,value="可用余额")
	private BigDecimal balance;
	/**
	 * 记录生成日期和时间
	 */
	@ApiModelProperty(required=false,value="记录生成日期和时间")
	private Date createTime;
	/**
	 * 最后更新日期和时间
	 */
	@ApiModelProperty(required=false,value="最后更新日期和时间")
	private Date updateTime;
	/**
	 * 操作人
	 */
	@ApiModelProperty(required=false,value="操作人")
	private Long operationUserId;

}
