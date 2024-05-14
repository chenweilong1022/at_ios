package io.renren.modules.client.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.utils.ConfigConstant;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.FirefoxService;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.client.vo.GetPhoneVO;
import io.renren.modules.ltt.enums.CountryCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 21:28
 */
@Service("cardMeServiceImpl")
@Game
@Slf4j
public class CardMeServiceImpl implements FirefoxService {

    @Resource(name = "caffeineCacheProjectWorkEntity")
    private Cache<String, ProjectWorkEntity> caffeineCacheProjectWorkEntity;

    public GetPhoneVO getPhone(){//http://www.firefox.fun/yhapi.ashx?act=getPhone&token=e2b49358e15f981824a022c629b55146_46756&iid=127&did=&country=th&operator=&provi=&city=&seq=0&mobile=
        try {//e2b49358e15f981824a022c629b55146_46756&pkey=C4AAE53ACE3A55E42DF9C491DBA98C91CD1AC433BAAB7CC0
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s?act=getPhone&token=%s&iid=%s&did=&country=%s&operator=&provi=&city=&seq=0&mobile=",
                    projectWorkEntity.getFirefoxBaseUrl(), projectWorkEntity.getFirefoxToken(),
                    projectWorkEntity.getFirefoxIid(), "tha");


            log.info("FirefoxService_getPhone param:{}", getPhoneHttp);
            String resp = HttpUtil.get(getPhoneHttp);
            log.info("FirefoxService_getPhone result:{}", resp);

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
                GetPhoneVO getPhoneVo = new GetPhoneVO().setNumber(number)
                        .setPkey(pkey).setTime(time).setCom(com).setCountry(country)
                        .setCountryCode(countryCode)
                        .setPhone(String.format("%s%s", CountryCode.CountryCode1.getKey(), phone))
                        .setOther(other);
                return getPhoneVo;
            }
            //{"com":"COM47","country":"hkg","countryCode":"852","number":"1","other":"","phone":"62783463",
            // "pkey":"12D6A90AC0EABC6CDC1290CF7FF39A1365C1F525A1D0587D","time":"2024-04-05T21:39:16"}
        }catch (Exception e) {
            log.info("FirefoxService_getPhone error:{}", e);
        }
        return null;
    }

    @Override//http://www.firefox.fun/yhapi.ashx?act=getPhoneCode&token=%s&pkey=%s
    public String getPhoneCode(String pKey) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s?act=getPhoneCode&token=%s&pkey=%s",
                    projectWorkEntity.getFirefoxBaseUrl(),projectWorkEntity.getFirefoxToken(),pKey);

            log.info("FirefoxService_getPhoneCode param:{}", getPhoneHttp);
            String resp = HttpUtil.get(getPhoneHttp);
            log.info("FirefoxService_getPhoneCode result:{}", resp);

            String[] split = resp.split("\\|");
            if (split.length == 3) {
                return split[1];
            }
        }catch (Exception e) {
            log.info("FirefoxService_getPhoneCode error:{}", e);

        }
        return null;
    }

    @Override
    public boolean setRel(String pKey) {
        try {
            ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
            String getPhoneHttp = String.format("%s?act=setRel&token=%s&pkey=%s",
                    projectWorkEntity.getFirefoxBaseUrl(),projectWorkEntity.getFirefoxToken(),pKey);

            log.info("FirefoxService_setRel param:{}", getPhoneHttp);
            String resp = HttpUtil.get(getPhoneHttp);
            log.info("FirefoxService_setRel result:{}", resp);

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
            log.info("FirefoxService_setRel error:{}", e);
        }
        return false;
    }

    @Override
    public boolean withBlackMobile(String pKey) {
        return false;
    }
}
