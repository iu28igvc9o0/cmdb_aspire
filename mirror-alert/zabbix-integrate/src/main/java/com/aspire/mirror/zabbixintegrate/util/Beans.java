package com.aspire.mirror.zabbixintegrate.util;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;


public class Beans implements ApplicationListener<ApplicationPreparedEvent> {
    private static ApplicationContext context;

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        setContext(event.getApplicationContext());
    }
    
    public static <T> T getBean(Class<T> t) {
        return context.getBean(t);
    }

    public static Object getBean(String name) {
        return context.getBean(name);
    }
    
    static Environment getAppEnvironment() {
        return context.getEnvironment();
    }
    
    public static void setContext(ApplicationContext context) {
    	Beans.context = context;
    }
}
