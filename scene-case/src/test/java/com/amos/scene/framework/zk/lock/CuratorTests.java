package com.amos.scene.framework.zk.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * ZK Client Test by Curator
 *
 * @author wangdaoyuan
 */
public class CuratorTests {

    private static final String ZK_LOCK_PATH = "/zk/lock";

    @Test
    public void newClient() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("127.0.0.1:2181",
                new ExponentialBackoffRetry(3, 1000));
        curatorFramework.start();

        new Thread(() -> lockMethod(curatorFramework)).start();
        new Thread(() -> lockMethod(curatorFramework)).start();

        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void lockMethod(CuratorFramework curatorFramework) {
        InterProcessMutex mutex = new InterProcessMutex(curatorFramework, ZK_LOCK_PATH);
        try {
            // 注意，这里要等待一定时间
            if (mutex.acquire(6, TimeUnit.SECONDS)) {
                System.out.println(Thread.currentThread().getName() + " 抢到了锁，开始执行");

                // 执行业务逻辑
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + " 执行完成");
            }
        } catch (Exception e) {
            System.out.println(Thread.currentThread().getName() + " 业务异常");
        } finally {
            try {
                mutex.release();
            } catch (Exception e) {
                System.out.println(Thread.currentThread().getName() + " 释放锁异常");
            }
        }
    }

}
