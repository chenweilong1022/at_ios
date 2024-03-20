// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.renren.modules.client.dto;
import lombok.Data;

import java.util.List;

@Data
public class UpdateProfileNameDTO {
    private String proxy;
    private String name;
    private String token;

    public String getProxy() { return proxy; }
    public void setProxy(String value) { this.proxy = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getToken() { return token; }
    public void setToken(String value) { this.token = value; }
}
