package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class Beans implements ApplicationContextAware {
    private static ApplicationContext context;

    private Beans() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Beans.setContext(applicationContext);
    }

    public static <T> T getBean(Class<T> t) {
        return context.getBean(t);
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static void setContext(ApplicationContext context) {
        Beans.context = context;
    }
}
