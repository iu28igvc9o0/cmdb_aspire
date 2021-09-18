package com.aspire.ums.cmdb.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectSchedule
 * Author:   zhu.juwang
 * Date:     2019/3/14 13:54
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class HttpUtil {
    private static final Integer CONNECT_OUT_TIME = 60000;
    private static final String ENCODE = "UTF-8";
    private static final HttpClient HTTP_CLIENT = new HttpClient(new SimpleHttpConnectionManager(true));

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
     * 设置方法请求头信息
     * @param method 方法对象
     */
    private static void setMethodHeader(final HttpMethod method) {
        HTTP_CLIENT.getParams().setConnectionManagerTimeout(CONNECT_OUT_TIME); // 设定60秒超时
        HTTP_CLIENT.getParams().setSoTimeout(CONNECT_OUT_TIME); // 设定60秒超时
        JSONObject header = new JSONObject();
        header.put("head_orgAccount", "alauda");
        header.put("head_userName", "alauda");
        header.put("head_isAdmin", true);
        header.put("head_isSuperUser", true);
        for (Object key : header.keySet()) {
            method.setRequestHeader(key.toString(), header.get(key.toString()).toString());
        }
    }
}
