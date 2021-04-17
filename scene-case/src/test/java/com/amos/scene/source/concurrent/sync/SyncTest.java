package com.amos.scene.source.concurrent.sync;

import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

/**
 * Synchronized Test
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/18
 */
public class SyncTest {

    /*
     * 一丢丢想法
     * <p>
     * 1、加锁了的对象，还能被 GC 回收掉吗？
     * 2、书上说，synchronized引用的对象可以作为 GC root，那 GC root 会被 GC 回收掉吗？
     * 3、锁竞争，查看一下对象头的 MarkWord
     */

    /**
     * 内存布局分为3个区域：对象头（Header）、实例数据（Instance Data）和对齐填充（Padding）
     * <p>
     * OFFSET  SIZE     TYPE DESCRIPTION       VALUE
     * 0       4          (object header)      05 00 00 00 (00000101 00000000 00000000 00000000) (5)         MarkWord
     * 4       4          (object header)      00 00 00 00 (00000000 00000000 00000000 00000000) (0)         MarkWord
     * 8       4          (object header)      01 03 00 20 (00000001 00000011 00000000 00100000) (536871681) 类型指针
     * 12      4      byte[] String.value      [97, 109, 111, 115, 46, 119, 97, 110, 103]
     * 16      4          int String.hash      0
     * 20      1        byte String.coder      0
     * 21      3          (loss due to the next object alignment) 对齐填充（非必须）
     * Instance size: 24 bytes
     * <p>
     * 锁标识位：01-未锁定 00-轻量级锁定 10-膨胀（重量级锁定） 11-GC标记 01-可偏向
     * 也即第一行中的，第一个串 00000101
     *
     * @see <a href="https://www.cnblogs.com/LemonFive/p/11246086.html">从打印Java对象头说起</a>
     */
    @Test
    public void printMarkWord() {
        String name = "amos.wang";
        int age = 18;
        boolean sex = true;
        System.out.println("对象地址: " + System.identityHashCode(name));
        System.out.println(ClassLayout.parseInstance(name).toPrintable());
        System.out.println(ClassLayout.parseInstance(age).toPrintable());
        System.out.println(ClassLayout.parseInstance(sex).toPrintable());
    }

    @Test
    public void objectSize() {
        String name = "88888888";
        System.out.println(GraphLayout.parseInstance(name).totalSize());
    }

    /**
     * 数组的对象头真的会多出来一个数组长度
     */
    @Test
    public void arrayObjectSize() {
        String[] names = {"11", "2", "33", "4", "55"};
        System.out.println("GraphLayout: " + GraphLayout.parseInstance(names).toPrintable());
        System.out.println("对象总大小: " + GraphLayout.parseInstance(names).totalSize());
        System.out.println("ClassLayout: " + ClassLayout.parseInstance(names).toPrintable());
    }

    @Test
    public void biasedLock0() {
        // 05 00 00 00 (00000101 00000000 00000000 00000000) (5)
        // 震惊，jdk-11.0.2，跑起来就是可偏向，但没有偏向于任何线程
        String name = "local test";

        System.out.println("ClassLayout: " + ClassLayout.parseInstance(name).toPrintable());
    }

    @Test
    public void biasedLock() {
        String name = "local test";

        synchronized (name) {
            // 就这就偏向主线程了
            // 05 b0 f5 b1 (00000101 10110000 11110101 10110001) (-1309298683)
            System.out.println("ClassLayout: " + ClassLayout.parseInstance(name).toPrintable());
        }
    }

    @Test
    public void lightWeightLock() throws InterruptedException {
        String name = "local test";

        Thread thread = new Thread(() -> {
            synchronized (name) {
                // 偏向锁
                // 05 78 b0 a6 (00000101 01111000 10110000 10100110) (-1498384379)
                System.out.println("thread lock!: " + ClassLayout.parseInstance(name).toPrintable());
            }
        });
        thread.start();
        thread.join();

        synchronized (name) {
            // 轻量级锁
            // 98 c5 2f c5 (10011000 11000101 00101111 11000101) (-986724968)
            System.out.println("main lock!: " + ClassLayout.parseInstance(name).toPrintable());
        }
    }

    @Test
    public void heavyWeightLock() throws InterruptedException {
        String name = "local test";

        Thread thread1 = new Thread(() -> {
            synchronized (name) {
                // 重量级锁
                // 02 16 90 b1 (00000010 00010110 10010000 10110001) (-1315957246)
                System.out.println("thread-1 lock!: " + ClassLayout.parseInstance(name).toPrintable());
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (name) {
                // 重量级锁
                // 02 16 90 b1 (00000010 00010110 10010000 10110001) (-1315957246)
                System.out.println("thread-2 lock!: " + ClassLayout.parseInstance(name).toPrintable());
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    @Test
    public void intSize() {
        // 16字节，对象头12字节，
        boolean b = true;
        System.out.println("boolean b = true; " + GraphLayout.parseInstance(b).totalSize());
        System.out.println("ClassLayout: " + ClassLayout.parseInstance(b).toPrintable());
        System.out.println("GraphLayout: " + GraphLayout.parseInstance(b).toPrintable());

        // 16字节，对象头12字节，
        int i = Integer.MAX_VALUE;
        System.out.println("int i = Integer.MAX_VALUE; " + GraphLayout.parseInstance(i).totalSize());
        System.out.println("ClassLayout: " + ClassLayout.parseInstance(i).toPrintable());
        System.out.println("GraphLayout: " + GraphLayout.parseInstance(i).toPrintable());

        String name = "amos";
        System.out.println("String name = \"amos\"; " + GraphLayout.parseInstance(name).totalSize());
        System.out.println("ClassLayout: " + ClassLayout.parseInstance(name).toPrintable());
        System.out.println("GraphLayout: " + GraphLayout.parseInstance(name).toPrintable());
    }

}
