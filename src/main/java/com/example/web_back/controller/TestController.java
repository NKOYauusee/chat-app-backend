package com.example.web_back.controller;

import com.example.web_back.annotation.FucLogger;
import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.mapper.UserFriMapper;
import com.example.web_back.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RestController
public class TestController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);


    @Resource
    UserFriMapper friMapper;

    @Resource
    UserMapper userMapper;

    //@RepeatSubmit
    @FucLogger("TestController")
    @RequestMapping("/test")
    public ResponseVo test(@RequestParam("val") String val) {
        return resSuccess("test success", val);
    }
}
