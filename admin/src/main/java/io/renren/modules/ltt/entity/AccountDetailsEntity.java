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
 * 流水明细表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-28 17:46:33
 */
@Data
@TableName("account_details")
@ApiModel("流水明细表")
@Accessors(chain = true)
public class AccountDetailsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 交易ID，唯一标识每笔交易
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="交易ID，唯一标识每笔交易")
	private Integer transactionId;
	/**
	 * 账户ID，关联账户余额表
	 */
	@ApiModelProperty(required=false,value="账户ID，关联账户余额表")
	private Integer accountId;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private Long sysUserId;
	/**
	 * 交易类型
	 */
	@ApiModelProperty(required=false,value="交易类型,枚举：AccountTransactionTypeEnum")
	private Integer transactionType;
	/**
	 * 交易金额，正数表示收入，负数表示支出
	 */
	@ApiModelProperty(required=false,value="交易金额，正数表示收入，负数表示支出")
	private BigDecimal amount;
	/**
	 * 交易状态，如1成功、0失败等
	 */
	@ApiModelProperty(required=false,value="交易状态，如1成功、0失败等 枚举：AccountTransactionStatusEnum")
	private Integer status;
	/**
	 * 交易描述，记录交易的详细信息
	 */
	@ApiModelProperty(required=false,value="交易描述，记录交易的详细信息")
	private String description;
	/**
	 * 交易日期和时间
	 */
	@ApiModelProperty(required=false,value="交易日期和时间")
	private Date transactionDate;
	/**
	 * 操作人
	 */
	@ApiModelProperty(required=false,value="操作人")
	private Long operationUserId;

}
