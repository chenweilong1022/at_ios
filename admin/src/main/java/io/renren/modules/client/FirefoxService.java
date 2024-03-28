package io.renren.modules.client;


import io.renren.modules.client.vo.GetPhoneVO;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 21:28
 */
public interface FirefoxService {

    public GetPhoneVO getPhone();
    public String getPhoneCode(String pKey);
    public boolean setRel(String pKey);
    public boolean withBlackMobile(String pKey);
}
