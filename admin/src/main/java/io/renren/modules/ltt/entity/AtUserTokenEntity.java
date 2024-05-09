package io.renren.modules.ltt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户token数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 02:03:03
 */
@Data
@TableName("at_user_token")
@ApiModel("用户token数据")
@Accessors(chain = true)
public class AtUserTokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * token
	 */
	@ApiModelProperty(required=false,value="token")
	private String token;
	/**
	 * 使用状态
	 */
	@ApiModelProperty(required=false,value="使用状态")
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
	/**
	 * 平台
	 */
	@ApiModelProperty(required=false,value="平台")
	private Integer platform;
	/**
	 * 分组id
	 */
	@ApiModelProperty(required=false,value="分组id")
	private Integer userGroupId;

	/**
	 * 管理账户id
	 */
	@ApiModelProperty(required=false,value="管理账户id")
	private Long sysUserId;

	@TableField(exist = false)
	private String telephone;

	@ApiModelProperty(required=false,value="昵称")
	@TableField(exist = false)
	private String nickName;

	@ApiModelProperty(required=false,value="token类型 1协议token 2真机token'")
	private Integer tokenType;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer openStatus;
	/**
	 * 加粉任务子任务
	 */
	@ApiModelProperty(required=false,value="加粉任务子任务")
	private Integer dataSubtaskId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Date openTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String errMsg;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String taskId;
}
