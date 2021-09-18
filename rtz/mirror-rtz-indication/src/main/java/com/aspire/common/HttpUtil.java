package com.aspire.common;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 项目名称:  咪咕微服务运营平台
 * 包:       com.migu.tsg.msp.atomicservice.region.util
 * 类名称:    com.migu.tsg.msp.atomicservice.region.util.HttpUtil.java
 * 类描述:
 * 创建人:    zhu.juwang
 * 创建时间:  2017/11/02 10:32
 * 版本:      v1.0
 */
@Slf4j
public class HttpUtil {
    private static final Integer CONNECT_OUT_TIME = 60000;
    private static final String ENCODE = "UTF-8";
    private static final HttpClient HTTP_CLIENT = new HttpClient(new SimpleHttpConnectionManager(true));

    public static String getMethod(String url, final JSONObject params) throws RuntimeException {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        try {
            log.info("Request url [{}] params [{}].", url, params == null ? "{}" : params.toString());
            //检测URL
            assertUrl(url);
            httpClient = HttpClients.createDefault();
            String paramString = "";
            if (params != null) {
                for (Object key : params.keySet()) {
                    paramString += "&" + key.toString() + "=" + params.get(key).toString();
                }
                if (url.endsWith("?")) {
                    url += paramString;
                } else {
                    url += "?" + paramString.substring(1);
                }
            }
            log.info("Request url [{}] -> params [{}].", url, params == null ? "{}" : params.toString());
            httpGet = new HttpGet(url);
            JSONObject header = new JSONObject();
            header.accumulate("head_orgAccount", "alauda");
            header.accumulate("head_userName", "alauda");
            header.accumulate("head_isAdmin", true);
            header.accumulate("head_isSuperUser", true);
            for (Object key : header.keySet()) {
                httpGet.addHeader(key.toString(), header.get(key).toString());
            }
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(60000)
                    .setSocketTimeout(60000).setConnectTimeout(60000).build();
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String responseString = EntityUtils.toString(entity, ENCODE);
                log.info("Response data {}", responseString);
                return responseString;
            }
            throw new RuntimeException("request url : " + url + " error. httpMethod=GET.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("Close http client error.", e);
                }
            }
        }
    }

    /**
     * Post请求提交
     *
     * @param apiUrl
     * @param params
     * @return
     * @throws IOException
     */
    public static Object postMethod(final String apiUrl, final JSONObject header, final JSONObject params) {
        PostMethod method = null;
        try {
            log.info("Request url [{}] header [{}] params [{}].", apiUrl, header == null ? "{}" : header.toString(),
                    params == null ? "{}" : params.toString());
            assertUrl(apiUrl);
            method = new PostMethod(apiUrl);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            setMethodHeader(method);
            if (params != null) {
                RequestEntity jsonEntity = new StringRequestEntity(params.toString(), "application/json", ENCODE);
                method.setRequestEntity(jsonEntity);
            }
            HTTP_CLIENT.executeMethod(method);
            String result;
            if (method.getStatusCode() == HttpStatus.SC_OK || method.getStatusCode() == HttpStatus.SC_CREATED
                    || method.getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                final byte[] responseBody = method.getResponseBody();
                result = new String(responseBody, ENCODE);
                log.info("request apiUrl:{}, response data:{}", apiUrl, result);
                return result;
            }
            throw new RuntimeException("request url " + apiUrl + " error. httpMethod=POST, params:" +
                    (params == null ? "{}" : params.toString()));
        } catch (Exception e) {
            throw new RuntimeException("request url " + apiUrl + " error.", e);
        } finally {
            //释放关闭连接
            if (method != null) {
                method.releaseConnection();
            }
        }
    }

    /**
     * Put请求提交
     *
     * @param apiUrl
     * @param params
     * @return
     * @throws IOException
     */
    public static Object putMethod(final String apiUrl, final JSONObject header, final JSONObject params){
        PutMethod method = null;
        try {
            log.info("Request url [{}] header [{}] params [{}].", apiUrl, header == null ? "{}" : header.toString(),
                    params == null ? "{}" : params.toString());
            assertUrl(apiUrl);
            method = new PutMethod(apiUrl);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            setMethodHeader(method);
            if (params != null) {
                RequestEntity jsonEntity = new StringRequestEntity(params.toString(), "application/json", ENCODE);
                method.setRequestEntity(jsonEntity);
            }
            HTTP_CLIENT.executeMethod(method);
            String result;
            if (method.getStatusCode() == HttpStatus.SC_OK || method.getStatusCode() == HttpStatus.SC_CREATED
                    || method.getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                final byte[] responseBody = method.getResponseBody();
                result = new String(responseBody, ENCODE);
                log.info("request apiUrl:{}, response data:{}", apiUrl, result);
                return result;
            }
            throw new RuntimeException("request url " + apiUrl + " error. httpMethod=POST, params:" +
                    (params == null ? "{}" : params.toString()));
        } catch (Exception e) {
            throw new RuntimeException("request url " + apiUrl + " error.", e);
        } finally {
            //释放关闭连接
            if (method != null) {
                method.releaseConnection();
            }
        }
    }

    /**
     * 设置方法请求头信息
     * @param method 方法对象
     */
    private static void setMethodHeader(final HttpMethod method) {
        HTTP_CLIENT.getParams().setConnectionManagerTimeout(CONNECT_OUT_TIME); // 设定60秒超时
        HTTP_CLIENT.getParams().setSoTimeout(CONNECT_OUT_TIME); // 设定60秒超时
        JSONObject header = new JSONObject();
        header.accumulate("head_orgAccount", "alauda");
        header.accumulate("head_userName", "alauda");
        header.accumulate("head_isAdmin", true);
        header.accumulate("head_isSuperUser", true);
        for (Object key : header.keySet()) {
            method.setRequestHeader(key.toString(), header.get(key.toString()).toString());
        }
    }

    private static void assertUrl(final String requestUrl) throws RuntimeException {
//        if (requestUrl == null || requestUrl.length() <= 0) {
//            throw new RuntimeException("URL -> [" + requestUrl + "] is not valid.");
//        }
//        int counts = 0;
//        HttpURLConnection connection = null;
//        boolean notConnection = true;
//        while (counts < 5) {
//            try {
//                URL url = new URL(requestUrl);
//                connection = (HttpURLConnection) url.openConnection();
//                if (connection.getResponseCode() == HttpStatus.SC_OK) {
//                    notConnection = false;
//                    break;
//                } else {
//
//                }
//            }catch (Exception ex) {
//                counts++;
//                log.error("Request url -> {} is not connection, will retry {} count after 10 second.", requestUrl, counts);
//                try {
//                    Thread.sleep(10 * 1000);
//                } catch (InterruptedException e) {
//                    log.error("Thread sleep error.", e);
//                }
//            }
//        }
//        if (connection != null) {
//            connection.disconnect();
//            connection = null;
//        }
//        if (notConnection) {
//            String msg = "Request url -> %s is not connection. Please checked the url is ok.";
//            throw new RuntimeException(String.format(msg, requestUrl));
//        }
    }
}
