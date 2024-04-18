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
 * 手机号筛选
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-04-04 16:13:39
 */
@Data
@TableName("cd_phone_filter_record")
@ApiModel("手机号筛选")
@Accessors(chain = true)
public class CdPhoneFilterRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer recordId;
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
	/**
	 * 修改时间
	 */
	@ApiModelProperty(required=false,value="修改时间")
	private Date updateTime;

	@ApiModelProperty(required=false,value="总数数量")
	private Long totalCount;

	@ApiModelProperty(required=false,value="成功数量")
	private Long successCount;

	@ApiModelProperty(required=false,value="失败数量")
	private Long failCount;

	@ApiModelProperty(required=false,value="文件地址（集合列表）")
	private String fileUrl;


}
