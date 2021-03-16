# 面试突击 ConcurrentHashMap

## 前置关键词

- segments 分段（jdk1.7）

## 老生常谈

- jdk1.7 采用 Segment数组（segments），每个 Segment都有独立的 ReentrantLock锁，并发操作互不影响
- jdk1.8 CAS + synchronized 实现每个 Node一个锁，缩小了锁粒度，提高了并发性能

## 加锁

并发情况下，HashMap 存在线程不安全的情况，线程不安全，当然要加锁了。

##### 加锁示例1

HashTable，get和put都加了synchronized修饰，这样带来的直接问题就是，性能比较差。

##### 1.7 分段锁

采用分段的思想，切分为多个Segment，默认为16个，可以初始化时指定，后期不能修改，Segment就相当于一个小的HashMap。

理论上支持segments.length个线程并发操作，默认也就是16个线程并发操作。

扩容时，也是每个Segment里边的table自己扩容，Segment数量如前边所说，初始化时指定，不可更改。

##### 1.7 使用分段锁 get/put

类似HashMap，只不过一个由一个数组，换成了一组数组，每个Segment中有一个数组，看下边源码。

```java
static final class Segment<K, V> extends ReentrantLock implements Serializable {

    private static final long serialVersionUID = 2249069246763182397L;

    static final int MAX_SCAN_RETRIES =
            Runtime.getRuntime().availableProcessors() > 1 ? 64 : 1;

    transient volatile HashEntry<K, V>[] table;

    transient int count;

    transient int modCount;

    transient int threshold;

    final float loadFactor;

    Segment(float lf, int threshold, HashEntry<K, V>[] tab) {
        this.loadFactor = lf;
        this.threshold = threshold;
        this.table = tab;
    }

    final V put(K key, int hash, V value, boolean onlyIfAbsent) {
        HashEntry<K, V> node = tryLock() ? null : scanAndLockForPut(key, hash, value);
        V oldValue;
        try {
            HashEntry<K, V>[] tab = table;
            int index = (tab.length - 1) & hash;
            HashEntry<K, V> first = entryAt(tab, index);
            // ...
        } finally {
            unlock();
        }
        return oldValue;
    }

}
```

put操作时，通过两次hash定位HashEntry位置，第一次找到在第几个Segment，第二次找到在Segment中table中的位置。

##### 1.7 计算size

TODO 比较有意思
