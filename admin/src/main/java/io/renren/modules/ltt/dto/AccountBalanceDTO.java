package io.renren.modules.ltt.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.base.dto.PageParam;
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
public class AccountBalanceDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID，标识账户所属的用户
	 */
	@ApiModelProperty(required=false,value="用户ID，标识账户所属的用户")
	private Long sysUserId;

	/**
	 * 交易类型
	 */
	@ApiModelProperty(required=false,value="交易类型")
	private Integer transactionType;

	/**
	 * 交易金额，正数表示收入，负数表示支出
	 */
	@ApiModelProperty(required=false,value="交易金额")
	private BigDecimal amount;

	/**
	 * 交易描述，记录交易的详细信息
	 */
	@ApiModelProperty(required=false,value="交易描述，记录交易的详细信息")
	private String description;

	/**
	 * 操作人
	 */
	@ApiModelProperty(required=false,value="操作人")
	private Long operationUserId;

}
