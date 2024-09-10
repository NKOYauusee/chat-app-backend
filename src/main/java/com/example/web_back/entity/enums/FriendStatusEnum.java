package com.example.web_back.entity.enums;


import lombok.Getter;

@Getter
public enum FriendStatusEnum {
    NORMAL(0),  // 正常
    BLOCKED(1), // 拉黑
    SHIELDED(2); // 屏蔽

    private final int statusCode;

    FriendStatusEnum(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    // 根据状态码获取枚举实例
    public static FriendStatusEnum fromStatusCode(int statusCode) {
        for (FriendStatusEnum status : FriendStatusEnum.values()) {
            if (status.getStatusCode() == statusCode) {
                return status;
            }
        }

        //throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        return NORMAL;
    }
}
