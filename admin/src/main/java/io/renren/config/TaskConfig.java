package io.renren.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;


/**
 * 生成验证码配置
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-20 19:22
 */
@Configuration
public class TaskConfig {

    @Bean
    public ConcurrentHashMap<String, Lock> lockMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(600); // 设置线程池大小
        scheduler.setThreadNamePrefix("scheduled-task-");
        scheduler.setAwaitTerminationSeconds(200);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

    @Bean("poolExecutor")
    public ThreadPoolTaskExecutor poolExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //设置线程池参数信息
        taskExecutor.setCorePoolSize(200);
        taskExecutor.setMaxPoolSize(600);
        taskExecutor.setQueueCapacity(600);
        taskExecutor.setKeepAliveSeconds(60);
        taskExecutor.setThreadNamePrefix("myExecutor2--");
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(200);
        //修改拒绝策略为使用当前线程执行
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化线程池
        taskExecutor.initialize();
        return taskExecutor;
    }

}
