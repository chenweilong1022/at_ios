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

/**
 * 消息记录
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-21 14:41:06
 */
@Data
@TableName("at_message_record")
@ApiModel("消息记录")
@Accessors(chain = true)
public class AtMessageRecordDTO extends PageParam implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * 客服id
	 */
	@ApiModelProperty(required=false,value="客服id")
	private Integer customerServiceId;
	/**
	 * 所属客服
	 */
	@ApiModelProperty(required=false,value="所属客服")
	private String customerService;
	/**
	 * 消息人
	 */
	@ApiModelProperty(required=false,value="消息人")
	private Integer messageUserId;
	/**
	 * 消息uid
	 */
	@ApiModelProperty(required=false,value="消息uid")
	private String messageUid;
	/**
	 * 消息类型
	 */
	@ApiModelProperty(required=false,value="消息类型")
	private Integer messageType;
	/**
	 * 消息内容
	 */
	@ApiModelProperty(required=false,value="消息内容")
	private String messageContent;
	/**
	 * 收发
	 */
	@ApiModelProperty(required=false,value="收发")
	private Integer sendReceive;
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
