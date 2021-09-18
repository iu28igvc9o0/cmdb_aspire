package com.aspire.ums.cmdb.automate.utils;

import com.aspire.ums.cmdb.util.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-08-25 10:51
 * @description map和javabean的转换工具类
 */
public class ConvertUtils {

    public static <T> T map2Bean(Map<String,Object> source,Class<T> instance) {
        try {
            T object = instance.newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                FieldName fieldName = field.getAnnotation(FieldName.class);
                if (null != fieldName) {
                    field.set(object,source.get(fieldName.value()));
                } else {
                    String name = field.getName();
                    if (StringUtils.isNotEmpty(name) && !"serialVersionUID".equals(name)) {
                        field. set(object,source.get(name));
                    }
                }
            }
            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
