package com.example.web_back.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.example.web_back.entity.enums.FriendStatusEnum;
import com.example.web_back.entity.po.GroupBean;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.mapper.GroupMapper;
import com.example.web_back.mapper.UserFriMapper;
import com.example.web_back.service.GroupService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class GroupServiceImpl implements GroupService {


    @Resource
    GroupMapper groupMapper;

    @Resource
    UserFriMapper friMapper;

    @Override
    public void createGroup(GroupBean groupBean) {
        groupBean.setDate(new Date().getTime());
        groupBean.setId(generateGroupId());

        groupMapper.insertGroup(groupBean);

        friMapper.insertFriend(groupBean.getCreator(), groupBean.getId());
    }

    @Override
    public void applyGroup(String who, String groupId) throws BusinessException {
        var res = friMapper.isFriendWithWho(who, groupId);
        if (res != null) {
            if (res.getStatus() != FriendStatusEnum.NORMAL.getStatusCode())
                throw new BusinessException("操作失败，该用户无法添加至群内");
            throw new BusinessException("该用户已在群内，请勿重复添加");
        }

        friMapper.insertFriend(who, groupId);
    }

    private String generateGroupId() {
        return "G" + RandomUtil.randomNumbers(11);
    }
}
