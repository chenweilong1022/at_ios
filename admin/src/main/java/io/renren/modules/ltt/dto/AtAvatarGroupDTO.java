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
import java.util.List;

/**
 * 头像分组
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-02-28 22:45:51
 */
@Data
@TableName("at_avatar_group")
@ApiModel("头像分组")
@Accessors(chain = true)
public class AtAvatarGroupDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * 分组名称
	 */
	@ApiModelProperty(required=false,value="分组名称")
	private String name;
	/**
	 * 头像列表
	 */
	@ApiModelProperty(required=false,value="头像列表")
	private List<String> avatarList;
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

}
