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
import java.time.LocalDate;
import java.util.Date;

/**
 * 数据汇总-定时更新
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-05-06 13:15:52
 */
@Data
@TableName("at_user_data_summary")
@ApiModel("数据汇总-定时更新")
@Accessors(chain = true)
public class AtUserDataSummaryDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="")
	private Integer id;
	/**
	 * 国家code
	 */
	@ApiModelProperty(required=false,value="国家code")
	private String countryCode;
	/**
	 * line注册数量
	 */
	@ApiModelProperty(required=false,value="line注册数量")
	private Integer lineRegisterCount;
	/**
	 * 账号使用数量
	 */
	@ApiModelProperty(required=false,value="账号使用数量")
	private Integer userUseCount;
	/**
	 * 生成日期
	 */
	@ApiModelProperty(required=false,value="生成日期")
	private Date createTime;

	@ApiModelProperty(required=false,value="日期")
	private String summaryDate;
	/**
	 * 库存
	 */
	@ApiModelProperty(required=false,value="库存")
	private Integer lineStock;
	/**
	 * 在线账号数量
	 */
	@ApiModelProperty(required=false,value="在线账号数量")
	private Integer userOnlineCount;

	@ApiModelProperty(required=false,value="修改时间")
	private Date updateTime;

	@ApiModelProperty(required=false,value="查询时间开始")
	private String searchStartTime;

	@ApiModelProperty(required=false,value="查询时间结束")
	private String searchEndTime;
}
