package com.amos.scene.thread.pool;

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
        int corePoolSize = NCPU;
        int maxPoolSize = 2 * NCPU;
        int queueCapacity = 200;

        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("io-线程-%d").build();
        ThreadPoolExecutor ioExecutor = new ThreadPoolExecutor(
                corePoolSize, maxPoolSize, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity), factory,
                new ThreadPoolExecutor.CallerRunsPolicy());

        // 如果核心线程数没有慢，则创建一个核心线程
        ioExecutor.prestartCoreThread();
        // 初始化所有和核心线程
        ioExecutor.prestartAllCoreThreads();

        // 核心线程可回收
        ioExecutor.allowCoreThreadTimeOut(true);

        System.out.printf("核心线程数 [%d], 最大线程数 [%d], 任务队列容量 [%d]\n", corePoolSize, maxPoolSize, queueCapacity);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        for (int i = 0; i < 100; i++) {
            final int num = i;
            ioExecutor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " 处理任务ID [" + num + "]>>> AMOS 测试! ");

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        ioExecutor.shutdown();
    }

}
