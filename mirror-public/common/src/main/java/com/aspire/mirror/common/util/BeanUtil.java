package com.aspire.mirror.common.util;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 实体转化为map工具类
 * <p>
 * 项目名称:  咪咕微服务运营平台-CICD
 * 包:       com.migu.tsg.msp.microservice.atomicservice.cicd.commom.util
 * 类名称:    BeanUtil.java
 * 类描述:    实体转化为map工具类
 * 创建人:    WuFan
 * 创建时间:  2017/08/23 19:44
 * 版本:      v1.0
 */
public class BeanUtil {

    /**
     * 将bean转化为map
     *
     * @param clazz  bean
     * @param <T>   泛型
     * @return   Map<String, Object> 返回对象
     */
    public static <T> Map<String, Object> toMap(final T clazz) {

        Map<String, Object> paramsMap = Maps.newHashMap();
        try {
            BeanInfo bi = Introspector.getBeanInfo(clazz.getClass());
            PropertyDescriptor[] pd = bi.getPropertyDescriptors();
            for (int i = 0; i < pd.length; i++) {
                String name = pd[i].getName();
                Method method = pd[i].getReadMethod();
                Object value = method.invoke(clazz);
                if (!CLASS.equals(name)) {
                    paramsMap.put(name, value);
                }
            }
        } catch (IntrospectionException ie) {
            LOGGER.error("IntrospectionException.className=" + clazz.getClass() + "BeanInfo error : ", ie);
        } catch (Exception e) {
            LOGGER.error("Exception.className=" + clazz.getClass() + "Method error :", e);
        }
        return paramsMap;
    }

    /** slf4j */
    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);

    private static final String CLASS = "class";
}