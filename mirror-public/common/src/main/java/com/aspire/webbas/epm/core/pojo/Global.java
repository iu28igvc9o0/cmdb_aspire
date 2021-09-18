package com.aspire.webbas.epm.core.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:  [webbas-component-epm]
 * 包名:      [com.aspire.webbas.epm.core.pojo]
 * 类名称:    [Global]
 * 类描述:    [一句话描述该类的功能]
 * 创建人:    [王磊]
 * 创建时间:  [2015年1月4日 下午2:16:30]
 */
public class Global {
	/** 全局拦截器列表 */
	private List<Interceptor> interceptorList = new ArrayList<Interceptor>();

	/** 全局配置参数 */
	private Map<String, Object> params = new HashMap<String, Object>();

	/**
	 * @return .
	 */
	public List<Interceptor> getInterceptorList() {
		return interceptorList;
	}

	/**
	 * @param interceptorList .
	 */
	public void setInterceptorList(List<Interceptor> interceptorList) {
		this.interceptorList = interceptorList;
	}

	/**
	 * @return .
	 */
	public Map<String, Object> getParams() {
		return params;
	}

	/**
	 * @param params .
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
