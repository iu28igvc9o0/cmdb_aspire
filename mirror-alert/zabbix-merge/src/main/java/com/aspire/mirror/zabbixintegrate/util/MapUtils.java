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
