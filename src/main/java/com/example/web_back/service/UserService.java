package com.example.web_back.service;

import com.example.web_back.entity.po.User;
import com.example.web_back.exception.BusinessException;

public interface UserService {
    void register(User user) throws BusinessException;

    String login(User user) throws BusinessException;

    void update(User user);
}

