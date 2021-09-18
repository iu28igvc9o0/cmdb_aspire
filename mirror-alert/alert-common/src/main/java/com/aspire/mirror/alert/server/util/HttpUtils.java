package com.aspire.mirror.alert.server.util;
import com.aspire.mirror.alert.server.domain.CommonHttpResponseVo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

/**
 * 监控-Http请求工具类
 * <p>
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.alert.server.util
 * 类名称:    HttpUtils.java
 * 类描述:    监控-Http请求工具类
 * 创建人:    LiangJun
 * 创建时间:  2019-06-05 14:17:17
 */
public class HttpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
    private static final String HTTP_CHARSET = "UTF-8";

    /**
     * HttpGET:get请求公用方法
     *
     * @param url
     * @param param
     * @return com.migu.tsg.microservice.monitor.manage.api.dto.MetricHttpResponse
     * @method doGet
     * @Author： LiangJun
     * @Date: 2017/9/26 13:52
     */
    public static CommonHttpResponseVo doGet(String url, Map<String, String> param) {
        CommonHttpResponseVo metricHttpResponse = new CommonHttpResponseVo();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            response = httpclient.execute(httpGet);
            metricHttpResponse.setStatus(response.getStatusLine().getStatusCode());
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity == null) {
                metricHttpResponse.setContent("");
            } else {
                metricHttpResponse.setContent(IOUtils.toString(httpEntity.getContent(), HTTP_CHARSET));
            }
        } catch (Exception e) {
            LOGGER.error("send request error URL: " + url, e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("close response error. ", e);
                }
            }
            IOUtils.closeQuietly(httpclient);
        }
        return metricHttpResponse;
    }

    /**
     * HttpGET:get请求公用方法
     *
     * @param url
     * @return com.migu.tsg.microservice.monitor.manage.api.dto.MetricHttpResponse
     * @method doGet
     * @Author： LiangJun
     * @Date: 2019-06-05 14:17:17
     */
    public static CommonHttpResponseVo doGet(String url) {
        return doGet(url, null);
    }

    /**
     * httpGet token请求
     *
     * @param url
     * @param token
     * @return com.migu.tsg.microservice.monitor.manage.api.dto.MetricHttpResponse
     * @method httpGet
     * @Author： LiangJun
     * @Date: 2019-06-05 14:17:17
     */
    public static CommonHttpResponseVo httpGet(final String url, final String token) {
        CommonHttpResponseVo metricHttpResponse = new CommonHttpResponseVo();
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            final HttpGet httpGet = new HttpGet(url);
            if (StringUtils.isNotEmpty(token)) {
                httpGet.addHeader("Authorization", "Token " + token);
            }
            response = httpClient.execute(httpGet);
            metricHttpResponse.setStatus(response.getStatusLine().getStatusCode());
            metricHttpResponse.setContent(IOUtils.toString(response.getEntity().getContent(), HTTP_CHARSET));
        } catch (IOException e) {
            LOGGER.error("close response error. ", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.error("close response error. ", e);
                }
            }
            IOUtils.closeQuietly(httpClient);
        }
        return metricHttpResponse;
    }

    /**
     * HttpPost:post请求公用方法 <br/>
     * 作者： jiangfuyi
     *
     * @param param 参数
     * @param url   url地址
     * @param token token
     */
    public static void httpPost(final String param, final String url, final String token) {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            final HttpPost httpPost = new HttpPost(url);
            if (StringUtils.isNotEmpty(token)) {
                httpPost.addHeader("Authorization", "Token " + token);
            }
            httpPost.setHeader("Content-Type", "application/json");
            final StringEntity entity = new StringEntity(param, HTTP_CHARSET);
            httpPost.setEntity(entity);

            final HttpResponse httpResponse = httpClient.execute(httpPost);

            LOGGER.debug("POST Response Status:" + httpResponse.getStatusLine().getStatusCode());
        } catch (UnsupportedCharsetException e) {
            LOGGER.error("send post error:{}", e);
        } catch (ClientProtocolException e) {
            LOGGER.error("send post error:{}", e);
        } catch (IOException e) {
            LOGGER.error("send post error:{}", e);
        } finally {
            IOUtils.closeQuietly(httpClient);
        }

    }

    /**
     * HttpPut:put请求公用方法 <br/>
     * 作者： jiangfuyi
     *
     * @param param 参数
     * @param url   url地址
     * @param token token
     */
    public static void httpPut(final String param, final String url, final String userAgent, final String token) {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            final HttpPut httpPut = new HttpPut(url);
            httpPut.setHeader("Content-Type","application/json");
            if (StringUtils.isNotEmpty(userAgent)) {
                httpPut.addHeader("User-Agent", userAgent);
            }
            if (StringUtils.isNotEmpty(token)) {
                httpPut.addHeader("Authorization", "Token " + token);
            }
            final StringEntity entity = new StringEntity(param, HTTP_CHARSET);
            httpPut.setEntity(entity);

            final HttpResponse httpResponse = httpClient.execute(httpPut);

            LOGGER.debug("Put Response Status: " + httpResponse.getStatusLine().getStatusCode());
        } catch (UnsupportedCharsetException e) {
            LOGGER.error("send post error: ", e);
        } catch (ClientProtocolException e) {
            LOGGER.error("send post error: ", e);
        } catch (IOException e) {
            LOGGER.error("send post error: ", e);
        } finally {
            IOUtils.closeQuietly(httpClient);
        }
    }

    /**
     * HttpPut:put请求公用方法 <br/>
     * 作者： jiangfuyi
     *
     * @param param 参数
     * @param url   url地址
     * @param token token
     */
    public static void httpPut(final String param, final String url, final String token) {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            final HttpPut httpPut = new HttpPut(url);
            if (StringUtils.isNotEmpty(token)) {
                httpPut.addHeader("Authorization", "Token " + token);
            }
            httpPut.setHeader("Content-Type", "application/json");
            final StringEntity entity = new StringEntity(param, "UTF-8");
            httpPut.setEntity(entity);
            final HttpResponse httpResponse = httpClient.execute(httpPut);
            LOGGER.debug("Put Response Status: " + httpResponse.getStatusLine().getStatusCode());
        } catch (UnsupportedCharsetException e) {
            LOGGER.error("send post error: ", e);
        } catch (ClientProtocolException e) {
            LOGGER.error("send post error: ", e);
        } catch (IOException e) {
            LOGGER.error("send post error: ", e);
        } finally {
            IOUtils.closeQuietly(httpClient);
        }
    }

    public static void httpDelete(final String url, final String token) {
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            final HttpDelete httpDelete = new HttpDelete(url);
            if (StringUtils.isNotEmpty(token)) {
                httpDelete.addHeader("Authorization", "Token " + token);
            }
            httpDelete.setHeader("Content-Type", "application/json");
            final HttpResponse httpResponse = httpClient.execute(httpDelete);
            LOGGER.debug("Delete Response Status: " + httpResponse.getStatusLine().getStatusCode());
        } catch (UnsupportedCharsetException e) {
            LOGGER.error("send post error: ", e);
        } catch (ClientProtocolException e) {
            LOGGER.error("send post error: ", e);
        } catch (IOException e) {
            LOGGER.error("send post error: ", e);
        } finally {
            IOUtils.closeQuietly(httpClient);
        }
    }
}
