package com.amos.scene.source.concurrent.hashmap;

import org.assertj.core.util.Objects;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ConcurrentHashMap Test
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/15
 */
public class ConcurrentHashMapTest {

    @Test
    public void test() {
        Map<Integer, String> map = new ConcurrentHashMap<>();
        map.put(3, "amos");
        map.put(7, "amos");
        map.put(11, "amos");

        System.out.println(map);
    }

    @Test
    public void segment() {
        Segment<Integer, String> map = new Segment<>();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(100);

        Thread[] planThreads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            final int finalInt = i;
            planThreads[i] = new Thread(() -> {
                try {
                    map.put(finalInt, "amos-" + finalInt);

                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            planThreads[i].start();
        }


        System.out.println(map);

        System.out.println(map.get(55));
    }

    static final class Segment<K, V> extends ReentrantLock implements Serializable {

        private HashEntry<K, V> root;

        public void put(K k, V v) {
            lock();
            try {
                if (root == null) {
                    root = new HashEntry<>(Objects.hashCodeFor(k), k, v);
                    return;
                }

                HashEntry<K, V> temp = root;
                while (temp.next != null) {
                    temp = temp.next;
                }

                temp.setNext(new HashEntry<>(Objects.hashCodeFor(k), k, v));
            } finally {
                unlock();
            }
        }

        public V get(K k) {
            if (root.key.equals(k)) {
                return root.value;
            }

            HashEntry<K, V> temp = root.next;
            while (temp != null) {
                if (temp.key.equals(k)) {
                    return temp.value;
                }
                temp = temp.next;
            }

            throw new RuntimeException("undefined");
        }

        @Override
        public String toString() {
            return "Segment{root=" + root + "}";
        }
    }

    static final class HashEntry<K, V> {
        final int hash;
        final K key;
        volatile V value;
        volatile HashEntry<K, V> next;

        HashEntry(int hash, K key, V value) {
            this.key = key;
            this.hash = hash;
            this.value = value;
        }

        final void setNext(HashEntry<K, V> n) {
            this.next = n;
        }

        @Override
        public String toString() {
            return String.format("HashEntry{Key=%s, value=%s, next=%s}",
                    key, value, (next == null ? null : next.key));
        }
    }

}
