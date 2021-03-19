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

}
