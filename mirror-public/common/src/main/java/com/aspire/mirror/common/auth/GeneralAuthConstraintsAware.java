package com.aspire.mirror.common.auth;


import org.springframework.util.CollectionUtils;

import java.util.List;

/**
* 封装层抽象类: 封装通用权限处理  <br/>
* Project Name:composite-api
* File Name:GeneralAuthConstraintsAware.java
* Package Name:com.aspire.mirror.composite.service.common
* ClassName: GeneralAuthConstraintsAware <br/>
* date: 2019年3月30日 下午3:15:26 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
public class GeneralAuthConstraintsAware implements IGeneralAuthConstraintsAware, IAuthConditionsSelfCheck {
	// 权限字段
	private List<String>	authPrecinctList;
	private List<String>	authDeviceTypeList;
	private List<String>	authDeviceIdList;
	private List<String> 	authBizSystemIdList;
	private List<String>	authIdcIdList;

	private List<String> authPipelineIdList;
	private List<String> authScriptIdList;
	private List<String> authYumIdList;
	private List<String> authGroupIdList;
	@Override
	public List<String> getAuthPipelineIdList() {
		return authPipelineIdList;
	}
	@Override
	public void setAuthPipelineIdList(List<String> authPipelineIdList) {
		this.authPipelineIdList = authPipelineIdList;
	}
	@Override
	public List<String> getAuthScriptIdList() {
		return authScriptIdList;
	}
	@Override
	public void setAuthScriptIdList(List<String> authScriptIdList) {
		this.authScriptIdList = authScriptIdList;
	}
	@Override
	public List<String> getAuthYumIdList() {
		return authYumIdList;
	}
	@Override
	public void setAuthYumIdList(List<String> authYumIdList) {
		this.authYumIdList = authYumIdList;
	}
	@Override
	public List<String> getAuthGroupIdList() {
		return authGroupIdList;
	}

	@Override
	public void setAuthGroupIdList(List<String> authGroupIdList) {
		this.authGroupIdList = authGroupIdList;
	}

	@Override
	public List<String> getAuthBizSystemIdList() {
		return authBizSystemIdList;
	}
	@Override
	public void setAuthBizSystemIdList(List<String> authBizSystemIdList) {
		this.authBizSystemIdList = authBizSystemIdList;
	}

	@Override
	public boolean checkAuthConditions() {
		return !CollectionUtils.isEmpty(authPrecinctList) || !CollectionUtils.isEmpty(authDeviceTypeList)
				|| !CollectionUtils.isEmpty(authDeviceIdList) || !CollectionUtils.isEmpty(authBizSystemIdList)
				|| !CollectionUtils.isEmpty(authGroupIdList)  || !CollectionUtils.isEmpty(authPipelineIdList)
				|| !CollectionUtils.isEmpty(authScriptIdList) || !CollectionUtils.isEmpty(authYumIdList);
	}
	
	@Override
	public List<String> getAuthPrecinctList() {
		return authPrecinctList;
	}
	
	@Override
	public void setAuthPrecinctList(List<String> authPrecintList) {
		this.authPrecinctList = authPrecintList;
	}
	
	@Override
	public List<String> getAuthDeviceTypeList() {
		return authDeviceTypeList;
	}
	
	@Override
	public void setAuthDeviceTypeList(List<String> authDeviceTypeList) {
		this.authDeviceTypeList = authDeviceTypeList;
	}
	
	@Override
	public List<String> getAuthDeviceIdList() {
		return authDeviceIdList;
	}
	
	@Override
	public void setAuthDeviceIdList(List<String> authDeviceIdList) {
		this.authDeviceIdList = authDeviceIdList;
	}

	@Override
	public List<String> getAuthIdcIdList() {
		return authIdcIdList;
	}
	@Override
	public void setAuthIdcIdList(List<String> authIdcIdList) {
		this.authIdcIdList = authIdcIdList;
	}
}
