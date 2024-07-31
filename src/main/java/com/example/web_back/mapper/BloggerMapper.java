package com.example.web_back.mapper;

import com.example.web_back.entity.po.BloggerPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BloggerMapper {

    BloggerPo selectByPhone(String phone);

    void updateJoinInfo(BloggerPo po);
}
