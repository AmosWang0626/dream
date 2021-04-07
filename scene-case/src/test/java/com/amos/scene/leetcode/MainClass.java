package com.amos.scene.leetcode;

/**
 * LeetCode Test Main
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/4/7
 */
public class MainClass {

    public static void main(String[] args) {
//        ListNode listNode1 = new ListNode(2, new ListNode(4, new ListNode(3)));
//        ListNode listNode2 = new ListNode(5, new ListNode(6, new ListNode(4)));
        ListNode listNode1 = new ListNode(9);
        ListNode listNode2 = new ListNode(1, new ListNode(9, new ListNode(9,
                new ListNode(9, new ListNode(9, new ListNode(9,
                        new ListNode(9, new ListNode(9, new ListNode(9, new ListNode(9))))))))));

        ListNode listNode = addTwoNumbers(listNode1, listNode2);
        System.out.println(listNode);
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        long num1 = 0, temp1 = 1;
        long num2 = 0, temp2 = 1;
        for (ListNode i = l1; i != null; i = i.next) {
            num1 += i.val * temp1;
            temp1 *= 10;
        }
        for (ListNode i = l2; i != null; i = i.next) {
            num2 += i.val * temp2;
            temp2 *= 10;
        }

        long sum = num1 + num2;

        System.out.printf("num1 %d, num2 %d, sum %d\n", num1, num2, sum);

        ListNode result = new ListNode((int) (sum % 10));
        ListNode preNode = result;
        long temp = sum;
        while ((temp = temp / 10) != 0) {
            preNode.next = new ListNode((int) (temp % 10));
            preNode = preNode.next;
        }

        return result;
    }

    static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

        @Override
        public String toString() {
            return "ListNode{" +
                    "val=" + val +
                    ", next=" + next +
                    '}';
        }
    }
}

 
 
 