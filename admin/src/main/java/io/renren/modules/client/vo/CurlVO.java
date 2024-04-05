// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.renren.modules.client.vo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class CurlVO {

    private boolean proxyUse;
    private String country;
    private String loc;
    private String hostname;
    private String city;
    private String org;
    private String timezone;
    private String ip;
    private String postal;
    private String readme;
    private String region;
}
