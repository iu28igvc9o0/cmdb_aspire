package com.aspire.ums.cmdb.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;

public class BpmAccessTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(BpmAccessTokenUtil.class);

    private static String token;

    private static String getHttpAccessToken(String url, String param) {
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
            if (statusCode == HttpStatus.OK.value()) {
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

    public static String getToken() {
        return token;
    }

    public static String genHttpAccessToken(String url, String account, String password) {
        // Http 文件上传接口使用 token
        Map<String, String> params = new HashMap<>();
        params.put("username", account);
        params.put("password", password);
        BpmAccessTokenUtil.token = getHttpAccessToken(url, JSON.toJSONString(params));
        return BpmAccessTokenUtil.token;
    }
}
