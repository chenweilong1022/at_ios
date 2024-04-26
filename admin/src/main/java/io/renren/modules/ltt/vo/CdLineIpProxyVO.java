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
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-27 17:49:13
 */
@Data
@TableName("cd_line_ip_proxy")
@ApiModel("")
@Accessors(chain = true)
public class CdLineIpProxyVO implements Serializable {
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
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer deleteFlag;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Date createTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String tokenPhone;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String lzCountry;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String outIpv4;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String country;


	@ApiModelProperty(required=false,value="ip类型 1动态ip 2固定ip")
	private Integer ipType;
}
