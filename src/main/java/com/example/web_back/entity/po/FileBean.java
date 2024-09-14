package com.example.web_back.entity.po;

import lombok.Data;

@Data
public class FileBean {
    String userId;
    String fileType;
    String fileSize;
    Long uploadTime;
    String fileData;
}
