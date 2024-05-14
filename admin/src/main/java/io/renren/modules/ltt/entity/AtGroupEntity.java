package io.renren.modules.ltt.entity;

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
public class AtGroupEntity implements Serializable {
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
	private String realGroupName;
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

	@ApiModelProperty(required=false,value="间隔秒数")
	private Integer intervalSecond;

	@ApiModelProperty(required=false,value="自动拉群")
	private Integer autoPullGroup;

	@ApiModelProperty(required=false,value="群名是否随机")
	private Integer randomGroupName;

	/**
	 * 修改群名称账号对应的国家
	 */
	@ApiModelProperty(required=false,value="修改群名称账号对应的国家")
	private Integer changeGroupCountryCode;

	/**
	 * 修改群名称账号对应的分组
	 */
	@ApiModelProperty(required=false,value="修改群名称账号对应的分组")
	private Integer changeGroupId;
	/**
	 * 修改群名称账号对应的分组
	 */
	@ApiModelProperty(required=false,value="修改群信息水军id")
	private Integer changeUserId;
	/**
	 * 代理ip
	 */
	@ApiModelProperty(required=false,value="代理ip")
	private Integer ipCountryCode;
	/**
	 * 间隔秒数
	 */
	@ApiModelProperty(required=false,value="间隔秒数")
	private Integer searchIntervalSecond;

}
