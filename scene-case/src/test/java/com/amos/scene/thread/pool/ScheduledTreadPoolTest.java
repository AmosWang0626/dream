package com.amos.scene.thread.pool;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Scheduled 线程池测试
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/4/17
 */
public class ScheduledTreadPoolTest {

    private static final int NCPU = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        int corePoolSize = NCPU;
        int queueCapacity = 200;

        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("scheduled-io-线程-%d").build();
        ScheduledThreadPoolExecutor ioExecutor = new ScheduledThreadPoolExecutor(
                corePoolSize, factory, new ThreadPoolExecutor.CallerRunsPolicy());

        System.out.printf("核心线程数 [%d], 最大线程数 Integer.MAX_VALUE, 任务队列容量 [%d]\n", corePoolSize, queueCapacity);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        for (int i = 0; i < 100; i++) {
            final int num = i;

            // 延迟执行任务
            ioExecutor.schedule(() -> System.out.println(
                    Thread.currentThread().getName() + " schedule 处理任务ID [" + num + "]>>> AMOS 测试! "
            ), 1, TimeUnit.SECONDS);

            // 延迟执行任务，同时每隔 period 再次执行任务
            ioExecutor.scheduleAtFixedRate(() -> System.out.println(
                    Thread.currentThread().getName() + " scheduleAtFixedRate 处理任务ID [" + num + "]>>> AMOS 测试! "
            ), 2, 2, TimeUnit.SECONDS);

            // 延迟执行任务，任务执行完成后间隔 delay 再次执行任务
            ioExecutor.scheduleWithFixedDelay(() -> System.out.println(
                    Thread.currentThread().getName() + " scheduleWithFixedDelay 处理任务ID [" + num + "]>>> AMOS 测试! "
            ), 3, 3, TimeUnit.SECONDS);
        }

        // 关掉，验证 scheduleXxx
//        ioExecutor.shutdown();
    }

}
