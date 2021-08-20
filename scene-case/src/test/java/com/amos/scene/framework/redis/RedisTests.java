package com.amos.scene.framework.redis;

import com.amos.scene.facade.ILock;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
class RedisTests {

    @Resource
    private ILock redisLock;

    private static final String LOCK_KEY = "amos_lock";

    @Test
    void contextLoads() throws InterruptedException {
        AtomicInteger count = new AtomicInteger();

        Runnable runnable = () -> {
            for (int i = 0; i < 1000; i++) {
                String lockVal = redisLock.lock(LOCK_KEY, 1000, 1000);
                if (Objects.nonNull(lockVal)) {
                    try {
                        System.out.println("increment " + (count.incrementAndGet()));
                    } finally {
                        redisLock.unlock(LOCK_KEY, lockVal);
                    }
                }
            }
        };

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);
        Thread thread3 = new Thread(runnable);
        Thread thread4 = new Thread(runnable);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();


        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

        System.out.println(">>>>>>>>>>>>>>>> Count: " + count.get());
    }

}
