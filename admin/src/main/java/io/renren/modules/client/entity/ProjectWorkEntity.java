package io.renren.modules.client.entity;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/22 00:43
 */
@Data
public class ProjectWorkEntity implements Serializable {
    private String lineBaseHttp;
    private String firefoxBaseUrl;
    private String firefoxToken;
    private String firefoxIid;
    private String firefoxCountry;
    private String firefoxCountry1;
    private Integer proxyUseCount;
    private Integer proxy;


    private String lineAb;

    private String lineAppVersion;

    private String lineTxtToken;

    private String sfGetPhoneCodeUrl;

    private Integer sfTimeZone;

    public String getLineBaseHttp(Integer serverMod) {
        List<String> urls = CollUtil.newArrayList(
                "http://137.184.112.207:22117",
                "http://146.190.167.155:22117"
        );
        int mod = serverMod % urls.size();
        return urls.get(mod);
    }
}
