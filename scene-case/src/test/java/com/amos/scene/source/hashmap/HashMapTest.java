package com.amos.scene.source.hashmap;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * HashMapTest
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/7
 */
public class HashMapTest {

    @Test
    public void init() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", "amos");

        System.out.println(hashMap);
    }

}
