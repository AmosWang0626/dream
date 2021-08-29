package com.amos.scene.framework.zk.lock;

/**
 * Zookeeper Lock api
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/8/29
 */
public interface ZkLockApi {

    boolean lock(String key);

    void invalidate(String key);

}
