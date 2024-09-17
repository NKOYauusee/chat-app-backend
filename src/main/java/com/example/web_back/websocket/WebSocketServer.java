package com.example.web_back.websocket;

import com.alibaba.fastjson2.JSON;
import com.example.web_back.entity.po.ChatBean;
import com.example.web_back.utils.MyJwtUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Component("WebSocketServer")
public class WebSocketServer extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    @Resource
    WebSocketManager webSocketManager;

    @Resource
    private MyJwtUtil myJwtUtil;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
//        // websocket入参
//        String rawPath = session.getUri().getPath(); //
//        String query = session.getUri().getQuery(); //  从url上面获取参数
//
//        logger.info(rawPath);
//        logger.info(query);
//
        HttpHeaders headers = session.getHandshakeHeaders(); // 从header上获取参数
        String token = Objects.requireNonNull(headers.get("token")).get(0);
        if (token != null) {
            WebSocketSession s = webSocketManager.get(token);
            if (s != null) {
                webSocketManager.remove(token);
            }

            // 重新建立session
            webSocketManager.add(token, session);
        }
    }

    /**
     * 接收消息事件 @OnMessage
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // 获得客户端传来的消息
        String payload = message.getPayload();
        //响应请求
        ChatBean chatBean = JSON.to(ChatBean.class, payload);
        chatBean.setSendTime(new Date().getTime());
        logger.info("server 待发送消息-> {},", JSON.toJSONString(chatBean));
        // TODO 校验好友状态
        session.sendMessage(new TextMessage(JSON.toJSONString(chatBean)));
        //转发请求
        webSocketManager.sendMsg(chatBean);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String token = Objects.requireNonNull(session.getHandshakeHeaders().get("token")).get(0);
        //logger.info("afterConnectionClosed {}", token);
        if (token != null) {
            // 用户退出，移除缓存
            webSocketManager.remove(token);
        }

        logger.info("{}连接断开", myJwtUtil.getJwtVal(token));
    }
}
