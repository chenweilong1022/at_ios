package io.renren.modules.ltt.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.renren.common.validator.group.AddGroup;
import io.renren.common.validator.group.UpdateGroup;
import io.renren.modules.sys.entity.AbstractEntity;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 新增或者修改客服信息
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:28:55
 */
@Data
public class CustomerUserOperationParamDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String username;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = AddGroup.class)
    private String password;

    /**
     * 创建者ID
     */
    private Long createUserId;
    /**
     * 是否开启发送翻译  0：否   1：是
     */
    private Integer sendTranslate;
    /**
     * 发送翻译语言
     */
    private Integer sendLanguage;
    /**
     * 是否开启接收翻译  0：否   1：是
     */
    private Integer receiveTranslate;
    /**
     * 发送接收语言
     */
    private Integer receiveLanguage;
}
