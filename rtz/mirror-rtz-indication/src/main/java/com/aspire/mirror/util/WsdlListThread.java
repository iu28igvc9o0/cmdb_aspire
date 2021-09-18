package com.aspire.mirror.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@NoArgsConstructor
public class WsdlListThread extends Thread {
    boolean isExit = false;
    private String wsdl;
    private String methodName;
    private Object[] parameters;
    private String qName;
    private List<Object> returnList;
    @Override
    public void run() {
        log.info("Begin call wsdl -> {}  method -> {}  parameters -> {}", getWsdl(), getMethodName(), getParameters());
        JaxWsDynamicClientFactory jaxWsDynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = jaxWsDynamicClientFactory.createClient(getWsdl(), (ClassLoader) null, null);
        try {
            client.setThreadLocalRequestContext(true);
            QName qName = new QName(getQName(), getMethodName());
            Object[] objects = client.invoke(qName, getParameters());
            log.info("invoke values : {}", JSONArray.fromObject(objects).toString());
            returnList = convertToList(objects[0]);
        } catch (Exception e) {
            log.error("Call wsdl [{}] method name [{}] parameters [{}] error.", getWsdl(), getMethodName(), getParameters(), e);
        } finally {
            client.destroy();
        }
        isExit = true;
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
