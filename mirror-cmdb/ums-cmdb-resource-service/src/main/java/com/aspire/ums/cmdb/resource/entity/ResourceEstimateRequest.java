package com.aspire.ums.cmdb.resource.entity;

/**
 * 项目名称:
 * 包: com.aspire.birp.modules.osa.web.controller.payload
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2018/9/29 17:16
 * 版本: v1.0
 */
public class ResourceEstimateRequest {

	private String collect_id;
	private String estimate_id;
	
	public String getCollect_id() {
		return collect_id;
	}
	
	public void setCollect_id(String collect_id) {
		this.collect_id = collect_id;
	}
	
	public String getEstimate_id() {
		return estimate_id;
	}
	
	public void setEstimate_id(String estimate_id) {
		this.estimate_id = estimate_id;
	}
}
