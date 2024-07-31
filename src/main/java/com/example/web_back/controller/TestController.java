package com.example.web_back.controller;

import com.example.web_back.annotation.FucLogger;
import com.example.web_back.entity.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin("*")
@RestController
public class TestController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @FucLogger("TestController")
    @RequestMapping("/test")
    public ResponseVo test(
            @RequestParam String name,
            @RequestParam int age) {
        return resSuccess(null, null);
    }


    @FucLogger("TestController")
    @RequestMapping("/post")
    public ResponseVo post(
            String name,
            int age) {
        return resSuccess("success", null);
    }
}
