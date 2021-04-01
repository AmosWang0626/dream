# MySQL

## 主键数据类型选型

推荐使用自增ID，那么如果自增ID用完了怎么办？

- int
    - 带符号 -21,4748,3648 ~ 21,4748,3647
    - 无符号 0 ~ 42,9496,7295（约42亿）

- bigint
    - 带符号 -922,3372,0368,5477,5808 ~ 922,3372,0368,5477,5807
    - 无符号 0 ~ 1844,6744,0737,0955,1615

1. 默认是int类型，自增ID最大值 4294967295，可以改为bigint类型
    - 怎么修改，直接Alter修改肯定需要停服，大概率是可以停服的
    - 如果不可以停服，主从切换，分别修改
2. 另外，单表数据量过亿，系统肯定早就应该重构了
    - 分库分表概括一下
    - 分库分表主键ID可采用雪花算法，或者其他分布式ID解决方案