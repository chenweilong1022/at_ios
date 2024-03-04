package io.renren.modules.client.vo;

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
 * @date 2024-01-29 22:59:57
 */
@Data
@TableName("cd_material_phone_send_record")
@ApiModel("")
@Accessors(chain = true)
public class CdMaterialPhoneSendRecordVO implements Serializable {
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
	private Integer groupTaskId;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private Integer lineRegisterId;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private Integer materialId;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private String contactKey;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private String mid;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private String sendText;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private Integer sendType;
	/**
	 * 
	 */
	@ApiModelProperty(required=false,value="")
	private Integer sendStatus;
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
	private String errMsg;

}
