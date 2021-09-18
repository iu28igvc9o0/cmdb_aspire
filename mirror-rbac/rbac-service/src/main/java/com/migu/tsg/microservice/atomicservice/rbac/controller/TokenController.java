package com.migu.tsg.microservice.atomicservice.rbac.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.common.client.LdapServiceClient;
import com.migu.tsg.microservice.atomicservice.common.config.KeycloakProperties;
import com.migu.tsg.microservice.atomicservice.common.helper.RedisCacheHelper;
import com.migu.tsg.microservice.atomicservice.common.util.AESUtil;
import com.migu.tsg.microservice.atomicservice.common.util.HttpsUtil;
import com.migu.tsg.microservice.atomicservice.ldap.dto.GetLdapUserResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.CommonResult;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.UserLoginForUmsVo;
import com.migu.tsg.microservice.atomicservice.rbac.service.TokenService;

import lombok.extern.slf4j.Slf4j;

/**
 * @BelongsProject: mirror-rbac
 * @BelongsPackage: com.migu.tsg.microservice.atomicservice.rbac.controller
 * @Author: baiwenping
 * @CreateTime: 2020-03-18 15:39
 * @Description: ${Description}
 */
@Slf4j
@RestController
public class TokenController implements TokenService{
    @Autowired
    private KeycloakProperties properties;
    @Autowired
    private LdapServiceClient ldapServiceClient;
    @Autowired
    RedisCacheHelper redisCacheHelper;
    /**
     * 获取keycloak登录token
     *
     * @param username
     * @return
     */
    @Override
    public Map<String, Object> token(@PathVariable("username") final String username) {
        Map<String, Object> map = Maps.newHashMap();
        String url = properties.getUrl();
        if (StringUtils.isEmpty(url)) {
            map.put("code", "0001");
            map.put("message", "keycloak url is not exist");
            return map;
        }
        //组装url
        StringBuilder urlSb = new StringBuilder(properties.getTokenUrl());
        if (!url.endsWith("/")) {
            urlSb.append("/");
        }
        urlSb.append("realms/").append(properties.getRealm()).append("/protocol/openid-connect/token");
        // 组装参数
        StringBuilder param = new StringBuilder("client_id=");
        param.append(properties.getClientId()).append("&grant_type=password&username=").append(username);
        param.append("&password=");
        String password = getPassword(username);
        if (StringUtils.isEmpty(password)) {
            map.put("code", "0002");
            map.put("message", "user"+username+" is not exist");
            return map;
        }
        param.append(password);

        if (properties.getTokenUrl().toLowerCase(Locale.getDefault()).startsWith("https")) {
            map = HttpsUtil.doPostForKeycloak(urlSb.toString(), param.toString());
        } else {
            map = getTokenByHttp(urlSb.toString(), param.toString());
        }
        return map;
    }

    @Override
    public Map<String, Object> userinfo(@RequestHeader(value = "Authorization") String authorization) {
        // 将前缀去掉
        authorization = authorization.replace("Bear ","");
        Map<String, Object> map = Maps.newHashMap();
        String url = properties.getUrl();
        if (StringUtils.isEmpty(url)) {
            map.put("code", "0001");
            map.put("message", "keycloak url is not exist");
            return map;
        }
        //组装url
        StringBuilder urlSb = new StringBuilder(properties.getTokenUrl());
        if (!url.endsWith("/")) {
            urlSb.append("/");
        }
        urlSb.append("realms/").append(properties.getRealm()).append("/protocol/openid-connect/userinfo");
        StringBuilder sb = new StringBuilder("access_toekn=");
        sb.append(authorization);
        if (properties.getTokenUrl().toLowerCase(Locale.getDefault()).startsWith("https")) {
            map = HttpsUtil.doPostForKeycloak(urlSb.toString(),sb.toString());
        } else {
            map = getUserInfoByHttp(urlSb.toString(),authorization);
        }
        return map;
    }

    private Map<String, Object> getUserInfoByHttp (String url, String authorization) {
        Map<String, Object> map = Maps.newHashMap();
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            //通过httpClient调用接口
            final HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("access_token", authorization));
            HttpEntity httpEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPost.setEntity(httpEntity);

            final HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            log.info("POST Response Status: {}"+statusCode);
            if (statusCode == 200) {
                String result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                ObjectMapper objectMapper = new ObjectMapper();
                map.putAll(objectMapper.readValue(result, Map.class));
                map.put("code", "0000");
            }
        } catch (UnsupportedCharsetException e) {
            log.error("send post error:"+ e);
            map.put("code", "9999");
            map.put("message", e.getMessage());
        } catch (ClientProtocolException e) {
            log.error("send post error:{}"+ e);
            map.put("code", "9999");
            map.put("message", e.getMessage());
        } catch (IOException e) {
            log.error("send post error:{}"+ e);
            map.put("code", "9999");
            map.put("message", e.getMessage());
        } finally {
            IOUtils.closeQuietly(httpClient);
            return map;
        }
    }

    private Map<String, Object> getTokenByHttp (String url, String param) {
        Map<String, Object> map = Maps.newHashMap();
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            //通过httpClient调用接口
            final HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            final StringEntity entity = new StringEntity(param, "UTF-8");
            httpPost.setEntity(entity);

            final HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            log.info("POST Response Status: {}"+statusCode);
            if (statusCode == 200) {
                String result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                ObjectMapper objectMapper = new ObjectMapper();
                map.putAll(objectMapper.readValue(result, Map.class));
                map.put("code", "0000");
            }
        } catch (UnsupportedCharsetException e) {
            log.error("send post error:"+ e);
            map.put("code", "9999");
            map.put("message", e.getMessage());
        } catch (ClientProtocolException e) {
            log.error("send post error:{}"+ e);
            map.put("code", "9999");
            map.put("message", e.getMessage());
        } catch (IOException e) {
            log.error("send post error:{}"+ e);
            map.put("code", "9999");
            map.put("message", e.getMessage());
        } finally {
            IOUtils.closeQuietly(httpClient);
            return map;
        }
    }

    /**
     * 根据用户查找跟账号
     * @param username
     * @return
     */
    private String getPassword (String username) {
        if (username.equalsIgnoreCase(properties.getNamespace())) {
            GetLdapUserResponse ldapAdmin = ldapServiceClient.getLdapAdmin(username);
            if (ldapAdmin != null) {
                return ldapAdmin.getPassword();
            }
        } else {
            GetLdapUserResponse ldapMember = ldapServiceClient.getLdapMember(properties.getNamespace(), username);
            if (ldapMember != null) {
                return ldapMember.getPassword();
            }
        }
        return null;
    }

	@Override
	public Map<String, Object> umsAuth(@RequestBody UserLoginForUmsVo authenticationRequest) {
		Map<String, Object> map = Maps.newHashMap();
      	String reqAccount = authenticationRequest.getUsername();
  		String password = authenticationRequest.getPassword();
  		String loginKey = authenticationRequest.getLoginKey();
  		if (StringUtils.isEmpty(loginKey)) {
  			map.put("code", "9999");
  	        map.put("message", "获取LoingKey为空!");
  	        return map;
  		}
  		boolean keyExist = redisCacheHelper.hasKey(loginKey);
  		if(!keyExist){
  			map.put("code", "9999");
  	        map.put("message", "LoingKey 已经过期或者不存在!");
  	        return map;
  		}
  		try {
  			String aesKey = (String) redisCacheHelper.getByKey(loginKey);
  			reqAccount = AESUtil.decrypt(reqAccount, aesKey);
  			password = AESUtil.decrypt(password, aesKey);
  			boolean oaValid = checkAuthByOa(reqAccount, password);
  			if(oaValid){
  				log.info("使用OA域账号密码登录UMS成功{}",reqAccount);
  			}else{
  				map.put("code", "9998");
  		        map.put("message", "用户名密码错误!");
  		        return map;
  			}
  			map.put("code", "200");
  			map.put("account", reqAccount);
  		} catch (Exception e) {
  			log.error("authForUms error:{}"+ e);
  		} finally {
  			redisCacheHelper.delete(loginKey);
  		}
  		return map;
	}
	
    private boolean checkAuthByOa(String reqAccount, String password) {
      	String ldapHost = "10.1.9.13";
  		String ldapPort = "389";
  		String ldapSearchBase = "dc=aspire,dc=aspire-tech,dc=com";
  		final String ldapURL = "ldap://" + ldapHost + ":" + ldapPort + "/";
  		Hashtable<String,String> env = new Hashtable<String,String>();
  		DirContext ctx = null;
  		env.put(Context.SECURITY_AUTHENTICATION, "simple");
  		env.put(Context.SECURITY_PRINCIPAL, "aspire\\" + reqAccount);
  		env.put(Context.SECURITY_CREDENTIALS, password);
  		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
  		env.put(Context.PROVIDER_URL, ldapURL + ldapSearchBase);

  		//** ldap登录，如果有异常说明登录失败 *//*
  		try {
  			ctx = new InitialDirContext(env);
  			return true;
  		} catch (Exception err) {
  			return false;
  		} finally {
  			if (null != ctx) {
  				try {
  					ctx.close();
  				} catch (Exception e) {
  					e.printStackTrace();
  				}
  			}
  		}
  	}
	
	@Override
	public CommonResult<String> getSignkey(@RequestParam("loginKey") String loginKey) {
		String aesKey = AESUtil.generateString(16);

        String keyBase64 = AESUtil.encodeKeyToBase64(aesKey);

        if (StringUtils.isEmpty(keyBase64)) {
            return new CommonResult<String>(false, "获取签名密钥失败！", keyBase64);
        } else {
            if (redisCacheHelper.hasKey(loginKey)) {
                return new CommonResult<String>(false, "获取签名密钥失败！loginKey已经存在", keyBase64);
            }
            int timeout = 300;
            redisCacheHelper.add(loginKey, aesKey, timeout);

            return new CommonResult<String>(true, "获取签名密钥成功！", keyBase64);
        }
	}
}
