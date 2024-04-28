package io.renren.common.base.dto;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 分页参数类
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2019-01-16 17:34:20
 */
@ApiModel("分页参数类")
@Data
@Accessors(chain = true)
public abstract class PageParam implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(required=false,value="当前页数",hidden = true)
	private Integer page;

	@ApiModelProperty(required=false,value="每页条数",hidden = true)
	private Integer limit;

	@TableField(exist = false)
	@ApiModelProperty(required=false,value="分页开始 sql使用",hidden = true)
	private Integer pageStart;

	public Integer getPage() {
		if (ObjectUtil.isNull(page)) {
			page = 1;
		}
		return page;
	}
}
