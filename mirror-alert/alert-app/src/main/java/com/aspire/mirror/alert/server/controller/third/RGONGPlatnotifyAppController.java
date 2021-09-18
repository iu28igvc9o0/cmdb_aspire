package com.aspire.mirror.alert.server.controller.third;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.charset.UnsupportedCharsetException;
import java.text.ParseException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.aspire.mirror.alert.api.dto.third.*;
import com.aspire.mirror.alert.server.util.IpUtils;
import com.aspire.mirror.alert.server.biz.helper.AlertsHandleV2Helper;
import com.aspire.mirror.alert.server.vo.alert.AlertsV2Vo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.api.service.third.RGONGPlatnotifyAppService;
import com.aspire.mirror.alert.server.biz.helper.CmdbHelper;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.google.common.collect.Lists;

@RestController
public class RGONGPlatnotifyAppController implements RGONGPlatnotifyAppService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RGONGPlatnotifyAppController.class);
	private static final String HTTP_CHARSET = "UTF-8";
	@Autowired
	private AlertsHandleV2Helper alertHandleHelper;
	@Autowired
	private CmdbHelper cmdbHelper;

	@Value("${RG_ONG.SOURCE_URL}")
	private String SOURCE_URL;
	
	@Value("${RG_ONG.TARGET_URL}")
	private String TARGET_URL;
	
	@Value("${RG_ONG.PORT}")
	private int PORT;
	
	@Value("${RG_ONG.USERNAME}")
	private String RG_ONG_USERNAME;

	@Value("${RG_ONG.PASSWORD}")
	private String RG_ONG_PASSWORD;

	@Override
	public CommonResp registerPlatnotifyApp(@RequestBody PlatnotifyAppReq thirdCreateAlertReq,
											@RequestParam(value = "ip") String ip) throws Exception {
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse httpResponse = null;
		// String url =
		// "http://%s:8181/restconf/operations/platnotify:platnotify:register-platnotify-app";
		String url = String.format(TARGET_URL, ip, "register-platnotify-app");
		try {
			final HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json");
			if(StringUtils.isEmpty(thirdCreateAlertReq.getInput().getUrl())) {
				thirdCreateAlertReq.getInput().setUrl(SOURCE_URL);
			}
			if(org.apache.commons.lang.StringUtils.isBlank(thirdCreateAlertReq.getInput().getAuth_type())) {
				thirdCreateAlertReq.getInput().setAuth_type("NONE");
			}
			LOGGER.info("url:{};obj:{}",url,JSON.toJSONString(thirdCreateAlertReq));
			final StringEntity entity = new StringEntity(JSON.toJSONString(thirdCreateAlertReq), HTTP_CHARSET);
			//entity.setContentType("application/json");
			httpPost.setEntity(entity);
			
			HttpHost targetHost = new HttpHost(ip, PORT, "http");
			HttpClientContext context = HttpClientContext.create();
			setAuth(targetHost,context);
			httpResponse = httpClient.execute(targetHost, httpPost, context);
			
             
			int code = httpResponse.getStatusLine().getStatusCode();
			CommonResp res = new CommonResp();
			res.setCode(code + "");
			if (code == 400) {
				res.setMessage("输入为空 或者通告对象名为空或者通告对象名非法或者url地址为空或者url地址非法");
			}
			if (code == 409) {
				res.setMessage("通告对象已存在");
			}
			if (code == 500) {
				res.setMessage("操作失败");
			}
			LOGGER.info("POST Response Status:" + httpResponse.getStatusLine().getStatusCode());
			return res;
		} catch (UnsupportedCharsetException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		} catch (ClientProtocolException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		} catch (IOException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		}finally {
			IOUtils.closeQuietly(httpClient);
		}
	}
	
	void setAuth(HttpHost targetHost,HttpClientContext context){
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
		        new AuthScope(targetHost.getHostName(), targetHost.getPort()),
		        new UsernamePasswordCredentials(RG_ONG_USERNAME, RG_ONG_PASSWORD));
		 
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);
		 
		// Add AuthCache to the execution context
		context.setCredentialsProvider(credsProvider);
		context.setAuthCache(authCache);
	}

	@Override
	public List<PlatnotifyApp> getPlatnotifyApp(@RequestParam(value = "ip") String ip) throws Exception {
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse httpResponse = null;
		// String url =
		// "http://%s:8181/restconf/operations/platnotify:platnotify:register-platnotify-app";
		String url = String.format(TARGET_URL, ip, "get-platnotify-apps");
		LOGGER.info("url:{}",url);
		StringBuffer result = new StringBuffer();
		BufferedReader in = null;
		try {
			final HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json");
			
			HttpHost targetHost = new HttpHost(ip, PORT, "http");
			HttpClientContext context = HttpClientContext.create();
			setAuth(targetHost,context);
			httpResponse = httpClient.execute(targetHost,httpPost,context);
			
			int code = httpResponse.getStatusLine().getStatusCode();
			List<PlatnotifyApp> list = Lists.newArrayList();
			if (code != 200) {
				return list;
			}
			in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
			String rs = result.toString();
			JSONObject js = JSON.parseObject(rs);
			JSONObject jss = js.getJSONObject("output");
			if(null == jss) {
				return list;
			}
			JSONArray jsa = jss.getJSONArray("all-platnotify-register-apps");
			if(null!=jsa && jsa.size()>0) {
				list = jsa.toJavaList(PlatnotifyApp.class);
			}
			return list;
		} catch (UnsupportedCharsetException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		} catch (ClientProtocolException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		} catch (IOException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			IOUtils.closeQuietly(httpClient);
		}
	}

	@Override
	public PlatnotifyApp getByName(@RequestParam(value = "app_name") String app_name,
			@RequestParam(value = "ip") String ip) throws Exception {

		final CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse httpResponse = null;
		// String url =
		// "http://%s:8181/restconf/operations/platnotify:platnotify:register-platnotify-app";
		String url = String.format(TARGET_URL, ip, "get-platnotify-app");
		
		StringBuffer result = new StringBuffer();
		BufferedReader in = null;
		try {
			//url += "?app_name=" + app_name;
			final HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json");
			JSONObject appob = new JSONObject();
			JSONObject ob = new JSONObject();
			appob.put("app_name", app_name);
			ob.put("input", appob);
			
			LOGGER.info("url:{};input:{}",url,ob.toJSONString());
			final StringEntity entity = new StringEntity(ob.toJSONString(), HTTP_CHARSET);
			httpPost.setEntity(entity);
			
			
			HttpHost targetHost = new HttpHost(ip, PORT, "http");
			HttpClientContext context = HttpClientContext.create();
			setAuth(targetHost,context);
			httpResponse = httpClient.execute(targetHost,httpPost,context);
			
			
			int code = httpResponse.getStatusLine().getStatusCode();
			if (code != 200) {
				String message = "操作失败,返回码:"+code;
				if (code == 400) {
					message = "通告对象名非法或者通告对象名为空";
				}
				throw new Exception(message);
			}
			in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
			String rs = result.toString();
			JSONObject js = JSON.parseObject(rs);
			JSONObject dataJson = js.getJSONObject("output");
			if(dataJson == null || dataJson.isEmpty() || "null".equals(dataJson)){
	               return null;
			}
			PlatnotifyApp jss = js.getObject("output", PlatnotifyApp.class);

			return jss;
		} catch (UnsupportedCharsetException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		} catch (ClientProtocolException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		} catch (IOException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			IOUtils.closeQuietly(httpClient);
		}

	}

	@Override
	public CommonResp update(@RequestBody PlatnotifyAppReq thirdCreateAlertReq, @RequestParam(value = "ip") String ip)
			throws Exception {
		final CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse httpResponse = null;
		// String url =
		// "http://%s:8181/restconf/operations/platnotify:platnotify:register-platnotify-app";
		String url = String.format(TARGET_URL, ip, "update-platnotify-app");
		LOGGER.info("url:{};obj:{}",url,JSON.toJSONString(thirdCreateAlertReq));
		try {
			final HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json");
			if(StringUtils.isEmpty(thirdCreateAlertReq.getInput().getUrl())) {
				thirdCreateAlertReq.getInput().setUrl(SOURCE_URL);
			}
			final StringEntity entity = new StringEntity(JSON.toJSONString(thirdCreateAlertReq), HTTP_CHARSET);
			httpPost.setEntity(entity);
			
			HttpHost targetHost = new HttpHost(ip, PORT, "http");
			HttpClientContext context = HttpClientContext.create();
			setAuth(targetHost,context);
			httpResponse = httpClient.execute(targetHost,httpPost,context);
			
			int code = httpResponse.getStatusLine().getStatusCode();
			CommonResp res = new CommonResp();
			res.setCode(code + "");
			if (code == 400) {
				res.setMessage("输入为空 或者通告对象名为空或者通告对象名非法或者url地址为空或者url地址非法");
			}
			if (code == 404) {
				res.setMessage("告对象不存在");
			}
			if (code == 500) {
				res.setMessage("操作失败");
			}
			LOGGER.info("POST Response Status:" + httpResponse.getStatusLine().getStatusCode());
			return res;
		} catch (UnsupportedCharsetException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		} catch (ClientProtocolException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		} catch (IOException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		}finally {
			IOUtils.closeQuietly(httpClient);
		}
	}

	@Override
	public CommonResp delete(@RequestParam(value = "app_name") String app_name, @RequestParam(value = "ip") String ip)
			throws Exception {

		final CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpResponse httpResponse = null;
		// String url =
		// "http://%s:8181/restconf/operations/platnotify:platnotify:register-platnotify-app";
		String url = String.format(TARGET_URL, ip, "unregister-platnotify-app");
		
		try {
			//url += "?app_name=" + app_name;
			final HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json");
			JSONObject appob = new JSONObject();
			JSONObject ob = new JSONObject();
			appob.put("app_name", app_name);
			ob.put("input", appob);
			final StringEntity entity = new StringEntity(ob.toJSONString(), HTTP_CHARSET);
			httpPost.setEntity(entity);
			
			LOGGER.info("url:{};input:{}",url,ob.toJSONString());
			HttpHost targetHost = new HttpHost(ip, PORT, "http");
			HttpClientContext context = HttpClientContext.create();
			setAuth(targetHost,context);
			httpResponse = httpClient.execute(targetHost,httpPost,context);
			
			int code = httpResponse.getStatusLine().getStatusCode();
			CommonResp res = new CommonResp();
			res.setCode(code + "");
			if (code == 400) {
				res.setMessage("输入为空 或者通告对象名为空或者通告对象名非法");
			}
			if (code == 404) {
				res.setMessage("告对象不存在");
			}
			if (code == 500) {
				res.setMessage("操作失败");
			}

			return res;
		} catch (UnsupportedCharsetException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		} catch (ClientProtocolException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		} catch (IOException e) {
			LOGGER.error("send post error:{}", e);
			throw e;
		}finally {
			IOUtils.closeQuietly(httpClient);
		}

	}

	@Override
	public CommonResp alertReport(@RequestBody List<PlatnotifyAppReportInfo> platnotifyAppReportList, HttpServletRequest request,
								  @PathVariable("level") String level) throws UnknownHostException {
		CommonResp result = new CommonResp();
		LOGGER.info("***************RG_ONG alertReport begin***************************************");
		String ip = IpUtils.getIpAddress(request);
		String idcType = cmdbHelper.getIdc(ip);
		LOGGER.info("RG_ONG alertReport platnotifyAppReportList:{}",JSON.toJSONString(platnotifyAppReportList));
		if(org.apache.commons.lang.StringUtils.isBlank(idcType)) {
			LOGGER.error("cancel insert alert of the null idcType: {} ", JSON.toJSONString(platnotifyAppReportList));
			result.setCode("405");
			result.setMessage("can not get idcType,maybe ip address do not config");
			return result;
		}
		if (StringUtils.isEmpty(level)) {
			LOGGER.error("cancel insert alert of the null alertLevel: {} ", JSON.toJSONString(platnotifyAppReportList));
			result.setCode("400");
			result.setMessage("the required parameter level is blank.");
			return result;
		}
		if(level.equals("INFO")) {
			level = "2";
		}else if(level.equals("WARN")) {
			level = "3";
		}else if(level.equals("ERROR")) {
			level = "4";
		}else {
			level = "1";
		}
		for(PlatnotifyAppReportInfo info:platnotifyAppReportList) {
			PlatnotifyAppReport platnotifyAppReport = info.getDevice_info();
			AlertsV2Vo dto = new AlertsV2Vo();
			dto.setAlertLevel(level);
			
			if (StringUtils.isEmpty(platnotifyAppReport.getMessage())) {
				LOGGER.error("cancel insert alert of the null MonitorIndex: {} ", JSON.toJSONString(platnotifyAppReport));
				result.setCode("400");
				result.setMessage("the required parameter message is blank.");
				return result;
			}
			dto.setMoniIndex(platnotifyAppReport.getMessage());
			//DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS");
			String[] times = new String[]{"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm:ss:SSS"};
			try {
				if (StringUtils.isNotEmpty(platnotifyAppReport.getTimestamp())) {
					dto.setCurMoniTime(DateUtils.parseDate(platnotifyAppReport.getTimestamp(), times));
					dto.setAlertStartTime(DateUtils.parseDate(platnotifyAppReport.getTimestamp(), times));
				}
			} catch (ParseException e) {
				LOGGER.error("tranform AlertStartTime error: {}", platnotifyAppReport.getTimestamp());
				result.setCode("404");
				result.setMessage("incorrect time format;yyyy-MM-dd HH:mm:ss:SSS");
				return result;
			}
			// 告警、消警
			dto.setDeviceIp(platnotifyAppReport.getDevice_ip());
			if (platnotifyAppReport.getAction().equals("ADD")) {
				dto.setAlertType(AlertsV2Vo.ALERT_ACTIVE);
			} else {
				dto.setAlertType(AlertsV2Vo.ALERT_REVOKE);
			}
	
			// dto.setDeviceType(platnotifyAppReport.getDevice_type());
			if (StringUtils.isEmpty(platnotifyAppReport.getSubject())) {
				LOGGER.error("cancel insert alert of the null MoniObject: {} ", JSON.toJSONString(platnotifyAppReport));
				result.setCode("400");
				result.setMessage("the required parameter Subject is blank.");
				return result;
			}
			
			dto.setItemId(platnotifyAppReport.getSubject()); // 后续均为保存微服务中定义的itemid
			dto.setMoniObject(platnotifyAppReport.getSubject());
			dto.setSource(AlertCommonConstant.RG_ONC_SOURCE);
	
			dto.setIdcType(idcType);
			dto.setObjectType(AlertsV2Vo.OBJECT_TYPE_DEVICE);
	
			if (StringUtils.isEmpty(dto.getDeviceIp())) {
				LOGGER.error("cancel insert alert of the null deviceIp: {} ", JSON.toJSONString(platnotifyAppReport));
				result.setCode("400");
				result.setMessage("the required parameter device_ip is blank.");
				return result;
			}
//			ObjectMapper objectMapper = new ObjectMapper();
//			String jsonString = "{}";
//			try {
//				jsonString = objectMapper.writeValueAsString(dto);
//			} catch (JsonProcessingException e) {
//			}
//			JSONObject alertJson = JSONObject.parseObject(jsonString);
//			List<AlertFieldRequestDTO> alertFieldList = alertHandleHelper.getModelField(AlertConfigConstants.REDIS_MODEL_ALERT);
//			cmdbHelper.queryDeviceForAlertV2(alertJson, alertFieldList,alertHandleHelper.getModelField(AlertConfigConstants.REDIS_MODEL_DEVICE_INSTANCE));
			try {
				LOGGER.info("alertHandleHelper.handleAlert:{}",JSON.toJSONString(dto));
				alertHandleHelper.handleAlert(dto);
			} catch (Exception e) {
				LOGGER.error("error {}", e);
				result.setCode("500");
				result.setMessage(e.getMessage());
				return result;
			}
		}
		return result;
	}

	String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
				}
			}
		} else if (ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		return ip;
	}
	
	@Override
	public SubjectsEnum[] getSubject() throws Exception {
		return SubjectsEnum.values();
	}
}
