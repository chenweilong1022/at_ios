package io.renren.modules.task;

import io.renren.modules.ltt.enums.LockMapKeyResource;
import io.renren.modules.ltt.service.AtUserDataSummaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuyuchan
 * @email liuyuchan286@gmail.com
 * @date 2023/11/21 18:45
 */
@Component
@Slf4j
@EnableAsync
@Profile({"prod", "register"})
public class DataSummaryTask {

    @Autowired
    private ConcurrentHashMap<String, Lock> lockMap;

    @Autowired
    private AtUserDataSummaryService atUserDataSummaryService;

    /**
     * 统计账号和line数据，保存数据库
     */
    @Scheduled(fixedDelay = 10 * 60 * 1000)
    @Transactional(rollbackFor = Exception.class)
    @Async
    public void task1() {
        String keyByResource = LockMapKeyResource.getKeyByResource(LockMapKeyResource.LockMapKeyResource11, "1");

        Lock lock = lockMap.computeIfAbsent(keyByResource, k -> new ReentrantLock());

        boolean triedLock = lock.tryLock();
        log.info("DataSummaryTask_start keyByResource = {} 获取的锁为 = {}", keyByResource, triedLock);
        if (triedLock) {
            try {
                LocalTime now = LocalTime.now();

                // 设置比较的时间点，这里为早上八点
                LocalTime comparisonTime = LocalTime.of(8, 0);

                // 判断当前时间是否大于八点
                LocalDate searchTime = now.isAfter(comparisonTime) ? LocalDate.now() : LocalDate.now().minusDays(1);

                //统计今日,所有国家
                atUserDataSummaryService.saveAtUserDataSummary(searchTime);
            } finally {
                lock.unlock();
            }
        } else {
            log.info("DataSummaryTask keyByResource = {} 在执行", keyByResource);
        }
    }

}
