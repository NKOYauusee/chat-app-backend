package com.example.web_back.entity.po;

import lombok.Data;

@Data
public class User {
    private Integer id;

    private String username;

    private String email;

    private String password;

    private String phone;

    private String avatar;
}
