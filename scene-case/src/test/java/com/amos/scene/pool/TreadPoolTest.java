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
