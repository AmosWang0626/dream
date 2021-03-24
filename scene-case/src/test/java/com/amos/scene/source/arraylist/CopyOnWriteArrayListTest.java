package com.amos.scene.source.arraylist;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 线程安全 List -- CopyOnWriteArrayList
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/24
 */
public class CopyOnWriteArrayListTest {

    /**
     * 写加锁，读不加锁，适合写少读多的场景，读时不需要强一致性。
     * 读时，如果需要强一致，推荐使用 {@link Collections.synchronizedList()}
     */
    @Test
    public void test() {
        CopyOnWriteArrayList<String> arrayList = new CopyOnWriteArrayList<>();
        arrayList.add("cs_01");
        arrayList.add("cs_02");
        arrayList.add("cs_03");
        arrayList.add("cs_04");
        arrayList.add("cs_05");

        System.out.println(arrayList.get(1));
        System.out.println(arrayList.contains("cs_03"));
    }

}
