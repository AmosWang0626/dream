package com.amos.scene.ds.linked;

import org.junit.jupiter.api.Test;

/**
 * 交叉链表找汇合节点
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/4/1
 */
public class CrossLinkedTest {

    @Test
    public void main() {
        Node commonNodes = commonNodes();

        // linked 1
        Node linked1 = linked1Nodes(commonNodes);
        System.out.println(linked1);

        // linked 2
        Node linked2 = linked2Nodes(commonNodes);
        System.out.println(linked2);

        cross(linked1, linked2);
    }

    private void cross(Node linked1, Node linked2) {
        int linked1Length = 0;
        int linked2Length = 0;

        Node temp1 = linked1;
        while (temp1 != null) {
            linked1Length++;

            temp1 = temp1.next;
        }

        Node temp2 = linked2;
        while (temp2 != null) {
            linked2Length++;

            temp2 = temp2.next;
        }

        if (temp1.equals(temp2)) {

            return;
        }

        System.out.printf("链表1长度 %d, 链表2长度 %d\n", linked1Length, linked2Length);

        int sub = linked2Length - linked1Length;
        int subAbs = Math.abs(sub);
        System.out.println(sub + " <<<< >>>> " + subAbs);


        if (sub == 0) {
            temp1 = linked1;
            temp2 = linked2;
            while (temp1.next != null) {


            }
            return;
        }


        if (sub > 0) {

        } else {

        }
    }


    /**
     * 初始化链表
     *
     * @return 头结点
     */
    private Node linked1Nodes(Node commonNodes) {
        Node n1 = new Node().setValue(1);
        Node n2 = new Node().setValue(2);
        Node n3 = new Node().setValue(3);
        Node n4 = new Node().setValue(4);

        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = commonNodes;

        return n1;
    }

    /**
     * 初始化链表
     *
     * @return 头结点
     */
    private Node linked2Nodes(Node commonNodes) {
        Node n5 = new Node().setValue(5);

        n5.next = commonNodes;

        return n5;
    }

    /**
     * 初始化链表
     *
     * @return 头结点
     */
    private Node commonNodes() {
        Node n6 = new Node().setValue(6);
        Node n7 = new Node().setValue(7);
        Node n8 = new Node().setValue(8);
        Node n9 = new Node().setValue(9);

        n6.next = n7;
        n7.next = n8;
        n8.next = n9;

        return n6;
    }
}
