package com.aspire.mirror.common.auth;

import java.util.List;

/**
* 区域--设备类型--设备设备   三层公共权限感应接口, 原子层所有执行资源查询的model, 涉及到这3层权限约束的，都需要实现该接口    <br/>
* Project Name:common-api
* File Name:IGeneralAuthConstraintsAware.java
* Package Name:com.aspire.mirror.common.auth
* ClassName: IGeneralAuthConstraintsAware <br/>
* date: 2019年3月30日 下午2:09:14 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
public interface IGeneralAuthConstraintsAware {
	
	/**
	* 获取帐号赋权的区域id列表. <br/>
	*
	* 作者： pengguihua
	* @return
	*/  
	public List<String> getAuthPrecinctList();
	
	/**
	* 设置赋权的区域id列表. <br/>
	*
	* 作者： pengguihua
	* @param authPrecintList
	*/  
	public void setAuthPrecinctList(List<String> authPrecintList);
	
	/**
	* 获取帐号赋权的设备类型列表. <br/>
	*
	* 作者： pengguihua
	* @return
	*/  
	public List<String> getAuthDeviceTypeList();
	
	/**
	* 设置赋权的设备类型列表. <br/>
	*
	* 作者： pengguihua
	* @param authDeviceTypeList
	*/  
	public void setAuthDeviceTypeList(List<String> authDeviceTypeList);
	
	/**
	* 获取帐号赋权的特殊设备id列表. <br/>
	*
	* 作者： pengguihua
	* @return
	*/  
	public List<String> getAuthDeviceIdList();
	
	/**
	* 设置赋权的设备id列表. <br/>
	*
	* 作者： pengguihua
	* @param authDeviceIdList
	*/  
	public void setAuthDeviceIdList(List<String> authDeviceIdList);

	List<String> getAuthPipelineIdList();

	void setAuthPipelineIdList(List<String> authPipelineIdList);

	List<String> getAuthScriptIdList();

	void setAuthScriptIdList(List<String> authScriptIdList);

	List<String> getAuthYumIdList();

	void setAuthYumIdList(List<String> authYumIdList);

	List<String> getAuthGroupIdList();

	void setAuthGroupIdList(List<String> authGroupIdList);

	public List<String> getAuthBizSystemIdList();

	public void setAuthBizSystemIdList(List<String> authBizSystemIdList);

	public List<String> getAuthIdcIdList();

	public void setAuthIdcIdList(List<String> authIdcIdList) ;
}
