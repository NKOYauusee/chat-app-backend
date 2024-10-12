package com.example.web_back.mapper;

import com.example.web_back.entity.po.GroupBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GroupMapper {

    void insertGroup(GroupBean groupBean);

    List<GroupBean> selectAll();
}
