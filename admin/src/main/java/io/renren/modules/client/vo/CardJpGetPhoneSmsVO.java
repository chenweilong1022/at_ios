// CardMeGetPhoneVO.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.renren.modules.client.vo;

import lombok.Data;

import java.util.List;

@lombok.Data
public class CardJpGetPhoneSmsVO {

        private String msg;
        private long code;
        private Data data;
        private String time;

        @lombok.Data
        public static class Data {
            private List<Ret> ret;

            @lombok.Data
            public static class Ret {
                private List<Sm> sms;
                private String phoneNumber;
                private long state;
                private long takeTime;
                private long takeid;

                @lombok.Data
                public static class Sm {
                    private String recvTime;
                    private String content;
                    private Long time;
                }
            }
        }

    }

