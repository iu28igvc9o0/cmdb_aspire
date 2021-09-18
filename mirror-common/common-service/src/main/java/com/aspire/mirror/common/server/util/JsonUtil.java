package com.aspire.mirror.common.server.util;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
* JsonPath工具类
* Project Name:jarchemist-service
* File Name:JsonUtil.java
* Package Name:com.migu.tsg.microservice.atomicservice.jalchemist.biz.util
* ClassName: JsonUtil <br/>
* date: 2018年3月7日 上午10:39:32 <br/>
* 
* @author pengguihua
* @version 
* @since JDK 1.6
*/
public final class JsonUtil {
    // ObjectMapper为线程安全类, 全局公用一个实例即可
    private static final ObjectMapper OBJ_MAPPER = configureJacksonMapper();
    
    
    /**
    * 默认jackson对象映射配置.<br/>
    *
    * 作者： pengguihua
    * @return
    */
    private static ObjectMapper configureJacksonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }
    
    /**
    * 基于jackson进行对象转换
    *
    * 作者： pengguihua
    * @param source
    * @param targetClazz
    * @return
    */
    public static <T> T jacksonConvert(Object source, Class<T> targetClazz) {
    	try {
	    	if (String.class.isInstance(source)) {
					return OBJ_MAPPER.readValue(String.class.cast(source), targetClazz);
	    	}
    	} catch (IOException e) {
    		throw new RuntimeException("Error to parse object based on jackson.", e);
    	}
        return OBJ_MAPPER.convertValue(source, targetClazz);
    }
    
    public static <T> T jacksonConvert(Object source, TypeReference<T> targetType) {
    	try {
	    	if (String.class.isInstance(source)) {
					return OBJ_MAPPER.readValue(String.class.cast(source), targetType);
	    	}
    	} catch (IOException e) {
    		throw new RuntimeException("Error to parse object based on jackson.", e);
    	}
    	return OBJ_MAPPER.convertValue(source, targetType);
    }
}
