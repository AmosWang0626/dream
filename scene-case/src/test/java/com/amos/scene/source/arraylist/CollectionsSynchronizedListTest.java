package com.amos.scene.source.arraylist;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 线程安全 List -- Collections.synchronizedList
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/24
 */
public class CollectionsSynchronizedListTest {

    /**
     * 写加锁，读 forEach 加锁，但 iterator 是不加锁的，需要用户手动同步
     *
     * @see Collections.SynchronizedList
     */
    @Test
    public void test() {
        List<String> arrayList = Collections.synchronizedList(new ArrayList<>());
        arrayList.add("cs_01");
        arrayList.add("cs_02");
        arrayList.add("cs_03");
        arrayList.add("cs_04");
        arrayList.add("cs_05");

        System.out.println(arrayList.get(1));
        System.out.println(arrayList.contains("cs_03"));
    }

}
