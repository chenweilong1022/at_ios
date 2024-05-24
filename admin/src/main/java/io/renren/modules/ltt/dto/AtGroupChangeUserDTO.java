package io.renren.modules.ltt.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@TableName("at_group_task")
@ApiModel("拉群时对应的修改群信息配置")
@Accessors(chain = true)
public class AtGroupChangeUserDTO implements Serializable {

	/**
	 * 国家
	 */
	@ApiModelProperty(required=false,value="修改群名称账号对应的国家")
	private Integer changeGroupCountryCode;
	/**
	 * 一号几群
	 */
	@ApiModelProperty(required=false,value="一号几群")
	private Integer accountGroupDistributed;

	/**
	 * 国家
	 */
	@ApiModelProperty(required=false,value="修改群名称账号对应的分组")
	private Integer changeGroupId;

}
