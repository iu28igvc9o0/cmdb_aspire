package com.aspire.mirror.inspection.server.clientservice;

import java.util.List;

import com.aspire.mirror.inspection.server.clientservice.payload.OsaCmdbDetail;
import com.aspire.mirror.inspection.server.clientservice.payload.OsaUserListResponse;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
* osa系统CMDB服务接口    <br/>
* Project Name:inspection-service
* File Name:InnerCmdbApiClient.java
* Package Name:com.aspire.mirror.inspection.server.clientservice
* ClassName: InnerCmdbApiClient <br/>
* date: 2018年8月27日 下午5:57:42 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
public interface OsaCmdbApiClient {
	public static final String	H_CONTENT_TYPE	= "Content-Type: application/json;charset=UTF-8";
	public static final String	H_ACCEPT		= "Accept: application/json;charset=UTF-8";
	
	/**
	* 使用指定的设备id, 查询设备详情, 多个设备id参数使用英文逗号分隔. <br/>
	*
	* 作者： pengguihua
	* @param joinedIds
	* @return
	*/ 
	@RequestLine("GET /outerCmdbModel/listDevicesByJoinedIds/{device_ids}")
	@Headers({H_CONTENT_TYPE, H_ACCEPT, "authToken: {authToken}"})
	List<OsaCmdbDetail> listDeviceDetailsByIdArr(
			@Param("device_ids") String joinedIds, @Param("authToken") String authToken);
	
	/**
	* 根据指定userid, 查询用户信息. <br/>
	*
	* 作者： pengguihua
	* @param joinedIds
	* @return
	*/ 
	@RequestLine("GET /user/queryUserInfo/{authToken}?params={userIds}")
	@Headers({H_CONTENT_TYPE, H_ACCEPT})
	OsaUserListResponse queryUserInfoList(
			@Param("authToken") String authToken, @Param("userIds") String joinUserIds);
}
