package com.aspire.app.ums.http;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class HttpClientService {  
	private static final Logger logger = LoggerFactory.getLogger(HttpClientService.class);
	private final String targetURL;
	
	public HttpClientService(String targetURL) {
		this.targetURL = targetURL;
	}
	
	/** 
	 * 功能描述: 发起post请求，获取响应内容字符串  
	 * <p>
	 * @param postContent
	 * @return
	 */
	public String postJson(String postContent) {
		return postJson(postContent, getDefaultHandler());
	}
	
	/** 
	 * 功能描述: 发起post请求，获取响应内容字符串  
	 * <p>
	 * @param postContent
	 * @return
	 */
	public String postJson(JSONObject postContent) {
		return postJson(postContent.toString(), getDefaultHandler());
	}
	
	public <T> T postJson(JSONObject postContent, ResponseHandler<T> handler) {
		return postJson(postContent.toString(), handler);
	}
	
	/** 
	 * 功能描述: TODO  
	 * <p>
	 * @param postContent
	 * @param handler
	 * @return
	 */
	public <T> T postJson(String postContent, ResponseHandler<T> handler) {
		HttpPost method = new HttpPost(targetURL);
		method.addHeader("Content-type","application/json; charset=utf-8");  
        method.setHeader("Accept", "application/json");  
        method.setEntity(new StringEntity(postContent, Charset.forName("UTF-8")));  

        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3000).setConnectionRequestTimeout(3000)  
        		.setSocketTimeout(20000).build(); 
        method.setConfig(requestConfig);
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
			T result = httpClient.execute(method, handler);
			logger.debug("request detail: url=" + targetURL + "|requestObj=" + postContent + "|result=" + result);
			return result;
		} catch (Exception e) {
			logger.error(null, e);
		} finally {
			HttpClientUtils.closeQuietly(httpClient);
		}
		return null;
	}
	
	public static ResponseHandler<String> getDefaultHandler() {
		return new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode >= 200 && statusCode < 300) {
					HttpEntity respEntity = response.getEntity();
					if (respEntity == null) {
						return null;
					}
					return EntityUtils.toString(respEntity);
				}
				logger.warn("The response status code is abnormal: " + statusCode);
				return null;
			}
		};
	}
	
	/** 
	 * 功能描述: 获取JSON转换器  
	 * <p>
	 * @return
	 */
	public static ResponseHandler<JSONObject> getResponseJsonHandler() {
		return new ResponseHandler<JSONObject>() {
			@Override
			public JSONObject handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode >= 200 && statusCode < 300) {
					HttpEntity respEntity = response.getEntity();
					if (respEntity == null) {
						return null;
					}
					String respCont = EntityUtils.toString(respEntity);
					try {
						respCont = respCont.replace("<pre>", "");
						return JSONObject.fromObject(respCont);
					} catch (Exception e) {
						logger.error("Failed to parse String(" +  respCont + ") to JSONObject", e);
					}
				}
				logger.warn("The response status code is abnormal: " + statusCode);
				return null;
			}
		};
	}
}  
