package com.example.web_back.mapper;


import com.example.web_back.entity.dao.UserFriendDao;
import com.example.web_back.entity.po.UserApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserFriMapper {
    // 查询某用户的好友列表
    List<UserFriendDao> getAllFriend(String email);

    List<UserFriendDao> getAllGroups(String email);


    // 设置某用户与该好友的状态 拉黑等
    void setFriendStatus(int status, String who, String friend);

    // 提交好友申请
    void insertUserApply(UserApply userApply);

    // 删除好友申请
    void deleteApply(String target, String apply);

    // 处理好友申请
    void setApplyStatus(UserApply userApply);

    // 查看某用户所有的好友申请处理状态
    List<UserApply> selectUseApplyList(String email);

    List<UserApply> getHasFriendApply(String email);

    // 添加好友
    void insertFriend(String u1, String u2);

    // 删除单个好友
    void deleteFriend(String u1, String u2);

    // 判断是否是好友
    UserFriendDao isFriendWithWho(String who, String friend);
}
