package com.amos.scene.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/4/18
 */
@RestController
public class HelloController {

    @GetMapping
    public String hello() {
        return "Hello World!";
    }

}
