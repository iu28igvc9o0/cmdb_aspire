package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import java.lang.reflect.Type;

import com.alibaba.fastjson.JSON;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;

public class FastJsonEncoder implements Encoder {

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        template.header("Content-Type", "application/json;charset=UTF-8");
        template.body(JSON.toJSONString(object));
    }
}
