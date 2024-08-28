package com.example.web_back.entity.po;

import lombok.Data;

@Data
public class SegmentFile {
    Integer id;
    String filePath;
    String fileName;
    Integer size;
    Integer segmentSize;
    Integer segmentTotal;
    Integer segmentIndex;
    String md5Key;

}
