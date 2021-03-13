# 面试突击 HashMap 篇

## 前置关键词

- `capacity`：容量，也就是数组的长度，扩容扩的也是数组的长度

## 基础参数

```java
public class HashMap<K, V> extends AbstractMap<K, V>
        implements Map<K, V>, Cloneable, Serializable {

    /* 默认容量 16 */
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    /* 最大容量 2^30  */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /* 负载因子 */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /* 树化阈值（链表 > 红黑树） */
    static final int TREEIFY_THRESHOLD = 8;

    /* 取消树化阈值（红黑树 > 链表） */
    static final int UNTREEIFY_THRESHOLD = 6;

    /* 如果容量小于 64，链表是不会转为红黑树的，而是会扩容 */
    static final int MIN_TREEIFY_CAPACITY = 64;

}
```

## hash方法

```text
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

### (h = key.hashCode()) ^ (h >>> 16)

先将hashCode右移16位，再通过异或运算，让hash值的低16位保持高低16位的特征

## 寻址优化

正常来说，就是 hash值 % 数组长度

但这里做了优化 `(n - 1) & hash`，结果是和 % 运算一样的，从计算机执行的效率来看，& 肯定 比 % 效率高。

补充两点：

1. 这里的 n 一定要是2的N次方，不然结果不对，这可能也是 HashMap的容量一定要是2的N次方的一个原因
2. 这里做的是与运算，数组长度（容量）一般来讲都不会超过2的16次方，所以大概率hash值的高16位是没用的，这也就是上一步hash优化的原因

举个例子：

```
假设数组长度为 16，现在来了一个 hash值为 5的元素，那这个元素应该放在哪里呢？

应该放在：5 % 16 = 5

那么接下来用 (n - 1) & hash 测试一下

0000 0000 0000 0000 0000 0000 0000 1111（n - 1 = 15）
0000 0000 0000 0000 0000 0000 0000 0101（5）
---------------------------------------（&）
0000 0000 0000 0000 0000 0000 0000 0101（5，结果一致）
```

```java
public class HashMap<K, V> {
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, i;
        // 这里可以说明，HashMap并不是new出来就会初始化的，resize时初始化的
        if ((tab = table) == null || (n = tab.length) == 0)
            n = (tab = resize()).length;
        // 这个if里的条件，计算出元素要在数组中的位置，如果该位置为空，就直接放这里了
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else { // 这里就是hash碰撞了，碰撞完要么放链表，要么放红黑树
            // ......
        }
        ++modCount;
        // 有点意思，put完，如果发现size大于阈值了，就会触发扩容
        if (++size > threshold)
            resize();
        afterNodeInsertion(evict);
        return null;
    }
}
```

## JDK 1.7 并发扩容死循环问题

> [老生常谈，HashMap的死循环-占小狼](https://www.jianshu.com/p/1e9cf0ac07f4)

```java
public class HashMap<K, V> {
    /**
     * Transfers all entries from current table to newTable.
     */
    void transfer(Entry[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        for (Entry<K, V> e : table) {
            while (null != e) {
                Entry<K, V> next = e.next;
                if (rehash) {
                    e.hash = null == e.key ? 0 : hash(e.key);
                }
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }
}
```

有点强迫症，看着 while 里的代码，总觉得有点理解不了，就写了个demo测了一下，这就是个典型的头插法，没有特殊含义。

```java
while (null != e) {
    Node next = e.next;

    e.next = table[i];
    table[i] = e;
    e = next;
}
```

```text
假设有三个节点：3 > 7 > 11

首次进入循环，e = 3，next = 7，此时 table[i] 为空，e.next = null，table[i] = 3{key=3, next=null}；最后：e = 7

下一次循环，e = 7，next = 11，e.next = table[i] = 3，table[i] = 7{key=7, next=3}；最后：e = 11

下一次循环，e = 11，next = null，e.next = table[i] = 11，table[i] = 11{key=11, next=7}；最后e = null

结束循环，头插法完成：11 > 7 > 3
```