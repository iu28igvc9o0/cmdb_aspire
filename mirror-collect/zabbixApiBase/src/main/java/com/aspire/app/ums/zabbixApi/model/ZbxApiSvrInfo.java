package com.aspire.app.ums.zabbixApi.model;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/** 
 *
 * 项目名称: zabbixApiBase 
 * <p/>
 * 
 * 类名: ApiServerInfo
 * <p/>
 *
 * 类功能描述: ZABBIX Server 配置信息
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2016年5月19日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2016 卓望公司-版权所有 
 *
 */
public class ZbxApiSvrInfo {
	@XStreamAsAttribute
	private String id;
	@XStreamAsAttribute
	private String url;
	@XStreamAsAttribute
	private String userName;
	@XStreamAsAttribute
	private String password;
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
}
