package com.aspire.mirror.indexproxy.eventprocess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.biz.BasicDataBiz;
import com.aspire.mirror.indexproxy.domain.MonitorActionRecord;
import com.aspire.mirror.indexproxy.domain.MonitorEventRecord;
import com.aspire.mirror.indexproxy.domain.MonitorItemRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTemplateRecord;
import com.aspire.mirror.indexproxy.indexprocess.Consts.EVENT_SOURCE;
import com.aspire.mirror.indexproxy.indexprocess.model.BizObjectIndexThreshold;
import com.aspire.mirror.indexproxy.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
* 指标值阈值事件处理器    <br/>
* Project Name:index-proxy
* File Name:IndexValueThresholdEventProcessor.java
* Package Name:com.aspire.mirror.indexproxy.eventprocess
* ClassName: IndexValueThresholdEventProcessor <br/>
* date: 2018年8月20日 下午5:17:20 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Component
class IndexValueThresholdEventProcessor implements IMonitorEventProcessor {
	@Autowired
	private BasicDataBiz	basicDataBiz;

	@Autowired
	@Qualifier("urlActionExecutor")
	private IEventActionExecutor	urlActionExecutor;

	@Autowired
	@Qualifier("functionActionExecutor")
	private IEventActionExecutor	functionActionExecutor;
	
	@Value("${inpsection.service.itemdata.callback}")
	private String inspItemDataCallBackUrl;

	@Value("${monitor.service.itemdata.callback}")
	private String monitorItemDataCallBackUrl;

	@Value("${bizmonitor.service.itemdata.callback}")
	private String bizMonitorItemDataCallBackUrl;
	
	@Override
	public boolean isAware(MonitorEventRecord event) {
		if (event != null && EVENT_SOURCE.TRIGGERS.name().equalsIgnoreCase(event.getSourceType())) {
			return true;
		}
		return false;
	}
	
	
	/**
	* 由于action动作的前端维护未写完,暂时使用配置中的URl回调地址回调动作
	*/
	@Override
	public void processEvent(MonitorEventRecord event) {
		log.info("Begin to handle the monitor event with event_id {}", event.getEventId());
		BizObjectIndexThreshold bizObj = JsonUtil.jacksonConvert(event.getObject(), BizObjectIndexThreshold.class);
		
		MonitorItemRecord itemInfo = basicDataBiz.getMonitorItemById(bizObj.getItemId());
		if (itemInfo == null) {
			log.error("There is no MonitorItemRecord with id {}", bizObj.getItemId());
			return;
		}
		MonitorTemplateRecord templateInfo = basicDataBiz.getMonitorTemplateById(itemInfo.getTemplateId());
		if (templateInfo == null) {
			log.error("There is no MonitorTemplateRecord with id {} which is refered by item with id {}", 
					itemInfo.getTemplateId(), bizObj.getItemId());
			return;
		}
		
		MonitorActionRecord urlAction = new MonitorActionRecord();
		urlAction.setActionId("0000_hardcoded_inspection_callback_url_0000");
		urlAction.setType(MonitorActionRecord.ACTION_TYPE_URL);
		if (MonitorTemplateRecord.FUN_TYPE_MONITOR.equals(templateInfo.getFunType())) {
			// 业务告警
//			if ("2".equals(bizObj.getObjectType())) {
//				urlAction.setDealer("bizMonitorItemDataCallBack");
//			} else {
//				urlAction.setDealer("monitorItemDataCallBack");
//			}
			if ("2".equals(bizObj.getObjectType())) {
				urlAction.setDealer(bizMonitorItemDataCallBackUrl);
			} else {
				urlAction.setDealer(monitorItemDataCallBackUrl);
			}
		} 
		else if (MonitorTemplateRecord.FUN_TYPE_INSPECTION.equals(templateInfo.getFunType())) {
//			urlAction.setDealer("inspItemDataCallBack");
			urlAction.setDealer(inspItemDataCallBackUrl);
		}
		
		try {
			urlActionExecutor.doAction(urlAction, bizObj);
		} catch (Throwable e) {
			String tip = String.format("Error when execute the action with id: %s", urlAction.getActionId());
			log.error(tip, e);
		}
	}
	
	/**
	@Override
	public void processEvent(MonitorEventRecord event) {
		log.info("Begin to handle the monitor event with event_id {}", event.getEventId());
		BizObjectIndexThreshold bizObj = JsonUtil.jacksonConvert(event.getObject(), BizObjectIndexThreshold.class);
		List<MonitorActionRecord> actionList = basicDataBiz.getMonitorActionListByTriggerId(bizObj.getTriggerId());
		
		for (MonitorActionRecord actionItem : actionList) {
			try {
				if (!isActionAware(actionItem, event)) {
					continue;
				}
				if (MonitorActionRecord.ACTION_TYPE_URL.equals(actionItem.getType())) {
					urlActionExecutor.doAction(actionItem, bizObj);
				} else if (MonitorActionRecord.ACTION_TYPE_FUNCTION.equals(actionItem.getType())) {
					functionActionExecutor.doAction(actionItem, bizObj);
				}
			} catch (Throwable e) {
				String tip = String.format("Error when execute the action with id: %s", actionItem.getActionId());
				log.error(tip, e);
			}
		}
	}
	
	/**
	* 事件类型和action是否匹配. <br/>
	*
	* 作者： pengguihua
	* @param actionRecord
	* @param event
	* @return
	private boolean isActionAware(final MonitorActionRecord actionRecord, final MonitorEventRecord event) {
		if (MonitorActionRecord.EVENT_TYPE_GENERAL.equals(actionRecord.getEventType())) {
			return true;
		}
		if (MonitorActionRecord.EVENT_TYPE_NORMAL.equals(actionRecord.getEventType())
				&& EVENT_VALUE.NORMAL.getCode().equals(event.getValue())) {
			return true;
		}
		if (MonitorActionRecord.EVENT_TYPE_EXCEPTION.equals(actionRecord.getEventType())
				&& EVENT_VALUE.EXCEPTION.getCode().equals(event.getValue())) {
			return true;
		}
		return false;
	}
	 */  
}
