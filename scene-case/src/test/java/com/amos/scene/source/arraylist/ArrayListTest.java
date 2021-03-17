package com.amos.scene.source.arraylist;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * ArrayListTest
 *
 * @author amos.wang
 * @date 2021/3/17 17:39
 */
public class ArrayListTest {

    /**
     * ArrayList底层是对象数组
     */
    @Test
    public void array() {
        // new ArrayList<>(0); 如果设0，那就会一点一点扩容，还不如不设，默认为10
        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("143");
        list.add("158");
        list.add("178");
        System.out.println(list);
    }

    /**
     * 每次扩容原始容量的1/2
     */
    @Test
    void newCapacity() {
        int oldCapacity = 10;
        int newCapacity = oldCapacity + (oldCapacity >> 1);

        System.out.println(newCapacity);
    }

}
