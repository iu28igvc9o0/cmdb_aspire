package com.aspire.mirror.util;

import com.cmcc.family.webservice.server.national.StringString;
import com.cmcc.family.webservice.server.national.StringStringMap;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

import javax.xml.namespace.QName;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Slf4j
@NoArgsConstructor
public class WsdlMapThread extends Thread {
    boolean isExit = false;
    private String wsdl;
    private String methodName;
    private Object[] parameters;
    private String qName;
    private Map<String, String> returnMap;
    @Override
    public void run() {
        log.info("Begin call wsdl -> {}  method -> {}  parameters -> {}", wsdl, methodName, parameters);
        JaxWsDynamicClientFactory jaxWsDynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = jaxWsDynamicClientFactory.createClient(wsdl, (ClassLoader) null, null);
        try {
            client.setThreadLocalRequestContext(true);
            QName qName = new QName(getQName(), methodName);
            Object[] objects = client.invoke(qName, parameters);
            returnMap = convertToMap(objects[0]);
        } catch (Exception e) {
            log.error("Call wsdl [{}] method name [{}] parameters [{}] error.", wsdl, methodName, parameters, e);
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
}
