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
 * 头像任务子任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-11 14:32:25
 */
@Data
@TableName("at_avatar_subtask")
@ApiModel("头像任务子任务")
@Accessors(chain = true)
public class AtAvatarSubtaskVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * 头像任务id
	 */
	@ApiModelProperty(required=false,value="头像任务id")
	private Integer avatarTaskId;
	/**
	 * 账户分组
	 */
	@ApiModelProperty(required=false,value="账户分组")
	private Integer userGroupId;
	/**
	 * 头像分组
	 */
	@ApiModelProperty(required=false,value="头像分组")
	private Integer avatarGroupId;
	/**
	 * 账户id
	 */
	@ApiModelProperty(required=false,value="账户id")
	private Integer userId;
	/**
	 * 头像id
	 */
	@ApiModelProperty(required=false,value="头像id")
	private Integer avatarId;
	/**
	 * 任务状态
	 */
	@ApiModelProperty(required=false,value="任务状态")
	private Integer taskStatus;
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
