// SearchUserIdVO.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.renren.modules.client.vo;
import lombok.Data;

import java.util.List;

@Data
public class SearchUserIdVO {
    private String msg;
    private long code;
    private Data data;


// Data.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

    @lombok.Data
    public static class Data {
        private boolean capableVideoCall;
        private String picturePath;
        private long settings;
        private String phoneticName;
        private String displayName;
        private String videoProfile;
        private String displayNameOverridden;
        private String mid;
        private boolean capableBuddy;
        private String type;
        private String pictureStatus;
        private String statusMessage;
        private String relation;
        private String recommendParams;
        private boolean capableMyhome;
        private String musicProfile;
        private String friendRequestStatus;
        private long createdTime;
        private boolean capableVoiceCall;
        private long favoriteTime;
        private long attributes;
        private String status;
        private String thumbnailUrl;

    }

}

