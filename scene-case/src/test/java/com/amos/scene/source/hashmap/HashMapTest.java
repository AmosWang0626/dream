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
    public void testResize() {
        // 注意JDK版本，不同版本resize时机可能不一
        // 1.7 是在put时，先判断size，然后扩容的
        // 1.8 是在put完成之后，判断size进行扩容的
        HashMap<Integer, String> hashMap = new HashMap<>(2);
        hashMap.put(3, "amos");
        hashMap.put(7, "amos");
        hashMap.put(11, "amos");

        System.out.println(hashMap);
    }

    @Test
    public void testHeadInset() {
        Node e = new Node(3, "3-3");
        Node n2 = new Node(7, "7-7");
        Node n3 = new Node(11, "11-11");

        // 构造正序链表 3 > 7 > 11
        e.next = n2;
        n2.next = n3;

        // 模拟 HashMap Entry 数组
        Node[] table = new Node[2];
        // 模拟放入数组位置
        int i = 1;

        while (null != e) {
            Node next = e.next;

            e.next = table[i];
            table[i] = e;
            e = next;
        }

        // 11 > 7 > 3
        System.out.println(table[i]);
    }

    public static class Node {
        final Integer key;
        String value;
        Node next;

        public Node(Integer key, String value) {
            this.key = key;
            this.value = value;
        }

        public Node(Integer key, String value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final Integer getKey() {
            return key;
        }

        public final String getValue() {
            return value;
        }

        public Node getNext() {
            return next;
        }

        @Override
        public String toString() {
            return String.format("Node{Key=%s, value=%s, next=%s}",
                    key, value, (next == null ? null : next.key));
        }
    }

}
