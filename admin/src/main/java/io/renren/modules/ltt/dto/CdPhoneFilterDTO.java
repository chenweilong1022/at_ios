package io.renren.modules.ltt.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.base.dto.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 手机号筛选
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-04-03 22:09:23
 */
@Data
@TableName("cd_phone_filter")
@ApiModel("手机号筛选")
@Accessors(chain = true)
public class CdPhoneFilterDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;


	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * 主键
	 */
	@ApiModelProperty(required=false,value="记录id")
	private Integer recordId;
	/**
	 * 任务状态
	 */
	@ApiModelProperty(required=false,value="任务状态")
	private Integer taskStatus;
	/**
	 * 任务状态
	 */
	@ApiModelProperty(required=false,value="任务状态")
	private Integer taskStatus2;
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
	private String displayName;
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
	 * 修改时间
	 */
	@ApiModelProperty(required=false,value="修改时间")
	private Date updateTime;
	/**
	 * line的协议返回信息
	 */
	@ApiModelProperty(required=false,value="line的协议返回信息")
	private String msg;
	@ApiModelProperty(required=false,value="")
	private List<String> textUrlList;
}
