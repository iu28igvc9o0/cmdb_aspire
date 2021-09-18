package com.aspire.ums.cmdb.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.aspire.ums.cmdb.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;


/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BpmHelper
 * Author:   hangfang
 * Date:     2020/2/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Component
@Data
public class BpmTokenHelper {
    @Value("${bpm.token.url:}")
    private String BPM_TOKEN_URL;
    @Value("${bpm.token.user.url:}")
    private String BPM_TOKEN_USER_URL;
    @Value("${bpm.token.username:}")
    private String BPM_TOKEN_USER;
    @Value("${bpm.token.password:}")
    private String BPM_TOKEN_PASSWORD;
    @Value("${system.change.tobpm.url:}")
    private String BUSINESSSYSTEMCHANGETOBPMURL;
    @Value( "${bpm.onlineBiz.url:}")
    private String BPM_ONLINE_BIZ_Url;
    @Value( "${system.change.tobpm.flowkey:}")
    private String FLOWKEY;
    @Value("${system.change.tobpm.datakey:}")
    private String DATAKEY;

    Logger logger = LoggerFactory.getLogger(BpmTokenHelper.class);

    protected volatile String token = new String();

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

    //获取用户的token，有效期一天
    public synchronized String getUserToken(String account) {
        Map<String, String> params = new HashMap<>();
        params.put("name", new String(org.apache.commons.codec.binary.Base64.encodeBase64(account.getBytes())));
//        token = doGetToken(BPM_TOKEN_USER_URL, params);
//      }
        return doGetToken(BPM_TOKEN_USER_URL, params);
    }

    public String doGetToken(String url, Map<String, String> param) {
        String token  = "";
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity responseEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.info("GET Response Status: {}", statusCode);
            if (statusCode == 200) {
                String result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                JSONObject json = JSON.parseObject(result);
                token = json.getString("token");
            }
        } catch (Exception e) {
            logger.error("send request error URL: " + url, e);
        }
        finally {
            IOUtils.closeQuietly(httpClient);
        }
        return token;
    }



//    public static void main(String[] args) {
//    	  Map<String, String> params = new HashMap<>();
//    	  String account = "liujiaxun";
//          params.put("name", new String(org.apache.commons.codec.binary.Base64.encodeBase64(account.getBytes())));
//          String token = doGetToken("http://10.12.70.37:8088/api/v1/userToken/user/getUserToken",params);
//          System.out.println("+++++++"+token);
//	}






    private String getHttpFileUploadToken (String url, String param) {
        String token  = "";
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            final HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            final StringEntity entity = new StringEntity(param, "UTF-8");
            httpPost.setEntity(entity);

            final HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.debug("POST Response Status: {}", statusCode);
            if (statusCode == 200) {
                String result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
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


    public String doGet(String token, String url, Map<String, String> param) {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            if (StringUtils.isNotEmpty(token)) {
                httpGet.setHeader("Authorization", "Bearer "+token);
                httpGet.setHeader("Content-Type", "application/json;charset=utf-8");
            }
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity responseEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.info("GET Response Status: {}", statusCode);
            if (statusCode == 200) {
                String result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                return result;
            }
        } catch (Exception e) {
            logger.error("send request error URL: " + url, e);
        }
        finally {
            IOUtils.closeQuietly(httpClient);
        }
        return null;
    }


}
