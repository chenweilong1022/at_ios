package io.renren.modules.ltt.dto;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.renren.common.base.dto.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 账户端口管理
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-17 20:55:14
 */
@Data
@TableName("at_user_port")
@ApiModel("账户端口管理")
@Accessors(chain = true)
public class AtUserPortDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * 端口数量
	 */
	@ApiModelProperty(required=false,value="端口数量")
	private Integer portNum;
	/**
	 * 管理账户id
	 */
	@ApiModelProperty(required=false,value="管理账户id")
	private Long sysUserId;
	/**
	 * 过期时间
	 */
	@ApiModelProperty(required=false,value="过期时间")
	private Date expireTime;
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
	 * 操作人id
	 */
	@ApiModelProperty(required=false,value="操作人id")
	private Long operationUserId;

}
