package com.aspire.mirror.zabbixintegrate.util;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:  微服务运维平台
 * 包:       com.migu.tsg.microservice.atomicservice.composite.util
 * 类名称:    MapUtils.java
 * 类描述:    entity转map
 * 创建人:    zhuxiaolong
 * 创建时间:  2019/6/25 20:39
 */
public class MapUtils {
    /**
     * 实体对象转成Map
     *
     * @param obj 实体对象
     * @return
     */
    public static Map<String, Object> entityToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static List<Map<String, Object>> toMapList(List<?> objs) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>(objs.size());
        for (Object obj : objs) {
            mapList.add(entityToMap(obj));
        }
        return mapList;
    }

    public static String getString (Map<String, Object> map, String key) {
        if (StringUtils.isEmpty(key) || map == null) {
            return null;
        }
        Object value = map.get(key);

        if (value == null) {
            return null;
        }

        return value.toString();
    }
}
