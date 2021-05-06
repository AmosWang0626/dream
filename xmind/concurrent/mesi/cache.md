# 高速缓存

## 高速缓存的数据结构

> 拉链散列表，缓存条目，地址解码

- 高速缓存
    - bucket
        - cache entry
            - tag（缓存数据在主内存中的数据地址）
            - cache line（缓存的数据，可以包含多个变量的值）
            - flag（缓存行状态）
        - cache entry
            - tag
            - cache line
            - flag
        - cache entry
            - tag
            - cache line
            - flag
    - bucket
        - cache entry
            - tag
            - cache line
            - flag
        - cache entry
            - tag
            - cache line
            - flag
        - cache entry
            - tag
            - cache line
            - flag

### 处理器中会操作一些变量，怎么在高速缓存里定位这个变量呢？

处理器在读写高速缓存的时候，实际上会根据变量名执行内存地址解码，会解析出来三个东西，index、tag和offset。

- index（定位Bucket）
- tag（定位Cache Entry）
- offset（定位Cache Line中的位置）

### 多级缓存

一级、二级、三级缓存，越靠近CPU读写效率越高