package com.example.web_back.mapper;

import com.example.web_back.entity.po.SegmentFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SegmentFileMapper {

    SegmentFile getProgressByMd5(String md5Key);
}
