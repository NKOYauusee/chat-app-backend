package com.example.web_back.controller;

import com.example.web_back.annotation.FucLogger;
import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.mapper.UserFriMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin("*")
@RestController
public class TestController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);


    @Resource
    UserFriMapper friMapper;

    //@RepeatSubmit
    @FucLogger("TestController")
    @RequestMapping("/test")
    public ResponseVo test(String who, String email) {

        return resSuccess("test success", friMapper.isFriendWithWho(who, email));
    }
}
