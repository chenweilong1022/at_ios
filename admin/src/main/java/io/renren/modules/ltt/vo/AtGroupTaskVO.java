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
public class AtGroupTaskVO implements Serializable {
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

	private String sysUsername;

	@ApiModelProperty(required=false,value="总使用账号数")
	private Integer usdUserCount;

	@ApiModelProperty(required=false,value="拉群成功数")
	private Integer successGroupCount;

	@ApiModelProperty(required=false,value="成功拉群账号数")
	private Integer successUserCount;

	@ApiModelProperty(required=false,value="拉取失败账号数量")
	private Integer failUserCount;

	public String getScheduleFloat() {
		return String.format("%.2f", (double) successGroupCount / usdUserCount * 100);
	}

}
