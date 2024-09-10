package com.example.web_back.websocket;

import com.alibaba.fastjson2.JSON;
import com.example.web_back.utils.MyJwtUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component("WebSocketAuthInterceptor")
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    private final Logger logger = LoggerFactory.getLogger(WebSocketAuthInterceptor.class);

    @Resource
    MyJwtUtil myJwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        logger.info("Websocket 准备连接");
        // TODO token验证
        HttpHeaders headers = request.getHeaders();
        List<String> header = headers.get("token");
        logger.info(JSON.toJSONString(header));
        if (header == null || header.isEmpty()) {
            return false;
        }
        String token = header.get(0);

        return myJwtUtil.validateToken(token);
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {
        logger.info("握手结束");
    }
}
