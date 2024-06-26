package io.renren.modules.ltt.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.utils.EnumUtil;
import io.renren.modules.ltt.enums.TaskStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 头像任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-11 14:32:25
 */
@Data
@TableName("at_avatar_task")
@ApiModel("头像任务")
@Accessors(chain = true)
public class AtAvatarTaskVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * 任务名称
	 */
	@ApiModelProperty(required=false,value="任务名称")
	private String taskName;
	/**
	 * 任务状态
	 */
	@ApiModelProperty(required=false,value="任务状态")
	private Integer taskStatus;
	/**
	 * 进度
	 */
	@ApiModelProperty(required=false,value="进度")
	private Integer schedule;
	/**
	 * 账户分组
	 */
	@ApiModelProperty(required=false,value="账户分组")
	private Integer userGroupId;
	/**
	 * 账户分组
	 */
	@ApiModelProperty(required=false,value="账户分组名称")
	private String userGroupName;
	/**
	 * 头像分组
	 */
	@ApiModelProperty(required=false,value="头像分组")
	private Integer avatarGroupId;
	/**
	 * 头像分组
	 */
	@ApiModelProperty(required=false,value="头像分组名称")
	private String avatarGroupName;
	/**
	 * 执行数量
	 */
	@ApiModelProperty(required=false,value="执行数量")
	private Integer executionQuantity;
	/**
	 * 成功数量
	 */
	@ApiModelProperty(required=false,value="成功数量")
	private Integer successfulQuantity;
	/**
	 * 失败数量
	 */
	@ApiModelProperty(required=false,value="失败数量")
	private Integer failuresQuantity;
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

	public String getScheduleFloat() {
		double v = (double) (successfulQuantity + failuresQuantity) / executionQuantity * 100;
		return String.format("%.2f", v);
	}

	public String getTaskStatusStr() {
		return EnumUtil.queryValueByKey(this.taskStatus, TaskStatus.values());
	}
}
