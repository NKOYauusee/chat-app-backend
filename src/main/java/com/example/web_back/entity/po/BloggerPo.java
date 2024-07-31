package com.example.web_back.entity.po;

import lombok.Data;

import java.util.Date;

@Data
public class BloggerPo {
    String phone;
    String sessionID;
    String ip;
    Date loginTime;
}
