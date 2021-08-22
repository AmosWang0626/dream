package com.amos.scene.framework.zk;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * zookeeper 分布式锁测试
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/8/21
 */
public class ZkLockTests {

    private static final String CONN_ZK_URL = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 50 * 1000;

    public static ZooKeeper instance = null;

    static {
        try {
            instance = new ZooKeeper(CONN_ZK_URL, SESSION_TIMEOUT, null);
        } catch (IOException e) {
            System.out.println("初始化 zookeeper 异常!!!");
        }
    }

    /**
     * ZK 分布式锁
     * 1. 有序的监听等待⌛️
     * 2. 临时节点，重启消失
     * 3. 来新的节点了
     * 3.1 判断是不是第一个，是第一个，就加锁执行
     * 3.2 不是第一个，找到有序的最后一个，并添加监听
     * 3.3 监听轮到自己了，就要执行业务逻辑了
     */
    @Test
    public void lockTest() throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = instance;
        final String basePath = "/zk_base";
        // 初始化基础路径
        createPersistent(zooKeeper, basePath);

        System.out.printf("[%s] 路径下有 [%d] 个子节点\n", basePath, zooKeeper.getAllChildrenNumber(basePath));

        // 创建临时有序节点
        final String zkLockPath = basePath + "/scene_lock_";
        for (int i = 0; i < 5; i++) {
            createEphemeralSequential(zooKeeper, zkLockPath);
        }

        System.out.printf("[%s] 路径下有 [%d] 个子节点\n", basePath, zooKeeper.getAllChildrenNumber(basePath));

        // 遍历基础路径
        zooKeeper.getChildren(basePath, null).forEach(System.out::println);

        System.out.println("============ debug lock ==============");
    }

    private void createPersistent(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException {
        Stat exists = zooKeeper.exists(path, null);
        if (Objects.isNull(exists)) {
            // data 默认值为 path
            String createdPath = zooKeeper.create(path, path.getBytes(StandardCharsets.UTF_8),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.printf("[%s] 创建成功! response [%s]\n", path, createdPath);
        }
    }

    private void createEphemeralSequential(ZooKeeper zooKeeper, String path) throws KeeperException, InterruptedException {
        Stat exists = zooKeeper.exists(path, null);
        String uuid = UUID.randomUUID().toString();
        if (Objects.isNull(exists)) {
            // data 默认值为 path
            String createdPath = zooKeeper.create(path, path.getBytes(StandardCharsets.UTF_8),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.printf("[%s] ZK Lock临时顺序节点 创建成功! response [%s]\n", path, createdPath);
        }
    }

    /**
     * 执行业务逻辑
     */
    public void doBusiness() {
        long startTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + " 开始执行业务逻辑...");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()
                + " 业务逻辑执行完成，耗时 ::: " + (System.currentTimeMillis() - startTime) + " 毫秒");
    }

}
