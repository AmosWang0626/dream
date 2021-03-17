package com.amos.scene.source.treeset;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;

/**
 * TreeSetTest
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/17
 */
public class TreeSetTest {

    /**
     * TreeSet底层采用 TreeMap实现，TreeMap就是红黑树
     *
     * @see java.util.TreeMap
     */
    @Test
    public void test() {
        Set<Integer> treeSet = new TreeSet<>();
        for (int i = 100; i > 0; i--) {
            treeSet.add(i);
        }
        treeSet.add(55);

        System.out.println(treeSet);
    }

}
