package io.renren.modules.ltt.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.base.dto.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-04-26 12:10:55
 */
@Data
@TableName("注册名称集合")
@ApiModel("")
@Accessors(chain = true)
public class RegisterNicknameDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 国家
	 */
	@ApiModelProperty(required=false,value="国家")
	private Integer countryCode;
	/**
	 * 使用次数
	 */
	@ApiModelProperty(required=false,value="使用次数")
	private String nickname;
}
