// CardMeGetPhoneVO.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.renren.modules.client.vo;
import lombok.Data;

import java.util.List;

@Data
public class CardJpGetPhoneVO {

    private String msg;

    private long code;

    private String time;

    private List<Data> data;

    @lombok.Data
    public class Data {

        /**
         * 取号ID
         */
        private String take_id;

        /**
         * 号码
         */
        private String phone_number;

    }
}
