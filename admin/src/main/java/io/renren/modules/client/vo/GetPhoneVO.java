package io.renren.modules.client.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 21:41
 */
@Data
@Accessors(chain = true)
public class GetPhoneVO {

    private String number;
    private String pkey;
    private List<String> pkeys;
    private String time;
    private String country;
    private String countryCode;
    private String other;
    private String com;
    private String phone;
    private List<String> phones;
}
