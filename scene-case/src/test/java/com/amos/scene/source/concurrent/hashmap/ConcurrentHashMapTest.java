package com.amos.scene.source.concurrent.hashmap;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap Test
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/15
 */
public class ConcurrentHashMapTest {

    @Test
    public void test() {
        Map<Integer, String> map = new ConcurrentHashMap<>();
        map.put(3, "amos");
        map.put(7, "amos");
        map.put(11, "amos");

        System.out.println(map);
    }

}
