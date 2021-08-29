package com.amos.scene.framework.zk.lock;

import org.apache.zookeeper.KeeperException;

/**
 * Zookeeper Lock api
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/8/29
 */
public interface ZkLockApi {

    void lock() throws InterruptedException, KeeperException;

    void unlock() throws InterruptedException, KeeperException;

}
