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
import java.math.BigDecimal;
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
@ApiModel("购买token入参")
@Accessors(chain = true)
public class AtOrdersTokenParamDTO implements Serializable {

	/**
	 * 用户ID
	 */
	@ApiModelProperty(required=false,value="用户ID")
	private Long sysUserId;

	/**
	 * 商品id
	 */
	@ApiModelProperty(required=false,value="商品id")
	private Integer productId;
//	/**
//	 * 商品类型
//	 */
//	@ApiModelProperty(required=false,value="商品类型")
//	private Integer productType;
//	/**
//	 * 国家code
//	 */
//	@ApiModelProperty(required=false,value="国家code")
//	private Integer countryCode;
	/**
	 * 购买数量
	 */
	@ApiModelProperty(required=false,value="购买数量")
	private Integer orderNumber;

}
