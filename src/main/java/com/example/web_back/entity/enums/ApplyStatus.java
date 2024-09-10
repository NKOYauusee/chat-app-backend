package com.example.web_back.entity.enums;

import com.example.web_back.exception.BusinessException;
import lombok.Getter;

// 申请处理状态枚举
@Getter
public enum ApplyStatus {
    PENDING(0, "未同意"),  // 状态 0: 未同意
    APPROVED(1, "已同意"), // 状态 1: 已同意
    REJECTED(2, "已拒绝");   // 状态 2: 拒绝

    // 获取状态码
    private final int code;
    // 获取状态描述
    private final String description;

    // 构造函数
    ApplyStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    // 根据状态码获取枚举实例
    public static ApplyStatus fromCode(int code) throws BusinessException {
        for (ApplyStatus status : ApplyStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }

        throw new BusinessException("好友申请处理失败");
        //return PENDING;
    }

    @Override
    public String toString() {
        return description;
    }
}
