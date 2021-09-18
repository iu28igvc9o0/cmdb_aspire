package com.aspire.mirror.alert.server.util;

import com.aspire.mirror.alert.server.annotation.ServiceLogAnnotation;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: AnnotationUtil
 * @description: 类
 * @author: luowenbo
 * @create: 2020-06-12 17:15
 **/
public class AnnotationUtil {

    /**
     * 设置注解中的字段值
     *
     * @param annotation 要修改的注解实例
     * @param fieldName  要修改的注解字段名
     * @param value      要设置的值
     */
    public static void setAnnotationValue(Annotation annotation, String fieldName, Object value) throws Exception{
        InvocationHandler handler = Proxy.getInvocationHandler(annotation);
        Field hField = handler.getClass().getDeclaredField("memberValues");
        ReflectionUtils.makeAccessible(hField);
        Map memberValues = (Map) hField.get(handler);
        memberValues.put(fieldName, value);
    }


    /**
     * 设置注解中的字段值,多参数传值
     *
     * @param annotation 要修改的注解实例
     * @param params 键值对组
     */
    public static void setAnnotationValueWithMulti(Annotation annotation, Map<String,Object> params) throws Exception{
        InvocationHandler handler = Proxy.getInvocationHandler(annotation);
        Field hField = handler.getClass().getDeclaredField("memberValues");
        ReflectionUtils.makeAccessible(hField);
        Map memberValues = (Map) hField.get(handler);
        for(Map.Entry<String,Object> item : params.entrySet()) {
            memberValues.put(item.getKey(), item.getValue());
        }
    }

    /*
    * 传递注解参数
    * @param paramArray 传递参数的键值对
    * @param targetClass 目标Class
    * @param methodName 方法名称
    * @param parameterTypes 方法的参数类型
    * */
    public static void passAnnotationParam(Map<String,Object> paramArray,Class targetClass,String methodName,Class<?>... parameterTypes){
        try {
            // 获取方法上的注解
            ServiceLogAnnotation serviceLogAnnotation = targetClass.getMethod(methodName,parameterTypes).getAnnotation(ServiceLogAnnotation.class);
            setAnnotationValueWithMulti(serviceLogAnnotation,paramArray);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("注解传递参数过程中出现异常!");
        }
    }

    /*
    *  日志操作的特定方法 1 : 封装日志的操作人和操作对象
    * */
    public static Map<String,Object> packageParam(String operator,String... ids){
        Map<String,Object> paramArray = new HashMap<>();
        paramArray.put("operator",operator);
        if(ids.length > 1){
            String[] a = null;
            String tmpIds = String.join(",",ids);
            paramArray.put("id",tmpIds);
        } else {
            paramArray.put("id",ids[0]);
        }
        return paramArray;
    }

    /*
    *  日志操作的特定方法 2 : 多参数,处理操作人和操作对象,传其他参数
    * */
    public static Map<String,Object> packageOtherParam(Map<String,Object> otherParams,String operator,String... ids){
        Map<String,Object> paramArray = packageParam(operator,ids);
        if(null != otherParams || !otherParams.isEmpty()) {
            paramArray.putAll(otherParams);
        }
        return paramArray;
    }
}
