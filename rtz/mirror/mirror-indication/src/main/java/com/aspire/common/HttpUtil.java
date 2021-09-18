package com.aspire.common;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

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

    public static Object getMethod(final String url, final JSONObject header) throws RuntimeException {
        HttpMethod method = null;
        try {
            method = new GetMethod(url);
            setMethodHeader(method, header);
            HTTP_CLIENT.getParams().setConnectionManagerTimeout(CONNECT_OUT_TIME); // 设定60秒超时
            HTTP_CLIENT.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                final byte[] responseBody = method.getResponseBody();
                return new String(responseBody, ENCODE);
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
     * Post请求提交
     *
     * @param apiUrl
     * @param params
     * @return
     * @throws IOException
     */
    public static Object postMethod(final String apiUrl, final JSONObject header, final Object params) {
        PostMethod method = null;
        try {
            method = new PostMethod(apiUrl);
            setMethodHeader(method, header);
            if (params != null) {
                RequestEntity jsonEntity = new StringRequestEntity(params.toString(), "application/x-www-form-urlencoded", ENCODE);
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

    public static void deleteMethod(final String apiUrl, final JSONObject header, final Object params)
            throws RuntimeException {
        DeleteMethod method = null;
        String queryString = "", finalApiUrl = apiUrl;
        try {
            if (params != null) {
                if (params instanceof JSONObject) {
                    Iterator iterator = (JSONObject.fromObject(params)).entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        queryString = "&" + entry.getKey().toString() + "=" + entry.getValue().toString();
                    }
                }
            }
            if (!finalApiUrl.contains("?")) {
                finalApiUrl = finalApiUrl + "?";
            }
            if (StringUtils.isNotEmpty(queryString)) {
                finalApiUrl = finalApiUrl + queryString;
            }
            method = new DeleteMethod(finalApiUrl);
            setMethodHeader(method, header);
            HTTP_CLIENT.executeMethod(method);
            if (!(method.getStatusCode() == HttpStatus.SC_OK || method.getStatusCode() == HttpStatus.SC_NO_CONTENT)) {
                throw new RuntimeException(
                        "Delete method execute failed, apiUrl=" + finalApiUrl + ", response code :" + method.getStatusCode());
            }
            log.info("Delete method execute success, apiUrl=" + finalApiUrl + ", response code :" + method.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
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
                log.info("set method header property [{},{}]", entry.getKey().toString(), entry.getValue().toString());
            }
        }
    }
}
