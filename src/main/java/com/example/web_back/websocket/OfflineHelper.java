package com.example.web_back.websocket;

import com.alibaba.fastjson2.JSON;
import com.example.web_back.entity.po.ChatBean;
import com.example.web_back.mapper.ChatBeanMapper;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("OfflineHelper")
public class OfflineHelper {
    @Resource
    ChatBeanMapper chatBeanMapper;

    private final Logger logger = LoggerFactory.getLogger(OfflineHelper.class);

    @Async
    public void saveOneLine(ChatBean chatBean) {
        chatBeanMapper.saveOfflineMsg(chatBean);
    }
}
