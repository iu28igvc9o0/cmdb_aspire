package com.aspire.ums.cmdb.v2.index.web;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @ClassName FeignBeanFactoryPostProcessor
 * @Description TODO
 * @Author luowenbo
 * @Date 2020/3/23 10:59
 * @Version 1.0
 */
@Component
public class FeignBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        if (containsBeanDefinition(configurableListableBeanFactory,"feignContext","eurekaAutoServiceRegistration")){
            BeanDefinition bd = configurableListableBeanFactory.getBeanDefinition("feignContext");
            bd.setDependsOn("eurekaAutoServiceRegistration");
        }
    }

    private boolean containsBeanDefinition(ConfigurableListableBeanFactory beanFactory, String... beans) {
        return Arrays.stream(beans).allMatch(b -> beanFactory.containsBeanDefinition(b));
    }

}
