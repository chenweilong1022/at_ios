package io.renren.modules.client.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import io.renren.common.utils.ConfigConstant;
import io.renren.datasources.annotation.Game;
import io.renren.modules.client.ProxyService;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.ltt.enums.ProxyStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/30 22:03
 */
@Service("lunaProxyImpl")
@Game
@Slf4j
public class LunaProxyImpl implements ProxyService {

    @Resource(name = "caffeineCacheListString")
    private Cache<String, Queue<String>> caffeineCacheListString;
    @Resource(name = "caffeineCacheProjectWorkEntity")
    private Cache<String, ProjectWorkEntity> caffeineCacheProjectWorkEntity;



    private boolean isProxyUse(String ip) {
        try {
            String format = String.format("curl --socks5 %s ipinfo.io",ip);
            List<String> strings = RuntimeUtil.execForLines(format);
            for (String string : strings) {
                if (string.toLowerCase().contains("country") && string.toLowerCase().contains("th")) {
                    return true;
                }
            }
            return false;
        }catch (Exception e) {

        }
        return false;
    }


    @Override
    public synchronized String getflowip() {
        //如果是ip2world
        ProjectWorkEntity projectWorkEntity = caffeineCacheProjectWorkEntity.getIfPresent(ConfigConstant.PROJECT_WORK_KEY);
        if (ObjectUtil.isNull(projectWorkEntity)) {
            return null;
        }
        if (ProxyStatus.ProxyStatus2.getKey().equals(projectWorkEntity.getProxy())) {
            String ip = getflowip1();
            if (StrUtil.isEmpty(ip)) {
                return ip;
            }
            return String.format("socks5://%s", ip);
        }else if (ProxyStatus.ProxyStatus3.getKey().equals(projectWorkEntity.getProxy())) {
            String ip = getflowip2();
            if (StrUtil.isEmpty(ip)) {
                return ip;
            }
            String format = String.format("socks5://%s", ip);
            return format;
        }
        Queue<String> getflowip = caffeineCacheListString.getIfPresent("getflowip");
        String ip = null;
        if (CollUtil.isEmpty(getflowip)) {
            String getPhoneHttp = String.format("https://tq.lunaproxy.com/getflowip?neek=1136881&num=500&type=1&sep=1&regions=%s&ip_si=1&level=1&sb=","th");
            String resp = HttpUtil.get(getPhoneHttp);
            if (JSONUtil.isJson(resp)) {
                return null;
            }
            String[] split = resp.split("\r\n");
            Queue<String> getflowipNew = new LinkedList<>();
            for (String s : split) {
                getflowipNew.offer(s);
            }
            ip = getflowipNew.poll();
            caffeineCacheListString.put("getflowip",getflowipNew);
        }else {
            ip = getflowip.poll();
            caffeineCacheListString.put("getflowip",getflowip);
        }
        log.info("proxy = {}","lunaproxy");
        if (StrUtil.isEmpty(ip)) {
            log.info("proxy = {}","lunaproxy empty");
            return ip;
        }
        log.info("proxy = {} ip = {}","lunaproxy",ip);
        return String.format("socks5://%s", ip);
    }

    public static void main(String[] args) {

        String encode = Base64.encode(new File("/Users/chenweilong/Downloads/2024-01-29 15.56.46.jpg"));
        System.out.println(encode);

    }

    @Override
    public String getflowip1() {
        Queue<String> getflowip = caffeineCacheListString.getIfPresent("getflowip");
        String ip = null;
        if (CollUtil.isEmpty(getflowip)) {
            String getPhoneHttp = String.format("http://api.proxy.ip2world.com/getProxyIp?regions=%s&lb=1&return_type=txt&protocol=socks5&num=50","th");
            String resp = HttpUtil.get(getPhoneHttp);
            String[] split = resp.split("\r\n");
            Queue<String> getflowipNew = new LinkedList<>();
            for (String s : split) {
                getflowipNew.offer(s);
            }
            ip = getflowipNew.poll();
            caffeineCacheListString.put("getflowip",getflowipNew);
        }else {
            ip = getflowip.poll();
            caffeineCacheListString.put("getflowip",getflowip);
        }
        log.info("proxy = {}","ip2world");
        return ip;
    }

    @Override
    public String getflowip2() {
        return "";
    }
}
