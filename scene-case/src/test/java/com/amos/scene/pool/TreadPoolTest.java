package com.amos.scene.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池测试
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/24
 */
public class TreadPoolTest {

    private static final int NCPU = Runtime.getRuntime().availableProcessors();

    /**
     * 拒绝策略
     * <p>
     * 1. CallerRunsPolicy 不抛弃不放弃(重试直至执行成功)
     * 2. AbortPolicy 拒绝任务并且抛出异常
     * 3. DiscardPolicy 拒绝任务不抛出异常
     * 4. DiscardOldestPolicy 喜新厌旧(抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列)
     */
    public static void main(String[] args) {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("io-thread-%d").build();
        ThreadPoolExecutor ioExecutor = new ThreadPoolExecutor(
                NCPU, 2 * NCPU, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(200), factory,
                new ThreadPoolExecutor.CallerRunsPolicy());

        System.out.printf("核心线程数 [%d], 最大线程数 [%d]\n", NCPU, NCPU * 2);

        for (int i = 0; i < 100; i++) {
            final int num = i;
            ioExecutor.execute(() -> {
                System.out.println(Thread.currentThread().getId() + " 处理任务ID [" + num + "]>>> AMOS 测试! ");

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

    }

}
