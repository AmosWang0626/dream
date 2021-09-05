package com.amos.scene;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author amoswang
 */
//@EnableKafka
@SpringBootApplication
public class SceneCaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SceneCaseApplication.class, args);
    }

}
