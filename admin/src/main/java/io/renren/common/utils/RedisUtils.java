package io.renren.common.utils;

import com.google.gson.Gson;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.enums.RedisKeys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-07-17 21:12
 */
@Component
@Slf4j
public class RedisUtils {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    private HashOperations<String, String, Object> hashOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;
    @Autowired
    private SetOperations<String, Object> setOperations;
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;
    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;
    private final static Gson gson = new Gson();

    public void set(String key, Object value, long expire){
        valueOperations.set(key, toJson(value));
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    public <T> T get(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, NOT_EXPIRE);
    }

    public String get(String key, long expire) {
        String value = valueOperations.get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return value;
    }

    public String get(String key) {
        return get(key, NOT_EXPIRE);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object){
        if(object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String){
            return String.valueOf(object);
        }
        return gson.toJson(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz){
        return gson.fromJson(json, clazz);
    }

    /**
     * 查询手机号注册次数
     */
    public Integer getPhoneRegisterCount(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return 0;
        }
        try {
            Object object = redisTemplate.opsForHash()
                    .get(RedisKeys.RedisKeys10.getValue(), phone);
            if (object != null) {
                return Integer.valueOf(String.valueOf(object));
            }
        } catch (Exception e) {
            log.error("查询手机号注册次数异常 {}, {}", phone, e);
        }
        return 0;
    }

    /**
     * 查询手机号是否可用
     * @return true:代表手机号可用可购买
     */
    public Boolean getPhoneRegisterState(String phone) {
        try {
            if (StringUtils.isNotEmpty(phone)) {
                Object object = redisTemplate.opsForValue()
                        .get(RedisKeys.RedisKeys12.getValue(phone));
                if (object != null) {
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("查询手机号注册次数异常 {}, {}", phone, e);
        }
        return true;
    }

}
