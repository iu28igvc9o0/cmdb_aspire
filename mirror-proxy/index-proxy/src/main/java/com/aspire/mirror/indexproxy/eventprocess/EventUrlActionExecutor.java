package com.aspire.mirror.indexproxy.eventprocess;

import com.aspire.mirror.indexproxy.domain.MonitorActionRecord;
import com.aspire.mirror.indexproxy.exception.IndexProxyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.aspire.mirror.indexproxy.client.ClientServiceBuilder.buildClientService;
import static java.lang.String.format;

/**
* URL回调动作执行    <br/>
* Project Name:index-proxy
* File Name:EventUrlActionExecutor.java
* Package Name:com.aspire.mirror.indexproxy.eventprocess
* ClassName: EventUrlActionExecutor <br/>
* date: 2018年8月21日 上午10:02:26 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Slf4j
@Component
@Qualifier("urlActionExecutor")
class EventUrlActionExecutor implements IEventActionExecutor {
//	@Autowired
//	private InspectionClient inspectionClient;
//
//	@Autowired
//	private AlertClient alertClient;
	@Override
	public void doAction(MonitorActionRecord actionRecord, Object bizObj) {
		validateActionRecord(actionRecord);
		String invokeUrl = actionRecord.getDealer();
		log.debug("Begin to invoke the item monitor event callback to URL {}", invokeUrl);
//		if (invokeUrl.equals("inspItemDataCallBack")) {
//			inspectionClient.onReportItemDataCallBack(bizObj);
//		} else {
//			alertClient.onItemMonitorEventCallBack(bizObj);
//		}
		IEventUrlInvokeClient client = buildClientService(IEventUrlInvokeClient.class, invokeUrl);
		client.invokeUrl(bizObj);
	}
	
	private void validateActionRecord(MonitorActionRecord actionRecord) {
		if (!MonitorActionRecord.ACTION_TYPE_URL.equals(actionRecord.getType())) {
			String tip = format("The actionRecord with id %s is not an url action.", actionRecord.getActionId());
			throw new IndexProxyException(tip);
		}
		
		String url = actionRecord.getDealer();
		if (StringUtils.isBlank(url)) {
			String tip = format("The actionRecord with id %s has no url.", actionRecord.getActionId());
			throw new IndexProxyException(tip);
		}
	}
}
