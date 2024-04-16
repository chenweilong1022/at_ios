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
 * 加粉任务子任务
 *
 * @author chenweilong
 * @email chenweilong@qq.com
 * @date 2024-03-18 10:53:33
 */
@Data
public class AtDataSubtaskParamPageDTO extends PageParam implements Serializable {

    @ApiModelProperty(required = false, value = "好友手机号")
    private String contactKey;

    @ApiModelProperty(required = false, value = "主号手机号")
    private String telephone;

    @ApiModelProperty(required = false, value = "主账户分组id")
    private Integer userGroupId;

    @ApiModelProperty(required = false, value = "主账户状态 枚举：UserStatus")
    private Integer userStatus;

    @ApiModelProperty(required = false, value = "主账户所属客服id")
    private Integer customerServiceId;

    @ApiModelProperty(required = false, value = "开始时间检索")
    private String updateStartTime;

    @ApiModelProperty(required = false, value = "结束时间检索")
    private String updateEndTime;

    /**
     * 管理账户id
     */
    @ApiModelProperty(required=false,value="管理账户id")
    private Long sysUserId;

    /**
     * 数据任务id
     */
    @ApiModelProperty(required=false,value="数据任务id")
    private Integer dataTaskId;
    @ApiModelProperty(required=false,value="任务状态")
    private Integer taskStatus;
    @ApiModelProperty(required=false,value="任务状态")
    private List<Integer> taskStatusList;
    /**
     * 群id
     */
    @ApiModelProperty(required=false,value="群id")
    private Integer groupId;
}
