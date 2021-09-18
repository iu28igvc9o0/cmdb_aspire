package com.aspire.mirror.alert.server.biz.helper;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.aspire.mirror.alert.server.vo.bpm.SmsBpmCallParam;
import com.aspire.mirror.alert.server.util.Md5Utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SmSendHelper {

    @Value("${sms.url}")
    private String smsUrl;

    @Value("${sms.target}")
    private String smsTarget;

    @Value("${sms.password}")
    private String smsPassword;
    
	@Value("${sms.env:}")
	protected String smsEnv;
	
	@Value("${sms.bpm.smsSendUrl:}")
	protected String smsBpmCall;

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
            // 如果发送失败，立马重发一次
            if (!sendOnly(mobiles, message)) {
                return sendOnly(mobiles, message);
            }
        } catch (Exception e) {
            log.error("call sms interface error : ", e);
            try {
                return sendOnly(mobiles, message);
            } catch (Exception e1) {
                log.error("call sms interface error : ", e);
            }
        }
        return false;
    }

    private boolean sendOnly(List<String> mobiles, String message) throws Exception {
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
        Document doc = DocumentHelper.parseText(result); // 将字符串转为XML
//            try {
//                doc = DocumentHelper.parseText(result); // 将字符串转为XML
//            } catch (DocumentException e) {
//                log.error("parse sms result error : ", e);
//            }
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

	public boolean sendSms(List<String> mobileList, String content, Map<String, String> mobileIdMap) {
		if("UMS".equalsIgnoreCase(smsEnv)){
			SmsBpmCallParam param = new SmsBpmCallParam(mobileList,content,mobileIdMap);
			new Thread(() -> postCallBpmUrlWithNoAuth(smsBpmCall,param)).start();
			// 默认返回true, 在BPM端尝试发送，如果出错会尝试5次，5次之后仍然失败，更新记录状态为失败
			return true;
		}
		return send(mobileList, content);
	}
    
	/**
	 * 调用BPMURL，需要BPM将URL放开权限校验。
	 * @param bpmUrl
	 * @param type
	 * @return
	 */
	public Object postCallBpmUrlWithNoAuth(String bpmUrl, Object type) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Content-Type", "application/json;charset=utf-8");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Object> formErntity = new HttpEntity<>(type, requestHeaders);
		Object result = restTemplate.exchange(bpmUrl, HttpMethod.POST, formErntity, Object.class, type);
		return result;
	}
}
