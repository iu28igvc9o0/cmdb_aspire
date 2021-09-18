package com.aspire.mirror.inspection.server.biz.cmdbadapt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
* Cmdb对接适配入口    <br/>
* Project Name:inspection-service
* File Name:CmdbAdaptFacade.java
* Package Name:com.aspire.mirror.inspection.server.biz.cmdbadapt
* ClassName: CmdbAdaptFacade <br/>
* date: 2018年9月12日 下午2:41:54 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Primary
@Component
public class CmdbAdaptFacade implements ICmdbAdapt {
	@Autowired
	@Qualifier("cmdbAdaptImpl")
	private ICmdbAdapt cmdbImpl;

	@Override
	public List<CommonCmdbDevice> listCmdbDeviceByIds(String joinedCmdbIds) {
		return cmdbImpl.listCmdbDeviceByIds(joinedCmdbIds);
	}
	
	@Override
	public List<CommonUserInfo> queryUserInfoListByIds(List<String> uidList) {
		return cmdbImpl.queryUserInfoListByIds(uidList);
	}
}
