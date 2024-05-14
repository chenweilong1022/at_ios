// CreateGroupResultVO.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.renren.modules.client.vo;
import lombok.Data;

@Data
public class CreateGroupResultVO {
    private String msg;
    private long code;
    private Data data;


    @lombok.Data
    public class Data {
        private long createTime;
        private String remark;
        private GroupInfo groupInfo;
        private String taskId;
        private long status;

        @lombok.Data
        public class GroupInfo {
            private String roomTicketId;
            private String chatRoomUrl;
            private String roomId;
        }
    }
}

