package com.example.web_back.entity.enums;

import lombok.Getter;

@Getter
public enum MessageType {

    TEXT(0, ""),    // 文本
    IMAGE(1, "[图片]"),   // 图片
    VIDEO(2, "[视频]"),   // 视频
    FILE(3, "[文件]");    // 文件

    // 获取枚举对应的数值
    private final int type;
    private final String desc;

    // 构造方法
    MessageType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    // 通过整型值获取对应的枚举类型
    public static MessageType fromType(int type) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.getType() == type) {
                return messageType;
            }
        }
        return null; // 未找到对应的枚举类型时返回 null
    }

    public static String getDescFromType(int type) {
        for (MessageType messageType : MessageType.values()) {
            if (messageType.getType() == type) {
                return messageType.getDesc();
            }
        }
        return null; // 未找到对应的枚举类型时返回 null
    }

}
