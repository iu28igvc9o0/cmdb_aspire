package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.http.HttpEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

import feign.FeignException;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;

public class FastJsonDecoder implements Decoder {
    private static final Class<?>[] EXCLUDE_CLASSES = { HttpEntity.class, ModelAndView.class, ModelMap.class,
            Model.class, View.class, Void.class };

    private Decoder decoder;

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        Class<?> clazz = null;
        if (type instanceof ParameterizedType) {
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType instanceof Class<?>) {
                clazz = (Class<?>) rawType;
            }
        } else if (type instanceof Class<?>) {
            clazz = (Class<?>) type;
        }

        if (clazz != null) {
            for (Class<?> c : EXCLUDE_CLASSES) {
                if (c.isAssignableFrom(clazz)) {
                    return getDecoder().decode(response, type);
                }
            }
        }

        return JSON.parseObject(response.body().asInputStream(), StandardCharsets.UTF_8, type);
    }

    public Decoder getDecoder() {
        if (decoder != null) {
            return decoder;
        }

        HttpMessageConvertersFactory fac = Beans.getBean(HttpMessageConvertersFactory.class);
        decoder = new ResponseEntityDecoder(new SpringDecoder(fac.get()));

        return decoder;
    }
}
