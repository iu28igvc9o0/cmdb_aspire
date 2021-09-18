package com.aspire.webbas.epm.core.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称:  [webbas-component-epm]
 * 包名:      [com.aspire.webbas.epm.core.pojo]
 * 类名称:    [Interceptor]
 * 类描述:    [环节拦截对象]
 * 创建人:    [王磊]
 * 创建时间:  [2014年8月29日 上午11:08:16]
 */
public class Interceptor {
	/**
	 * 拦截处理类名称
	 */
	private String bean;

	/**
	 * 参数
	 */
	private Map<String, String> map = new HashMap<String, String>();

	/**
	 * @return .
	 */
	public String getBean() {
		return bean;
	}

	/**
	 * @param bean .
	 */
	public void setBean(String bean) {
		this.bean = bean;
	}

	/**
	 * @return .
	 */
	public Map<String, String> getMap() {
		return map;
	}

	/**
	 * @param map .
	 */
	public void setMap(Map<String, String> map) {
		this.map = map;
	}

}
