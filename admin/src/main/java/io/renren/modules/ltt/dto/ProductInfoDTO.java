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
 * 商品信息表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-28 11:39:25
 */
@Data
@TableName("product_info")
@ApiModel("商品信息表")
@Accessors(chain = true)
public class ProductInfoDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="")
	private Integer productId;
	/**
	 * 商品名称
	 */
	@ApiModelProperty(required=false,value="商品名称")
	private String productName;
	/**
	 * 商品描述
	 */
	@ApiModelProperty(required=false,value="商品描述")
	private String description;
	/**
	 * 商品价格
	 */
	@ApiModelProperty(required=false,value="商品价格")
	private BigDecimal price;
	/**
	 * 库存数量
	 */
	@ApiModelProperty(required=false,value="库存数量")
	private Integer stockQuantity;
	/**
	 * 所属分类
	 */
	@ApiModelProperty(required=false,value="所属分类")
	private Integer categoryType;
	/**
	 * 商品图片URL地址
	 */
	@ApiModelProperty(required=false,value="商品图片URL地址")
	private String imageUrl;
	/**
	 * 商品状态（1上架 0下架）
	 */
	@ApiModelProperty(required=false,value="商品状态（1上架 0下架）")
	private Integer status;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(required=false,value="创建时间")
	private Date createTime;
	/**
	 * 最后更新时间
	 */
	@ApiModelProperty(required=false,value="最后更新时间")
	private Date updateTime;

}
