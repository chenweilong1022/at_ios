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
import java.util.Date;

/**
 * 昵称
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-28 22:56:04
 */
@Data
@TableName("at_username")
@ApiModel("昵称")
@Accessors(chain = true)
public class AtUsernameDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * 昵称分组id
	 */
	@ApiModelProperty(required=false,value="昵称分组id")
	private Integer usernameGroupId;
	/**
	 * 昵称
	 */
	@ApiModelProperty(required=false,value="昵称")
	private String username;
	/**
	 * 使用标识
	 */
	@ApiModelProperty(required=false,value="使用标识")
	private Integer useFlag;
	/**
	 * 删除标志
	 */
	@ApiModelProperty(required=false,value="删除标志")
	private Integer deleteFlag;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(required=false,value="创建时间")
	private Date createTime;

}
