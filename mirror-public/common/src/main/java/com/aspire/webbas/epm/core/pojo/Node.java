package com.aspire.webbas.epm.core.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:  [webbas-component-epm]
 * 包名:      [com.aspire.webbas.epm.core.pojo]
 * 类名称:    [Node]
 * 类描述:    [流程节点对象]
 * 创建人:    [王磊]
 * 创建时间:  [2014年8月29日 上午11:07:36]
 */
public class Node {
	/**
	 * 节点状态
	 */
	private String status;

	/**
	 * 目标流程节点
	 */
	//private String to;

	/**
	 * 流程类型
	 * 分为fork和join，当类型为fork时每个并发流程会根据choice分叉到不同的环节
	 * 对于节点而言，一般是join
	 */
	private String processType;

	/** 拦截器列表 */
	private List<Interceptor> interceptorList = new ArrayList<Interceptor>();

	/** 选择器列表 */
	private List<Choice> choiceList = new ArrayList<Choice>();

	/** 节点参数 */
	private Map<String, Object> params = new HashMap<String, Object>();

	/**
	 * @return .
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status .
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return .
	 */
	public String getProcessType() {
		return processType;
	}

	/**
	 * @param processType .
	 */
	public void setProcessType(String processType) {
		this.processType = processType;
	}

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
	public List<Choice> getChoiceList() {
		return choiceList;
	}

	/**
	 * @param choiceList .
	 */
	public void setChoiceList(List<Choice> choiceList) {
		this.choiceList = choiceList;
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
