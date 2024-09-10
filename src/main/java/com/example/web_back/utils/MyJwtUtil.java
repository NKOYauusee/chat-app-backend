package com.example.web_back.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.example.web_back.constants.UserConstants;
import com.example.web_back.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyJwtUtil {
    @Value("${nko.jwtKey}")
    private String jwtKey;
    private final Logger logger = LoggerFactory.getLogger(MyJwtUtil.class);

    @Resource
    UserMapper userMapper;

    public String generateToken(String email) {
        Map<String, Object> map = new HashMap<>() {
            @Serial
            private static final long serialVersionUID = 1L;

            {
                put("uid", email);
                put("expire_time", System.currentTimeMillis() + UserConstants.DAY_ONE * 2);
            }
        };


        return JWTUtil.createToken(map, jwtKey.getBytes());
    }

    // false 为 正确token
    public boolean validateToken(String token) {
        logger.info("=========================");
        return JWTUtil.verify(token, jwtKey.getBytes());
    }

    public String getJwtVal(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        //logger.info("账户：{}", val);
        return (String) jwt.getPayload("uid");
    }
}

