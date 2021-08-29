package com.amos.scene.framework.zk.lock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Zookeeper Lock api
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/8/29
 */
public class ZkLockApiImpl implements ZkLockApi {

    private static final String CONN_ZK_URL = "127.0.0.1:2181";
    private static final int SESSION_TIMEOUT = 50 * 1000;
    private static ZooKeeper instance = null;

    static {
        try {
            instance = new ZooKeeper(CONN_ZK_URL, SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {

                }
            });
        } catch (IOException e) {
            System.out.println("初始化 zookeeper 异常!!!");
        }
    }

    public static ZooKeeper getInstance() {
        return instance;
    }

    @Override
    public boolean lock(String key) {
        return false;
    }

    @Override
    public void invalidate(String key) {

    }
}
