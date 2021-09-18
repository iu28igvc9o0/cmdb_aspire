package com.aspire.mirror.third.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.third.server.client.LdapUserServiceClient;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;

/**
 * ldap相关对外暴露接口
 * @ClassName LdapController
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/12/19
 * @Version V1.0
 **/
@RestController
public class LdapController {

    @Autowired
    private LdapUserServiceClient ldapUserServiceClient;

    @Value("${validCode.check: false}")
    private boolean checkValid;
    /**
     * 校验验证码
     * @param checkCode 输入数据
     */
    @ApiOperation("校验验证码")
    @GetMapping(value="/v1/third/user/checkValidCode")
    public Map checkValidCode(@RequestParam(value = "checkCode") String checkCode) {
        Map<String, String> map = Maps.newHashMap();
        String str;
        try {
            str = new String(Base64.getDecoder().decode(checkCode), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            map.put("status", "ERROR");
            return map;
        }
        JSONObject jsonObject = JSONObject.parseObject(str);
        if (jsonObject == null
                || StringUtils.isEmpty(jsonObject.getString("inputCode"))
                || StringUtils.isEmpty(jsonObject.getString("validCodeKey"))) {
            map.put("status", "ERROR");
            return map;
        }

        String inputCode = jsonObject.getString("inputCode");
        String validCodeKey = jsonObject.getString("validCodeKey");
        if (!checkValid) {
            map.put("status", "CORRECT");
            return map;
        }
        return ldapUserServiceClient.checkValidCode(inputCode, validCodeKey);
    }
}
