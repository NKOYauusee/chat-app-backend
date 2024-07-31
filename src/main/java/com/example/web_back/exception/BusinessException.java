package com.example.web_back.exception;


import com.example.web_back.entity.enums.ResponseCodeEnum;
import lombok.Getter;

@Getter
public class BusinessException extends Exception {
    private final Integer code;
    private final String status;

    public BusinessException(ResponseCodeEnum responseCodeEnum) {
        this.code = responseCodeEnum.getCode();
        this.status = responseCodeEnum.getStatus();
    }

    public BusinessException(String msg) {
        this.code = ResponseCodeEnum.CODE_600.getCode();
        this.status = msg;
    }
}
