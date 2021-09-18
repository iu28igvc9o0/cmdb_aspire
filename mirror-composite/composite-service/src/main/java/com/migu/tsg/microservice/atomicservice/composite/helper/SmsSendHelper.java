package com.migu.tsg.microservice.atomicservice.composite.helper;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.migu.tsg.microservice.atomicservice.composite.controller.util.Md5Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SmsSendHelper {

    @Value("${sms.url:}")
    private String smsUrl;

    @Value("${sms.target:}")
    private String smsTarget;

    @Value("${sms.password:}")
    private String smsPassword;

    /*本次方法调用欲完成的功能*/
    private final String action = "00";
    private final String SMS_METHOD = "smsService";
    private final String space = "http://www.sms.upbms.hp.com";

    public boolean send(List<String> mobiles, String message) {
        if (org.apache.commons.lang.StringUtils.isEmpty(smsUrl)
                || org.apache.commons.lang.StringUtils.isEmpty(smsTarget)
                || org.apache.commons.lang.StringUtils.isEmpty(smsPassword)) {
            log.error("SMS server info is not correct ! url: {}, target: {}, password: {}",
                    smsUrl, smsTarget, smsPassword);
            return false;
        }
        log.info("SMS server info -> url: {}, target: {}, password: {}", smsUrl, smsTarget, smsPassword);
        if (CollectionUtils.isEmpty(mobiles)) {
            log.error("SMS mobiles is empty !");
            return false;
        }

        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(smsUrl));
            call.setUseSOAPAction(true);
            call.setOperationName(new QName(space, SMS_METHOD));// WSDL里面描述的接口名称
            String xmlData = generateXmlData(mobiles, message);
            String brief = Md5Utils.generateCheckCode(smsPassword + action + xmlData + smsTarget);
            log.info("SMS xmlData: {}, brief: {}", xmlData, brief);
            call.addParameter(new QName("target"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.addParameter(new QName("action"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.addParameter(new QName("xmldata"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.addParameter(new QName("brief"), org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型
            //调用webservice请求
            String result = (String) call.invoke(new Object[] { smsTarget, action, xmlData, brief});
            log.info("调用短信接口返回参数  : {}", result);
            Document doc = null;
            try {
                doc = DocumentHelper.parseText(result); // 将字符串转为XML
            } catch (DocumentException e) {
                log.error("parse sms result error : ", e);
            }
            Element rootElt = doc.getRootElement(); // 获取根节点
            Element codeEl = rootElt.element("Response").element("RspCode");
            String rspCode = codeEl.getText();
            if ("0000".equals(rspCode)){
                log.debug("发送成功");
                return true;
            } else{
                log.error("send sms failed, response code is : {}", rspCode);
                return false;
            }
        } catch (Exception e) {
            log.error("call sms interface error : ", e);
        }
        return false;
    }

    /**
     * 拼装xmlData
     * @param mobiles
     * @param contents
     * @return
     */
    private String generateXmlData(List<String> mobiles, String contents) {
        StringBuffer mobile = new StringBuffer();
        for (String mobil : mobiles) {
            mobile.append(mobil);
            mobile.append(",");
        }
        mobile.deleteCharAt(mobile.length() - 1);
        StringBuffer xmlDataBuf = new StringBuffer();
        xmlDataBuf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><SmsServiceReq><SmsList><Mobile>")
                .append(mobile).append("</Mobile><Contents><![CDATA[").append(contents)
                .append("]]></Contents></SmsList></SmsServiceReq>");
        return xmlDataBuf.toString();
    }
}
