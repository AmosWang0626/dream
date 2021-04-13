package com.amos.scene.leetcode;

/**
 * 简单实现一个加权轮询算法
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/4/13
 */
public class LoopMain {

    /**
     * 假设有一个服务集群，要保证集群里的每个服务均衡调用
     */
    private static final String[] SERVICE_ARR = new String[20];

    static {
        for (int i = 0; i < SERVICE_ARR.length; i++) {
            if (i < 5) {
                // 5
                SERVICE_ARR[i] = "svc-01";
            } else if (i < 15) {
                // 10
                SERVICE_ARR[i] = "svc-02";
            } else {
                // 1
                SERVICE_ARR[i] = "svc-" + i;
            }
        }
    }

    public static void main(String[] args) {
        int length = SERVICE_ARR.length;
        for (int i = 0; i < 100; i++) {
            System.out.println("Service " + SERVICE_ARR[i % length] + " 提供服务!");
        }
    }

}
