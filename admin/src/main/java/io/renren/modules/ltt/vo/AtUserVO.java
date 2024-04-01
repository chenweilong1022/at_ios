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
 * 账号数据
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-01 01:55:49
 */
@Data
@TableName("at_user")
@ApiModel("账号数据")
@Accessors(chain = true)
public class AtUserVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	@ApiModelProperty(required=false,value="主键")
	private Integer id;
	/**
	 * 头像
	 */
	@ApiModelProperty(required=false,value="头像")
	private String avatar;
	/**
	 * 国家
	 */
	@ApiModelProperty(required=false,value="国家")
	private String nation;
	/**
	 * 电话
	 */
	@ApiModelProperty(required=false,value="电话")
	private String telephone;
	/**
	 * 昵称
	 */
	@ApiModelProperty(required=false,value="昵称")
	private String nickName;
	/**
	 * 好友数量
	 */
	@ApiModelProperty(required=false,value="好友数量")
	private Integer numberFriends;
	/**
	 * 密码
	 */
	@ApiModelProperty(required=false,value="密码")
	private String password;
	/**
	 * 分组id
	 */
	@ApiModelProperty(required=false,value="分组id")
	private Integer userGroupId;
	/**
	 * 分组名称
	 */
	@ApiModelProperty(required=false,value="分组名称")
	private String userGroupName;
	/**
	 * 所属客服id
	 */
	@ApiModelProperty(required=false,value="所属客服id")
	private Integer customerServiceId;
	/**
	 * 所属客服
	 */
	@ApiModelProperty(required=false,value="所属客服")
	private String customerService;
	/**
	 * 删除标志
	 */
	@ApiModelProperty(required=false,value="删除标志")
	private Integer deleteFlag;
	/**
	 * 状态
	 */
	@ApiModelProperty(required=false,value="状态")
	private Integer status;
	/**
	 * 刷新联系人状态
	 */
	@ApiModelProperty(required=false,value="刷新联系人状态")
	private Integer refreshContactStatus;
	/**
	 * 状态msg
	 */
	@ApiModelProperty(required=false,value="状态msg")
	private String msg;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(required=false,value="创建时间")
	private Date createTime;
	/**
	 * 用户tokenid
	 */
	@ApiModelProperty(required=false,value="用户tokenid")
	private Integer userTokenId;

	/**
	 * 管理账户id
	 */
	@ApiModelProperty(required=false,value="管理账户id")
	private Long sysUserId;

	@ApiModelProperty(required=false,value="账号来源协议 1协议 2真机")
	private Integer userSource;

}
