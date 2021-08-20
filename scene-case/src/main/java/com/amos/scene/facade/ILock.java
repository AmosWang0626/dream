package com.amos.scene.facade;

/**
 * 分布式锁 API
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2020/3/31
 */
public interface ILock {

    /**
     * 获取锁
     *
     * @param key     key
     * @param timeout 获取锁等待时间
     * @param expire  锁过期时间
     * @return value
     */
    String lock(String key, int timeout, int expire);

    /**
     * 释放锁（仅在 key/value 匹配时才让释放）
     *
     * @param key   key
     * @param value value
     */
    void unlock(String key, String value);

}
