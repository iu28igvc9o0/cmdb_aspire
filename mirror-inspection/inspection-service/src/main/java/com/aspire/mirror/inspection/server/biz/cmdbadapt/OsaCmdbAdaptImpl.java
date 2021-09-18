package com.aspire.mirror.inspection.server.biz.cmdbadapt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.aspire.mirror.inspection.server.clientservice.OsaCmdbApiClient;
import com.aspire.mirror.inspection.server.clientservice.feign.ClientServiceBuilder;
import com.aspire.mirror.inspection.server.clientservice.payload.OsaCmdbDetail;
import com.aspire.mirror.inspection.server.clientservice.payload.OsaUserListResponse;
import com.aspire.mirror.inspection.server.util.OsaAuthTokenUtil;

/**
* 对接osa系统CMDB适配    <br/>
* Project Name:inspection-service
* File Name:OsaCmdbAdaptImpl.java
* Package Name:com.aspire.mirror.inspection.server.biz.cmdbadapt
* ClassName: OsaCmdbAdaptImpl <br/>
* date: 2018年9月12日 下午2:09:36 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Component
@Qualifier("cmdbAdaptImpl")
@ConditionalOnProperty(name="cmdb.service.identity", havingValue="osa")
class OsaCmdbAdaptImpl implements ICmdbAdapt {
	@Value("${cmdb.service.url}")
	private String osaUrl;
	
	@Override
	public List<CommonCmdbDevice> listCmdbDeviceByIds(String joinedCmdbIds) {
		OsaCmdbApiClient client = ClientServiceBuilder.buildClientService(OsaCmdbApiClient.class, osaUrl);
		String authToken = OsaAuthTokenUtil.generateOsaAuthToken();
		List<OsaCmdbDetail> deviceList = client.listDeviceDetailsByIdArr(joinedCmdbIds, authToken);
		return parse(deviceList);
	}
	
	private List<CommonCmdbDevice> parse(List<OsaCmdbDetail> deviceList) {
		List<CommonCmdbDevice> resultList = new ArrayList<>();
		if (CollectionUtils.isEmpty(deviceList)) {
			return resultList;
		}
		for (OsaCmdbDetail device : deviceList) {
			CommonCmdbDevice commonDevice = new CommonCmdbDevice();
			commonDevice.setDeviceId(device.getDeviceId());
			commonDevice.setDeviceIp(device.getDeviceIp());
			commonDevice.setName(device.getDeviceName());
			commonDevice.setRoomId(device.getRoomId());
			commonDevice.setDeviceType(device.getDeviceType());
			resultList.add(commonDevice);
		}
		return resultList;
	}
	
	@Override
	public List<CommonUserInfo> queryUserInfoListByIds(List<String> uidList) {
		OsaCmdbApiClient client = ClientServiceBuilder.buildClientService(OsaCmdbApiClient.class, osaUrl);
		String authToken = OsaAuthTokenUtil.generateOsaAuthToken();
		OsaUserListResponse response = client.queryUserInfoList(authToken, StringUtils.join(uidList, ","));
		if (response != null && response.getUserList() != null) {
			return response.parse();
		}
		return new ArrayList<CommonUserInfo>();
	}
}
