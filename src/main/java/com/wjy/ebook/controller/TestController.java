package com.wjy.ebook.controller;

import com.wjy.ebook.domain.Test;
import com.wjy.ebook.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {

    @Resource
    private TestService testService;

    @GetMapping("/hello")
    public String hello () {
        return "hello word!";
    }

    @GetMapping("/test/demo")
    public List<Test> list () {
        return testService.list();
    }

}
