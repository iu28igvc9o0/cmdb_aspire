package com.aspire.mirror.alert.server.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.common.entity
 * 类名称:    FieldUtil.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 14:33
 * 版本:      v1.0
 */
public class FieldUtil {
    /**
     * 获取属性名和值map
     * */
    public static Map<String, Object> getFiledMap(Object o){
        Field[] fields = o.getClass().getDeclaredFields();
        Map<String, Object> fieldMap= new HashMap<>();
        for(int i=0;i<fields.length;i++){
            String fieldName=fields[i].getName();
            fieldMap.put(fields[i].getName(), getFieldValueByName(fieldName, o));
        }
        return fieldMap;
    }
    /**
     * 根据属性名获取属性值
     * */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

//    public static void main(String[] args) {
//        AlertsPageRequset alertsPageRequset = new AlertsPageRequset();
//        alertsPageRequset.setBizSys("SB");
//        alertsPageRequset.setOrderStatus("1");
//        Map<String, Object> map = FieldUtil.getFiledMap(alertsPageRequset);
//        PageRequest page = new PageRequest();
//        for(String key : map.keySet()) {
//            page.addFields(key, map.get(key));
//        }
//        System.out.println(page.getDynamicQueryFields());
//    }
}
