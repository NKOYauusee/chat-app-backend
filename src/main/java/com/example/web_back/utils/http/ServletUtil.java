package com.example.web_back.utils.http;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ServletUtil {
    private static final Logger logger = LoggerFactory.getLogger(ServletUtil.class);

    public static void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            //e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}
