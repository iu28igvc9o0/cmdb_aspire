package com.migu.tsg.microservice.atomicservice.composite.helper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.helper
 * 类名称:    BpmTokenHelper.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/9/2 14:53
 * 版本:      v1.0
 */
@Component
public class BpmTokenHelper {
    @Value("${bpm.token.url:}")
    private String BPM_TOKEN_URL;

    @Value("${bpm.token.username:}")
    private String BPM_TOKEN_USER;

    @Value("${bpm.token.password:}")
    private String BPM_TOKEN_PASSWORD;
    
    @Value("${bpm.token.userUrl:}")
    private String BPM_USER_TOKEN_URL;


    Logger logger = LoggerFactory.getLogger(BpmTokenHelper.class);

    protected volatile String token = "";

    public synchronized String getTokenByUser(){
    	RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		String account = authCtx.getUser().getUsername();
		String userTokenUrl = BPM_USER_TOKEN_URL + "?name=" + Base64.getEncoder().encodeToString(account.getBytes());
		return getBpmUserToken(userTokenUrl);
    }
    
    public synchronized String getToken() {
//        if (StringUtils.isEmpty(token)) {
            // Http 文件上传接口使用 token
            Map<String, String> params = new HashMap<>();
            params.put("username", BPM_TOKEN_USER);
            params.put("password", BPM_TOKEN_PASSWORD);
            token = getHttpFileUploadToken(BPM_TOKEN_URL, JSON.toJSONString(params));
//        }
        return token;
    }
    private String getHttpFileUploadToken (String url, String param) {
        String token  = "";
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            final HttpPost httpGet = new HttpPost(url);
            httpGet.setHeader("Content-Type", "application/json");
            final StringEntity entity = new StringEntity(param, "UTF-8");
            httpGet.setEntity(entity);

            final HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity responseEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.debug("POST Response Status: {}", statusCode);
            if (statusCode == 200) {
                String result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                JSONObject json = JSON.parseObject(result);
                token = json.getString("token");
            }
        } catch (UnsupportedCharsetException e) {
            logger.error("send post error:", e);
        } catch (ClientProtocolException e) {
            logger.error("send post error:{}", e);
        } catch (IOException e) {
            logger.error("send post error:{}", e);
        } finally {
            IOUtils.closeQuietly(httpClient);
        }
        return token;
    }
    
    private String getBpmUserToken (String url) {
        System.out.println("请求的URL:" + url);
        String token  = "";
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            final HttpGet httpPost = new HttpGet(url);
            httpPost.setHeader("Content-Type", "application/json");

            final HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.debug("POST Response Status: {}", statusCode);
            if (statusCode == 200) {
                String result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                JSONObject json = JSON.parseObject(result);
                token = json.getString("token");
            }
        } catch (UnsupportedCharsetException e) {
            logger.error("send post error:", e);
        } catch (ClientProtocolException e) {
            logger.error("send post error:{}", e);
        } catch (IOException e) {
            logger.error("send post error:{}", e);
        } finally {
            IOUtils.closeQuietly(httpClient);
        }
        return token;
    }
}
