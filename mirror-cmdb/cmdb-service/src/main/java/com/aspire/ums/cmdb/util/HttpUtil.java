package com.aspire.ums.cmdb.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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
     * author: luowenbo
     * date: 2020-05-21
     * GET请求提交
     *
     * @param apiUrl
     * @param params
     * @return
     * @throws IOException
     */
    public static Object getMethod(final String apiUrl, final JSONObject header, final String params) {
        GetMethod method = null;
        try {
            log.info("Request url [{}] header [{}] params [{}].", apiUrl, header == null ? "{}" : header.toString(),
                    params == null ? "{}" : params);
            method = new GetMethod(apiUrl+ "?" +params);
            setMethodHeader(method);
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

    public static void main(String[] args) {
        String url = "http://10.12.70.40:8075/v1/cmdb/monthReport/countCompResource";
        String param = "idcType=&month=2020-04";
        Object s = getMethod(url,null,param);
        System.out.println(s);
    }

    /**
     * 发送HttpPost请求
     *
     * @param strURL
     *            服务地址
     * @param params
     *            json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
     * @return 成功:返回json字符串<br/>
     */
    public static String post(String strURL, String params) throws Exception {
        return post(strURL, 10 * 1000, 10 * 1000, params);
    }

    /**
     * 设置HTTP请求超时时间
     *
     * @param strURL
     *            服务地址
     * @param connectTimeout
     *            连接超时时间
     * @param readTimeout
     *            读取流超时时间
     * @param params
     *            json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
     * @return 成功:返回json字符串<br/>
     */
    public static String post(String strURL, int connectTimeout, int readTimeout, String params) throws Exception {
        HttpURLConnection connection = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;

        String result = "";
        try {
//            logger.info("OSA HttpUtils post function url=[{}],param=[{}]", strURL, params);
            URL url = new URL(strURL);// 创建连接
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.setConnectTimeout(connectTimeout);
            connection.setReadTimeout(readTimeout);
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(params);
            out.flush();
            out.close();
            // 读取响应
            // int length = (int) connection.getContentLength();// 获取长度
            inputStream = connection.getInputStream();
            byte[] data = new byte[1024];
            int len = 0;
            if (inputStream != null) {
                while ((len = inputStream.read(data)) != -1) {
                    byteArrayOutputStream.write(data, 0, len);
                }
                result = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
            }
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
