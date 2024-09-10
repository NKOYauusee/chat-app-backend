package com.example.web_back.mapper;

import com.example.web_back.entity.po.ChatBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatBeanMapper {

    List<ChatBean> get20OfflineMsg(String key);

    List<ChatBean> get2DayOfflineMsg(String key, long date);

    List<ChatBean> getOfflineWithSb(String key, String sender, Long date);

    void saveOfflineMsg(ChatBean chats);

    void deleteOfflineMsg(String key);
}
