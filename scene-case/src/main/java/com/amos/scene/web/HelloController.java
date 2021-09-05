package com.amos.scene.web;

import com.amos.scene.service.DocOpsService;
import com.amos.scene.service.DocQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Hello
 *
 * @author <a href="mailto:daoyuan0626@gmail.com">amos.wang</a>
 * @date 2021/4/18
 */
@RestController
public class HelloController {

    @Resource
    private DocOpsService docOpsService;
    @Resource
    private DocQueryService docQueryService;


    @GetMapping
    public String hello() {
        return "Hello World! " + docQueryService.query();
    }

    @GetMapping("save/{val}")
    public String save(@PathVariable("val") String val) {
        return docOpsService.getAndSave(val);
    }

}
