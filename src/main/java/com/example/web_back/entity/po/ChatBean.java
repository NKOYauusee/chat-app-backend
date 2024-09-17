package com.example.web_back.entity.po;


import lombok.Data;

@Data
public class ChatBean {
    String sender;
    String senderName;
    String receiver;
    String receiverName;
    String message;
    long sendTime;
    String owner;
    int msgType;
}
