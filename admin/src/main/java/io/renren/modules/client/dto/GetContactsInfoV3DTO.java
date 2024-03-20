// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.renren.modules.client.dto;
import lombok.Data;

import java.util.List;

@Data
public class GetContactsInfoV3DTO {
    private String proxy;
    private String userMid;
    private String token;

    public String getProxy() { return proxy; }
    public void setProxy(String value) { this.proxy = value; }

    public String getUserMid() { return userMid; }
    public void setUserMid(String value) { this.userMid = value; }

    public String getToken() { return token; }
    public void setToken(String value) { this.token = value; }
}
