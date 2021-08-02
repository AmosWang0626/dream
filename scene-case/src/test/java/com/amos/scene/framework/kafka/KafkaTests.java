package com.amos.scene.framework.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.Resource;

/**
 * KafkaTests
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/8/2
 */
@SpringBootTest
public class KafkaTests {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void contextLoads() {
        String message = "Hello World! by Kafka Message!";

        for (int i = 0; i < 10; i++) {
            kafkaTemplate.send("boot-topic", "AMOS_PRODUCER", message + "_" + i);
        }
    }

}
