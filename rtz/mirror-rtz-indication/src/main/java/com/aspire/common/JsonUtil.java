package com.aspire.common;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Json类转化工具
 * <p>
 * 项目名称: 咪咕微服务运营平台 包: com.migu.tsg.msp.atomicservice.region.util 类名称:
 * com.migu.tsg.msp.atomicservice.region.util.JsonUtil.java 类描述: 创建人: zhu.juwang
 * 创建时间: 2017/08/23 21:41 版本: v1.0
 */
public class JsonUtil {

    /**
     * 支持深沉层次获取Json中的key值
     *
     * @param srcObject 源JSONObect
     * @param keys json的key值
     * @return
     */
    public static Object getObject(JSONObject srcObject, String... keys) {
        JSONObject tempObject = srcObject;
        for (int i = 0; i < keys.length; i++) {
            if (tempObject == null || tempObject.size() == 0) {
                return null;
            }
            if (i == keys.length - 1) {
                return tempObject.get(keys[i]);
            }
            Object tempValue = tempObject.get(keys[i]);
            if (tempValue instanceof String) {
                tempObject = JSONObject.fromObject(tempValue);
            }
            if (tempValue instanceof JSONObject) {
                tempObject = (JSONObject) tempValue;
            }
        }
        return null;
    }

    /**
     * 获取JsonObject
     *
     * @param srcObject  源JSONObect
     * @param key json的key值
     * @return
     */
    public static JSONObject getJSONObect(JSONObject srcObject, String key) {
        return getJSONObect(srcObject, key, null);
    }

    /**
     * 获取JsonObject
     *
     * @param srcObject 源JSONObect
     * @param key json的key值
     * @return
     */
    public static JSONObject getJSONObect(JSONObject srcObject, String key, JSONObject defaultJson) {
        if (srcObject == null) {
            return defaultJson;
        }
        if (!srcObject.containsKey(key) || srcObject.get(key) == null) {
            return defaultJson;
        }
        return srcObject.getJSONObject(key);
    }

    /**
     * 获取Json值, 并转化成String
     *
     * @param srcObject 源JSONObject
     * @param key json的key值
     * @return
     */
    public static String getString(JSONObject srcObject, String key) {
        if (srcObject == null) {
            return null;
        }
        if (!srcObject.containsKey(key) || srcObject.get(key) == null) {
            return null;
        }
        return srcObject.getString(key);
    }

    /**
     * 获取Json值, 并转化成String
     *
     * @param srcObject 源JSONObject
     * @param key json的key值
     * @return
     */
    public static String getString(JSONObject srcObject, String key, String defaultValue) {
        if (srcObject == null) {
            return defaultValue;
        }
        if (!srcObject.containsKey(key) || srcObject.get(key) == null) {
            return defaultValue;
        }
        return srcObject.getString(key);
    }

    /**
     * 获取JSONArray
     * @param srcObject 源JSONObect
     * @param key json的key值
     * @return
     */
    public static JSONArray getJSONArray(JSONObject srcObject, String key) {
        JSONArray jsonArray = new JSONArray();
        Object object = srcObject.get(key);
        if (object != null) {
            return JSONArray.fromObject(object);
        }
        return jsonArray;
    }

    public static Integer getInteger(JSONObject srcObject, String key) {
        if (srcObject == null) {
            return null;
        }
        if (!srcObject.containsKey(key) || srcObject.get(key) == null) {
            return null;
        }
        return srcObject.getInt(key);
    }

    /**
     * 替换或者新增key-value
     *
     * @param srcObject 源JSONObect
     * @param key  json的key值
     * @param value 替换的值
     */
    public static void replaceOrAdd(JSONObject srcObject, String key, Object value) {
        if (srcObject.containsKey(key)) {
            srcObject.put(key, value);
        } else {
            srcObject.accumulate(key, value);
        }
    }
}
