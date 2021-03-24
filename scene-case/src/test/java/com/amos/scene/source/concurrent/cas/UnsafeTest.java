package com.amos.scene.source.concurrent.cas;

import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * UnsafeTest
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/24
 */
public class UnsafeTest {

    @Test
    public void test() throws InterruptedException {
        final MyObject object = new MyObject();

        // 开启5个线程开始竞争
        new Thread(object).start();
        new Thread(object).start();
        new Thread(object).start();
        new Thread(object).start();
        new Thread(object).start();

        TimeUnit.SECONDS.sleep(15);
    }

    static class MyObject implements Runnable {

        int state;

        @Override
        public void run() {
            try {
                Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafe.setAccessible(true);
                Unsafe unsafe = (Unsafe) theUnsafe.get(null);

                while (true) {
                    // state == 100的话，可能只能有一个线程 break
                    if (state >= 100) {
                        break;
                    }

                    long offset = unsafe.objectFieldOffset(MyObject.class.getDeclaredField("state"));
                    int toState = state + 1;
                    boolean cas = unsafe.compareAndSwapInt(this, offset, state, toState);
                    if (cas) {
                        System.out.println("CAS 竞争成功: " + toState);
                    } else {
                        System.out.println("CAS 竞争失败，等待重试!");
                    }

                    TimeUnit.MILLISECONDS.sleep(500);
                }
            } catch (NoSuchFieldException | InterruptedException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
