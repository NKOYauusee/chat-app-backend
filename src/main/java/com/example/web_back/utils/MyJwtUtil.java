package com.example.web_back.utils;

import cn.hutool.jwt.JWTUtil;
import com.example.web_back.constants.UserConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyJwtUtil {
    @Value("${nko.jwtKey}")
    private String jwtKey;

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

    public boolean validateToken(String token) {
        return JWTUtil.verify("map", jwtKey.getBytes());
    }
}

