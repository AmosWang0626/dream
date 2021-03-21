package com.amos.scene.source.string;

import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

/**
 * StringTest
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/3/21
 */
public class StringTest {

    @Test
    public void testIdentityHashCode() {
        String name = "amos";
        String name2 = "am" + "os";

        System.out.println("identityHashCode: " + System.identityHashCode(name));
        System.out.println("identityHashCode: " + System.identityHashCode(name2));

        System.out.println(ClassLayout.parseInstance(name).toPrintable());
        System.out.println(ClassLayout.parseInstance(name2).toPrintable());
    }

}
