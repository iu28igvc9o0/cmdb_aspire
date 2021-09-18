package com.migu.tsg.microservice.atomicservice.composite.controller.rbac;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.LdapUserServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.Authentication;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.ICompLdapUserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * @ClassName CompLdapUserController
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/12/26
 * @Version V1.0
 **/
@RestController
public class CompLdapUserController implements ICompLdapUserService{

    @Autowired
    private LdapUserServiceClient ldapUserServiceClient;

    @Value("${validCode.check: false}")
    private boolean checkValid;

    @Override
    public Map getValidCode() {
        Map validCodeMap = ldapUserServiceClient.getValidCode();
        String s = JSONObject.toJSONString(validCodeMap);
        String base64Code = new String (Base64.getEncoder().encode(s.getBytes()));
        Map map = Maps.newHashMap();
        map.put("checkCode",base64Code);
        return map;
    }

    /**
     * 校验验证码
     * @param checkCode 输入数据
     */
    @Authentication(anonymous = true)
    public Map checkValidCode(@RequestParam(value = "checkCode") String checkCode) {
        Map<String, String> map = Maps.newHashMap();
        String str;
        str = new String(Base64.getDecoder().decode(checkCode), StandardCharsets.UTF_8);
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
