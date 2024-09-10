package com.example.web_back.controller;

import com.example.web_back.annotation.FucLogger;
import com.example.web_back.entity.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin("*")
@RestController
public class TestController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    //@RepeatSubmit
    @FucLogger("TestController")
    @RequestMapping("/test")
    public ResponseVo test() {
        return resSuccess("test success", null);
    }
}
