package com.migu.tsg.microservice.atomicservice.composite.helper;

import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.UserPayload;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @projectName: rbacHelper
 * @description: 类
 * @author: luowenbo
 * @create: 2020-07-02 16:19
 **/
@Component
public class RbacHelper {

    protected static Logger logger = LoggerFactory.getLogger(RbacHelper.class);

    @Autowired
    private UmsSyncUserToBpmHelper umsSyncUserToBpmHelper;

    /**
     * 同步user到bpm,返回同步成功的数据
     *
     * @param syncToBpmMap
     * @return
     */
    public void syncPortalUserToBpm(Map<String, List<Map<String,String>>> syncToBpmMap, Boolean initFlag) {
        List<Map<String,String>> addList = syncToBpmMap.get("addList");
        List<Map<String,String>> modiList = syncToBpmMap.get("modiList");
        List<Map<String,String>> delList = syncToBpmMap.get("delList");
        try {
            if (!CollectionUtils.isEmpty(modiList)) {
                for (Map<String,String> user : modiList) {
                    Map<String, Object> syscUserResult = umsSyncUserToBpmHelper.syscUserData(user, UmsSyncUserToBpmHelper.UMS_USER_OPERATE_TYPE_MODI);
                    if ((Boolean) syscUserResult.get("result")) {
                        logger.info("update portal user to bpm success, user name:" + user.get("loginName"));
                    } else {
                        logger.error("update portal user to bpm failed, user name:" + user.get("loginName"));
                        if (null != syscUserResult.get("message")) {
                            String msg = syscUserResult.get("message").toString();

                            if (msg.contains("账号不存在")) {
                                logger.info("try re-add portal user to bpm , user name:" + user.get("loginName"));
                                addList.add(user);
                            }
                        }
                    }
                }
            }

            if (!CollectionUtils.isEmpty(addList)) {
                for (Map<String,String> user : addList) {
                    // 同步新增用户
                    Map<String, Object> syscUserResult = umsSyncUserToBpmHelper.syscUserData(user,
                            UmsSyncUserToBpmHelper.UMS_USER_OPERATE_TYPE_ADD);
                    if ((Boolean) syscUserResult.get("result")) {
                        logger.info("sync portal user to bpm success, user name:" + user.get("loginName"));
                    } else {
                        logger.error("sync portal user to bpm failed, user name:" + user.get("loginName"));
                    }
                }
            }

            if (!CollectionUtils.isEmpty(delList)) {
                for (Map<String,String> user : delList) {
                    Map<String, Object> deleteResult = umsSyncUserToBpmHelper.syscUserData(user,
                            UmsSyncUserToBpmHelper.UMS_USER_OPERATE_TYPE_DEL);
                    if ((Boolean) deleteResult.get("result")) {
                        logger.info("delete portal user to bpm success, user name:" + user.get("loginName"));
                    } else {
                        logger.error("delete portal user to bpm failed, user name:" + user.get("loginName"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<Map<String,String>>> packageParams(Map<String,String> user,String operateType){
        Map<String, List<Map<String,String>>> rsMp = new HashMap<>();
        List<Map<String,String>> mpList = new ArrayList<>();
        Map<String,String> obj = new HashMap<>();
        obj.put("loginName",user.get("loginName"));
        obj.put("name",user.get("name"));
        obj.put("mobile",user.get("mobile"));
        obj.put("email",user.get("email"));
        if("ADD".equals(operateType)) {
            // 如果为null,默认密码
            String pwd = null == user.get("password") ? UmsSyncUserToBpmHelper.INSTR : user.get("password");
            obj.put("password",generateCheckCode(pwd));
            mpList.add(obj);
            rsMp.put("addList",mpList);
        } else if ("UPDATE".equals(operateType)){
            // 修改密码
            if(null != user.get("password")) {
                obj.put("password",generateCheckCode(user.get("password")));
            }
            mpList.add(obj);
            rsMp.put("modiList",mpList);
        }
        return rsMp;
    }

    public String generateCheckCode(String source) {
        // logger.info("MD5 source: " + source);
        String result = null;
        // Used to convert 16-byte hexadecimal characters.
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source.getBytes(StandardCharsets.UTF_8));
            // MD5 calculation is a 128-bit long integer, that is with 16-byte byte.
            byte[] tmp = md.digest();
            // Each byte expressed in hexadecimal using 2 characters,so that 32 bytes as
            // hexadecimal.
            char[] str = new char[16 * 2];
            int k = 0; // The index of character in convert result.
            // Convert each byte to hexadecimal of MD5.
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            // Convert the result from byte to string.
            result = new String(str);
        } catch (Exception e) {
            logger.error("SlpMD5Util:generateCheckCode", e);
        }
        return result;
    }
}
