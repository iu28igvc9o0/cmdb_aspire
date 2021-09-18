package com.aspire.ums.cmdb.schedule;

import com.aspire.ums.cmdb.sync.entity.User;
import com.aspire.ums.cmdb.sync.util.UmsWebServiceUtils;
import com.google.common.collect.Maps;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.util.Map;

public class EpcUserSyncTaskServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(EpcUserSyncTaskServiceTest.class);

    private String url = "http://10.12.8.194:8088/service/ThirdBizSysService?wsdl";

    /**
     * @param user
     * @return
     */
    public String handleBpmResult(User user, String type) {
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
            String result = (String) call.invoke(new Object[] { user.getLoginName(), user.getMobile(), user.getName(), user.getPassword(), user.getEmail(), type });
            logger.info("返回结果：" + result);
            return result;

        } catch (Exception e) {
            logger.error("call service return failure,", e);
            return "{\"result\":\"false\",\"message\":\""+e.getMessage()+"\"}";

        }
    }

    public Map<String, Object> syscUserData(com.aspire.ums.cmdb.sync.entity.User user, String operateType) {
        Map<String, Object> result = Maps.newHashMap();
        String returnValue = handleBpmResult(user, operateType);
        if (returnValue.matches("\\{\"result\"\\s*:\\s*\"true\"\\}")) {
            result.put("result", Boolean.TRUE);
        } else {
            result.put("result", Boolean.FALSE);
            String msg = returnValue.substring(returnValue.indexOf("message"), returnValue.length());
            msg = msg.replace("\"", "").replace("}", "").replace(":", "").replace("message", "");
            result.put("message", msg);
            logger.info("BPM接口调用异常,调用方法:sysUserOperate,返回信息:" + returnValue);
        }
        return result;
    }

    public static void main(String[] args) {
        User user = new User();
        user.setLoginName("yunhao");
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        user.setMobile("18347917282");
        user.setEmail("yunhao@aspirecn.com");
//        user.setSuspendFlag("1");
        user.setName("云昊");

        EpcUserSyncTaskServiceTest test = new EpcUserSyncTaskServiceTest();
        test.syscUserData(user, UmsWebServiceUtils.UMS_USER_OPERATE_TYPE_MODI);
    }
}
