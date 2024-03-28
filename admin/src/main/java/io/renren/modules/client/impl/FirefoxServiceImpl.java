package io.renren.modules.client.impl;

import cn.hutool.http.HttpUtil;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.utils.ConfigConstant;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.FirefoxService;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.client.vo.GetPhoneVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 21:28
 */
@Service("firefoxServiceImpl")
@Game
public class FirefoxServiceImpl implements FirefoxService {

    @Resource(name = "caffeineCacheProjectWorkEntity")
    private Cache<String, ProjectWorkEntity> caffeineCacheProjectWorkEntity;

    public GetPhoneVO getPhone(){
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s?act=getPhone&token=%s&iid=%s&did=&country=%s&operator=&provi=&city=&seq=0&mobile=",projectWorkEntity.getFirefoxBaseUrl(), projectWorkEntity.getFirefoxToken(), projectWorkEntity.getFirefoxIid(), projectWorkEntity.getFirefoxCountry());
            String resp = HttpUtil.get(getPhoneHttp);
            String[] split = resp.split("\\|");
            if (split.length == 8) {
                String number = split[0];
                String pkey = split[1];
                String time = split[2];
                String country = split[3];
                String countryCode = split[4];
                String other = split[5];
                String com = split[6];
                String phone = split[7];
                GetPhoneVO getPhoneVo = new GetPhoneVO().setNumber(number).setPkey(pkey).setTime(time).setCom(com).setCountry(country).setCountryCode(countryCode).setPhone(phone).setOther(other);
                return getPhoneVo;
            }
        }catch (Exception e) {

        }
        return null;
    }

    @Override
    public String getPhoneCode(String pKey) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s?act=getPhoneCode&token=%s&pkey=%s",projectWorkEntity.getFirefoxBaseUrl(),projectWorkEntity.getFirefoxToken(),pKey);
            String resp = HttpUtil.get(getPhoneHttp);
            String[] split = resp.split("\\|");
            if (split.length == 3) {
                return split[1];
            }
        }catch (Exception e) {

        }
        return null;
    }

    @Override
    public boolean setRel(String pKey) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s?act=setRel&token=%s&pkey=%s", projectWorkEntity.getFirefoxBaseUrl(),projectWorkEntity.getFirefoxToken(),pKey);
            String resp = HttpUtil.get(getPhoneHttp);
            String[] split = resp.split("\\|");
            if ("1".equals(split[0])) {
                return true;
            }else {
                String status = split[1];
                if ("-6".equals(status) || "-5".equals(status) || "-4".equals(status) || "-3".equals(status)) {
                    return true;
                }
            }
        }catch (Exception e) {

        }
        return false;
    }

    @Override
    public boolean withBlackMobile(String pKey) {
        return false;
    }
}
