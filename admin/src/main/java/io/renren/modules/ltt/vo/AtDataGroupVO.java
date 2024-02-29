package io.renren.modules.ltt.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.utils.EnumUtil;
import io.renren.modules.ltt.enums.GroupType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据分组
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 00:27:43
 */
@Data
@TableName("at_data_group")
@ApiModel("数据分组")
@Accessors(chain = true)
public class AtDataGroupVO implements Serializable {
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
	 * 分组类型
	 */
	@ApiModelProperty(required=false,value="分组类型")
	private Integer groupType;
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
	private Integer dataGroupIdCount;

	public String getGroupTypeStr() {
		return EnumUtil.queryValueByKey(this.groupType, GroupType.values());
	}
}
