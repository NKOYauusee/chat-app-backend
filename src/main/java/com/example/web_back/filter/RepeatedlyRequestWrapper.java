package com.example.web_back.filter;

import com.example.web_back.utils.http.HttpHelper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper {
    private byte[] body;


    public RepeatedlyRequestWrapper(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        super(request);

        request.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());

        body = HttpHelper.getBodyString(request).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return stream.read();
            }

            @Override
            public int available() throws IOException {
                return body.length;
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }
}
