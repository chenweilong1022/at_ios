package io.renren.modules.ltt.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.utils.EnumUtil;
import io.renren.modules.ltt.enums.GroupStatus;
import io.renren.modules.ltt.enums.GroupType;
import io.renren.modules.ltt.enums.TaskStatus;
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
 * @date 2024-03-29 17:07:23
 */
@Data
@TableName("at_group")
@ApiModel("")
@Accessors(chain = true)
public class AtGroupVO implements Serializable {
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
	private String groupName;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String proxy;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer intervals;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String roomId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String chatRoomUrl;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String roomTicketId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer uploadGroupNumber;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer currentExecutionsNumber;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer successfullyAttractGroupsNumber;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer groupStatus;
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
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String taskId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer groupTaskId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer userId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer addType;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer materialId;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer materialPhoneType;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String msg;
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
	 * 群id
	 */
	@ApiModelProperty(required=false,value="群id")
	private Integer groupId;

	/**
	 * 电话
	 */
	@ApiModelProperty(required=false,value="电话")
	private String userTelephone;


	@ApiModelProperty(required=false,value="间隔秒数")
	private Integer intervalSecond;

	/**
	 * 代理ip
	 */
	@ApiModelProperty(required=false,value="代理ip")
	private Integer ipCountryCode;

	/**
	 * 国家
	 */
	@ApiModelProperty(required=false,value="国家")
	private Integer countryCode;
	public String getScheduleFloat() {
		double v = (double) (successfulQuantity + failuresQuantity) / addTotalQuantity * 100;
		return String.format("%.2f", v);
	}


	public String getGroupTypeStr() {
		return EnumUtil.queryValueByKey(this.groupType, GroupType.values());
	}

	public String getTaskStatusStr() {
		return EnumUtil.queryValueByKey(this.taskStatus, TaskStatus.values());
	}



	public String getGroupStatusStr() {
		return EnumUtil.queryValueByKey(this.groupStatus, GroupStatus.values());
	}
}
