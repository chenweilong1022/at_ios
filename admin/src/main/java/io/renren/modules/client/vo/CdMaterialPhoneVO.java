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
 * @date 2023-12-12 12:30:34
 */
@Data
@TableName("cd_material_phone")
@ApiModel("")
@Accessors(chain = true)
public class CdMaterialPhoneVO implements Serializable {
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
	private String luid;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String contactType;
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
	private String createdTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String type;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String status;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String relation;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String displayName;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String phoneticName;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String pictureStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String thumbnailUrl;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String statusMessage;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String displayNameOverridden;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String favoriteTime;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String capableVoiceCall;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String capableVideoCall;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String capableMyhome;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String capableBuddy;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String attributes;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String settings;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String picturePath;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String recommendpArams;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String friendRequestStatus;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String musicProfile;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private String videoProfile;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer materialType;
	/**
	 *
	 */
	@ApiModelProperty(required=false,value="")
	private Integer materialPhoneStatus;
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
