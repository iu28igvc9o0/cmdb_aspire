package com.aspire.mirror.log.server.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* 把model抽取出来进行转化
* Project Name:composite-service2
* File Name:PayloadParseUtil.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.util
* ClassName: PayloadParseUtil <br/>
* date: 2017年8月30日 上午10:30:10 <br/>
* @author yangshilei
* @version
* @since JDK 1.6
*/
public class PayloadParseUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayloadParseUtil.class);

    /**
    * 基于Jackson的对象转换.<br/>
    *
    * 作者： pengguihua
    * @param target
    * @param sourceList
    * @return
    */
    public static <T, S> List<T> jacksonBaseParse(Class<T> target, List<S> sourceList) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return sourceList.stream().map((sourceItem) -> {
            try {
                String json = mapper.writeValueAsString(sourceItem);
                if (CharSequence.class.isInstance(sourceItem)) {
                    json = String.valueOf(sourceItem);
                }
                return  mapper.readValue(json, target);
            } catch (Exception e) {
                throw new RuntimeException("Fail to parse object to json {}", e);
            }
        }).collect(Collectors.toList());
    }
    
    
    /**
    * 把net.sf.json.JSONArray数组转成基于JACKSON目标对象数组.<br/>
    *
    * 作者： pengguihua
    * @param target
    * @param sourceArr
    * @return
    */
    public static <T> List<T> jacksonJsonArrayParse(Class<T> target, JSONArray sourceArr) {
        List<Object> jsonItemList = new ArrayList<Object>();
        for (int i = 0; i < sourceArr.size(); i++) {
            Object item = sourceArr.get(i);
            jsonItemList.add(item);
        }
        return jacksonBaseParse(target, jsonItemList);
    }

    /**
    * 基于Jackson的对象转换. <br/>
    *
    * 作者： pengguihua
    * @param target
    * @param source
    * @return
    */
    public static <T, S> T jacksonBaseParse(Class<T> target, S source) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String json = mapper.writeValueAsString(source);
            if (CharSequence.class.isInstance(source)) {
                json = String.valueOf(source);
            }
            return mapper.readValue(json, target);
        } catch (Exception e) {
            throw new RuntimeException("Fail to parse object to json {}", e);
        }
    }
    
    /**
     * 基于Jackson的对象转换. <br/>
     *
     * 作者： pengguihua
     * @param target
     * @param source
     * @return
     */
     public static <T, S> T fastjsonBaseParse(Class<T> target, S source) {
         try {
             ObjectMapper mapper = new ObjectMapper();
             mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
             String json = mapper.writeValueAsString(source);
             if (CharSequence.class.isInstance(source)) {
                 json = String.valueOf(source);
             }
             return JSON.parseObject(json,target);
         } catch (Exception e) {
             throw new RuntimeException("Fail to parse object to json {}", e);
         }
     }
    
    
    /**
    * 把source对象基于jackson转化成json字符串.<br/>
    *
    * 作者： pengguihua
    * @param source
    * @return
    */
    public static String jacksonBase2JsonStr(Object source) {
        if (CharSequence.class.isInstance(source)) {
            return source.toString();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.writeValueAsString(source);
        } catch (Exception e) {
            throw new RuntimeException("Fail to parse object to json {}", e);
        }
    }

    
}
