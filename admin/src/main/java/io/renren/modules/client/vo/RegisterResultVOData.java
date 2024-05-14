package io.renren.modules.client.vo;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/1 02:17
 */

@Data
public class RegisterResultVOData {
    private String taskId;
    private Long status;
    private String remark;
    private Long createTime;
}
