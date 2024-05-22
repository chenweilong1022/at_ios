package io.renren.modules.ltt.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户分组表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-27 17:18:34
 */
@Data
@TableName("at_user_group")
@ApiModel("用户分组表")
@Accessors(chain = true)
public class AtUserGroupVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键;主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键;主键")
	private Integer id;
	/**
	 * 分组名称
	 */
	@ApiModelProperty(required=false,value="分组名称")
	private String name;

	@ApiModelProperty(required=false,value="分组类型 AtUserGroupTypeEnum")
	private Integer userGroupType;
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

	/**
	 * 管理账户id
	 */
	@ApiModelProperty(required=false,value="管理账户id")
	private Long sysUserId;
}
