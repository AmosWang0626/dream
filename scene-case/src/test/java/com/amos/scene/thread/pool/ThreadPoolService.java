package com.amos.scene.thread.pool;


import com.alibaba.ttl.threadpool.TtlExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The Spring thread pool is not static and cannot be used by static methods. It also has no way to monitor its internal status,
 * that is why we need this thread pool service.<p/>
 * Please use this thread pool throughout the project. Don't create separate thread pools.
 *
 * @author from robot-search（适配多租户 TtlExecutors）
 */
public class ThreadPoolService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolService.class);

    /**
     * number of cpu processors
     */
    private static final int NCPU = Runtime.getRuntime().availableProcessors();
    /**
     * shutdown wait times
     */
    private static final int SHUTDOWN_TIMEOUT = 2;

    private static final long LIVE_TIME = 60;

    private static final int WORK_QUEUE_SIZE = 50000;


    /**
     * 处理io密集型任务
     */
    private static ExecutorService ioIntensiveExecutor;

    /**
     * 处理cpu密集型
     */
    private static ScheduledExecutorService cpuIntensiveExecutor;

    /**
     * 单线程执行器
     */
    private static ScheduledExecutorService singleExecutor;

    /**
     * TODO make it configurable
     */
    private static final boolean ALLOW_DUMP = true;

    // Start ASAP
    static {
        initPool();
        recurDumpStatus();
    }

    private static void initPool() {
        singleExecutor = new ScheduledThreadPoolExecutorDecorator(1,
                new CustomThreadFactory("singleExecutor", Thread.NORM_PRIORITY));

        ioIntensiveExecutor = new CustomThreadPoolExecutor(NCPU, 2 * NCPU, LIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(WORK_QUEUE_SIZE),
                new CustomThreadFactory("ioIntensiveExecutor", Thread.NORM_PRIORITY),
                new ThreadPoolExecutor.DiscardOldestPolicy());

        cpuIntensiveExecutor = new ScheduledThreadPoolExecutorDecorator(NCPU,
                new CustomThreadFactory("cpuIntensiveExecutor", Thread.MAX_PRIORITY));

        // 多租户线程池线程复用，ThreadLocal可能为旧的兼容 
        singleExecutor = TtlExecutors.getTtlScheduledExecutorService(singleExecutor);
        ioIntensiveExecutor = TtlExecutors.getTtlExecutorService(ioIntensiveExecutor);
        cpuIntensiveExecutor = TtlExecutors.getTtlScheduledExecutorService(cpuIntensiveExecutor);

        // register a hook , Only call this when jvm was shutdown.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ioIntensiveExecutor.shutdown();
            singleExecutor.shutdown();
            try {
                if (!ioIntensiveExecutor.awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.SECONDS)
                        && !cpuIntensiveExecutor.awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.SECONDS)
                        && !singleExecutor.awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.SECONDS)) {
                    LOGGER.warn("threadPool did not shutdown in the specified time.");
                    ioIntensiveExecutor.shutdownNow();
                    cpuIntensiveExecutor.shutdownNow();
                    singleExecutor.shutdownNow();
                    LOGGER.warn("threadPool was shutdownNow. all tasks will not be executed.");
                } else {
                    LOGGER.info("threadPool was shutdown complete.");
                }
            } catch (InterruptedException e) {
                LOGGER.error("threadPool was shutdown occurred interrupted exception error...");
            }
        }));
    }

    /**
     * Fast pool has more than 10 threads in the pool so it should be able to execute tasks as soon as it is added to pool.
     */
    synchronized static public ExecutorService ofIoIntensiveExecutor() {
        // In some cases the pool is not started in static block.
        if (ioIntensiveExecutor == null) {
            initPool();
        }
        return ioIntensiveExecutor;
    }

    synchronized static public ExecutorService ofCpuIntensiveExecutor() {
        if (cpuIntensiveExecutor == null) {
            initPool();
        }
        return cpuIntensiveExecutor;
    }


    /**
     * Single thread pool is used for the cases like we sent tasks to a converter but converter can only handle one
     * task at a time,
     * more tasks can be cached in the thread pool
     */
    static public ExecutorService ofSingleThreadPool() {
        if (singleExecutor == null) {
            initPool();
        }
        return singleExecutor;
    }

    /**
     * To monitor the thread pool status.
     * <br/> We don't use the <code>scheduleAtFixedRate()</code> because any exception will break the following execution.
     */
    static private void recurDumpStatus() {
        if (ALLOW_DUMP) {
            cpuIntensiveExecutor.scheduleWithFixedDelay(() -> {
                try {
                    dumpPoolStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 0, 60, TimeUnit.SECONDS);
        }
    }

    private static void dumpPoolStatus() {
        if (ioIntensiveExecutor instanceof CustomThreadPoolExecutor) {
            logStatus((CustomThreadPoolExecutor) ioIntensiveExecutor, "io intensive pool");
        }
        if (cpuIntensiveExecutor instanceof ScheduledThreadPoolExecutorDecorator) {
            logStatus((ScheduledThreadPoolExecutorDecorator) cpuIntensiveExecutor, "cpu intensive pool");
        }
        if (singleExecutor instanceof ScheduledThreadPoolExecutorDecorator) {
            logStatus((ScheduledThreadPoolExecutorDecorator) singleExecutor, "single thread pool");
        }
    }

    private static void logStatus(ThreadPoolExecutor executor, String name) {
        LOGGER.warn(String.format(
                "%s - pool has %s pending tasks, and %s active threads in all %s.[%d(current)/%d(free)]",
                name,
                executor.getQueue().size(),
                executor.getActiveCount(),
                executor.getPoolSize(),
                bytesToMB(Runtime.getRuntime().totalMemory()),
                bytesToMB(Runtime.getRuntime().freeMemory()))
        );

        if (executor.getQueue().size() > 1) {
            for (Runnable r : executor.getQueue()) {
                LOGGER.warn("pending job: " + Integer.toHexString(r.hashCode()));
            }
        }
    }

    private static long bytesToMB(long value) {
        return value / 1024 / 1024;
    }

    /**
     * 常规线程池
     */
    public static class CustomThreadPoolExecutor extends ThreadPoolExecutor {

        CustomThreadPoolExecutor(int corePoolSize,
                                 int maximumPoolSize,
                                 long keepAliveTime,
                                 TimeUnit unit,
                                 BlockingQueue<Runnable> workQueue,
                                 CustomThreadFactory threadFactory,
                                 RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            LOGGER.warn("start execute task [{}]", Integer.toHexString(r.hashCode()));
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            LOGGER.warn("done execute task [{}]", Integer.toHexString(r.hashCode()));
        }

    }


    /**
     * To log information to the log
     */
    public static class ScheduledThreadPoolExecutorDecorator extends ScheduledThreadPoolExecutor {

        private final SimpleTriggerContext triggerContext = new SimpleTriggerContext();

        ScheduledThreadPoolExecutorDecorator(int corePoolSize, CustomThreadFactory threadFactory) {
            super(corePoolSize, threadFactory);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            LOGGER.debug(String.format("start execute task %s", Integer.toHexString(r.hashCode())));
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            LOGGER.debug(String.format("done execute task %s", Integer.toHexString(r.hashCode())));
        }

        /**
         * 根据Crontab 表达式执行计划任务
         */
        public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
            Date scheduledExecutionTime = trigger.nextExecutionTime(this.triggerContext);
            if (scheduledExecutionTime == null) {
                return null;
            }
            long initialDelay = scheduledExecutionTime.getTime() - System.currentTimeMillis();

            return super.schedule(task, initialDelay, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 自定义TF线程生成策略
     */
    private static class CustomThreadFactory implements ThreadFactory {
        static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namespace;
        final int priority;

        CustomThreadFactory(String namespace, int priority) {
            this.namespace = namespace + "-" + POOL_NUMBER.getAndIncrement();
            this.priority = priority;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namespace + "-" + threadNumber.getAndIncrement() + "");
            t.setPriority(this.priority);
            return t;
        }
    }
}
