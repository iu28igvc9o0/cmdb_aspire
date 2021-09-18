package com.aspire.ums.cmdb.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
    * 基于指定的json字符串或对象, 构建默认JsonPath解析的上下文.<br/>
    *
    * 作者： pengguihua
    * @param json
    * @return
    */
    public static DocumentContext buildDefaultJsonPathContext(Object json) {
        Configuration conf = Configuration.defaultConfiguration()
                            .mappingProvider(new JacksonMappingProvider(OBJ_MAPPER))
                            .jsonProvider(new JacksonJsonProvider(OBJ_MAPPER))
                            .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
                            .addOptions(Option.SUPPRESS_EXCEPTIONS);
        
        // 当对JsonPath进行filter过滤计算时，字符串json构造的JsonPath可以正常解析过滤条件，
        // 但Object形式的json构造的jsonPath却无法解析过滤条件，所以针对json对象判断类型, 如果为String类型，则转换成String
        if (String.class.isInstance(json)) {
            return JsonPath.using(conf).parse(String.class.cast(json));
        }
        return JsonPath.using(conf).parse(json);
    }
    
    public static String toPrettyJson(DocumentContext ctx) {
    	Object jsonObj = ctx.json();
    	return toJacksonPrettyJson(jsonObj);
    }
    
    /**
     * 基于Jackson的把对象转成json字符串. <br/>
     *
     * 作者： pengguihua
     * @param target
     * @param source
     * @return
     */
     public static <T> String toJacksonJson(T obj2Parse) {
         try {
             if (CharSequence.class.isInstance(obj2Parse)) {
                 return String.valueOf(obj2Parse);
             }
             if (DocumentContext.class.isInstance(obj2Parse)) {
            	 Object jsonObj = DocumentContext.class.cast(obj2Parse).json();
            	 return OBJ_MAPPER.writeValueAsString(jsonObj);
             }
             return OBJ_MAPPER.writeValueAsString(obj2Parse);
         } catch (Throwable e) {
             throw new RuntimeException(e);
         }
     }
     
     public static <T> String toJacksonPrettyJson(T obj2Parse) {
    	 try {
             if (CharSequence.class.isInstance(obj2Parse)) {
            	 return String.valueOf(obj2Parse);
             }
             if (DocumentContext.class.isInstance(obj2Parse)) {
            	 return toPrettyJson(DocumentContext.class.cast(obj2Parse));
             }
             return OBJ_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj2Parse);
         } catch (Throwable e) {
             throw new RuntimeException(e);
         }
     }
    
    /**
    * 读取指定的值，如果读取属性为null, 则返回指定的默认值.<br/>
    *
    * 作者： pengguihua
    * @param ctx
    * @param path
    * @param defaultVal
    * @return
    */
    public static <T> T readWithDefault(DocumentContext ctx, String path, T defaultVal) {
        T val = ctx.read(path);
        return val == null ? defaultVal : val;
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
    
    /**
     * 格式化json字符串, 把单引号替换成双引号. <br/>
     *
     * 作者： pengguihua
     * @param json2Format
     * @return
     */
     public static String formatJsonSingleQuotes(String json2Format) {
         if (json2Format == null) {
             return null;
         }
         return json2Format.replaceAll("'", "\"");
     }

    /**
     * 将json对象转换为HashMap
     *
     * @param json
     * @return
     */
    public static Map<String, Object> parseJSON2Map(JSONObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 最外层解析
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            // 如果内层还是json数组的话，继续解析
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Iterator<JSONObject> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2));
                }
                map.put(k.toString(), list);
            } else if (v instanceof JSONObject) {
                // 如果内层是json对象的话，继续解析
                map.put(k.toString(), parseJSON2Map((JSONObject) v));
            } else {
                // 如果内层是普通对象的话，直接放入map中
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    /**
     * 将json字符串转换为Map
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> parseJSONstr2Map(String jsonStr) {
        JSONObject json = JSONObject.fromObject(jsonStr);
        Map<String, Object> map = parseJSON2Map(json);
        return map;
    }

    public static Map<String, Object> toSimpleMap(String jsonString) {
        Map<String, Object> hmRet = new HashMap<String, Object>();
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        for (Object key : jsonObject.keySet()) {
            Object val = jsonObject.get(key);
            if (JSONObject.class.isInstance(val)) {
                JSONObject jVal = (JSONObject) val;
                hmRet.put((String) key, jVal == null ? null : toSimpleMap(jVal.toString()));
            } else if (JSONArray.class.isInstance(val)) {
                JSONArray jVal = (JSONArray) val;
                hmRet.put((String) key, jVal == null ? null : JSONArray.toArray(jVal));
            } else {
                hmRet.put((String) key, val);
            }
        }
        return hmRet;
    }
}
