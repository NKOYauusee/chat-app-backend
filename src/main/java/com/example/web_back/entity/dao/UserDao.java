package com.example.web_back.entity.dao;

import lombok.Data;

@Data
public class UserDao {
    private Integer id;

    private String username;

    private String email;

    private String phone;

    private String avatar;

    private String token;
}
