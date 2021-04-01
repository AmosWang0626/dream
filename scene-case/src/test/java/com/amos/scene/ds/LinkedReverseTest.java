package com.amos.scene.ds;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

/**
 * 链表
 *
 * @author amos.wang
 * @date 2021/4/1 9:10
 */
public class LinkedReverseTest {

    @Test
    public void main() {
        Node head = initNodes();
        System.out.println(head);
        
        System.out.println(reverse(head));
    }


    /**
     * 反转链表
     * 1 > 2 > 3 > 4 > 5
     * <p>
     *
     * @param head 头结点
     * @return 新的头结点
     */
    private Node reverse(Node head) {
        Node curr = head;
        Node next = curr.next;
        // 清空原头结点next
        curr.next = null;

        while (next != null) {
            curr = next;
            next = next.next;

            next.next = curr;
        }

        return curr;
    }

    /**
     * 初始化链表
     *
     * @return 头结点
     */
    private Node initNodes() {
        Node head = new Node().setValue(1);
        Node n2 = new Node().setValue(2);
        Node n3 = new Node().setValue(3);

        n3.next = new Node().setValue(4);
        n2.next = n3;
        head.next = n2;

        return head;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    private static class Node {
        private Integer value;
        private Node next;

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", next=" + next +
                    '}';
        }
    }

}
