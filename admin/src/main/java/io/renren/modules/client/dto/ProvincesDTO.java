package io.renren.modules.client.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.base.dto.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 省份信息表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2023-06-08 11:35:41
 */
@Data
@TableName("provinces")
@ApiModel("省份信息表")
@Accessors(chain = true)
public class ProvincesDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="")
	private Integer id;
	/**
	 * 省
	 */
	@ApiModelProperty(required=false,value="省")
	private String provinceId;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private String province;

}
