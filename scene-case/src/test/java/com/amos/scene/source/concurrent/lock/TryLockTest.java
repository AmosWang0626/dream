package com.amos.scene.source.concurrent.lock;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TryLockTest
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/20
 */
public class TryLockTest {

    @Test
    public void tryLock() {
        ReentrantLock lock = new ReentrantLock();
        int i = 0;
        while (lock.tryLock()) {
            try {
                System.out.println("············ >>> " + i);
                if (++i >= 5) {
                    break;
                }
            } finally {
                lock.unlock();
            }
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
