package com.example.web_back.entity.enums;

import lombok.Getter;

@Getter
public enum ResponseCodeEnum {
    CODE_200(200, "SUCCESS"),
    CODE_400(400, "Bad Request"),
    CODE_403(403, "Forbidden"),
    CODE_404(404, "Not Found"),
    CODE_500(500, "Internal Server Error"),
    CODE_600(600, "Request Parameter Error");

    private final Integer code;

    private final String status;

    ResponseCodeEnum(int code, String status) {
        this.code = code;
        this.status = status;
    }
}
