package com.amos.scene.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * 线程池自定义拒绝策略测试
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/24
 */
public class CustomRejectedTreadPoolTest {


    /**
     * 拒绝策略
     * <p>
     * 1. CallerRunsPolicy 不抛弃不放弃(重试直至执行成功)<br>
     * 2. AbortPolicy 拒绝任务并且抛出异常<br>
     * 3. DiscardPolicy 拒绝任务不抛出异常<br>
     * 4. DiscardOldestPolicy 喜新厌旧(抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列)<br>
     */
    public static void main(String[] args) {
        int corePoolSize = 2;
        int maxPoolSize = 4;
        int queueCapacity = 2;

        // ThreadPoolExecutor.CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();

        RejectedExecutionHandler customRejectedPolicy = (r, executor) ->
                System.out.printf("触发自定义拒绝策略! 参数: %s\n",
                        r instanceof MyRunnable<?> ? Arrays.toString(((MyRunnable<?>) r).params) : null);

        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("io-thread-%d").build();
        ThreadPoolExecutor ioExecutor = new ThreadPoolExecutor(
                corePoolSize, maxPoolSize, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity), factory,
                customRejectedPolicy);

        System.out.printf("核心线程数 [%d], 最大线程数 [%d], 任务队列容量 [%d]\n", corePoolSize, maxPoolSize, queueCapacity);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        for (int i = 0; i < 10; i++) {
            ioExecutor.execute(new MyRunnable<>(i));
        }

        ioExecutor.shutdown();
    }

    /**
     * 自定义任务
     */
    private static class MyRunnable<T> implements Runnable {

        private final T[] params;

        @SafeVarargs
        public MyRunnable(T... params) {
            this.params = params;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " 处理任务ID [" + params[0] + "]>>> AMOS 测试! ");

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
