package io.renren.modules.client.vo;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/3/12 01:06
 */
@Data
public class UpdateProfileImageResultVO {
    private String msg;
    private long code;
    private Data data;

    @lombok.Data
    public static class Data {
        private long createTime;
        private String remark;
        private String taskId;
        private long status;
    }
}
