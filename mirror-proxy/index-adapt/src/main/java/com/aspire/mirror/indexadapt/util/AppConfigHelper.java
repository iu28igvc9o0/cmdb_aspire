package com.aspire.mirror.indexadapt.util;

/**
 * 配置读取帮助类
 * Project Name:jarchemist-service
 * File Name:AppConfigHelper.java
 * Package Name:com.migu.tsg.microservice.atomicservice.spectre.util
 * ClassName: AppConfigHelper <br/>
 * date: 2018年3月1日 下午5:07:58 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
public final class AppConfigHelper {

    public static String getProperty(String key) {
        return Beans.getAppEnvironment().getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return Beans.getAppEnvironment().getProperty(key, defaultValue);
    }

    public static <T> T getProperty(String key, Class<T> targetType) {
        return Beans.getAppEnvironment().getProperty(key, targetType);
    }

    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return Beans.getAppEnvironment().getProperty(key, targetType, defaultValue);
    }
}
