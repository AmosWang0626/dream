# Zookeeper Api 文档

> [https://zookeeper.apache.org/doc/current/apidocs/zookeeper-server/index.html](https://zookeeper.apache.org/doc/current/apidocs/zookeeper-server/index.html)

## docker

> 连接CLI `docker exec -it zk zkCli.sh`

- 查询 `ls /`
- 创建 `cretae -s -e /zk_lock "12345678"`
  - -s 顺序节点
  - -e 临时节点（重启消失）
- 状态 `stat /zk_lock`
- 修改 `set /zk_lock "amos.wang"`
- 获取 `get /zk_lock`
- 删除 `delete /zk_lock`