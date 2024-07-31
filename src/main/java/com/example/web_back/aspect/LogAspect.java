package com.example.web_back.aspect;

import com.alibaba.fastjson2.JSONObject;
import com.example.web_back.annotation.FucLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before(value = "@annotation(com.example.web_back.annotation.FucLogger)")
    public void before(JoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        FucLogger fucLogger = methodSignature.getMethod().getAnnotation(FucLogger.class);

        //String[] parameterNames = methodSignature.getParameterNames();
        //Object [] paramVal = point.getArgs();

        logger.info(" {} -> {}", fucLogger.value(), JSONObject.toJSONString(point.getArgs()));
    }
}
