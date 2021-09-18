package com.aspire.mirror.common.auth;

/**
* 权限约束自检接口   <br/>
* Project Name:composite-api
* File Name:IAuthConstraintsCheck.java
* Package Name:com.aspire.mirror.composite.service.auth
* ClassName: IAuthConstraintsCheck <br/>
* date: 2019年3月27日 下午3:52:20 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
public interface IAuthConditionsSelfCheck {
	public boolean checkAuthConditions();
}
