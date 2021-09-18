package com.aspire.ums.cmdb.util;

import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 利用Dozer实现对象之间的转化.
 *
 * @author jiangxuwen
 * @date 2020/5/14 14:08
 */
public class DozerMapper {

    private DozerMapper() {}

    private static Logger logger = LoggerFactory.getLogger(DozerMapper.class);

    private static DozerBeanMapper dozer = new DozerBeanMapper();

    public static DozerBeanMapper get() {
        return dozer;
    }

    /**
     * 基于Dozer转换对象的类型.
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    /**
     * 基于Dozer转换Collection中对象的类型.
     */
    public static <T> List<T> mapList(Collection<?> sourceList, Class<T> destinationClass) {
        List<T> destinationList = Lists.newArrayList();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 基于Dozer将对象A的值拷贝到对象B中.
     */
    public static void copy(Object source, Object destinationObject) {
        dozer.map(source, destinationObject);
    }
}
