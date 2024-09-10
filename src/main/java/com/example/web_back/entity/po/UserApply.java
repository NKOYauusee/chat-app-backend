package com.example.web_back.entity.po;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserApply implements Serializable {
    String target;
    String applicant;
    String applicantName;
    String info;
    Long time;
    // 申请状态 0未同意 1已同意 2拒绝
    Integer status;
}
