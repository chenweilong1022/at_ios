package io.renren.modules.ltt.dto;

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
 * 加粉任务子任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-18 10:53:33
 */
@Data
@ApiModel("联系人列表")
@Accessors(chain = true)
public class AtDataSubtaskResultDto implements Serializable {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * 数据任务id
	 */
	@ApiModelProperty(required=false,value="数据任务id")
	private Integer dataTaskId;
	/**
	 * 账户id
	 */
	@ApiModelProperty(required=false,value="账户id")
	private Integer userId;
	/**
	 * 任务状态
	 */
	@ApiModelProperty(required=false,value="任务状态")
	private Integer taskStatus;
	/**
	 * 类型
	 */
	@ApiModelProperty(required=false,value="类型")
	private Integer groupType;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String luid;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String contactType;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String contactKey;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String mid;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String createdTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String updateTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String type;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String status;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String relation;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String displayName;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String phoneticName;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String pictureStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String thumbnailUrl;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String statusMessage;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String displayNameOverridden;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String favoriteTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String capableVoiceCall;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String capableVideoCall;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String capableMyhome;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String capableBuddy;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String attributes;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String settings;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String picturePath;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String recommendpArams;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String friendRequestStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String musicProfile;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String videoProfile;
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
	 * line协议的任务id
	 */
	@ApiModelProperty(required=false,value="line协议的任务id")
	private String lineTaskId;
	/**
	 * line的协议返回信息
	 */
	@ApiModelProperty(required=false,value="line的协议返回信息")
	private String msg;
	/**
	 * 刷新联系人状态
	 */
	@ApiModelProperty(required=false,value="刷新联系人状态")
	private Integer refreshContactStatus;

	@ApiModelProperty(required = false, value = "主号手机号")
	private String telephone;

	@ApiModelProperty(required = false, value = "主账户分组id")
	private Integer userGroupId;

	@ApiModelProperty(required = false, value = "主账户状态 枚举：UserStatus")
	private Integer userStatus;

	@ApiModelProperty(required = false, value = "主账户所属客服id")
	private Integer customerServiceId;

	@ApiModelProperty(required = false, value = "主账户所属客服")
	private Integer customerServiceName;

	/**
	 * 管理账户id
	 */
	@ApiModelProperty(required=false,value="管理账户id")
	private Long sysUserId;
}
