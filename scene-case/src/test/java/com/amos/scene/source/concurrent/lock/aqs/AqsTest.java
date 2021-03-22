package com.amos.scene.source.concurrent.lock.aqs;

import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * AQS Test
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/22
 */
public class AqsTest {

    @Test
    public void tryLock() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock(true);

        Num num = new Num();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 50000; j++) {
                    lock.lock();
                    try {
                        num.increment();
                    } finally {
                        lock.unlock();
                    }
                }
            });
            thread.start();
        }

        Thread.sleep(10000);

        System.out.println(num.getI());
    }

    static class Num {
        int i = 0;

        public void increment() {
            i++;
        }

        public int getI() {
            return i;
        }
    }


}
