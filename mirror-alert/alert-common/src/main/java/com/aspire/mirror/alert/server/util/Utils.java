package com.aspire.mirror.alert.server.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
* JsonPath工具类
* Project Name:jarchemist-service
* File Name:JsonUtil.java
* Package Name:com.migu.tsg.microservice.atomicservice.jalchemist.biz.util
* ClassName: JsonUtil <br/>
* date: 2018年3月7日 上午10:39:32 <br/>
* 
* @author pengguihua
* @version 
* @since JDK 1.6
*/
public final class Utils {
	public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
	
	public static double formatDouble(double d,int decimal) {
		   
        BigDecimal bg = BigDecimal.valueOf(d).setScale(decimal, BigDecimal.ROUND_HALF_UP);
        
        return bg.doubleValue();
    }
	
	public static float getOperValue(long val,long sum){
    	if(sum==0 || val==0) {
    		return 0;
    	}
    	float v = new BigDecimal((float)val*100/sum).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue();
    	return v;
    }
}
