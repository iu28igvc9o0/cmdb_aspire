package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BodyReaderRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    public BodyReaderRequestWrapper(HttpServletRequest request) {
        super(request);

        try {
            body = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return input.read();
            }

//            @Override
            public void setReadListener(ReadListener listener) {
            }

//            @Override
            public boolean isReady() {
                return true;
            }

//            @Override
            public boolean isFinished() {
                return input.available() > 0;
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ServletInputStream inputStream = getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return new BufferedReader(inputStreamReader);
    }
}
