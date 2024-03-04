package io.renren.modules.client.vo;

import lombok.Data;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/8 12:36
 */
@Data
public class RefreshAccessTokenVO {
    private String msg;
    private long code;
    private Data data;
    @lombok.Data
    public class Data {
        private RetryPolicy retryPolicy;
        private long tokenIssueTimeEpochSec;
        private String accessToken;
        private long durationUntilRefreshInSec;
        private String refreshToken;
        @lombok.Data
        class RetryPolicy {
            private double jitterRate;
            private long initialDelayInMillis;
            private long multiplier;
            private long maxDelayInMillis;
        }
    }
}
