package com.aspire.mirror.util;

import com.aspire.common.EnvConfigProperties;
import com.aspire.mirror.api.IPlatformFactory;
import com.cmcc.family.webservice.server.national.StringString;
import com.cmcc.family.webservice.server.national.StringStringMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class WSDLUtil {

    private EnvConfigProperties getEnvConfig() {
        return SpringUtil.getBean(EnvConfigProperties.class);
    }
    /**
     * 调用WebService接口
     * @param wsdl wsdl接口地址
     * @param methodName 调用webservice的方法
     * @param parameters 方法传入的参数
     * @return 返回map返回值
     */
    public Map<String, String> getWsdlServiceReturnMap(final ClassLoader classLoader, final String wsdl, final String methodName, final Object ... parameters) {
        String s1 = null, s2 = null, s3 = null, s4 = null, s5 = null, s6 = null, s7 = null, s8 = null;
        List<String> l1 = null, l4 = null;
        if (parameters.length > 0 && parameters[0] != null) {
            if (parameters[0] instanceof List) {
                l1 = (List<String>) parameters[0];
            } else if (parameters[0] instanceof String) {
                s1 = parameters[0].toString();
            }
        }
        if (parameters.length > 1 && parameters[1] != null) {
            s2 = parameters[1].toString();
        }
        if (parameters.length > 2 && parameters[2] != null) {
            s3 = parameters[2].toString();
        }
        if (parameters.length > 3 && parameters[3] != null) {
            if (parameters[3] instanceof List) {
                l4 = (List<String>) parameters[3];
            } else if (parameters[3] instanceof String) {
                s4 = parameters[4].toString();
            }
        }
        if (parameters.length > 4 && parameters[4] != null) {
            s5 = parameters[4].toString();
        }
        if (parameters.length > 5 && parameters[5] != null) {
            s6 = parameters[5].toString();
        }
        if (parameters.length > 6 && parameters[6] != null) {
            s7 = parameters[6].toString();
        }
        if (parameters.length > 7 && parameters[7] != null) {
            s8 = parameters[7].toString();
        }
        try {
            if (methodName.equals("getTotalUserStb")) {
                return IPlatformFactory.getNationalUserInfo().getTotalUserStb(s1, s2, s3, s4, s5, s6, s7);
            } else if (methodName.equals("getViewingUser")) {
                return IPlatformFactory.getNationalUserInfo().getViewingUser(l1, s2, s3, s4, s5, s6);
            } else if (methodName.equals("getStbAllIndex")) {
                return IPlatformFactory.getNationalProgramInfo().getStbAllIndex(l1, s2, s3, s4, s5, s6);
            } else if (methodName.equals("getProgramKartunTimeRate")) {
                return IPlatformFactory.getNationalProgramInfo().getProgramKartunTimeRate(l1, s2, s3, s4, s5, s6);
            } else if (methodName.equals("getAlarmInfoSplitNew")) {
                return IPlatformFactory.getNationalUserInfo().getAlarmInfoSplitNew(s1, s2, s3, s4, s5, s6, s7, s8);
            } else if (methodName.equals("getTotalUserOsgi")) {
                return IPlatformFactory.getNationalOsgiInfo().getTotalUserOsgi(s1, s2, s3, s4, s5, s6);
            } else if (methodName.equals("getOsgiActivation")) {
                return IPlatformFactory.getNationalOsgiInfo().getOsgiActivation(l1, s2, s3, l4,s5);
            } else {
                log.error("Can't find method -> {} factory.", methodName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
//
//
//
//
//        Thread.currentThread().setContextClassLoader(classLoader);
//        log.info("Begin call wsdl -> {}  method -> {}  parameters -> {}", wsdl, methodName, parameters);
//        JaxWsDynamicClientFactory jaxWsDynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
//        Client client = jaxWsDynamicClientFactory.createClient(wsdl, (ClassLoader) null, null);
//        Object value = null;
//        try {
//            client.setThreadLocalRequestContext(false);
//            QName qName = new QName(getEnvConfig().getFamilyOpen().getQName(), methodName);
//            Object[] objects = client.invoke(qName, parameters);
//            value = objects[0];
//        } catch (Exception e) {
//            log.error("Call wsdl [{}] method name [{}] parameters [{}] error.", wsdl, methodName, parameters, e);
//        } finally {
//            client.destroy();
//        }
        //return convertToMap(value);
    }

    /**
     * 将StringStringMap返回值转化成Map返回
     * @param value
     * @return
     */
    private Map<String, String> convertToMap(Object value) {
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
    public List<Long> getWsdlServiceReturnList(final String methodName, final Object ... parameters) {
        String s1 = null, s2 = null;
        List<String> l2 = null;
        if (parameters.length > 0 && parameters[0] != null) {
            s1 = parameters[0].toString();
        }
        if (parameters.length > 1 && parameters[1] != null) {
            if (parameters[1] instanceof List) {
                l2 = (List<String>) parameters[1];
            } else if (parameters[1] instanceof String) {
                s2 = parameters[1].toString();
            }
        }
        try {
            if (methodName.equals("getStbConfigCntByCodeList")) {
                return IPlatformFactory.getNationalStbConfigServer().getStbConfigCntByCodeList(s1, l2);
            } else {
                log.error("Can't find method -> {} factory.", methodName);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
//        Thread.currentThread().setContextClassLoader(classLoader);
//        log.info("Begin call wsdl -> {}  method -> {}  parameters -> {}", wsdl, methodName, parameters);
//        JaxWsDynamicClientFactory jaxWsDynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
//        Client client = jaxWsDynamicClientFactory.createClient(wsdl, (ClassLoader) null, null);
//        Object value = null;
//        try {
//            client.setThreadLocalRequestContext(true);
//            QName qName = new QName(getEnvConfig().getFamilyOpen().getQName(), methodName);
//            Object[] objects = client.invoke(qName, parameters);
//            log.info("invoke values : {}", JSONArray.fromObject(objects).toString());
//            value = objects[0];
//        } catch (Exception e) {
//            log.error("Call wsdl [{}] method name [{}] parameters [{}] error.", wsdl, methodName, parameters, e);
//        } finally {
//            client.destroy();
//        }
//        return convertToList(value);
    }

    /**
     * 将StringStringMap返回值转化成Map返回
     * @param value
     * @return
     */
    private ArrayList<Object> convertToList(Object value) {
        ArrayList<Object> resultMap = new ArrayList<Object>();
        if (value != null) {
            return (ArrayList<Object>) value;
        }
        return resultMap;
    }
}
