package com.amos.scene.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka 消息监听
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/7/31
 */
//@Component
public class KafkaMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageListener.class);

    @KafkaListener(topics = "boot-topic", groupId = "amos.kafka")
    public void onMessage(String message) {
        LOGGER.info("=====================================");
        LOGGER.info("onMessage: content[{}]", message);
        LOGGER.info("=====================================");
    }

    @KafkaListener(topics = "boot-topic", groupId = "amos.kafka2")
    public void onMessage2(String message) {
        LOGGER.info("=====================================");
        LOGGER.info("onMessage2: content[{}]", message);
        LOGGER.info("=====================================");
    }

}
