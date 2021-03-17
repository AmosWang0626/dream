package com.amos.scene.source.treemap;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

/**
 * TreeMapTest
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/17
 */
public class TreeMapTest {

    @Test
    public void test() {
        Map<Integer, Integer> treeSet = new TreeMap<>();
        for (int i = 100; i > 0; i--) {
            treeSet.put(i, i);
        }
        treeSet.put(22, 999);

        System.out.println(treeSet);
    }

}
