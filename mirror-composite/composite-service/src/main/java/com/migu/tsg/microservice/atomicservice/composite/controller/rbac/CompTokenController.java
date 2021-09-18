package com.migu.tsg.microservice.atomicservice.composite.controller.rbac;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.StringUtil;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.TokenServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.common.RedisCacheHelper;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.Authentication;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.AESUtil;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.ICompTokenService;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RoleDetailResponsePer;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.CommonResult;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.UserLoginForUmsVo;

import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.rsa.RSAVerifier;
import lombok.extern.slf4j.Slf4j;

/**
 * @BelongsProject: msp-composite
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.composite.controller.rbac
 * @Author: baiwenping
 * @CreateTime: 2020-03-18 18:01
 * @Description: ${Description}
 */
@Slf4j
@RestController
public class CompTokenController implements ICompTokenService {

    @Autowired
    private TokenServiceClient tokenServiceClient;
    @Autowired
    private RbacRolesController rbacRolesController;
    @Autowired
    RedisCacheHelper redisCacheHelper;

    private static final String TOKEN="token";

    private static final String SSO_TOKEN="ssoToken";

    private static final String MIRROR_TOKEN="mirrorToken";

    private static final String SSO_TOKEN_FOR4A="iamcaspticket";

    @Value("${epc.sso.checkurl:}")
    private String epcSsoCheckUrl;
    // for test
    @Value("${userSync.publicKey: test}")
    private String rsaPublicKeyPath;
    @Value("${userSync.redirectUrl: http://10.1.203.100:8080?mirrorToken=}")
    private String redirectUrl;

    @Value("${4a.sso.checkurl:}")
    private String ssoCheckUrlFor4A;
    @Value("${4a.sso.appCode:}")
    private String appCodeFor4A;
    @Value("${4a.sso.tenant:}")
    private String tenantFor4A;
    

    /**
     * 获取keycloak登录token
     *
     * @param type
     * @param token
     * @return
     */
    @Override
    @Authentication(anonymous = true)
    public Map<String, Object> token(@RequestParam("type") String type, @RequestParam("token") String token) {
        Map<String, Object> rs = new HashMap<>();
        String username = null;
        if (MIRROR_TOKEN.equalsIgnoreCase(type) || SSO_TOKEN.equalsIgnoreCase(type)) {
            username = new String(Base64.getDecoder().decode(token));
            if (!StringUtils.isEmpty(username)) {
                username = username.split("\\|\\|")[0];
            }
        } else if (TOKEN.equalsIgnoreCase(type)) {
            username = getUerNameWithEpcOauth(token);
        } else if(SSO_TOKEN_FOR4A.equalsIgnoreCase(type)) {
            username = getUerNameWith4AVerify(token);
            if(!StringUtils.isEmpty(username)) {
                // 返回用户角色和用户是否为管理员
                rs.put("userName",username);
                List<RoleDetailResponsePer> respList = rbacRolesController.retrieveRoleByUserName("alauda", username);
                if(CollectionUtils.isEmpty(respList)) {
                    rs.put("isAdmin",false);
                } else {
                    rs.put("isAdmin",respList.get(0).getAdminRole());
                }
            }
        }
        if (StringUtils.isEmpty(username)) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("code","0005");
            map.put("message", "invalid token");
            return map;
        }
        // luowenbo 2020-07-24修改：null引用缺陷
        Map<String, Object> loginMp = tokenServiceClient.token(null == username ? username : username.trim());
        if(!CollectionUtils.isEmpty(loginMp)) {
            rs.putAll(loginMp);
        } else {
            rs.put("code","0005");
            rs.put("message", "invalid username");
        }
        System.out.println(rs.toString());
        return rs;
    }

    /**
     *
     * jwt token test:
     * eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI5MjllNjI0Zi0zYTI1LTQwZDEtYWFmNi01ZDk1M2NkOWM4NDYiLCJqdGkiOiJrZ194aWV0aWFuIiwiaXNzIjoiY21yaS5ocS5jbWNjIiwiZXhwIjoxNTk4NTAwNzM3LCJpYXQiOjE1MTYyMzkwMjJ9.bU1KF8HklVP3OjinaSxUGqYEKkVxkI44YdM4mfjS3_ayJUBeDR3G7fMcTFYou-JtWnFD8fiQjohLZO4SdXwNy7XKU8bRRppbqYOHzGmAeGfTjMNmAHUvi0sVgcLy4HCd7FSmtLQ2-h3cOXfRxdbMDDm-DoAi6wSN-IChLf5pIpIcWiOa3axkA5H88wCwPFp1mQa_VF1ujVZ-4X_OOQKIq9wVbGd0L5ljhquouOKp-fA0d0GKVrbEd83briL9FHAUNZc1_TuqCOS_UhfjnUHCjDrKqMVTq_DkFqKQgbBEWcxozccPmCkaCGMfvhwMDuQxApVFf1LhddkGiuFIJ-W_hQ
     *
     *
     * @Author huanggongrui
     * @CreateTime 2020-07-21 16:23:12
     * 获取keycloak登录token
     * uim-user: 当前请求的用户名（UID）
     * uim-verifier: 用户ID校验串（JWT TOKEN）
     * iv-user: 当前请求的用户名（UID），已废弃，兼容历史IBM webseal网关
     * @param request
     * @return
     */
    @Override
    @Authentication(anonymous = true)
    public RedirectView tokenKg(HttpServletRequest request) {
        RedirectView redirectView = new RedirectView();
        redirectView.setContextRelative(false);
        String uimUser = request.getHeader("uim-user");
        if (StringUtils.isEmpty(uimUser)) {
            uimUser = request.getHeader("CAS_uim-user");
        }
        log.info("科管单点CAS_uim-user: {}", uimUser);
        String uimVerfier = request.getHeader("uim-verifier");
        if (StringUtils.isEmpty(uimVerfier)) {
            uimVerfier = request.getHeader("CAS_uim-verifier");
        }
        log.info("科管单点CAS_uim-verifier: {}", uimVerfier);
        String ivUser = request.getHeader("iv-user");
        Verifier verifier = null;
        try {
            // test
            // rsaPublicKeyPath = "E:\\aspire\\njbpm\\rsa_public_test.key";
            Path path = Paths.get(rsaPublicKeyPath);
            verifier = RSAVerifier.newVerifier(new String(Files.readAllBytes(path)));
            JWT jwt = JWT.getDecoder().decode(uimVerfier, verifier);
            String subject = jwt.subject;
            String issuer = jwt.issuer;
            String uniqueId = jwt.uniqueId;
            // redirectUrl = "";
            if (!jwt.isExpired() && StringUtils.isNotBlank(uniqueId) && uniqueId.equals(uimUser)) {
                String token = Base64Utils.encodeToString(uimUser.getBytes());
                redirectView.setUrl(redirectUrl + token);
            } else {
                redirectView.setUrl(redirectUrl);
                log.info("科管部统一认证单点登录请求失败：jwt token过期或验证不通过");
            }
        } catch (Exception e) {
            log.error("科管部统一认证单点登录请求失败：", e);
        }

        return redirectView;
    }

    private String getUerNameWithEpcOauth(String token) {
        String username = null;
        if(StringUtil.isNotEmpty(token)) {
            StringBuffer url =  new StringBuffer();
            url.append(epcSsoCheckUrl);
            url.append(token);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post= new HttpPost(url.toString());
            HttpResponse respon = null;

            try {
                respon = httpClient.execute(post);
                // 请求发送成功并得到响应
                if (respon.getStatusLine().getStatusCode() == 200) {
                    // 读取响应字符串
                    HttpEntity httpEntity = respon.getEntity();
                    String entityStr = EntityUtils.toString(httpEntity);
                    log.info("call check token interface return is : " + entityStr);
                    // 将字符串转换成Json对象
                    ObjectMapper objectMapper = new ObjectMapper();
                    // luowenbo 2020.07.24 JSON注入缺陷
                    JsonStringEncoder encoder = JsonStringEncoder.getInstance();
                    // quoteAsUTF8 方法将字符创按照 JSON 标准处理并编码为 UTF-8
                    byte[] bytes = encoder.quoteAsUTF8(entityStr);
                    String newJson = new String(bytes, StandardCharsets.UTF_8);
                    newJson = StringEscapeUtils.unescapeJava(newJson);
                    JsonNode checkJNode = objectMapper.readTree(newJson);
                    if(checkJNode!=null && checkJNode.isObject()) {
                        JsonNode resultValue = checkJNode.findValue("entity");
                        Boolean result = resultValue.get("result").asBoolean();
                        if(result){
                            username = resultValue.get("username").asText();
                            log.error("EPC单点登录uername："+username);
                        }

	        				 /*String responseEntitys = checkJNode.get("entity").asText();
	        				 JsonNode responseEntity = JsonUtil.toJsonNode(responseEntitys);
	        				 Boolean result = responseEntity.get("result").asBoolean();
	 	                     if(result){
	 	                    	username = responseEntity.get("username").asText();
	 	                     }*/
                    }
                }
            }catch (Exception e){
                // 认证失败
                log.error("call check token interface error : ", e);
            } finally {
                // luowenbo 2020-07-24 添加：socket链接关闭
                if(null != post) {
                    post.releaseConnection();
                }
            }
        }
        return username;
    }

    private String getUerNameWith4AVerify(String token) {
        String username = null;
        if(StringUtil.isNotEmpty(token)) {
            // 创建HttpClient实例
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = null;
            try {
                httpPost = new HttpPost(ssoCheckUrlFor4A);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("appCode", appCodeFor4A);
                jsonObject.put("tenant", tenantFor4A);
                jsonObject.put("data", token);
                httpPost.setEntity(new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON));
                HttpResponse httpResponse = client.execute(httpPost);
                String reStr = EntityUtils.toString(httpResponse.getEntity());
                System.out.println("接口相应结果:" + reStr);
                if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    // 将字符串转换成Json对象
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonStringEncoder encoder = JsonStringEncoder.getInstance();
                    // quoteAsUTF8 方法将字符创按照 JSON 标准处理并编码为 UTF-8
                    byte[] bytes = encoder.quoteAsUTF8(reStr);
                    String newJson = new String(bytes, StandardCharsets.UTF_8);
                    newJson = StringEscapeUtils.unescapeJava(newJson);
                    JsonNode checkJNode = objectMapper.readTree(newJson);
                    if(checkJNode!=null && checkJNode.isObject()) {
                        JsonNode resultValue = checkJNode.findValue("retCode");
                        String retCode = resultValue.asText();
                        if ("1000".equals(retCode)) {
                            username = checkJNode.get("userInfo").get("accountID").asText();
                            log.error("4A单点登录uername：" + username);
                        }
                    }
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("传过来的Token为null");
        }
        return username;
    }
    
    @Authentication(anonymous = true)
  	@Override
  	public Map<String, Object> umsAuth(@RequestBody UserLoginForUmsVo authenticationRequest) {
    	Map<String, Object> map = tokenServiceClient.umsAuth(authenticationRequest);
    	if("200".equals(map.get("code").toString())){
    		String account = map.get("account").toString();
    		return tokenServiceClient.token(null == account ? account : account.trim());
    	}
    	return map;
  	}
  	
  	@Authentication(anonymous = true)
  	@Override
  	public CommonResult<String> getSignkey(@RequestParam("loginKey") String loginKey) {
  		return tokenServiceClient.getSignkey(loginKey);
  	}
}
