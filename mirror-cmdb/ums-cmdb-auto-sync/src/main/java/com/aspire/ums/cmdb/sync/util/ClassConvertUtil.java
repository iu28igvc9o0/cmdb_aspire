package com.aspire.ums.cmdb.sync.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;



/**
 * 项目名称: 咪咕微服务运营平台 包: com.migu.tsg.msp.atomicservice.region.common 类名称:
 * com.migu.tsg.msp.atomicservice.region.common.ClassConvertUtil.java 类描述: 创建人:
 * zhu.juwang 创建时间: 2017/08/22 16:12 版本: v1.0
 */
public class ClassConvertUtil {

    private static Logger logger = LoggerFactory.getLogger(ClassConvertUtil.class);
    private static SimpleDateFormat format = null;
    private final static String TYPE_STRING = "java.lang.String";
    private final static String TYPE_JSON = "net.sf.json.JSONObject";
    private final static String TYPE_DATE = "java.util.Date";
    private final static String TYPE_LIST = "java.util.List";

    private static synchronized SimpleDateFormat getFormat() {
        if (format == null) {
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        return format;
    }

    /**
     * 类转化器 主要用于日期转化
     *
     * @param sourceClass
     * @param targetClass
     * @return
     */
    public static Object convertClass(Object sourceClass, Class<?> targetClass) {
        Object tempTargetClass = null;
        try {
            if (sourceClass != null && targetClass != null) {
               // logger.info("start convert class[" + sourceClass.getClass().getName() + "] to class[" + targetClass
                 //       .getClass().getName() + "]");
                Class<?> className = Class.forName(targetClass.getName());
                tempTargetClass = className.newInstance();
                Field[] fields = sourceClass.getClass().getDeclaredFields();
                Field[] targetFields = tempTargetClass.getClass().getDeclaredFields();
                for (Field field : fields) {
                    try {
                        String fieldName = field.getName(), fieldType = field.getType().getName();
                        String targetFieldName = "", targetFieldType = "";
                        /**
                         * 由于接口文档中传入/返回的信息会有"_"的存在, 所以这里需要转义
                         */
                        int index = 0;
                        if (fieldName.indexOf("_") != -1) {
                            String[] fieldPrefix = fieldName.split("_");
                            StringBuffer temp = new StringBuffer("");
                            for (String prefix : fieldPrefix) {
                                if (index > 0) {
                                    prefix = prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
                                }
                                temp.append(prefix);
                                index ++;
                            }
                            targetFieldName = temp.toString();
                        } else {
                            targetFieldName = fieldName;
                            for (char prefix : fieldName.toCharArray()) {
                                if (Character.isUpperCase(prefix)) {
                                    targetFieldName = targetFieldName.replace(String.valueOf(prefix),
                                            "_" + String.valueOf(prefix).toLowerCase());
                                }
                            }
                        }
                        for (Field targetField : targetFields) {
                            if (targetField.getName().equals(targetFieldName)) {
                                targetFieldName = targetField.getName();
                                targetFieldType = targetField.getType().getName();
                                break;
                            }
                        }
                        if (targetFieldName.isEmpty() || targetFieldType.isEmpty()) {
                            continue;
                        }
                        /**
                         * 获取set方法
                         */
                        Method setMethod = tempTargetClass.getClass().getMethod(
                                "set" + targetFieldName.substring(0, 1).toUpperCase() + targetFieldName.substring(1),
                                Class.forName(targetFieldType));

                        /**
                         * 获取get方法
                         */
                        Method getMethod = sourceClass.getClass()
                                .getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                        Object targetValue = getMethodValue(sourceClass, getMethod, fieldType, targetFieldType);
                        setMethod.invoke(tempTargetClass, targetValue);
                    } catch (Exception e1) {
                        logger.error("convert class failed .", e1);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("convert class failed .", e);
        } catch (InstantiationException e) {
            logger.error("convert class failed .", e);
        } catch (ClassNotFoundException e) {
            logger.error("convert class failed .", e);
        }
       // logger.info("convert class end .");
        return tempTargetClass;
    }

    /**
     * 获取get方法值
     *
     * @param sourceClass     源类
     * @param getMethod       get方法
     * @param fieldType       字段类型
     * @param targetFieldType 目标字段类型
     * @return
     */
    private static Object getMethodValue(Object sourceClass, Method getMethod, String fieldType,
            String targetFieldType) {
        Object targetValue = null;
        try {
            targetValue = getMethod.invoke(sourceClass);
            // 判断一下 日期格式互转
            if (TYPE_DATE.equals(fieldType) && TYPE_STRING.equals(targetFieldType)) {
                Object tempValue = null;
                try {
                    tempValue = getMethod.invoke(sourceClass);
                    targetValue = getFormat().format(tempValue);
                } catch (IllegalAccessException e) {
                    logger.error(" method invoke failed .", e);
                }
            }
            if (TYPE_STRING.equals(fieldType) && TYPE_DATE.equals(targetFieldType)) {
                Object tempValue = null;
                tempValue = getMethod.invoke(sourceClass);
                if (tempValue != null && !tempValue.equals("")) {
                    targetValue = getFormat().parse(tempValue.toString());
                }
            }
            if (TYPE_STRING.equals(fieldType) && TYPE_JSON.equals(targetFieldType)) {
                Object tempValue = getMethod.invoke(sourceClass);
                if (tempValue != null && !tempValue.equals("")) {
                    targetValue = JSONObject.parseObject(tempValue.toString());
                }
            }
            if (TYPE_JSON.equals(fieldType) && TYPE_STRING.equals(targetFieldType)) {
                Object tempValue = getMethod.invoke(sourceClass);
                if (tempValue != null && !tempValue.equals("")) {
                    targetValue = tempValue.toString();
                }
            }
            if (fieldType.equals(TYPE_LIST) && targetFieldType.equals(TYPE_STRING)) {
                Object tempValue = getMethod.invoke(sourceClass);
                if (tempValue != null && !tempValue.equals("")) {
                    targetValue = tempValue.toString();
                }
            }
            if (fieldType.equals(TYPE_STRING) && TYPE_LIST.equals(targetFieldType)) {
                Object tempValue = getMethod.invoke(sourceClass);
                if (tempValue != null && !tempValue.equals("")) {
                    targetValue = tempValue;
                }
            }
        } catch (ParseException e) {
            logger.warn(
                    "class[" + sourceClass.getClass().getName() + "] method[" + getMethod.getName() + "] parse fail.");
        } catch (InvocationTargetException e) {
            logger.error(" class pasre failed .", e);
        } catch (IllegalAccessException e) {
            logger.error("class pasre failed .", e);
        }
        return targetValue;
    }
}
