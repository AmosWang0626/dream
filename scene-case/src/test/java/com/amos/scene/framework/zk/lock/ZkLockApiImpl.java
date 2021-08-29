package com.amos.scene.framework.zk.lock;

import com.amos.scene.framework.zk.base.ZkUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Zookeeper Lock api
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/8/29
 */
public class ZkLockApiImpl implements ZkLockApi {

    private static final String CONN_ZK_URL = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 50 * 1000;
    private static final String BASE_LOCK_PATH = "/zk_base";

    /**
     * 这里的成员变量不要定义成 static 的，会影响逻辑执行
     */
    private String currentPath = null;
    private String waitedPrePath = null;
    private final ZooKeeper zookeeper;
    private final CountDownLatch waitLatch = new CountDownLatch(1);


    private ZkLockApiImpl() throws IOException, InterruptedException, KeeperException {
        zookeeper = new ZooKeeper(CONN_ZK_URL, SESSION_TIMEOUT, event -> {
            // 监听节点删除，并且是前一个节点被删除
            if (Watcher.Event.EventType.NodeDeleted.equals(event.getType())
                    && event.getPath().equals(waitedPrePath)) {
                // 释放阻塞等待
                waitLatch.countDown();
            }
        });

        // 初始化基础路径
        try {
            ZkUtils.create(zookeeper, BASE_LOCK_PATH, CreateMode.PERSISTENT);
        } catch (Exception ignore) {
        }
    }

    public static ZkLockApi getInstance() {
        ZkLockApi zkLockApi = null;
        try {
            zkLockApi = new ZkLockApiImpl();
        } catch (IOException | InterruptedException | KeeperException e) {
            e.printStackTrace();
            System.out.println("初始化 zookeeper 异常!!!");
        }
        return zkLockApi;
    }

    @Override
    public void lock() throws InterruptedException, KeeperException {
        // 创建临时有序节点
        final String zkLockPath = BASE_LOCK_PATH + "/scene_lock_";
        currentPath = ZkUtils.create(zookeeper, zkLockPath, CreateMode.EPHEMERAL_SEQUENTIAL);

        List<String> childrenList = zookeeper.getChildren(BASE_LOCK_PATH, false);
        if (childrenList.size() == 1) {
            return;
        }

        Collections.sort(childrenList);

        // 当前节点路径
        String currentNodePath = currentPath.replace(BASE_LOCK_PATH + "/", "");
        // 当前节点在队列中的位置
        int currentNodeIndex = childrenList.indexOf(currentNodePath);
        if (currentNodeIndex == -1) {
            throw new RuntimeException("系统异常，请检查代码逻辑!");
        }

        // 常见临时节点 和 获取所有子节点 不是原子操作，所以这里要校验一下当前节点是不是第 0 个
        if (currentNodeIndex == 0) {
            return;
        }

        // 监听前一个节点释放
        waitedPrePath = BASE_LOCK_PATH + "/" + childrenList.get(currentNodeIndex - 1);
        zookeeper.getData(waitedPrePath, true, null);

        // 开始阻塞等待
        waitLatch.await();
    }

    @Override
    public void unlock() throws InterruptedException, KeeperException {
        zookeeper.delete(currentPath, -1);
    }
}
