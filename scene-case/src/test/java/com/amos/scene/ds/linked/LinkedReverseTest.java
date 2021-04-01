package com.amos.scene.ds.linked;

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

        // 测试单个节点
        System.out.println(reverse(new Node().setValue(123)));
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
        // 无需反转
        if (head.next == null) {
            return head;
        }

        Node pre = null, curr = head, temp;

        while (curr != null) {
            temp = curr.next;

            curr.next = pre;

            pre = curr;
            curr = temp;
        }

        return pre;
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
        Node n4 = new Node().setValue(4);
        Node n5 = new Node().setValue(5);

        head.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;

        return head;
    }

}
