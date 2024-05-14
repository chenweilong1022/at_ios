package io.renren.modules.ltt.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    @Async
    public void executeAsync(Runnable task) {
        task.run();
    }
}
