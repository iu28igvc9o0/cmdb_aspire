package com.migu.tsg.microservice.atomicservice.composite.helper;

import java.util.Map;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.aspire.mirror.composite.service.order.payload.PageBean;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;

@Component
public class BpmCallHelper {

	@Autowired
	BpmTokenHelper bpmTokenHelper;

	public Object getCallUrl(String bpmUrl, Map<String, Object> params) {
		HttpHeaders requestHeaders = new HttpHeaders();
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		String account = authCtx.getUser().getUsername();
		if("admin".equals(account)){
			String token = bpmTokenHelper.getToken();
			requestHeaders.add("Authorization", "Basic " + token);
		}else{
			String token = bpmTokenHelper.getTokenByUser();
			requestHeaders.add("Authorization", "Bearer " + token);
		}
		requestHeaders.add("Content-Type", "application/json;charset=utf-8");
		RestTemplate restTemplate = new RestTemplate();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(bpmUrl);
		params.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(), o.getValue()));
		String url = builder.build().encode().toString();
		@SuppressWarnings("rawtypes")
		HttpEntity formErntity = new HttpEntity<>(params, requestHeaders);
		Object result = restTemplate.exchange(url, HttpMethod.GET, formErntity, Object.class, params);
		return result;
	}

	public Object postCallUrlWithPageBean(String bpmUrl, PageBean pageBean) {
		String token = bpmTokenHelper.getToken();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Authorization", "Bearer " + token);
		requestHeaders.add("Content-Type", "application/json;charset=utf-8");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<PageBean> formErntity = new HttpEntity<>(pageBean, requestHeaders);
		Object result = restTemplate.exchange(bpmUrl, HttpMethod.POST, formErntity, Object.class, pageBean);
		return result;
	}
	
	public Object postCallUrl(String bpmUrl, Object type) {
		HttpHeaders requestHeaders = new HttpHeaders();
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		String account = authCtx.getUser().getUsername();
		if("admin".equals(account)){
			String token = bpmTokenHelper.getToken();
			requestHeaders.add("Authorization", "Basic " + token);
		}else{
			String token = bpmTokenHelper.getTokenByUser();
			requestHeaders.add("Authorization", "Bearer " + token);
		}
		requestHeaders.add("Content-Type", "application/json;charset=utf-8");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Object> formErntity = new HttpEntity<>(type, requestHeaders);
		Object result = restTemplate.exchange(bpmUrl, HttpMethod.POST, formErntity, Object.class, type);
		return result;
	}
}
