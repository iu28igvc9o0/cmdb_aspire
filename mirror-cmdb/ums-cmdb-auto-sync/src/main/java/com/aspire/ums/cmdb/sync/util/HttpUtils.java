package com.aspire.ums.cmdb.sync.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
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

/**
 * Http 请求工具类
 */
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
    private static final String HTTP_CHARSET = "UTF-8";

    /**
     * HttpPost:post请求公用方法 <br/>
     * 作者： liangjun
     *
     * @param param
     *            参数
     * @param url
     *            url地址
     */
    public static String httpPost(final String param, final String url) {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            final HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            final StringEntity entity = new StringEntity(param, HTTP_CHARSET);
            httpPost.setEntity(entity);

            final HttpResponse httpResponse = httpClient.execute(httpPost);
            String body = null;
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (HttpStatus.OK.value() == statusCode) {
                HttpEntity resEntity = httpResponse.getEntity();
                if (resEntity != null) {
                    // 按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(resEntity, Charset.forName("utf-8"));
                }
                EntityUtils.consume(resEntity);
            }
            return body;
        } catch (UnsupportedCharsetException e) {
            LOGGER.error("send post error:", e);
        } catch (ClientProtocolException e) {
            LOGGER.error("send post error:", e);
        } catch (IOException e) {
            LOGGER.error("send post error:", e);
        } finally {
            IOUtils.closeQuietly(httpClient);
        }
        return null;
    }

    /**
     * HttpPost:post请求公用方法 <br/>
     * 作者： liangjun
     *
     *            参数
     * @param url
     *            url地址
     */
    public static String httpGet(final String url) {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            final HttpGet httpGet = new HttpGet(url);
            // httpGet.setHeader("Content-Type", "application/json");
            LOGGER.info("请求url：" + url);
            final HttpResponse httpResponse = httpClient.execute(httpGet);
            String body = null;
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (HttpStatus.OK.value() == statusCode) {
                HttpEntity resEntity = httpResponse.getEntity();
                if (resEntity != null) {
                    // 按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(resEntity, Charset.forName("utf-8"));
                }
                EntityUtils.consume(resEntity);
            } else {
                LOGGER.info("请求返回异常：" + EntityUtils.toString(httpResponse.getEntity()));
            }
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(httpClient);
        }
        return null;
    }
}
