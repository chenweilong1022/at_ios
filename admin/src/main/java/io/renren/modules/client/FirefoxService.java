package io.renren.modules.client;


import io.renren.modules.client.vo.CardJpGetPhoneSmsVO;
import io.renren.modules.client.vo.GetPhoneVO;

import java.util.Map;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 21:28
 */
public interface FirefoxService {

    /**
     * 获取手机号
     * @return
     */
    public GetPhoneVO getPhone();

    /**
     * 获取验证码
     * @param pKey
     * @return
     */
    public String getPhoneCode(String pKey);
    /**
     * 获取验证码
     * @param pKey
     * @return
     */
    default Map<Long, CardJpGetPhoneSmsVO.Data.Ret.Sm> getPhoneCodes(String pKeys){
        return null;
    };

    /**
     * 释放手机号
     * @param pKey
     * @return
     */
    public boolean setRel(String pKey);

    /**
     * 拉黑手机号
     * @param pKey
     * @return
     */
    public boolean withBlackMobile(String pKey);
}
