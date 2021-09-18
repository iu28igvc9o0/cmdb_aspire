package com.aspire.cmdb.agent.util;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监控-Http请求工具类
 * <p>
 * 项目名称: 微服务运维平台 包: com.aspire.mirror.alert.server.util 类名称: HttpUtils.java 类描述:
 * 监控-Http请求工具类 创建人: LiangJun 创建时间: 2019-06-05 14:17:17
 */
public class HttpUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);
	private static final String HTTP_CHARSET = "UTF-8";

	public static CommonHttpResponse httpPut(final String param, final String url, final String token
			,String tokenHeaderName) {
		CommonHttpResponse metricHttpResponse = new CommonHttpResponse();
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		//LOGGER.info("Put info:{},{},{}",param,url,token);
		try {
			final HttpPut httpPut = new HttpPut(url);
			if (!StringUtils.isEmpty(token)) {
				httpPut.addHeader(tokenHeaderName, token);
			}
			httpPut.setHeader("Content-Type", "application/json");
			final StringEntity entity = new StringEntity(param, HTTP_CHARSET);
			httpPut.setEntity(entity);
			final HttpResponse httpResponse = httpClient.execute(httpPut);
			int code = httpResponse.getStatusLine().getStatusCode();
			metricHttpResponse.setStatus(code);
			metricHttpResponse.setContent(IOUtils.toString(httpResponse.getEntity().getContent(), HTTP_CHARSET));
		
			LOGGER.info("Put Response Status: " + httpResponse.getStatusLine().getStatusCode());
		} catch (Exception e) {
			metricHttpResponse.setContent(e.getClass().getName() + ":" + e.getMessage().toString());
			metricHttpResponse.setResponsed(false);
			LOGGER.error("send put error: ", e);
		} finally {
			IOUtils.closeQuietly(httpClient);
		}
		return metricHttpResponse;
	}

	public static CommonHttpResponse httpGet(final String url, final String token,String tokenHeaderName) {
		//LOGGER.info("Get info:{},{}",url,token);
		CommonHttpResponse metricHttpResponse = new CommonHttpResponse();
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {
			URL url1 = new URL(url);			
			URI uri = new URI(url1.getProtocol(), url1.getAuthority(), url1.getPath(), url1.getQuery(), null);
			final HttpGet httpGet = new HttpGet(uri);
			if (!StringUtils.isEmpty(token)) {
				httpGet.addHeader(tokenHeaderName, token);
			}
			httpGet.setHeader("Content-Type", "application/json");
			httpGet.setHeader("Accept", "application/json");
			httpGet.setHeader("Accept-Charset", "utf-8");
			response = httpClient.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			metricHttpResponse.setStatus(code);
			metricHttpResponse.setContent(IOUtils.toString(response.getEntity().getContent(), HTTP_CHARSET));
			
		} catch (Exception e) {
			metricHttpResponse.setContent(e.getClass().getName() + ":" + e.getMessage().toString());
			metricHttpResponse.setResponsed(false);
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

	public static CommonHttpResponse httpPost(final String url, final String token, final String entityStr
			,String tokenHeaderName) {
		//LOGGER.debug("post info:{},{},{}",entityStr,url,token);
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		CommonHttpResponse metricHttpResponse = new CommonHttpResponse();
		HttpResponse httpResponse = null;
		try {
			final HttpPost httpPost = new HttpPost(url);
			if (!StringUtils.isEmpty(token)) {
				httpPost.addHeader(tokenHeaderName, token);
			}
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Accept-Charset", "utf-8");
			final StringEntity entity = new StringEntity(entityStr, HTTP_CHARSET);
			httpPost.setEntity(entity);

			httpResponse = httpClient.execute(httpPost);

			int code = httpResponse.getStatusLine().getStatusCode();
			metricHttpResponse.setStatus(code);
			metricHttpResponse.setContent(IOUtils.toString(httpResponse.getEntity().getContent(), HTTP_CHARSET));
			
			
		}catch (Exception e) {
			metricHttpResponse.setContent(e.getClass().getName() + ":" + e.getMessage().toString());
			metricHttpResponse.setResponsed(false);
			LOGGER.error("send post error: ", e);
		} finally {
			IOUtils.closeQuietly(httpClient);
		}
		return metricHttpResponse;
	}

	/**
	 * HttpGET:get请求公用方法
	 *
	 * @param url
	 * @param param
	 * @return com.migu.tsg.microservice.monitor.manage.api.dto.MetricHttpResponse
	 * @method doGet @Author： LiangJun
	 * @Date: 2017/9/26 13:52
	 */
	public static CommonHttpResponse doGet(String url, Map<String, String> param) {
		CommonHttpResponse metricHttpResponse = new CommonHttpResponse();
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
	 * @method doGet @Author： LiangJun
	 * @Date: 2019-06-05 14:17:17
	 */
	public static CommonHttpResponse doGet(String url) {
		return doGet(url, null);
	}

	/* *//**
			 * HttpPut:put请求公用方法 <br/>
			 * 作者： jiangfuyi
			 *
			 * @param param 参数
			 * @param url   url地址
			 * @param token token
			 */
	public static void doHttpPut(final String param, final String url, final String userAgent, final String token) {
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			final HttpPut httpPut = new HttpPut(url);
			httpPut.setHeader("Content-Type", "application/json");
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
	
	
	private static CloseableHttpClient getHttpsClient() {
		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
		ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
		registryBuilder.register("http", plainSF); // 指定信任密钥存储对象和连接套接字工厂
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType()); // 信任任何链接
			TrustStrategy anyTrustStrategy = new TrustStrategy() {

				@Override
				public boolean isTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException { // TODO Auto-generated method stub
					return true;
				}
			};
			SSLConnectionSocketFactory.getSocketFactory();
			SSLConnectionSocketFactory sslSF = SSLConnectionSocketFactory.getSocketFactory();
			
			

			registryBuilder.register("https", sslSF);
		} catch (

		KeyStoreException e) {
			throw new RuntimeException(e);
		}
		Registry<ConnectionSocketFactory> registry = registryBuilder.build(); // 设置连接管理器
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry); // 构建客户端
		return HttpClientBuilder.create().setConnectionManager(connManager).build();
	}
	
	
	/*
	 * private static SSLContext createIgnoreVerifySSL() throws
	 * NoSuchAlgorithmException, KeyManagementException { SSLContext sc =
	 * SSLContext.getInstance("SSLv3");
	 * 
	 * // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法 X509TrustManager trustManager =
	 * new X509TrustManager() {
	 * 
	 * @Override public void checkClientTrusted(java.security.cert.X509Certificate[]
	 * paramArrayOfX509Certificate, String paramString) throws CertificateException
	 * { }
	 * 
	 * @Override public void checkServerTrusted(java.security.cert.X509Certificate[]
	 * paramArrayOfX509Certificate, String paramString) throws CertificateException
	 * { }
	 * 
	 * @Override public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	 * return null; } };
	 * 
	 * sc.init(null, new TrustManager[] { trustManager }, null); return sc; }
	 */
	/*
	 * private static CloseableHttpClient getHttpsClient2() {
	 * RegistryBuilder<ConnectionSocketFactory> registryBuilder =
	 * RegistryBuilder.<ConnectionSocketFactory>create(); ConnectionSocketFactory
	 * plainSF = new PlainConnectionSocketFactory();
	 * registryBuilder.register("http", plainSF); // 指定信任密钥存储对象和连接套接字工厂 try {
	 * KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType()); //
	 * 信任任何链接 TrustStrategy anyTrustStrategy = new TrustStrategy() {
	 * 
	 * @Override public boolean isTrusted(java.security.cert.X509Certificate[] arg0,
	 * String arg1) throws java.security.cert.CertificateException { // TODO
	 * Auto-generated method stub return true; } }; SSLContext sslContext =
	 * SSLContexts.custom().useTLS().loadTrustMaterial(trustStore,
	 * anyTrustStrategy).build(); LayeredConnectionSocketFactory sslSF = new
	 * SSLConnectionSocketFactory(sslContext,
	 * SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	 * registryBuilder.register("https", sslSF); } catch (KeyStoreException e) {
	 * throw new RuntimeException(e); } catch (KeyManagementException e) { throw new
	 * RuntimeException(e); } catch (NoSuchAlgorithmException e) { throw new
	 * RuntimeException(e); } Registry<ConnectionSocketFactory> registry =
	 * registryBuilder.build(); // 设置连接管理器 PoolingHttpClientConnectionManager
	 * connManager = new PoolingHttpClientConnectionManager(registry); // 构建客户端
	 * return HttpClientBuilder.create().setConnectionManager(connManager).build();
	 * }
	 */
}
