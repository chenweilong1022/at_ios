package io.renren.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.renren.modules.client.entity.ProjectWorkEntity;
import io.renren.modules.client.vo.CardJpSFGetPhoneSmsVO;
import io.renren.modules.ltt.entity.AtGroupEntity;
import io.renren.modules.ltt.entity.AtUserEntity;
import io.renren.modules.ltt.entity.AtUserTokenEntity;
import io.renren.modules.ltt.vo.IOSTaskVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Queue;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean(value = "caffeineCacheCodeListObject")
    public Cache<String, List<String>> caffeineCacheCodeListObject() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(5, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(value = "caffeineCacheCode")
    public Cache<String, String> caffeineCacheCode() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(7200, TimeUnit.DAYS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(value = "cardJpSFGetPhoneSmsVOCache")
    public Cache<String, CardJpSFGetPhoneSmsVO> cardJpSFGetPhoneSmsVOCache() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(7200, TimeUnit.DAYS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(value = "caffeineCacheListString")
    public Cache<String, Queue<String>> caffeineCacheListString() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(100, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(value = "jpSfPhoneCacheListString")
    public Cache<String, Queue<String>> jpSfPhoneCacheListString() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(100, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(value = "stringQueueCacheIOSTaskVO")
    public Cache<String, Queue<IOSTaskVO>> stringQueueCacheIOSTaskVO() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(70, TimeUnit.DAYS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(value = "stringListAtUserEntitys")
    public Cache<Integer, AtUserEntity> stringListAtUserEntitys() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(2, TimeUnit.HOURS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(value = "stringListAtUserTokenEntitys")
    public Cache<Integer, AtUserTokenEntity> stringListAtUserTokenEntitys() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(2, TimeUnit.HOURS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(value = "caffeineCacheProjectWorkEntity")
    public Cache<String, ProjectWorkEntity> caffeineCacheProjectWorkEntity() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(7200, TimeUnit.DAYS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(value = "caffeineCacheAtGroupEntity")
    public Cache<Integer, AtGroupEntity> caffeineCacheAtGroupEntity() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(7200, TimeUnit.DAYS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(value = "caffeineCacheDate")
    public Cache<Integer, Date> caffeineCacheDate() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(7200, TimeUnit.DAYS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(value = "caffeineCacheDateSearch")
    public Cache<Integer, Date> caffeineCacheDateSearch() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(7200, TimeUnit.DAYS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

    @Bean(value = "cardJpSms")
    public Cache<String, Date> cardJpSms() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后两个小时后过期
                .expireAfterWrite(30, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(50000)
                .build();
    }

    @Bean(value = "cardJpSmsOver")
    public Cache<String, String> cardJpSmsOver() {
        return Caffeine.newBuilder()
                // 5秒到期
                .expireAfterWrite(6, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(200)
                .build();
    }

}
