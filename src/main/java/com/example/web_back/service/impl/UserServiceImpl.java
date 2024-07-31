package com.example.web_back.service.impl;

import cn.hutool.jwt.JWTUtil;
import com.example.web_back.entity.po.User;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.mapper.user.UserMapper;
import com.example.web_back.service.UserService;
import com.example.web_back.utils.MyJwtUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    UserMapper userMapper;

    @Resource
    MyJwtUtil jwtUtil;

    @Override
    public void register(User user) throws BusinessException {
        User res = userMapper.selectByEmail(user.getEmail());
        if (res != null) {
            throw new BusinessException("该账户已注册");
        }
        userMapper.register(user);
    }

    @Override
    public String login(User user) throws BusinessException {
        User res = userMapper.selectByEmail(user.getEmail());
        if (res == null) {
            throw new BusinessException("该账户未注册");
        }
        if (!user.getPassword().equals(res.getPassword())) {
            throw new BusinessException("密码错误");
        }
        logger.info("User->{} login success", user.getEmail());
        return jwtUtil.generateToken(user.getEmail());
    }

    @Override
    public void update(User user){
        userMapper.updateByEmailSelective(user);
    }

}
