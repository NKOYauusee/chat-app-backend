package com.example.web_back.service;

import com.example.web_back.entity.po.UserApply;
import com.example.web_back.entity.dao.UserFriendDao;
import com.example.web_back.exception.BusinessException;

import java.util.List;

public interface UserFriService {

    List<UserFriendDao> getFriendList(String email);

    void insertApplyFriend(UserApply userApply) throws BusinessException;

    void setApplyStatus(UserApply userApply);

    List<UserApply> selectUserApplyStatus(String applicant);

    int getHasFriendApplyList(String applicant);

    void deleteFriend(String email, String friend);

    void batchDeleteFriend(List<String> friendDaoList, String who);

    void setFriendStatus(UserFriendDao userFriend) throws BusinessException;

}
