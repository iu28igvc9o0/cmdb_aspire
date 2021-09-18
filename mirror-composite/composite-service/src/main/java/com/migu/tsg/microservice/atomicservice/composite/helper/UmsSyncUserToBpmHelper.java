package com.migu.tsg.microservice.atomicservice.composite.helper;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.Map;

/**
 * @projectName: UmsSyncUserToBpmHelper
 * @description: Ums同步用户到bpm
 * @author: luowenbo
 * @create: 2020-07-02 15:59
 **/
@Component
public class UmsSyncUserToBpmHelper {

    @Value("${ums.webService.url:}")
    private String url;

    protected static Logger logger = LoggerFactory.getLogger(UmsSyncUserToBpmHelper.class);

    public static final String INSTR = "123456";

    public static final String UMS_BUSINESS_OPERATE_BIZ_LEVEL_1 = "1";
    public static final String UMS_BUSINESS_OPERATE_BIZ_LEVEL_2 = "2";
    public static final String UMS_BUSINESS_OPERATE_LEFT_NODE_TRUE = "1";
    public static final String UMS_BUSINESS_OPERATE_LEFT_NODE_FLASE = "2";

    public static final String UMS_USER_OPERATE_TYPE_ADD = "1";// 添加账号, 并发送邮件通知
    public static final String UMS_USER_OPERATE_TYPE_MODI = "2";
    public static final String UMS_USER_OPERATE_TYPE_DEL = "3";
//    public static final String UMS_USER_OPERATE_TYPE_MODI_PWD = "4";
    public static final String UMS_USER_OPERATE_TYPE_ADD_FOR_PROCEDURE = "5";// 添加账号, 并发起工单
    public static final String UMS_USER_OPERATE_TYPE_ADD_FOR_SYNC = "6";// 仅添加账号
    public static final String UMS_USER_OPERATE_TYPE__SUSPEND = "7"; // 挂起账号

    /*
     * public static Boolean modifyPassword(String userName,String oldPassword,
     * String newPassword,String sysName,String type) { Boolean result =
     * Boolean.FALSE; try { oldPassword =
     * oldPassword+"||"+System.currentTimeMillis(); oldPassword=new String(
     * Base64.encode(oldPassword.getBytes())); newPassword =
     * newPassword+"||"+System.currentTimeMillis(); newPassword=new String(
     * Base64.encode(newPassword.getBytes())); String returnValue =
     * getThirdBizSysService().modPassword(userName, oldPassword, newPassword,
     * sysName,type); if(returnValue.matches("\\{\"result\"\\s*:\\s*\"true\"\\}")){
     * result = Boolean.TRUE; } } catch (Exception e) { e.printStackTrace(); }
     * return result; }
     *
     * public static Boolean authLoginUser(String userName,String password,String
     * sysName) { Boolean result = Boolean.FALSE; try { password =
     * password+"||"+System.currentTimeMillis(); password=new String(
     * Base64.encode(password.getBytes())); String returnValue =
     * getThirdBizSysService().authLoginUser(userName, password, sysName);
     * if(returnValue.matches("\\{\"result\"\\s*:\\s*\"true\"\\}")){ result =
     * Boolean.TRUE; } } catch (Exception e) { e.printStackTrace(); } return result;
     * }
     */
    public Map<String, Object> syscUserData(Map<String,String> user, String operateType) {
        Map<String, Object> result = Maps.newHashMap();
        String returnValue = handleBpmResult(user, operateType);
        if (returnValue.matches("\\{\"result\"\\s*:\\s*\"true\"\\}")) {
            result.put("result", Boolean.TRUE);
        } else {
            result.put("result", Boolean.FALSE);
            String msg = returnValue.substring(returnValue.indexOf("message"));
            msg = msg.replace("\"", "").replace("}", "").replace(":", "").replace("message", "");
            result.put("message", msg);
            logger.info("BPM接口调用异常,调用方法:sysUserOperate,返回信息:" + returnValue);
        }
        return result;
    }

    /**
     * @param user
     * @return
     */
    public String handleBpmResult(Map<String,String> user, String type) {
        if (user == null) {
            return "{\"result\":\"false\",\"message\":\"user is null\"}";
        }
        try {
            logger.info("请求bpm的webService地址:" + url
                    + ",method:sysUserOperate,space:http://impl.webservice.platform.hotent.com/");
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(url));
            call.setUseSOAPAction(true);
            call.setOperationName(new QName("http://impl.webservice.platform.hotent.com/", "sysUserOperate"));// WSDL里面描述的接口名称
            call.addParameter(new QName("account"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.addParameter(new QName("mobile"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.addParameter(new QName("fullname"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.addParameter(new QName("password"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.addParameter(new QName("email"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.addParameter(new QName("operatetype"), org.apache.axis.encoding.XMLType.XSD_STRING,
                    javax.xml.rpc.ParameterMode.IN);// 接口的参数
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);// 设置返回类型
            // 调用webservice请求
            logger.info("#-->>> 请求bpm的webService账号信息: {}, 操作类型: {}", JSON.toJSONString(user), type);
            String result = (String) call.invoke(new Object[] { user.get("loginName"), user.get("mobile"),user.get("name"), user.get("password"), user.get("email"), type });
            logger.info("返回结果：" + result);
            return result;
        } catch (Exception e) {
            logger.error("call service return failure,", e);
            return "{\"result\":\"false\",\"message\":\""+e.getMessage()+"\"}";
        }
    }
}
