package com.amos.scene.framework.zk.base;

import com.sun.istack.internal.NotNull;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.checkerframework.checker.units.qual.C;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * zookeeper util
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/8/29
 */
public class ZkUtils {

    public static String create(@NotNull ZooKeeper zooKeeper, @NotNull String path, @NotNull CreateMode createMode)
            throws KeeperException, InterruptedException {
        // 顺序节点无需校验是否存在
        boolean checkExist = CreateMode.PERSISTENT.equals(createMode) || CreateMode.EPHEMERAL.equals(createMode);
        // 存在直接返回该路径
        if (checkExist && Objects.nonNull(zooKeeper.exists(path, null))) {
            return path;
        }

        String createdPath = zooKeeper.create(path,
                // data 默认值为 path
                path.getBytes(StandardCharsets.UTF_8),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                createMode
        );

        System.out.printf(createMode + "节点创建成功! [%s]\n", createdPath);

        return createdPath;
    }

}
