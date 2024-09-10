package com.example.web_back.entity.dao;

import lombok.Data;

@Data
public class UserFriendDao {
    String email;
    String username;
    String avatar;
    String owner;
    // 0 正常 1拉黑 2屏蔽
    Integer status;
}
