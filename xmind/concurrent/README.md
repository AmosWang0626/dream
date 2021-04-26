# Java并发编程

## 三大问题

#### 1. 原子性

Java底层保证赋值写操作是原子性的，复杂的不能保证

#### 2. 可见性

MESI协议，flush、refresh，配合起来，解决可见性问题

#### 3. 有序性

- 编译阶段，javac、JIT
- 现代处理器提升性能，指令乱序和猜测执行机制
- 高速缓存和写缓冲器内存重排序乱序（LoadLoad,StoreStore,LoadStore,StoreLoad）

## synchronized

- 原子性：同一段代码只能一个执行。
- 可见性：同步代码块，写操作释放锁时，强制flush；读操作时，强制refresh。
- 有序性：通过各种内存屏障，来解决LoadLoad等等重排序。

## volatile

## 主要涉及内容

- synchronized
- volatile
- CAS（Compare And Set）
- AQS（AbstractQueuedSynchronizer）
- JUC（java.util.concurrent）
- 线程池

## CAS

> 无锁化，实现线程安全

Compare And Set（或者 Compare And Swap）

顾名思义，先比较，再赋值。

两个线程并发执行，只会有一个线程能完成CAS，另一个线程CAS失败，要么放弃执行、要么自旋重试（重新获取最新值，再执行赋值操作）。

一般使用 Unsafe 类，但 Unsafe 不是直接能用的，所以要通过特殊的方式使用，具体使用方法后续补充上。
`sun.misc.Unsafe.getUnsafe().compareAndSwapObject()`

```java
public class UnsafeTest {

    public void execute() {
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
```

## AQS

> 一种锁的实现方式，例如常用的 ReentrantLock 里边的锁就是基于 AQS 实现的

Abstract Queued Synchronizer

核心思想有三：

- state 同步状态（volatile int），初始值为0，如果同一个线程多次加锁，state++（可重入），释放锁时，加几次锁就得解锁几次
- Queue 队列，都来加锁，加锁失败自然是要进队列的
- exclusiveOwnerThread 当前独占线程

```java
/**
 * ReentrantLock
 */
public class ReentrantLock implements Lock {
    abstract static class Sync extends AbstractQueuedSynchronizer {
    }

    /**
     * 非公平锁
     */
    static final class NonfairSync extends Sync {
    }

    /**
     * 公平锁
     */
    static final class FairSync extends Sync {
    }
}

/**
 * 看下类继承关系，以及类结构
 */
public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer {
    /**
     * 对头
     */
    private transient volatile Node head;
    /**
     * 队尾
     */
    private transient volatile Node tail;
    /**
     * 状态
     */
    private volatile int state;

    static final class Node {
        volatile Node prev;
        volatile Node next;
        volatile Thread thread;
    }
}

public abstract class AbstractOwnableSynchronizer {
    /**
     * 独占锁的线程
     */
    private transient Thread exclusiveOwnerThread;
}
```