package com.example.web_back.annotation;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {
    int interval() default 5;
    String message() default "请求频繁, 请稍候再试";
}
