package com.example.web_back.websocket;

import com.alibaba.fastjson2.JSON;
import com.example.web_back.entity.po.ChatBean;
import com.example.web_back.utils.MyJwtUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketManager {
    public static final Logger logger = LoggerFactory.getLogger(WebSocketManager.class);
    public static ConcurrentHashMap<String, WebSocketSession> conns = new ConcurrentHashMap<>();

    @Resource
    MyJwtUtil jwtUtil;

    @Resource
    OfflineHelper offlineHelper;


    @Resource
    MessageHelper helper;

    public boolean verifyAndAdd(String token, WebSocketSession ws) {
        conns.put(token, ws);
        return true;
    }

    public WebSocketSession get(String token) {
        String t = jwtUtil.getJwtVal(token).toLowerCase();
        return conns.get(t);
    }

    public void remove(String token) {
        String t = jwtUtil.getJwtVal(token).toLowerCase();
        WebSocketSession remove = conns.remove(t);
        try {
            remove.close();
        } catch (IOException e) {
            logger.error("ws断开错误");
        }
    }

    public void add(String token, WebSocketSession session) {
        String t = jwtUtil.getJwtVal(token).toLowerCase();
        logger.info("连接成功 {}", t);
        conns.put(t, session);
    }

    public void sendMsg(ChatBean chatBean) throws IOException {
        //logger.info("转发消息 {}", JSON.toJSONString(chatBean));
        WebSocketSession receiver = conns.get(chatBean.getReceiver().toLowerCase());

        chatBean.setOwner(chatBean.getReceiver());

        if (!helper.isSavaOfflineMsg(chatBean.getReceiver(), chatBean.getSender())) {
            return;
        }


        if (receiver != null) {
            String msg = JSON.toJSONString(chatBean);
            receiver.sendMessage(new TextMessage(msg));
        } else {
            // todo 数据存储
            logger.info("该账户离线中 receiver: {}", chatBean.getReceiver());
            try {
                offlineHelper.saveOneLine(chatBean);
            } catch (Exception e) {
                logger.error("",e);
            }
        }
    }
}
