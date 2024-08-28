package com.example.web_back.interceptor;

import com.alibaba.fastjson2.JSON;
import com.example.web_back.annotation.RepeatSubmit;
import com.example.web_back.entity.enums.ResponseCodeEnum;
import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.utils.http.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component("RepeatSubmitInterceptor")
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(RepeatSubmitInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                if (this.isRepeatSubmit(request, annotation)) {
                    ResponseVo responseVo = new ResponseVo<>(ResponseCodeEnum.CODE_400, annotation.message(), null);
                    ServletUtil.renderString(response, JSON.toJSONString(responseVo));
                    return false;
                }
            }

            return true;
        }

        return true;
    }

    protected abstract boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation) throws Exception;
}
