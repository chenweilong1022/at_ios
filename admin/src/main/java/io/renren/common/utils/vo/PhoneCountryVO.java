// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.renren.common.utils.vo;
import lombok.Data;

import java.util.List;

@Data
public class PhoneCountryVO {
    private String number;
    private String regionCode;
    private long nationalNumber;
    private long countryCode;
    private String description;
    private String fullNumber;

    public String getNumber() { return number; }
    public void setNumber(String value) { this.number = value; }

    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String value) { this.regionCode = value; }

    public long getNationalNumber() { return nationalNumber; }
    public void setNationalNumber(long value) { this.nationalNumber = value; }

    public long getCountryCode() { return countryCode; }
    public void setCountryCode(long value) { this.countryCode = value; }

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }

    public String getFullNumber() { return fullNumber; }
    public void setFullNumber(String value) { this.fullNumber = value; }
}
