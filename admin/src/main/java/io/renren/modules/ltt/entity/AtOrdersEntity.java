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
 * 订单表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-29 17:15:12
 */
@Data
@TableName("at_orders")
@ApiModel("订单表")
@Accessors(chain = true)
public class AtOrdersEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 订单ID
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="订单ID")
	private Integer orderId;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(required=false,value="用户ID")
	private Long sysUserId;
	/**
	 * 订单状态（待处理，处理中，已完成）
	 */
	@ApiModelProperty(required=false,value="订单状态（待处理，处理中，已完成）")
	private Integer orderStatus;
	/**
	 * 订单总金额
	 */
	@ApiModelProperty(required=false,value="订单总金额")
	private BigDecimal totalAmount;
	/**
	 * 订单创建时间
	 */
	@ApiModelProperty(required=false,value="订单创建时间")
	private Date orderTime;
	/**
	 * 订单最后更新时间
	 */
	@ApiModelProperty(required=false,value="订单最后更新时间")
	private Date updateTime;
	/**
	 * 订单备注
	 */
	@ApiModelProperty(required=false,value="订单备注")
	private String notes;
	/**
	 * 商品id
	 */
	@ApiModelProperty(required=false,value="商品id")
	private Integer productId;
	/**
	 * 商品类型
	 */
	@ApiModelProperty(required=false,value="商品类型")
	private Integer productType;
	/**
	 * 国家code
	 */
	@ApiModelProperty(required=false,value="国家code")
	private Integer countryCode;
	/**
	 * 购买数量
	 */
	@ApiModelProperty(required=false,value="购买数量")
	private Integer orderNumber;
	@ApiModelProperty(required=false,value="成功数量")
	private Integer successNumber;
	@ApiModelProperty(required=false,value="商品名称")
	private String productName;

}
