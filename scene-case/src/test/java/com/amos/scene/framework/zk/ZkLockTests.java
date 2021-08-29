package com.amos.scene.framework.zk;

import com.amos.scene.framework.zk.lock.ZkLockApi;
import com.amos.scene.framework.zk.lock.ZkLockApiImpl;
import org.apache.zookeeper.KeeperException;
import org.junit.jupiter.api.Test;

import java.util.Objects;
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
    public void lockTest() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            execute();
        }

        // 等着吧
        Thread.sleep(Long.MAX_VALUE);
    }

    private void execute() {
        new Thread(() -> {
            ZkLockApi zkLockApi = ZkLockApiImpl.getInstance();
            if (Objects.isNull(zkLockApi)) {
                return;
            }

            String currentThreadName = Thread.currentThread().getName();
            try {
                System.out.println(currentThreadName + " >>>>>> 开始加锁...");
                zkLockApi.lock();
                System.out.println(currentThreadName + " >>>>>> 加锁成功.");

                // 核心业务逻辑
                doBusiness();
            } catch (InterruptedException | KeeperException e) {
                e.printStackTrace();
            } finally {
                try {
                    System.out.println(currentThreadName + " <<<<<< 释放锁...");
                    zkLockApi.unlock();
                    System.out.println(currentThreadName + " <<<<<< 释放锁成功.");
                } catch (InterruptedException | KeeperException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 执行业务逻辑
     */
    public void doBusiness() {
        long startTime = System.currentTimeMillis();
        System.out.println("\t" + Thread.currentThread().getName() + " :::::: 开始执行业务逻辑...");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\t" + Thread.currentThread().getName()
                + " :::::: 业务逻辑执行完成，耗时 ::: " + (System.currentTimeMillis() - startTime) + " 毫秒");
    }

}
