package io.renren.modules.ltt.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.utils.EnumUtil;
import io.renren.modules.ltt.enums.GroupType;
import io.renren.modules.ltt.enums.TaskStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 加粉任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-17 18:52:43
 */
@Data
@TableName("at_data_task")
@ApiModel("加粉任务")
@Accessors(chain = true)
public class AtDataTaskVO implements Serializable {
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
	 * 账户分组
	 */
	@ApiModelProperty(required=false,value="账户分组")
	private Integer userGroupId;
	/**
	 * 数据分组
	 */
	@ApiModelProperty(required=false,value="数据分组")
	private Integer dataGroupId;
	/**
	 * 类型
	 */
	@ApiModelProperty(required=false,value="类型")
	private Integer groupType;
	/**
	 * 加粉总数
	 */
	@ApiModelProperty(required=false,value="加粉总数")
	private Integer addTotalQuantity;
	/**
	 * 成功数
	 */
	@ApiModelProperty(required=false,value="成功数")
	private Integer successfulQuantity;
	/**
	 * 失败数
	 */
	@ApiModelProperty(required=false,value="失败数")
	private Integer failuresQuantity;
	/**
	 * 状态
	 */
	@ApiModelProperty(required=false,value="状态")
	private Integer taskStatus;
	/**
	 * 进度
	 */
	@ApiModelProperty(required=false,value="进度")
	private Integer schedule;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(required=false,value="更新时间")
	private Date updateTime;
	/**
	 * 加粉数量
	 */
	@ApiModelProperty(required=false,value="加粉数量")
	private Integer addQuantityLimit;
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

	public String getGroupTypeStr() {
		return EnumUtil.queryValueByKey(this.groupType, GroupType.values());
	}

	public String getTaskStatusStr() {
		return EnumUtil.queryValueByKey(this.taskStatus, TaskStatus.values());
	}

	public double getScheduleFloat() {
		return (double) (successfulQuantity + failuresQuantity) / addTotalQuantity * 100;
	}
}
