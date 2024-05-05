package io.renren.modules.client.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

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
}
