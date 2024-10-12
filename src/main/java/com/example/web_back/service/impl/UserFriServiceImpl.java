package com.example.web_back.service.impl;

import com.example.web_back.entity.enums.ApplyStatus;
import com.example.web_back.entity.po.User;
import com.example.web_back.entity.po.UserApply;
import com.example.web_back.entity.dao.UserFriendDao;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.mapper.UserFriMapper;
import com.example.web_back.mapper.UserMapper;
import com.example.web_back.service.UserFriService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("UserFriServiceImpl")
public class UserFriServiceImpl implements UserFriService {
    @Resource
    UserFriMapper friMapper;

    @Resource
    UserMapper userMapper;

    @Override
    public List<UserFriendDao> getFriendList(String email) {
        var friendList = friMapper.getAllFriend(email);
        friendList.addAll(friMapper.getAllGroups(email));

        return friendList;
    }

    // 申请
    @Override
    public void insertApplyFriend(UserApply userApply) throws BusinessException {
        User res = userMapper.selectByEmail(userApply.getTarget());
        if (res == null) {
            throw new BusinessException("用户账号异常");
        }

        userApply.setStatus(ApplyStatus.PENDING.getCode());
        userApply.setTime(new Date().getTime());

        insertUserApply(userApply);
    }

    // 处理申请
    @Override
    public void setApplyStatus(UserApply userApply) {
        friMapper.setApplyStatus(userApply);
        if (ApplyStatus.APPROVED.getCode() == userApply.getStatus()) {
            friMapper.deleteFriend(userApply.getApplicant(), userApply.getTarget());
            friMapper.insertFriend(userApply.getApplicant(), userApply.getTarget());

            friMapper.deleteFriend(userApply.getTarget(), userApply.getApplicant());
            friMapper.insertFriend(userApply.getTarget(), userApply.getApplicant());
        }
    }

    @Override
    public List<UserApply> selectUserApplyStatus(String applicant) {
        return friMapper.selectUseApplyList(applicant);
    }

    @Override
    public int getHasFriendApplyList(String applicant) {
        return friMapper.getHasFriendApply(applicant).size();
    }

    @Override
    public void deleteFriend(String email, String friend) {
        // 单方面删除
        friMapper.deleteFriend(email, friend);
        // friMapper.deleteFriend(friend, email);
    }

    @Override
    public void batchDeleteFriend(List<UserFriendDao> friendDaoList) {
        for (UserFriendDao userFriendDao : friendDaoList) {
            friMapper.deleteFriend(userFriendDao.getOwner(), userFriendDao.getEmail());
        }
    }

    @Override
    public void setFriendStatus(UserFriendDao userFriend) {
        UserFriendDao res = friMapper.isFriendWithWho(userFriend.getOwner(), userFriend.getEmail());

        if (res == null) {
            // throw new BusinessException("请先添加好友再进行操作");
            friMapper.insertFriend(userFriend.getOwner(), userFriend.getEmail());
        }

        friMapper.setFriendStatus(userFriend.getStatus(), userFriend.getOwner(), userFriend.getEmail());
    }

    private void insertUserApply(UserApply userApply) {
        friMapper.deleteApply(userApply.getTarget(), userApply.getApplicant());
        friMapper.insertUserApply(userApply);
    }
}
