package com.aspire.cdn.esdatawrap.biz.umsalert;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.cdn.esdatawrap.biz.client.IAlertServiceClient;
import com.aspire.cdn.esdatawrap.biz.client.model.SyncItemResponse;
import com.aspire.cdn.esdatawrap.client.ClientServiceBuilder;
import com.aspire.cdn.esdatawrap.config.model.UmsAlertIntegrateProps;

/** 
 *
 * 项目名称: cdn-integrate 
 * <p/>
 * 
 * 类名: UmsAlertSendHelper
 * <p/>
 *
 * 类功能描述: UMS告警发送类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年6月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Component
public class UmsAlertSendHelper {
	
	@Autowired(required=false)
	private UmsAlertIntegrateProps		integrateProps;
	
	public SyncItemResponse sendUmsAlertMessage(UmsAlertMessage alertMsg) {
		return getAlertServiceClient().postAlertMessage(alertMsg);
	}
	
	public SyncItemResponse batchSendUmsAlertMessage(List<UmsAlertMessage> alertMsgList) {
		return getAlertServiceClient().batchPostAlertMessageList(alertMsgList);
	}
	
	private IAlertServiceClient getAlertServiceClient() {
		return ClientServiceBuilder.buildClientService(IAlertServiceClient.class, integrateProps.getServiceUrl());
	}
}
