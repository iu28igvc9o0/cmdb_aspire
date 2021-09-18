package com.migu.tsg.microservice.atomicservice.composite.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 时间工具类
 * <p>
 * 项目名称: 咪咕微服务运营平台-CICD 包:
 * com.migu.tsg.msp.microservice.atomicservice.cicd.commom.util 类名称:
 * DateUtil.java 类描述: 时间工具类 创建人: WuFan 创建时间: 2017/08/28 18:52 版本: v1.0
 */
public class ToolUtils {

	public static double formatDouble(double d,int i) {
   
        BigDecimal bg = new BigDecimal(d).setScale(i, RoundingMode.UP);
        
        return bg.doubleValue();
    }
}