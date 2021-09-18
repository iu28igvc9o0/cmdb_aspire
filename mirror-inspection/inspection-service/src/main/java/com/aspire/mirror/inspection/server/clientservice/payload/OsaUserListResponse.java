package com.aspire.mirror.inspection.server.clientservice.payload;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.aspire.mirror.inspection.server.biz.cmdbadapt.CommonUserInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* osa_web用户信息获取响应model   <br/>
* Project Name:inspection-service
* File Name:OsaUserListResponse.java
* Package Name:com.aspire.mirror.inspection.server.clientservice.payload
* ClassName: OsaUserListResponse <br/>
* date: 2018年11月27日 下午6:02:38 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
@JsonInclude(Include.NON_NULL)
public class OsaUserListResponse {
	private boolean result;
	private boolean success;
	private String msg;
	@JsonProperty("data")
	private List<OsaUserInfo> userList = new ArrayList<>();
	
	@Data
	@JsonInclude(Include.NON_NULL)
	public static class OsaUserInfo {
		@JsonProperty("id")
		private String id;
		private String name;
		private String email;
	}
	
	public List<CommonUserInfo> parse() {
		List<CommonUserInfo> resultList = new ArrayList<>();
		if (CollectionUtils.isEmpty(userList)) {
			return resultList;
		}
		for (OsaUserInfo user : userList) {
			CommonUserInfo userInfo = new CommonUserInfo();
			userInfo.setUsername(user.getName());
			userInfo.setEmail(user.getEmail());
			resultList.add(userInfo);
		}
		return resultList;
	}
}
