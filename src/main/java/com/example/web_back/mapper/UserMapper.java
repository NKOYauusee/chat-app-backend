package com.example.web_back.mapper;

import com.example.web_back.entity.po.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    void register(User user);

    int insert(User record);

    int deleteByEmail(String email);

    User selectByEmail(String email);

    int updateByEmailSelective(User record);

    List<User> searchWithKeyword(String searchContent);
}
