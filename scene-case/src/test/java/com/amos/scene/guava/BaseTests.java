package com.amos.scene.guava;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Guava、Apache Common Base Tests
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/9/26
 */
public class BaseTests {

    @Test
    public void test1() {
        System.out.println(Lists.newArrayList(1, 1, 2, 3, 3, 4, 5));
    }

    @Test
    public void test2() {
        System.out.println(new ImmutablePair<>(123, 456).getLeft());
    }

    @Test
    public void test3() {
        ArrayList<Integer> arr1 = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        ArrayList<Integer> arr2 = Lists.newArrayList(2, 4, 6, 8, 10);

        // 并集
        System.out.println(CollectionUtils.union(arr1, arr2));
        // 交集
        System.out.println(CollectionUtils.intersection(arr1, arr2));
        // 交集的补集
        System.out.println(CollectionUtils.disjunction(arr1, arr2));
        // 差
        System.out.println(CollectionUtils.subtract(arr1, arr2));
        System.out.println(CollectionUtils.subtract(arr2, arr1));
    }

}
