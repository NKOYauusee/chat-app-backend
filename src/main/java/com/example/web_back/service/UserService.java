package com.example.web_back.service;

import com.example.web_back.entity.dao.UserDao;
import com.example.web_back.entity.po.User;
import com.example.web_back.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDao register(User user) throws BusinessException;

    String getToken(User user) throws BusinessException;

    void update(User user);

    UserDao login(User user) throws BusinessException;

    UserDao registerWithProfile(User user, MultipartFile fileData) throws BusinessException;

    List<User> search(String searchContent, int page);
}

