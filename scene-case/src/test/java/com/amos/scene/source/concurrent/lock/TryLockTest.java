package com.amos.scene.source.concurrent.lock;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TryLockTest
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/20
 */
public class TryLockTest {

    private static int i = 0;

    @Test
    public void tryLock() {
        ReentrantLock lock = new ReentrantLock();
        Runnable runnable = () -> {
            while (lock.tryLock()) {
                int temp = i;
                try {
                    System.out.println("············ " + Thread.currentThread().getName() + " >>> " + temp);
//                    TimeUnit.MILLISECONDS.sleep(50);
                    if (++temp >= 20) {
                        break;
                    }
                } finally {
                    lock.unlock();
                    i = temp;
                }
            }
        };

        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();
        new Thread(runnable).start();

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("finish!");
    }

    @Test
    public void testIfConditionalAssignment() {
        int a = ThreadLocalRandom.current().nextBoolean() ? 4 : 10;
        int i;
        if ((i = a) > 5) {
            System.out.println(i);
        }
    }

}
