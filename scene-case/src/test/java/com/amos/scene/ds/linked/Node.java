package com.amos.scene.ds.linked;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 单向链表Node
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/4/1
 */
@Getter
@Setter
@Accessors(chain = true)
public class Node {

    Integer value;

    Node next;

    @Override
    public String toString() {
        return "Node{" + "value=" + value + ", next=" + next + '}';
    }
}