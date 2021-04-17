# 线程池总结

> 最近参加了几次面试，基本每一面都会提到线程池。
>
> 这几次面试在没准备的情况下进行的，也想看看自己的真实水平，确实发挥不稳定，熟悉的领域，顺风顺水，拿了几个不错的offer；不熟悉的，就翻车了，被喷初级。

## 1、核心参数

> 面试官：你能说一说线程池的核心参数吗？

有几个核心参数，分别是什么意思？

当时掰指头算了一下，6个，但心里知道是7个，emm

```java
public class ThreadPoolExecutor extends AbstractExecutorService {

    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
    }

}
```

1. corePoolSize（核心线程数）
2. maximumPoolSize（最大线程数）
3. keepAliveTime（无任务时保持存活时间）
    - 一来，用作 **非核心线程** 回收
    - 二来，用作 **设置了allowCoreThreadTimeOut的核心线程** 回收
4. unit（keepAliveTime时间单位）
    - 最终会被转化成纳秒
    - `this.keepAliveTime = unit.toNanos(keepAliveTime);`
5. workQueue（阻塞队列）
    - 先进先出
    - `new LinkedBlockingQueue<Runnable>()` 无参构造方法犹如无界队列
    - `new SynchronousQueue<Runnable>()` 没有数据缓冲，有公平与非公平两种实现 `fair ? TransferQueue : TransferStack`
6. threadFactory（线程工厂）
    - 常用于设置名称、优先级、daemon
    - daemon 也即守护线程（与之对应的是用户线程），当所有线程都是守护线程时，JVM退出。
7. handler（线程池拒绝策略）
    - AbortPolicy：直接抛异常处理，默认的拒绝策略
    - DiscardPolicy：直接抛弃不处理，不抛异常
    - DiscardOldestPolicy：丢弃队列中最老的任务
    - CallerRunsPolicy：将任务分配给当前执行execute方法线程来处理
    - 自定义拒绝策略（入库后边补偿或者人工处理）

## 2、你可能不知道的设置

#### prestartAllCoreThreads()

> 面试官：说说线程池工作流程？还没全部说完，面试官打断说，核心线程是执行时才创建的，还是一开始就全部创建了？

一说，还真有点怀疑，根据自己学的，是执行时创建的呀，然后就被带偏了。

面试官语重心长地说，要多看书，书上的内容一般都是经得起推敲的，害。

然后了解到了这个方法 prestartAllCoreThreads，调用它会初始化所有核心线程。

`ioExecutor.prestartAllCoreThreads();`

`ioExecutor.prestartCoreThread();` 顺便知道了这个方法，如果核心线程数未满，则创建一个核心线程。

#### allowCoreThreadTimeOut

> 面试官：举一个场景，我们项目中有20个地方使用了线程池，有几个线程池每隔2小时才会执行一次，我怎么在他不使用时回收掉那些核心线程，等有任务时再创建？

使得核心线程在 keepAliveTime 后可以被回收。

`ioExecutor.allowCoreThreadTimeOut(true);`

## 3、线程池有哪几种实现方式

#### Executors >>> ThreadPoolExecutor

- Executors.newFixedThreadPool

   ```java
   public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
       return new ThreadPoolExecutor(nThreads, nThreads,
                                     0L, TimeUnit.MILLISECONDS,
                                     new LinkedBlockingQueue<Runnable>(),
                                     threadFactory);
   }
   ```

- Executors.newSingleThreadExecutor

   ```java
   public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
       return new FinalizableDelegatedExecutorService
           (new ThreadPoolExecutor(1, 1,
                                   0L, TimeUnit.MILLISECONDS,
                                   new LinkedBlockingQueue<Runnable>(),
                                   threadFactory));
   }
   ```

- Executors.newCachedThreadPool
   ```java
   public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
       return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                     60L, TimeUnit.SECONDS,
                                     new SynchronousQueue<Runnable>(),
                                     threadFactory);
   }
   ```

#### Executors >>> ScheduledThreadPoolExecutor

- Executors.newScheduledThreadPool
   ```java
   public static ScheduledExecutorService newScheduledThreadPool(
         int corePoolSize, ThreadFactory threadFactory) {
     return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
   }
   ```

- Executors.newScheduledThreadPool

    ```java
    public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
        return new DelegatedScheduledExecutorService
            (new ScheduledThreadPoolExecutor(1, threadFactory));
    }
    ```

## 4、项目中线程池用1个好，还是多个好

尽量不要使用一个。如果只有一个线程池，项目中某个耗时操作占用着不释放，那其他地方来使用时，只能排队等待，导致项目case。

另外线程堆栈日志中，一般是有线程名字的，每个线程池应该按对应业务独立命名，方便排查异常。