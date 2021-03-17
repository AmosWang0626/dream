package com.amos.scene.source.hashset;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;

/**
 * HashSetTest
 *
 * @author amos.wang
 * @date 2021/3/17 17:27
 */
public class HashSetTest {

    /**
     * 底层为HashMap，所以同样存在线程不安全的问题
     */
    @Test
    void hashTest() {
        Person person = new Person().setName("hello").setAge(16);
        Person person2 = new Person().setName("hello").setAge(16);

        // 既然底层是HashMap，那肯定是有Value的，value是个 private static final Object PRESENT = new Object();
        HashSet<Person> hashSet = new HashSet<>();
        hashSet.add(person);
        hashSet.add(person2);
        // 可以添加 null
        hashSet.add(null);
        hashSet.add(null);
        System.out.println("HashSet<Person>.size() = " + hashSet.size());

        // foreach
        System.out.println();
        // forEachRemaining，其中 Remaining 表示剩余的，iterator遍历一个少一个，遍历完，再想遍历就没了
        hashSet.spliterator().forEachRemaining(s -> System.out.print(s + "\t"));
        System.out.println();
    }

    @Test
    void checkEquals() {
        Person person = new Person().setName("hello").setAge(16);
        Person person2 = new Person().setName("hello").setAge(16);

        System.out.println(person + "\t" + person.hashCode());
        System.out.println(person2 + "\t" + person2.hashCode());
        // equals 比较的是 hashCode
        System.out.println("equals ? " + person.equals(person2));

        // == 比较的是 内存地址
        System.out.println("person:" + System.identityHashCode(person));
        System.out.println("person2:" + System.identityHashCode(person2));
        System.out.println("== ? " + (person == person2));
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    private static class Person {
        private String name;
        private Integer age;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Person person = (Person) o;
            return Objects.equals(name, person.name) &&
                    Objects.equals(age, person.age);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }

}
