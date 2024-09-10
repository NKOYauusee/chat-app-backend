package com.example.web_back.websocket;

import com.example.web_back.entity.po.ChatBean;
import com.example.web_back.mapper.ChatBeanMapper;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component("OfflineHelper")
public class OfflineHelper {
    @Resource
    ChatBeanMapper chatBeanMapper;

    @Async
    public void saveOneLine(ChatBean chatBean) {
        chatBeanMapper.saveOfflineMsg(chatBean);
    }
}
