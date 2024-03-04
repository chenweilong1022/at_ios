package io.renren.modules.client.vo;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/1/29 22:24
 */
@Data
public class CreateThreadResultVO {
    private String msg;
    private long code;
    private Data data;

    @lombok.Data
    public static class Data {
        private long createTime;
        private long negotiatePublicKeyId;
        private String negotiatePublicKeyBase64;
        private String remark;
        private String taskId;
        private long status;
    }
}
