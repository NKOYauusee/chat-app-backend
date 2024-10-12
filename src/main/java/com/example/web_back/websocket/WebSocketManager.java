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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketManager {
    public static final Logger logger = LoggerFactory.getLogger(WebSocketManager.class);

    public static ConcurrentHashMap<String, WebSocketSession> conns = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, List<WebSocketSession>> group = new ConcurrentHashMap<>();


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

        if (!isGroup(chatBean.getReceiver())) {
            logger.info("user");

            if (!helper.isSavaOfflineMsg(chatBean.getReceiver(), chatBean.getSender())) {
                return;
            }

            chatBean.setOwner(chatBean.getReceiver());
            sendToUser(receiver, chatBean);
        } else {
            logger.info("group");

            //logger.info("receiver {}", chatBean.getReceiver().toLowerCase());
            // wrong
            joinGroup(chatBean.getReceiver(), conns.get(chatBean.getSender().toLowerCase()));
            broadcast(chatBean.getReceiver(), chatBean);
        }
    }

    private void joinGroup(String groupId, WebSocketSession ws) throws IOException {
        if (ws == null)
            return;

        group.computeIfAbsent(groupId, k -> new ArrayList<>());
        if (!group.get(groupId).contains(ws)) {
            logger.info("加入群组 {}", groupId);
            group.get(groupId).add(ws);
        } else {
            if (!ws.isOpen()) {
                ws.close();
            }
            group.get(groupId).remove(ws);
            group.get(groupId).add(ws);
        }
    }

    private void broadcast(String groupId, ChatBean chat) throws IOException {
        if (!group.containsKey(groupId))
            return;

        logger.info("开始群发");
        for (WebSocketSession member : group.get(groupId)) {
            if (member.isOpen()) {
                if (!(conns.get(chat.getSender()) == member)) {
                    String msg = JSON.toJSONString(chat);
                    member.sendMessage(new TextMessage(msg));
                }
            } else {
                member.close();
                group.get(groupId).remove(member);
                logger.info("群发异常");
            }
        }
    }

    private void sendToUser(WebSocketSession receiver, ChatBean chatBean) throws IOException {
        if (receiver != null) {
            String msg = JSON.toJSONString(chatBean);
            receiver.sendMessage(new TextMessage(msg));
        } else {
            // todo 数据存储
            logger.info("该账户离线中 receiver: {}", chatBean.getReceiver());
            try {
                offlineHelper.saveOneLine(chatBean);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    private boolean isGroup(String receiver) {
        if (receiver.charAt(0) == 'G') {
            try {
                var id = receiver.substring(1);
                if (id.length() != 11)
                    return false;

                return isAllDigits(id);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return false;
    }

    public boolean isAllDigits(String str) {
        if (str == null) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
