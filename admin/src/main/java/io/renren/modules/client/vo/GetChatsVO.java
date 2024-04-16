package io.renren.modules.client.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/12/13 02:02
 */
@Data
@Accessors(chain = true)
public class GetChatsVO {

    private String msg;
    private long code;
    private Data data;

    @lombok.Data
    public class Data {

        private List<Chat> chats;
        @lombok.Data
        public class Chat {
            private String picturePath;
            private boolean notificationDisabled;
            private long favoriteTimestamp;
            private Extra extra;
            private long createdTime;
            private String chatName;
            private long type;
            private String chatMid;
            @lombok.Data
            public class Extra {
                private GroupExtra groupExtra;
                @lombok.Data
                public class GroupExtra {
                    private String creator;
                    private String invitationTicket;
                    private boolean addFriendDisabled;
                    private Map<String, Long> memberMids;
                    private boolean ticketDisabled;
                    private boolean preventedJoinByTicket;
                }
            }
        }
    }
}
