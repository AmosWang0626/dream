package com.amos.scene.test;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

/**
 * MatchTest
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/8/17
 */
public class MatchTest {

    /*
     * 判读一个字符串是否是纯数字，试了如下两种方式，以为会快点
     * 还有一种方式更快，那就是 char code 那种
     */

    /**
     * 耗时: 9550
     */
    @Test
    public void hello() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            "123456".matches("[+0-9]");
        }
        System.out.println("123456".matches("[0-9]+"));
        System.out.println("耗时: " + (System.currentTimeMillis() - startTime));
    }

    /**
     * 耗时: 2874
     */
    @Test
    public void hello2() {
        long startTime = System.currentTimeMillis();
        Pattern number = Pattern.compile("[0-9]+");
        for (int i = 0; i < 100000000; i++) {
            number.matcher("123456").matches();
        }
        System.out.println(number.matcher("123456").matches());
        System.out.println("耗时: " + (System.currentTimeMillis() - startTime));
    }

    /**
     * 耗时: 2874
     */
    @Test
    public void hello3() {
        long startTime = System.currentTimeMillis();
        Pattern number = Pattern.compile("[0-9]+");
        for (int i = 0; i < 100000000; i++) {
            number.matcher("123456").matches();
        }
        System.out.println(number.matcher("123456").matches());
        System.out.println("耗时: " + (System.currentTimeMillis() - startTime));
    }

}
