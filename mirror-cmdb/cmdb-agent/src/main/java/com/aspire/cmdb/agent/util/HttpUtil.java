package com.aspire.cmdb.agent.util;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;

import java.util.Iterator;
import java.util.Map;

@Slf4j
public class HttpUtil {
    private static final Integer CONNECT_OUT_TIME = 60000;
    private static final String ENCODE = "UTF-8";
    private static HttpClient HTTP_CLIENT = new HttpClient(new SimpleHttpConnectionManager(true));

    public static Object getMethod(final String url, final JSONObject header) {
        System.out.println("请求URL:" + url);
        HttpMethod method = null;
        try {
            method = new GetMethod(url);
            setMethodHeader(method, header);
            HTTP_CLIENT.getParams().setConnectionManagerTimeout(CONNECT_OUT_TIME); // 设定60秒超时
            HTTP_CLIENT.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                final byte[] responseBody = method.getResponseBody();
                String rs = new String(responseBody, ENCODE);
                return rs;
            }
            throw new RuntimeException("request url : " + url + " error. httpMethod=GET.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
    }

    /**
     * author: luowenbo
     * date: 2020-05-21
     * GET请求提交
     *
     * @param apiUrl
     * @param params
     * @return
     * @throws IOException
     */
    public static Object getMethod(final String apiUrl, final com.alibaba.fastjson.JSONObject header, final String params) {
        GetMethod method = null;
        try {
            log.info("Request url [{}] header [{}] params [{}].", apiUrl, header == null ? "{}" : header.toString(),
                    params == null ? "{}" : params);
            method = new GetMethod(apiUrl+ "?" +params);
            // setMethodHeader(method);
            HTTP_CLIENT.executeMethod(method);
            String result;
            log.info("请求返回状态码：{}", method.getStatusCode());
            final byte[] responseBodys = method.getResponseBody();
            result = new String(responseBodys, ENCODE);
            log.info("request apiUrl:{}, response data:{}", apiUrl, result);
            if (method.getStatusCode() == HttpStatus.SC_OK || method.getStatusCode() == HttpStatus.SC_CREATED
                    || method.getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                final byte[] responseBody = method.getResponseBody();
                result = new String(responseBody, ENCODE);
                log.info("request apiUrl:{}, response data:{}", apiUrl, result);
                return result;
            }
            throw new RuntimeException("request url " + apiUrl + " error. httpMethod=POST, params:" +
                    (params == null ? "{}" : params));
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
     * @param header 头部信息
     */
    private static void setMethodHeader(final HttpMethod method, final JSONObject header) {
        if (header != null) {
            Iterator iterator = header.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                method.setRequestHeader(entry.getKey().toString(), entry.getValue().toString());
                //log.info("set method header property [{},{}]", entry.getKey().toString(), entry.getValue().toString());
            }
        }
    }
}
