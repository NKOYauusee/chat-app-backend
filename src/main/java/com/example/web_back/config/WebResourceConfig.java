package com.example.web_back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration("WebResourceConfig")
public class WebResourceConfig implements WebMvcConfigurer {
    @Value("${nko.filePath}")
    String path;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/android/images/**")
                .addResourceLocations("file:///" + path);
    }
}
