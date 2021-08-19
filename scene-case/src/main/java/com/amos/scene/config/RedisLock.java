package com.amos.scene.config;

import com.amos.scene.facade.ILock;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;

/**
 * Redis分布式锁
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/8/19
 */
@Component
public class RedisLock implements ILock {

    @Resource
    private JedisPool jedisPool;

    @Override
    public void clearLock(String key) {
        try (Jedis client = jedisPool.getResource()) {
            client.del(key);
        }
        System.out.println("初始化锁完成 >>>>> go go go");
    }

    @Override
    public String lock(String key, int timeout, int expire) {
        try (Jedis client = jedisPool.getResource()) {
            String value = UUID.randomUUID().toString();
            long lastTime = System.currentTimeMillis() + timeout;
            while (System.currentTimeMillis() < lastTime) {
                /// [方式一] 下边代码问题, setnx 成功, expire 失败, 锁无法自行释放
                // if (client.setnx(key, value) == 1) {
                //     if (client.ttl(key) == -1) {
                //         client.expire(key, expire);
                //     }
                //     return value;
                // }

                // 推荐,原因就是简单
                /// [方式二] setnx 和 expire 原子性
                String res = client.set(key, value, SetParams.setParams().nx().px(expire));
                if ("OK".equals(res)) {
                    return value;
                }

                /// [方式三] lua脚本 (EX = seconds; PX = milliseconds)
                // String luaScript = "if redis.call('set', KEYS[1], ARGV[1], 'EX', ARGV[2], 'NX') then return 1 else return 0 end";
                // Long luaRes = (Long) client.eval(luaScript, Collections.singletonList(key), Arrays.asList(value, String.valueOf(expire)));
                // if (luaRes == 1L) {
                //     return value;
                // }
            }
        }

        return null;
    }

    @Override
    public void unlock(String key, String value) {
        try (Jedis client = jedisPool.getResource()) {
            // lua脚本 (注意是 KEYS|ARGV 纯大写)
            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                    "return redis.call('del', KEYS[1]) " +
                    "else return 0 end";
            client.eval(luaScript, Collections.singletonList(key), Collections.singletonList(value));
        }
    }

}
