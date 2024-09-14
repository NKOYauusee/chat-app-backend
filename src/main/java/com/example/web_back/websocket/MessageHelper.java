package com.example.web_back.websocket;


import com.example.web_back.entity.dao.UserFriendDao;
import com.example.web_back.mapper.UserFriMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component("MyMessageHelper")
public class MessageHelper {

    @Resource
    UserFriMapper friMapper;

    public boolean isSavaOfflineMsg(String targetUser, String friendEmail) {
        UserFriendDao friendDao = friMapper.isFriendWithWho(targetUser, friendEmail);
        //判断是否为好友
        if (friendDao == null)
            return false;

        //是否拉黑
        return friendDao.getStatus() == 0;
    }
}
