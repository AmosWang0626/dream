package com.amos.scene.framework.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.Test;

/**
 * ZK Client Test by Curator
 *
 * @author wangdaoyuan
 */
public class ZkTests {

    @Test
    public void newClient() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(
                "amos.wang:2181", new ExponentialBackoffRetry(3, 1000));
        curatorFramework.start();
    }


}
