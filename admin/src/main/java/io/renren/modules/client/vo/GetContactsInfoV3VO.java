package io.renren.modules.client.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2024/3/20 17:05
 */
@Data
public class GetContactsInfoV3VO {
    private String msg;
    private long code;
    private Data data;

    @lombok.Data
    public static class Data {
        private Map<String, DMap> contacts;

        @lombok.Data
        public static class DMap{
            private long userStatus;
            private Contact contact;
            private long snapshotTimeMillis;
        }

        @lombok.Data
        public static  class Contact {

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
}
