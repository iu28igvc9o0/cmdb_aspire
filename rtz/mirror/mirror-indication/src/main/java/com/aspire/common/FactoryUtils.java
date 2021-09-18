package com.aspire.common;

import com.aspire.mirror.indication.day.AbstractDayIndicationFactory;
import com.aspire.mirror.indication.hour.AbstractHourIndicationFactory;
import com.aspire.real_mirror.indication.AbstractRealMirrorIndicationFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/24
 * 软探针异常指标监控系统
 * com.aspire.common.FactoryUtils
 */
@Slf4j
@Component
public class FactoryUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    @SuppressWarnings("unchecked")
    public static AbstractDayIndicationFactory invokeDayFactory(String clazzString) throws RuntimeException {
        try {
            Class clz = Class.forName(clazzString);
            Constructor constructor = clz.getConstructor();
            return (AbstractDayIndicationFactory) constructor.newInstance();
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException error : {}", clazzString, e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException error : {}", clazzString, e);
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            log.error("InstantiationException error : {}", clazzString, e);
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            log.error("NoSuchMethodException error : {}", clazzString, e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            log.error("InvocationTargetException error : {}", clazzString, e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static AbstractRealMirrorIndicationFactory invokeRealFactory(String clazzString) throws RuntimeException {
        try {
            Class clz = Class.forName(clazzString);
            Constructor constructor = clz.getConstructor();
            return (AbstractRealMirrorIndicationFactory) constructor.newInstance();
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException error : {}", clazzString, e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException error : {}", clazzString, e);
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            log.error("InstantiationException error : {}", clazzString, e);
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            log.error("NoSuchMethodException error : {}", clazzString, e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            log.error("InvocationTargetException error : {}", clazzString, e);
            throw new RuntimeException(e);
        }
    }

//    @SuppressWarnings("unchecked")
//    public static AbstractMinuteIndicationFactory invokeMinuteFactory(String clazzString)
//            throws RuntimeException {
//        try {
//            Class clz = Class.forName(clazzString);
//            Constructor constructor = clz.getConstructor();
//            return (AbstractMinuteIndicationFactory) constructor.newInstance();
//        } catch (ClassNotFoundException e) {
//            log.error("ClassNotFoundException error : {}", clazzString, e);
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            log.error("IllegalAccessException error : {}", clazzString, e);
//            throw new RuntimeException(e);
//        } catch (InstantiationException e) {
//            log.error("InstantiationException error : {}", clazzString, e);
//            throw new RuntimeException(e);
//        } catch (NoSuchMethodException e) {
//            log.error("NoSuchMethodException error : {}", clazzString, e);
//            throw new RuntimeException(e);
//        } catch (InvocationTargetException e) {
//            log.error("InvocationTargetException error : {}", clazzString, e);
//            throw new RuntimeException(e);
//        }
//    }
    
    @SuppressWarnings("unchecked")
    public static AbstractHourIndicationFactory invokeHourFactory(String clazzString)
            throws RuntimeException {
        try {
            Class clz = Class.forName(clazzString);
            Constructor constructor = clz.getConstructor();
            return (AbstractHourIndicationFactory) constructor.newInstance();
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException error : {}", clazzString, e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException error : {}", clazzString, e);
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            log.error("InstantiationException error : {}", clazzString, e);
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            log.error("NoSuchMethodException error : {}", clazzString, e);
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            log.error("InvocationTargetException error : {}", clazzString, e);
            throw new RuntimeException(e);
        }
    }

//    @SuppressWarnings("unchecked")
//    public static AbstractActualIndicationFactory invokeActualFactory(String clazzString)
//            throws RuntimeException {
//        try {
//            Class clz = Class.forName(clazzString);
//            Constructor constructor = clz.getConstructor();
//            return (AbstractActualIndicationFactory) constructor.newInstance();
//        } catch (ClassNotFoundException e) {
//            log.error("ClassNotFoundException error : {}", clazzString, e);
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            log.error("IllegalAccessException error : {}", clazzString, e);
//            throw new RuntimeException(e);
//        } catch (InstantiationException e) {
//            log.error("InstantiationException error : {}", clazzString, e);
//            throw new RuntimeException(e);
//        } catch (NoSuchMethodException e) {
//            log.error("NoSuchMethodException error : {}", clazzString, e);
//            throw new RuntimeException(e);
//        } catch (InvocationTargetException e) {
//            log.error("InvocationTargetException error : {}", clazzString, e);
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if ( FactoryUtils.applicationContext == null ) {
            FactoryUtils.applicationContext = applicationContext;
        }
    }

    /**
     * 获取applicationContext
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);

    }

    /**
     * 通过class获取Bean.
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
