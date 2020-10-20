# 缓存 Cache

Cache Aside Pattern

（1）读的时候，先读缓存，缓存没有的话，那么就读数据库，然后取出数据放缓存

（2）更新的时候，先删除缓存，再更新数据库

## 1、Redis单线程模型

![1_Redis单线程模型](./01_Redis单线程模型.png)

## 2、Redis数据类型

![2_Redis数据类型](./02_Redis数据类型.png)

## 3、Redis过期策略、淘汰策略、LRU简单实现

![3_Redis过期淘汰策略](./03_Redis过期淘汰策略.png)

## 4、Redis主从复制

![4_Redis主从复制](./04_Redis主从复制.png)

## 5、Redis主从复制深入剖析

![5_Redis主从复制深入剖析](./05_Redis主从复制深入剖析.png)

## 6、Redis哨兵

![6_Redis哨兵](./06_Redis哨兵.png)

## 7、Redis哨兵主备切换数据丢失问题

![7_Redis哨兵主备切换数据丢失问题](./07_Redis哨兵主备切换数据丢失问题.png)

## 8、Redis哨兵底层原理与选举算法

![8_Redis哨兵底层原理与选举算法](./08_Redis哨兵底层原理与选举算法.png)

## 9、Redis持久化机制对比

![9_Redis持久化机制对比](./09_Redis持久化机制对比.png)

## 10、Redis集群模式

![10_Redis集群模式](./10_Redis集群模式.png)

## 12、Redis缓存雪崩穿透击穿

![12_Redis缓存雪崩穿透击穿](./12_Redis缓存雪崩穿透击穿.png)

## n、Redis VS Memcached

![redis vs memcached](./redis_vs_memcached.png)

