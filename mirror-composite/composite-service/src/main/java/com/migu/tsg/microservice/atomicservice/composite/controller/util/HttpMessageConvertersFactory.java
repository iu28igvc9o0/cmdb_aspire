package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.stereotype.Component;

@Component
public class HttpMessageConvertersFactory {
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    public ObjectFactory<HttpMessageConverters> get() {
        return messageConverters;
    }
}
