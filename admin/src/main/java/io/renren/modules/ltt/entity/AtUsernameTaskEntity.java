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
 * 昵称任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-20 21:46:41
 */
@Data
@TableName("at_username_task")
@ApiModel("昵称任务")
@Accessors(chain = true)
public class AtUsernameTaskEntity implements Serializable {
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
	 * 昵称分组
	 */
	@ApiModelProperty(required=false,value="昵称分组")
	private Integer usernameGroupId;
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

}
