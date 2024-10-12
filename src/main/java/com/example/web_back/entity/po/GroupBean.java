package com.example.web_back.entity.po;


import lombok.Data;

@Data
public class GroupBean {
    String id;
    String avatar;
    String name;
    String creator;
    String info;
    long date;
}
