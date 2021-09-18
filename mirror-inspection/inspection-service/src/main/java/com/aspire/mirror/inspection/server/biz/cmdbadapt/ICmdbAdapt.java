package com.aspire.mirror.inspection.server.biz.cmdbadapt;

import java.util.List;

/**
* cmdb业务接口   <br/>
* Project Name:inspection-service
* File Name:ICmdbAdapt.java
* Package Name:com.aspire.mirror.inspection.server.biz.cmdbadapt
* ClassName: ICmdbAdapt <br/>
* date: 2018年9月12日 下午2:01:58 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
public interface ICmdbAdapt {
	
	/**
	* 查询指定id的cmdb数据. <br/>
	*
	* 作者： pengguihua
	* @param joinedCmdbIds
	* @return
	*/  
	public List<CommonCmdbDevice> listCmdbDeviceByIds(String joinedCmdbIds);
	
	/**
	* 查询用户信息. <br/>
	*
	* 作者： pengguihua
	* @param uidList
	* @return
	*/  
	public List<CommonUserInfo> queryUserInfoListByIds(List<String> uidList);
}
