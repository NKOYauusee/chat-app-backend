package com.example.web_back.service.impl;

import com.example.web_back.constants.UserConstants;
import com.example.web_back.entity.po.ChatBean;
import com.example.web_back.mapper.ChatBeanMapper;
import com.example.web_back.service.ChatService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("ChatServiceImpl")
public class ChatServiceImpl implements ChatService {
    @Resource
    ChatBeanMapper mapper;

    // 获取最近20条消息
    @Override
    public List<ChatBean> get20OfflineMsg(String key) {
        long date = new Date().getTime() - UserConstants.DAY_ONE * 2;
        return mapper.get20OfflineMsg(key);
    }

    // 获取最近两天的消息
    @Override
    public List<ChatBean> get2DayOfflineMsg(String key) {
        long date = new Date().getTime() - UserConstants.DAY_ONE * 2;
        var list = mapper.get2DayOfflineMsg(key, date);
        mapper.deleteOfflineMsg(key);
        return list;
    }

    @Override
    public List<ChatBean> getOfflineWithSb(String key, String sender, Long day) {
        Long date = null;
        if (day != null) {
            date = new Date().getTime() - UserConstants.DAY_ONE * day;
        }
        List<ChatBean> res = mapper.getOfflineWithSb(key, sender, date);
        mapper.deleteOfflineMsg(key);
        return res;
    }
}
