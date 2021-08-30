package com.amos.scene.framework.zk.lock;

import com.amos.scene.framework.zk.base.ZkUtils;
import org.apache.zookeeper.*;

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
public class ZkLockApiImpl implements ZkLockApi, Watcher {

    private static final String CONNECT_ZK_URL = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 50 * 1000;
    private static final String ROOT_PATH = "/zk_lock_root";

    /**
     * 这里的成员变量不要定义成 static 的，会影响逻辑执行
     */
    private String currentPath = null;
    private String waitedPrePath = null;
    private final ZooKeeper zookeeper;
    private final CountDownLatch waitLatch = new CountDownLatch(1);
    private final CountDownLatch connectLatch = new CountDownLatch(1);


    private ZkLockApiImpl() throws IOException, InterruptedException, KeeperException {
        zookeeper = new ZooKeeper(CONNECT_ZK_URL, SESSION_TIMEOUT, this);
        connectLatch.await();
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
    public void process(WatchedEvent event) {
        if (!Event.KeeperState.SyncConnected.equals(event.getState())) {
            return;
        }
        if (Event.EventType.None.equals(event.getType())) {
            // 释放阻塞等待
            connectLatch.countDown();
            try {
                ZkUtils.create(zookeeper, ROOT_PATH, CreateMode.PERSISTENT);
            } catch (Exception ignore) {
            }
        } else if (Event.EventType.NodeDeleted.equals(event.getType())) {
            // 监听节点删除，并且是前一个节点被删除
            if (event.getPath().equals(waitedPrePath)) {
                waitLatch.countDown();
            }
        }
    }

    @Override
    public void lock() throws InterruptedException, KeeperException {
        // 创建临时有序节点
        final String zkLockPath = ROOT_PATH + "/scene_lock_";
        currentPath = ZkUtils.create(zookeeper, zkLockPath, CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println(Thread.currentThread().getName() + " >>>>>> 创建临时节点 " + currentPath);

        List<String> childrenList = zookeeper.getChildren(ROOT_PATH, false);
        if (childrenList.size() == 1) {
            return;
        }

        Collections.sort(childrenList);

        // 当前节点路径
        String currentNodePath = currentPath.replace(ROOT_PATH + "/", "");
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
        waitedPrePath = ROOT_PATH + "/" + childrenList.get(currentNodeIndex - 1);
        zookeeper.exists(waitedPrePath, true);

        // 开始阻塞等待
        waitLatch.await();
    }

    @Override
    public void unlock() throws InterruptedException, KeeperException {
        zookeeper.delete(currentPath, -1);
    }
}
