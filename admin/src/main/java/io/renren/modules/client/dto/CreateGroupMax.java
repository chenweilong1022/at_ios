// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.renren.modules.client.dto;
import lombok.Data;

import java.util.List;

@Data
public class CreateGroupMax {
    private List<String> userMidList;
    private String proxy;
    private String groupName;
    private String token;
}
