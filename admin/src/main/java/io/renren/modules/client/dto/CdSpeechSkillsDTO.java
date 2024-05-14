package io.renren.modules.client.dto;

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
 * 
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-01-29 17:14:58
 */
@Data
@TableName("cd_speech_skills")
@ApiModel("")
@Accessors(chain = true)
public class CdSpeechSkillsDTO extends PageParam implements Serializable {
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
	private String word;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private String picture;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private String link;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private Integer type;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private Integer orderIndex;
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

}
