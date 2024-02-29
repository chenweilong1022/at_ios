package io.renren.modules.ltt.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.utils.EnumUtil;
import io.renren.modules.ltt.enums.Platform;
import io.renren.modules.ltt.enums.UseFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户token数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 02:03:03
 */
@Data
@TableName("at_user_token")
@ApiModel("用户token数据")
@Accessors(chain = true)
public class AtUserTokenVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * token
	 */
	@ApiModelProperty(required=false,value="token")
	private String token;
	/**
	 * 使用状态
	 */
	@ApiModelProperty(required=false,value="使用状态")
	private Integer useFlag;
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
	 * 平台
	 */
	@ApiModelProperty(required=false,value="平台")
	private Integer platform;
	/**
	 * 分组id
	 */
	@ApiModelProperty(required=false,value="分组id")
	private Integer userGroupId;


	public String getUseFlag() {
		return EnumUtil.queryValueByKey(this.useFlag, UseFlag.values());
	}

	public String getPlatform() {
		return EnumUtil.queryValueByKey(this.platform, Platform.values());
	}
}
