// LineTokenJson.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.renren.modules.client.dto;
import lombok.Data;

@Data
public class LineTokenJson {
    private String appVersion;
    private long deviceWidth;
    private String language;
    private long deviceTotalDiskSpaceGB;
    private long deviceHeight;
    private String password;
    private String clientE2EENonce;
    private String localCountry;
    private String serverE2EEPublicKey;
    private String host;
    private S5Obj s5Obj;
    private long updateProfileImageStatus;
    private boolean accountExistStatus;
    private String clientPrivateKey;
    private String ab;
    private String nickName;
    private String accessToken;
    private long reqSeq;
    private String acceptLanguageLal;
    private String idfa;
    private String phone;
    private String channelTokenPost;
    private String serverNonce;
    private String projectName;
    private String tdid;
    private String cid;
    private String projectVersion;
    private String bindEmail;
    private String mnc;
    private String authToken;
    private long deviceCoreCount;
    private String mid;
    private String sid;
    private String deviceTimeZoneName;
    private String clientE2EEPublicKey;
    private String clientPublicKey;
    private String txtToken;
    private long deviceDensity;
    private String osVersion;
    private String countryCode;
    private String clientNonce;
    private String serverPublicKey;
    private String storeUserId;
    private String networkType;
    private String lineTlUpstreamId;
    private long deviceRemainingDiskSpaceGB;
    private String scid;
    private String deviceZoneAbbrev;
    private String sessionId;
    private String mcc;
    private String lineTsVersion;
    private String lineHost;
    private String authSessionId;
    private String carrier;
    private String acceptLanguage;
    private long eventSeq;
    private long clientE2EEPrivateKeyId;
    private String channelTokenNotification;
    private String encryptedAccessToken;
    private String clientE2EEPrivateKey;
    private String tcid;
    private String osMachine;
    private String neloInstallID;
    private String osProductVersion;
    private String refreshToken;

    @Data
    public class S5Obj {
        private long port;
        private String ip;
        private boolean isUse;
        private String pwd;
        private String account;
        private String proxyType;
    }

}
