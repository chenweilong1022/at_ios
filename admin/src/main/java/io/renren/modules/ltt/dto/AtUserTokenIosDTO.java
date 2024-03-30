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

/**
 * 用户ios token数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-31 00:16:44
 */
@Data
@TableName("at_user_token_ios")
@ApiModel("用户ios token数据")
@Accessors(chain = true)
public class AtUserTokenIosDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * country
	 */
	@ApiModelProperty(required=false,value="country")
	private String country;
	/**
	 * bundleId
	 */
	@ApiModelProperty(required=false,value="bundleId")
	private String bundleId;
	/**
	 * appUserId
	 */
	@ApiModelProperty(required=false,value="appUserId")
	private String appUserId;
	/**
	 * userName
	 */
	@ApiModelProperty(required=false,value="userName")
	private String userName;
	/**
	 * phoneNumber
	 */
	@ApiModelProperty(required=false,value="phoneNumber")
	private String phoneNumber;
	/**
	 * mid
	 */
	@ApiModelProperty(required=false,value="mid")
	private String mid;
	/**
	 * token
	 */
	@ApiModelProperty(required=false,value="token")
	private String token;
	/**
	 * ios_token
	 */
	@ApiModelProperty(required=false,value="ios_token")
	private String iosToken;
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
	 * deviceId
	 */
	@ApiModelProperty(required=false,value="deviceId")
	private String deviceId;
}
