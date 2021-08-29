package com.amos.scene.framework.zk;

import com.amos.scene.framework.zk.lock.ZkLockApiImpl;
import com.amos.scene.framework.zk.base.ZkUtils;
import org.apache.zookeeper.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * zookeeper 分布式锁测试
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/8/21
 */
public class ZkLockTests {

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
    public void lockTest() throws InterruptedException, KeeperException {
        ZooKeeper zooKeeper = ZkLockApiImpl.getInstance();
        final String basePath = "/zk_base";
        // 初始化基础路径
        ZkUtils.create(zooKeeper, basePath, CreateMode.PERSISTENT);

        System.out.printf("[%s] 路径下有 [%d] 个子节点\n", basePath, zooKeeper.getAllChildrenNumber(basePath));

        // 创建临时有序节点
        final String zkLockPath = basePath + "/scene_lock_";
        for (int i = 0; i < 5; i++) {
            ZkUtils.create(zooKeeper, zkLockPath, CreateMode.EPHEMERAL_SEQUENTIAL);
        }

        System.out.printf("[%s] 路径下有 [%d] 个子节点\n", basePath, zooKeeper.getAllChildrenNumber(basePath));

        // 遍历基础路径
        zooKeeper.getChildren(basePath, null).forEach(System.out::println);

        System.out.println("============ debug lock ==============");
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
