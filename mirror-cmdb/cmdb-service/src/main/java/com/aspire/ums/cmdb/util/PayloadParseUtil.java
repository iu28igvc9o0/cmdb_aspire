package com.aspire.ums.cmdb.util;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.throwable.CmdbRuntimeException;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
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
                if (sourceItem instanceof CharSequence) {
                    json = String.valueOf(sourceItem);
                }
                // luowenbo 2020.07.17 JSON注入缺陷
                JsonStringEncoder encoder = JsonStringEncoder.getInstance();
                // quoteAsUTF8 方法将字符创按照 JSON 标准处理并编码为 UTF-8
                byte[] bytes = encoder.quoteAsUTF8(json);
                String newJson = new String(bytes, StandardCharsets.UTF_8);
                newJson = StringEscapeUtils.unescapeJava(newJson);
                return  mapper.readValue(newJson, target);
            } catch (Exception e) {
                throw new CmdbRuntimeException("Fail_to_parse_object_to_json", e);
            }
        }).collect(Collectors.toList());
    }

    public static <T, S> Result<T> jacksonResultParse(Class<T> target, Result<S> source) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Result<T> returnVal = new Result<>();
        if (source != null && source.getData() != null) {
            List<T> targetList =  source.getData().stream().map((sourceItem) -> {
                try {
                    String json = mapper.writeValueAsString(sourceItem);
                    if (sourceItem instanceof CharSequence) {
                        json = String.valueOf(sourceItem);
                    }
                    // luowenbo 2020.07.17 JSON注入缺陷
                    JsonStringEncoder encoder = JsonStringEncoder.getInstance();
                    // quoteAsUTF8 方法将字符创按照 JSON 标准处理并编码为 UTF-8
                    byte[] bytes = encoder.quoteAsUTF8(json);
                    String newJson = new String(bytes, StandardCharsets.UTF_8);
                    newJson = StringEscapeUtils.unescapeJava(newJson);
                    return mapper.readValue(newJson, target);
                } catch (Exception e) {
                    throw new CmdbRuntimeException("Fail_to_parse_object_to_json", e);
                }
            }).collect(Collectors.toList());
            returnVal.setData(targetList);
            returnVal.setColumns(source.getColumns());
            returnVal.setTotalSize(source.getTotalSize());
        }
        return returnVal;
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
            if (source instanceof CharSequence) {
                json = String.valueOf(source);
            }
            // luowenbo 2020.07.17 JSON注入缺陷
            JsonStringEncoder encoder = JsonStringEncoder.getInstance();
            // quoteAsUTF8 方法将字符创按照 JSON 标准处理并编码为 UTF-8
            byte[] bytes = encoder.quoteAsUTF8(json);
            String newJson = new String(bytes, StandardCharsets.UTF_8);
            newJson = StringEscapeUtils.unescapeJava(newJson);
            return mapper.readValue(newJson, target);
        } catch (Exception e) {
            throw new CmdbRuntimeException("Fail_to_parse_object_to_json", e);
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
             if (source instanceof CharSequence) {
                 json = String.valueOf(source);
             }
             return JSON.parseObject(json,target);
         } catch (Exception e) {
             throw new CmdbRuntimeException("Fail_to_parse_object_to_json", e);
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
        if (source instanceof CharSequence) {
            return source.toString();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.writeValueAsString(source);
        } catch (Exception e) {
            throw new CmdbRuntimeException("Fail_to_parse_object_to_json", e);
        }
    }

    /**
     * bean转换成map
     * @param objList
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> objectToMap(List objList) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if(CollectionUtils.isEmpty(objList)){
            return resultList;
        }
        for (Object obj: objList) {
            Map<String, Object> map = new HashMap<String, Object>();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
            resultList.add(map);
        }
        return resultList;
    }

}
