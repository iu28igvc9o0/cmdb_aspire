package com.aspire.mirror.inspection.server.biz.cmdbadapt;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.aspire.mirror.inspection.server.clientservice.InnerCmdbApiClient;
import com.aspire.mirror.inspection.server.clientservice.RbacServiceClient;
import com.aspire.mirror.inspection.server.clientservice.payload.GetOrgUserDetailResponse;
import com.aspire.mirror.inspection.server.clientservice.payload.InnerCmdbDeviceDetail;

import lombok.extern.slf4j.Slf4j;

/**
* 对接内部微服务CMDB的适配  <br/>
* Project Name:inspection-service
* File Name:InnerCmdbAdaptImpl.java
* Package Name:com.aspire.mirror.inspection.server.biz.cmdbadapt
* ClassName: InnerCmdbAdaptImpl <br/>
* date: 2018年9月12日 下午2:49:33 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Component
@Qualifier("cmdbAdaptImpl")
@ConditionalOnProperty(name="cmdb.service.identity", havingValue="cmdb", matchIfMissing=true)
class InnerCmdbAdaptImpl implements ICmdbAdapt {
	private static final String	USER_ID_SPLIT	= "/";
	@Autowired
	private InnerCmdbApiClient	cmdbClient;
	@Autowired
	private RbacServiceClient	rbacClient;

	@Override
	public List<CommonCmdbDevice> listCmdbDeviceByIds(String joinedCmdbIds) {
		List<InnerCmdbDeviceDetail> deviceList = cmdbClient.listDeviceDetailsByIdArr(joinedCmdbIds);
		return parse(deviceList);
	}
	
	private List<CommonCmdbDevice> parse(List<InnerCmdbDeviceDetail> deviceList) {
		List<CommonCmdbDevice> resultList = new ArrayList<>();
		if (CollectionUtils.isEmpty(deviceList)) {
			return resultList;
		}
		for (InnerCmdbDeviceDetail device : deviceList) {
			CommonCmdbDevice commonDevice = new CommonCmdbDevice();
			commonDevice.setDeviceId(device.getId());
			commonDevice.setName(device.getDeviceName());
			commonDevice.setDeviceIp(device.getIp());
			if (StringUtils.isBlank(commonDevice.getDeviceIp())) {
				commonDevice.setDeviceIp(device.getDeviceName());
			}
			commonDevice.setRoomId(device.getRoomId());
			commonDevice.setDeviceType(device.getDeviceType());
			resultList.add(commonDevice);
		}
		return resultList;
	}
	
	@Override
	public List<CommonUserInfo> queryUserInfoListByIds(List<String> uidList) {
		List<CommonUserInfo> userList = new ArrayList<>();
		for (String userId : uidList) {
			String[] splitArr = userId.split(USER_ID_SPLIT);
			if (splitArr.length < 2) {
				log.error("The userId '{}' is not in the expected format.", userId);
				continue;
			}
			GetOrgUserDetailResponse userDetail = rbacClient.getOrgUserDetail(splitArr[0], splitArr[1]);
			if (userDetail != null) {
				userList.add(userDetail.parse());
			}
		}
		return userList;
	}
}
