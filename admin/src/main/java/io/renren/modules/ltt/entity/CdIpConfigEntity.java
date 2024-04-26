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
 * 
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-04-26 12:10:55
 */
@Data
@TableName("cd_ip_config")
@ApiModel("")
@Accessors(chain = true)
public class CdIpConfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="")
	private Integer id;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private String ip;
	/**
	 * 国家
	 */
	@ApiModelProperty(required=false,value="国家")
	private Integer countryCode;
	/**
	 * 使用次数
	 */
	@ApiModelProperty(required=false,value="使用次数")
	private Integer usedCount;
	/**
	 * 端口
	 */
	@ApiModelProperty(required=false,value="端口")
	private String httpPort;
	/**
	 * sock5的端口
	 */
	@ApiModelProperty(required=false,value="sock5的端口")
	private String sock5Port;
	/**
	 * 帐号
	 */
	@ApiModelProperty(required=false,value="帐号")
	private String account;
	/**
	 * 密码
	 */
	@ApiModelProperty(required=false,value="密码")
	private String password;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private Integer deleteFlag;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(required=false,value="创建时间")
	private Date createTime;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(required=false,value="更新时间")
	private Date updateTime;

}
