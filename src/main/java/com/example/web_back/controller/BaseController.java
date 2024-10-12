package com.example.web_back.controller;


import com.example.web_back.entity.enums.ResponseCodeEnum;
import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.exception.BusinessException;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BaseController {
    private final Logger logger = LoggerFactory.getLogger(BaseController.class);

//    @ExceptionHandler(Exception.class)
//    public ResponseVo handleException(Exception e) {
//        logger.error(e.getMessage());
//        return resFail(e.getMessage(), null);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseVo handleMethodArgumentNotValidException(Exception e) {
        logger.error("ERROR", e);
        return resFail(e);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseVo handleBusinessException(BusinessException e) {
        logger.error("ERROR INFO\n", e);
        return resFail(e);
    }

    public ResponseVo resSuccess() {
        return new ResponseVo<>(ResponseCodeEnum.CODE_200, null, null);
    }

    public <T> ResponseVo resSuccess(String info, T data) {
        return new ResponseVo<>(ResponseCodeEnum.CODE_200, info, data);
    }

    public ResponseVo resFail() {
        return new ResponseVo<>(ResponseCodeEnum.CODE_500, null, null);
    }

    public <T> ResponseVo resFail(String info, T data) {
        return new ResponseVo<>(ResponseCodeEnum.CODE_400, info, data);
    }

    private ResponseVo resFail(Exception e) {
        if (e instanceof BusinessException) {
            return resFailException((BusinessException) e);
        } else if (e instanceof MethodArgumentNotValidException) {
            return resFailException((MethodArgumentNotValidException) e);
        } else if (e instanceof MessagingException) {
            return resFail("验证码发送失败，请稍后再试", null);
        }
        return resFail();
    }

    private ResponseVo resFailException(BusinessException e) {
        return new ResponseVo<>(e);
    }

    private ResponseVo resFailException(MethodArgumentNotValidException e) {
        Map<String, String> result = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        for (ObjectError objectError : bindingResult.getAllErrors()) {
            FieldError fieldError = (FieldError) objectError;
            result.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return resFail("", result);
    }
}
