package io.renren.modules.client.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2023-06-08 11:34:05
 */
@Data
@TableName("dept")
@ApiModel("")
@Accessors(chain = true)
public class DeptVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="")
	private Integer deptid;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private String deptname;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private String deptresponsibility;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private Integer parentdeptid;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private String deptman;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private String depttel;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private String remark;

}
