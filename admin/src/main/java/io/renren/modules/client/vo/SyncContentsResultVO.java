package io.renren.modules.client.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/12 23:58
 */
@Data
public class SyncContentsResultVO implements Serializable {
    private String msg;
    private long code;
    private Data data;
    
    @lombok.Data
    public class Data {
        private long createTime;
        private String remark;
        private Map<String, ContactsMap> contactsMap;
        private String taskId;
        private long status;
        @lombok.Data
        public class ContactsMap {
            private String luid;
            private Contact contact;
            private String contactKey;
            private String contactType;
            @lombok.Data
            public class Contact {
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
}
