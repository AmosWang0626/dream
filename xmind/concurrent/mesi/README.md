# MESI协议（缓存一致性）

- S：Share 共享的
- I：Invalid 无效的
- E：Exclusive 独占的
- M：Modified 已修改

Cache Entry（tag, cache line, flag）

1. 处理器0读取变量，高速缓存里没有，就向总线发送消息，总线从主内存或者其他处理器的高速缓存中读取到该变量，
返回给处理器0，处理器0将变量放到 Cache Entry 里边，此时，flag = S
2. 同时处理器1也读取了这个变量，同样 flag = S
3. 处理器0要修改了该变量，就向总线发送invalidate消息，尝试让其他处理器将对应Cache Entry全部变成 I
4. 处理器1嗅探到invalidate消息，将自己的Cache Entry设置为 I，并返回invalidate ack消息到总线
5. 处理器0嗅探到所有处理器都返回invalidate ack消息了，就将自己的Cache Entry先设置为 E，独占这条消息
6. 如果有别的同时来修改，并发送invalidate，这个处理器0是不会返回invalidate ack消息的
7. 接着，处理器0开始修改这条数据，并设置 flag = M，也有可能把数据强制写会主内存，具体看硬件实现

