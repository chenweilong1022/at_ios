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
 * 头像
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-29 17:21:50
 */
@Data
@TableName("at_avatar")
@ApiModel("头像")
@Accessors(chain = true)
public class AtAvatarVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * 头像分组id
	 */
	@ApiModelProperty(required=false,value="头像分组id")
	private Integer avatarGroupId;
	/**
	 * 头像
	 */
	@ApiModelProperty(required=false,value="头像")
	private String avatar;
	/**
	 * 使用标识
	 */
	@ApiModelProperty(required=false,value="使用标识")
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
	 * 管理账户id
	 */
	@ApiModelProperty(required=false,value="管理账户id")
	private Long sysUserId;

}
