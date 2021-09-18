package com.aspire.common;

import com.cmcc.family.webservice.server.national.StringString;
import com.cmcc.family.webservice.server.national.StringStringMap;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: WsdlUtils
 * Author:   zhu.juwang
 * Date:     2019/9/17 16:32
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class WsdlUtils {

    private static ClassLoader cl = Thread.currentThread().getContextClassLoader();
    public static String PROGRAM_INFO_SERVER = "programinfoserver";
    public static String USER_INFO_SERVER = "userinfoserver";
    public static String OSGI_INFO_SERVER = "osgiinfoserver";
    public static String OSGI_ALARM_INFO_SERVER = "osgialarminfoserver";
    public static String STB_CONFIG_SERVER = "national/stbConfigServer";


    /**
     * 调用WebService接口
     * @param wsdl wsdl接口地址
     * @param methodName 调用webservice的方法
     * @param parameters 方法传入的参数
     * @return 返回map返回值
     */
    public static Map<String, String> getWsdlServiceReturnMap(String wsdl, final String methodName, final Object ... parameters) {
        Thread.currentThread().setContextClassLoader(cl);
        if (!wsdl.endsWith("?wsdl")) {
            wsdl = wsdl + "?wsdl";
        }
        log.info("Begin call wsdl -> {}  method -> {}  parameters -> {}", wsdl, methodName, parameters);
        Object value = null;
        JaxWsDynamicClientFactory jaxWsDynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = jaxWsDynamicClientFactory.createClient(wsdl);
        try {
            client.setThreadLocalRequestContext(false);
            Object[] objects = client.invoke(methodName, parameters);
            value = objects[0];
        } catch (Exception e) {
            log.error("Call wsdl [{}] method name [{}] parameters [{}] error.", wsdl, methodName, parameters, e);
        } finally {
            client.destroy();
        }
        return convertToMap(value);
    }

    /**
     * 将StringStringMap返回值转化成Map返回
     * @param value
     * @return
     */
    private static Map<String, String> convertToMap(Object value) {
        Map<String, String> resultMap = new LinkedHashMap();
        if (value != null) {
            StringStringMap v = (StringStringMap) value;
            Iterator var2 = v.getEntries().iterator();
            while(var2.hasNext()) {
                StringString e = (StringString)var2.next();
                resultMap.put(e.getKey(), e.getValue());
            }
        }
        return resultMap;
    }
    /**
     * 调用WebService接口
     * @param wsdl wsdl接口地址
     * @param methodName 调用webservice的方法
     * @param parameters 方法传入的参数
     * @return 返回map返回值
     */
    public static List<Object> getWsdlServiceReturnList(String wsdl, final String methodName, final Object ... parameters) {
        Thread.currentThread().setContextClassLoader(cl);
        if (!wsdl.endsWith("?wsdl")) {
            wsdl = wsdl + "?wsdl";
        }
        log.info("Begin call wsdl -> {}  method -> {}  parameters -> {}", wsdl, methodName, parameters);
        JaxWsDynamicClientFactory jaxWsDynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = jaxWsDynamicClientFactory.createClient(wsdl);
        Object value = null;
        try {
            client.setThreadLocalRequestContext(false);
            Object[] objects = client.invoke(methodName, parameters);
            log.info("invoke values : {}", JSONArray.fromObject(objects).toString());
            value = objects[0];
        } catch (Exception e) {
            log.error("Call wsdl [{}] method name [{}] parameters [{}] error.", wsdl, methodName, parameters, e);
        } finally {
            client.destroy();
        }
        return convertToList(value);
    }

    /**
     * 将StringStringMap返回值转化成Map返回
     * @param value
     * @return
     */
    private static ArrayList<Object> convertToList(Object value) {
        ArrayList<Object> resultMap = new ArrayList<Object>();
        if (value != null) {
            return (ArrayList<Object>) value;
        }
        return resultMap;
    }
}
