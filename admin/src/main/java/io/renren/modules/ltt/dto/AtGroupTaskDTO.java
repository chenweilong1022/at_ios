package io.renren.modules.ltt.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.List;
import io.renren.common.base.dto.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;
import java.util.Queue;

/**
 * 拉群任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-29 17:07:23
 */
@Data
@TableName("at_group_task")
@ApiModel("拉群任务")
@Accessors(chain = true)
public class AtGroupTaskDTO extends PageParam implements Serializable {
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
	 * 群名称
	 */
	@ApiModelProperty(required=false,value="群名称")
	private String groupName;
	/**
	 * 水军
	 */
	@ApiModelProperty(required=false,value="水军")
	private List<String> navyUrlList;
	/**
	 * 料子
	 */
	@ApiModelProperty(required=false,value="料子")
	private List<String> materialUrlList;
	/**
	 * 群数量
	 */
	@ApiModelProperty(required=false,value="群数量")
	private Integer groupCount;
	/**
	 * 群初始化下标
	 */
	@ApiModelProperty(required=false,value="群初始化下标")
	private Integer groupCountStart;
	/**
	 * 剩余料子
	 */
	@ApiModelProperty(required=false,value="剩余料子")
	private String remaining;
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
	 * 国家
	 */
	@ApiModelProperty(required=false,value="国家")
	private Integer countryCode;
	/**
	 * 合群国家
	 */
	@ApiModelProperty(required=false,value="合群国家")
	private Integer countryCodeH;
	/**
	 * 拉群号数量
	 */
	@ApiModelProperty(required=false,value="拉群号数量")
	private Integer pullGroupNumber;
	/**
	 * 用户分组id
	 */
	@ApiModelProperty(required=false,value="用户分组id")
	private Integer userGroupId;
	/**
	 * 合群用户分组
	 */
	@ApiModelProperty(required=false,value="合群用户分组")
	private Integer userGroupIdH;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(required=false,value="更新时间")
	private Date updateTime;
	/**
	 * 删除标志
	 */
	@ApiModelProperty(required=false,value="删除标志")
	private Integer deleteFlag;
	/**
	 * 删除标志
	 */
	@ApiModelProperty(required=false,value="删除标志")
	private Integer groupCountTotal;
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
	/**
	 * 剩余
	 */
	@ApiModelProperty(required=false,value="剩余")
	private Queue<String> materialUrlsQueue;


	@ApiModelProperty(required=false,value="间隔秒数")
	private Integer intervalSecond;
	/**
	 * 间隔秒数
	 */
	@ApiModelProperty(required=false,value="间隔秒数")
	private Integer searchIntervalSecond;

	@ApiModelProperty(required=false,value="打开app")
	private Integer openApp;


	/**
	 * 代理ip
	 */
	@ApiModelProperty(required=false,value="代理ip")
	private Integer ipCountryCode;
}
