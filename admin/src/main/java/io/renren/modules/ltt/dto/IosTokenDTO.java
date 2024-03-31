// IosTokenDTO.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.renren.modules.ltt.dto;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class IosTokenDTO {
    private String body;
    private String country;
    private String bundleId;
    private String appUserId;
    private String userName;
    private Token token;
    private String deviceId;

    @Data
    public static class Token {
        private String appVersion;
        private String mnc;
        private String mid;
        private String mcc;
        private String token;
        private String carrier;
        private String phoneNumber;
        private String host;
        private Map<String, KeychainDic> keychainDic;
        private List<String> updateInfoArray;
        private String osMachine;
        private String refreshToken;
        private String neloInstallID;

        @Data
        public static class KeychainDic {
            private String pdmn;
            private String gena;
            private String svce;
            private String vData;
            private String agrp;
            private String keychainDicClass;
            private String acct;
        }

    }
}
