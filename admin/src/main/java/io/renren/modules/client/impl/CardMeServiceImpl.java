package io.renren.modules.client.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.FirefoxService;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.client.vo.CardMeGetPhoneVO;
import io.renren.modules.client.vo.GetPhoneVO;
import io.renren.modules.client.vo.GetSmsVO;
import io.renren.modules.client.vo.ReleaseMobileVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 21:28
 */
@Service("cardMeServiceImpl")
@Game
public class CardMeServiceImpl implements FirefoxService {

    @Resource(name = "caffeineCacheProjectWorkEntity")
    private Cache<String, ProjectWorkEntity> caffeineCacheProjectWorkEntity;

    static String baseHttp = "http://134.122.130.205:8880";
    static String token = "545148dc498842ca8cea980b1a677b27";

    static String projectId = "202";

    public GetPhoneVO getPhone(){
        try {
            String getPhoneHttp =  String.format("%s/app/cdcardgroup/getDeviceIdByProjectId",baseHttp);
            Map<String,String> param = new HashMap<>();
            param.put("projectId",projectId);
            String jsonStr = JSONUtil.toJsonStr(param);
            String resp = HttpUtil.createPost(getPhoneHttp).header("token",token).body(jsonStr).execute().body();
            System.out.println(resp);
            CardMeGetPhoneVO cardMeGetPhoneVO = JSON.parseObject(resp, CardMeGetPhoneVO.class);
            if (ObjectUtil.isNotNull(cardMeGetPhoneVO) && ObjectUtil.isNotNull(cardMeGetPhoneVO.getData())) {
                String phone = cardMeGetPhoneVO.getData().getPhone();
                GetPhoneVO getPhoneVo = new GetPhoneVO().setNumber("").setPkey(cardMeGetPhoneVO.getData().getIccid()).setTime(null).setCom("").setCountry("").setCountryCode("").setPhone(phone).setOther("");
                return getPhoneVo;
            }
        }catch (Exception e) {
        }
        return null;
    }

    public String extractVerificationCode(String smsText) {
        // 使用正则表达式匹配短信内容中的验证码
        Pattern pattern = Pattern.compile("\\d{6}"); // 此处使用六位数字作为验证码的示例
        Matcher matcher = pattern.matcher(smsText);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    @Override
    public String getPhoneCode(String pKey) {
        try {
            String getPhoneHttp =  String.format("%s/app/cdcardlock/getSms",baseHttp);
            Map<String,String> param = new HashMap<>();
            param.put("projectId",projectId);
            param.put("iccid",pKey);
            String jsonStr = JSONUtil.toJsonStr(param);
            String resp = HttpUtil.createPost(getPhoneHttp).header("token",token).body(jsonStr).execute().body();
            System.out.println(resp);
            GetSmsVO getSmsVO = JSON.parseObject(resp, GetSmsVO.class);
            if (ObjectUtil.isNotNull(getSmsVO) && StrUtil.isNotEmpty(getSmsVO.getData())) {
                String data = getSmsVO.getData();
                data = extractVerificationCode(data);
                return data;
            }
        }catch (Exception e) {

        }
        return null;
    }

    @Override
    public boolean setRel(String pKey) {
        try {
            String getPhoneHttp =  String.format("%s/app/cdcardlock/releaseMobile",baseHttp);
            Map<String,String> param = new HashMap<>();
            param.put("projectId",projectId);
            param.put("iccid",pKey);
            String jsonStr = JSONUtil.toJsonStr(param);
            String resp = HttpUtil.createPost(getPhoneHttp).header("token",token).body(jsonStr).execute().body();
            System.out.println(resp);
            ReleaseMobileVO releaseMobileVO = JSON.parseObject(resp, ReleaseMobileVO.class);
            if (ObjectUtil.isNotNull(releaseMobileVO) && 0 ==  releaseMobileVO.getCode()) {
                return true;
            }
            if (ObjectUtil.isNotNull(releaseMobileVO) && 500 ==  releaseMobileVO.getCode()) {
                return true;
            }
            if (ObjectUtil.isNotNull(releaseMobileVO) && ObjectUtil.isNotNull(releaseMobileVO.getData())) {
                return releaseMobileVO.getData();
            }
        }catch (Exception e) {

        }
        return false;
    }

    @Override
    public boolean withBlackMobile(String pKey) {
        try {
            String getPhoneHttp =  String.format("%s/app/cdcardlock/withBlackMobile",baseHttp);
            Map<String,String> param = new HashMap<>();
            param.put("projectId",projectId);
            param.put("iccid",pKey);
            String jsonStr = JSONUtil.toJsonStr(param);
            String resp = HttpUtil.createPost(getPhoneHttp).header("token",token).body(jsonStr).execute().body();
            System.out.println(resp);
            ReleaseMobileVO releaseMobileVO = JSON.parseObject(resp, ReleaseMobileVO.class);
            if (ObjectUtil.isNotNull(releaseMobileVO) && 0 ==  releaseMobileVO.getCode()) {
                return true;
            }
            if (ObjectUtil.isNotNull(releaseMobileVO) && 500 ==  releaseMobileVO.getCode()) {
                return true;
            }
            if (ObjectUtil.isNotNull(releaseMobileVO) && ObjectUtil.isNotNull(releaseMobileVO.getData())) {
                return releaseMobileVO.getData();
            }
        }catch (Exception e) {

        }
        return false;
    }
}
