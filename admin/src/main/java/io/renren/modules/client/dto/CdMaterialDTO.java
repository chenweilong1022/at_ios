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
import java.util.Date;

/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2023-12-12 12:30:34
 */
@Data
@TableName("cd_material")
@ApiModel("")
@Accessors(chain = true)
public class CdMaterialDTO extends PageParam implements Serializable {
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
	private String remark;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String materialUrl;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String navyUrl;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer uploadNumber;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer type;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer materialNumber;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer navyNumber;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer synchronizationsNumber;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer successesNumber;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer failuresNumber;
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

}
