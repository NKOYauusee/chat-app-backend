package com.example.web_back.service.impl;

import com.example.web_back.entity.dao.UserDao;
import com.example.web_back.entity.po.User;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.mapper.UserMapper;
import com.example.web_back.service.UserService;
import com.example.web_back.utils.MyJwtUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    UserMapper userMapper;

    @Resource
    MyJwtUtil jwtUtil;

    @Resource
    MediaServiceImpl mediaService;

    @Override
    public UserDao register(User user) throws BusinessException {
        User res = userMapper.selectByEmail(user.getEmail());
        if (res != null) {
            throw new BusinessException("该账户已注册");
        }

        userMapper.register(user);
        return getUserDao(user);
    }

    @Override
    public UserDao registerWithProfile(User user, MultipartFile file) throws BusinessException {
        User res = userMapper.selectByEmail(user.getEmail());
        if (res != null) {
            throw new BusinessException("该账户已注册");
        }
        userMapper.register(user);
        UserDao resDao = getUserDao(user);

        String avatar = mediaService.uploadProfile(file, resDao.getId().toString(), resDao.getEmail());
        resDao.setAvatar(avatar);

        return resDao;
    }

    @Override
    public List<User> search(String searchContent) {
        return userMapper.searchWithKeyword(searchContent);
    }


    @Override
    public String getToken(User user) throws BusinessException {
        User res = userMapper.selectByEmail(user.getEmail());
        verifyLoginUser(user, res);
        return jwtUtil.generateToken(user.getEmail());
    }

    @Override
    public void update(User user) {
        userMapper.updateByEmailSelective(user);
    }

    @Override
    public UserDao login(User user) throws BusinessException {
        return getUserDao(user);
    }

    void verifyLoginUser(User user, User res) throws BusinessException {
        if (res == null) {
            throw new BusinessException("该账户未注册");
        }
        if (!user.getPassword().equals(res.getPassword())) {
            throw new BusinessException("密码错误");
        }
        logger.info("User->{} login success", user.getEmail());
    }

    private UserDao getUserDao(User user) throws BusinessException {
        User res = userMapper.selectByEmail(user.getEmail());
        verifyLoginUser(user, res);

        UserDao userDao = new UserDao();
        userDao.setId(res.getId());
        userDao.setUsername(res.getUsername());
        userDao.setEmail(res.getEmail());
        userDao.setAvatar(res.getAvatar());
        userDao.setPhone(res.getPhone());
        userDao.setToken(jwtUtil.generateToken(user.getEmail()));

        return userDao;
    }
}
