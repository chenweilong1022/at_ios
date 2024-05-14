package io.renren.modules.ltt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2022-08-02 14:05:16
 */
@Data
@TableName("ltt_user")
@ApiModel("用户表")
@Accessors(chain = true)
public class LttUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="id")
	private Long id;
	/**
	 * 昵称
	 */
	@ApiModelProperty(required=false,value="昵称")
	private String nickname;
	/**
	 * 性别
	 */
	@ApiModelProperty(required=false,value="性别")
	private Integer sex;
	/**
	 * 性别描述
	 */
	@ApiModelProperty(required=false,value="性别描述")
	private String sexDesc;
	/**
	 * 区号
	 */
	@ApiModelProperty(required=false,value="区号")
	private String countryCode;
	/**
	 * 语言
	 */
	@ApiModelProperty(required=false,value="语言")
	@TableField(value = "`language`")
	private String language;
	/**
	 * 国家
	 */
	@ApiModelProperty(required=false,value="国家")
	private String country;
	/**
	 * 省
	 */
	@ApiModelProperty(required=false,value="省")
	private String province;
	/**
	 * 市
	 */
	@ApiModelProperty(required=false,value="市")
	private String city;
	/**
	 * 微信头像
	 */
	@ApiModelProperty(required=false,value="微信头像")
	private String headimgUrl;
	/**
	 * openId
	 */
	@ApiModelProperty(required=false,value="openId")
	private String openid;
	/**
	 * token
	 */
	@ApiModelProperty(required=false,value="token")
	private String token;
	/**
	 * 累计消费金额
	 */
	@ApiModelProperty(required=false,value="累计消费金额")
	private BigDecimal money;
	/**
	 * 累计使用次数
	 */
	@ApiModelProperty(required=false,value="累计使用次数")
	private Integer number;
	/**
	 * 用户绑定的手机号（国外手机号会有区号）
	 */
	@ApiModelProperty(required=false,value="用户绑定的手机号（国外手机号会有区号）")
	private String phoneNumber;
	/**
	 * 没有区号的手机号
	 */
	@ApiModelProperty(required=false,value="没有区号的手机号")
	private String mobile;
	/**
	 * 手机品牌
	 */
	@ApiModelProperty(required=false,value="手机品牌")
	private String brand;
	/**
	 * 手机型号
	 */
	@ApiModelProperty(required=false,value="手机型号")
	private String model;
	/**
	 * 微信版本号
	 */
	@ApiModelProperty(required=false,value="微信版本号")
	private String version;
	/**
	 * 操作系统版本
	 */
	@ApiModelProperty(required=false,value="操作系统版本")
	@TableField(value = "`system`")
	private String system;
	/**
	 * 客户端平台
	 */
	@ApiModelProperty(required=false,value="客户端平台")
	private String platform;
	/**
	 * 客户端基础库版本
	 */
	@ApiModelProperty(required=false,value="客户端基础库版本")
	private String sdkVersion;
	/**
	 * ip地址
	 */
	@ApiModelProperty(required=false,value="ip地址")
	private String ip;
	/**
	 * 纬度
	 */
	@ApiModelProperty(required=false,value="纬度")
	private String latitude;
	/**
	 * 经度
	 */
	@ApiModelProperty(required=false,value="经度")
	private String longitude;
	/**
	 * 1:公众号2:小程序
	 */
	@ApiModelProperty(required=false,value="1:公众号2:小程序")
	private Integer source;
	/**
	 * 1:未授权2:已授权
	 */
	@ApiModelProperty(required=false,value="1:未授权2:已授权")
	private Integer isAuth;
	/**
	 * 1:未授权2:已授权
	 */
	@ApiModelProperty(required=false,value="1:未授权2:已授权")
	private Integer isPhone;
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

}
