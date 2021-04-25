package com.amos.scene.ds.aba;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ABA问题
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/4/22
 */
public class AbaQuestion {

    public static void main(String[] args) {
        int initialStamp = 1;
        int initialRef = 55;

        AtomicStampedReference<Integer> integerStampedRef =
                new AtomicStampedReference<>(initialRef, initialStamp);

        new Thread(() -> {
            final boolean compareAndSet = integerStampedRef.compareAndSet(
                    initialRef, initialRef + 1,
                    integerStampedRef.getStamp(), integerStampedRef.getStamp() + 1
            );

            System.out.println("T1 CAS result is " + compareAndSet);
        }).start();

        new Thread(() -> {
            final boolean compareAndSet = integerStampedRef.compareAndSet(
                    initialRef, initialRef + 1,
                    integerStampedRef.getStamp(), integerStampedRef.getStamp() + 1
            );

            System.out.println("T2 CAS result is " + compareAndSet);
        }).start();


        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
