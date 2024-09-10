package com.example.web_back.service;

import com.example.web_back.entity.po.ChatBean;

import java.util.List;

public interface ChatService {

    List<ChatBean> get20OfflineMsg(String key);

    List<ChatBean> get2DayOfflineMsg(String key);

    List<ChatBean> getOfflineWithSb(String key, String sender, Long date);
}
