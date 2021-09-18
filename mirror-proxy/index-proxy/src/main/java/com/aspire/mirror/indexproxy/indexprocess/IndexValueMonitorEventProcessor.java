package com.aspire.mirror.indexproxy.indexprocess;

import static com.aspire.mirror.indexproxy.domain.MonitorTemplateRecord.FUN_TYPE_INSPECTION;
import static java.lang.String.format;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.biz.BasicDataBiz;
import com.aspire.mirror.indexproxy.domain.MonitorEventRecord;
import com.aspire.mirror.indexproxy.domain.MonitorItemRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTemplateRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;
import com.aspire.mirror.indexproxy.domain.StandardIndex;
import com.aspire.mirror.indexproxy.helper.SimpleCacheHelper;
import com.aspire.mirror.indexproxy.indexprocess.Consts.EVENT_ACKNOWLEDGE_STATUS;
import com.aspire.mirror.indexproxy.indexprocess.Consts.EVENT_SOURCE;
import com.aspire.mirror.indexproxy.indexprocess.Consts.EVENT_VALUE;
import com.aspire.mirror.indexproxy.indexprocess.model.BizObjectIndexThreshold;
import com.aspire.mirror.indexproxy.indexprocess.model.MonitorItemValue;
import com.aspire.mirror.indexproxy.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/**
* 指标阈值告警事件处理器    <br/>
* Project Name:index-proxy
* File Name:StandardIndexValueThresholdProcessor.java
* Package Name:com.aspire.mirror.indexproxy.indexprocess
* ClassName: StandardIndexValueThresholdProcessor <br/>
* date: 2018年8月16日 下午1:44:32 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Order(0)
@Component
class IndexValueMonitorEventProcessor implements IStandardIndexProcess {
	private static final String					INDEX_ALARM_CACHE_KEY	= "lastAlarm_cache_%s_%s_%s_%s_%s";
	
	@Autowired
	private SimpleCacheHelper					cacheHelper;
	@Autowired
	private InternalMonitorEventPublisher		moniEventPublisher;
	
	@Autowired
	private BasicDataBiz						basicDataBiz;
	@Autowired
	private IndexValueThresholdResolver			thresholdResolver;

	@Override
	public boolean processStandardIndex(StandardIndex index) { 
		log.debug("Begin to process StandardIndex with itemid {}", index.getItemId());
		MonitorItemRecord itemRecord = basicDataBiz.getMonitorItemById(index.getItemId());
		if (itemRecord == null) {
			log.error("There is no monitorItemRecord with itemId {}.", index.getItemId());
			return false;
		}
		MonitorTemplateRecord template = basicDataBiz.getMonitorTemplateById(itemRecord.getTemplateId());
		if (template == null || template.getSysType().equals("SCRIPT")) {
			log.warn("There is no MonitorTemplate or sys_type is SCRIPT with templateId {} which is refered by the MonitorItem "
				   + "with itemId {}.", itemRecord.getTemplateId(), index.getItemId());
			return false;
		}
		List<MonitorTriggerRecord> triggerList = basicDataBiz.getOrderedTriggerListByItemId(index.getItemId());
		if (triggerList == null || triggerList.isEmpty()) {
			log.warn("There is no MonitorTriggers refer to the MonitorItem with itemId {}.", index.getItemId());
			return true;
		}
		
		MonitorItemValue itemVal = MonitorItemValue.from(itemRecord, index.getValue());
		List<Pair<MonitorTriggerRecord, Boolean>> thresholdResult 
										= thresholdResolver.resolveThresholds(itemVal, triggerList);
		resolveMonitorEvent(index, itemRecord, template, thresholdResult);
		return true;
	}
	
	private void resolveMonitorEvent(StandardIndex index, MonitorItemRecord itemRecord, 
			MonitorTemplateRecord template, List<Pair<MonitorTriggerRecord, Boolean>> thresholdResult) {
		// 遍历触发器阈值匹配处理事件生成
		for (Pair<MonitorTriggerRecord, Boolean> matchTrigger : thresholdResult) {
			try {
				// 巡检的监控项, 无论正常异常均生成事件
				if (FUN_TYPE_INSPECTION.equals(template.getFunType())) {
					generateAndPublishMonitorEvent(index, itemRecord, matchTrigger);
					continue;
				}
				
				// 非巡检的监控项, 从缓存中检索是否之前存在异常
				String groupKey = index.getGroupKey() == null ? "" : index.getGroupKey(); 
				String cacheKey = format(INDEX_ALARM_CACHE_KEY, index.getObjectType(), index.getObjectId(),
									index.getItemId(), matchTrigger.getLeft().getPriority(), groupKey);
				String alarmFlag = cacheHelper.getValueByKey(cacheKey);
				String trueText = String.valueOf(Boolean.TRUE);
				
				// 匹配到触发器, 生成异常事件
				if (matchTrigger.getRight()) {
					// 存入缓存标记, 供下次告警恢复事件时使用
					if (alarmFlag == null) {
						cacheHelper.putCacheEntry(cacheKey, trueText);
					}
					generateAndPublishMonitorEvent(index, itemRecord, matchTrigger);
				} 
				else if (trueText.equals(alarmFlag)) { // 触发器未匹配, 指标值正常, 此时判断上次是否存在异常标记, 存在则生成正常事件, 并从缓存移除标记
					cacheHelper.removeCacheKey(cacheKey);
					generateAndPublishMonitorEvent(index, itemRecord, matchTrigger);
				}
			} catch (Throwable e) {
				log.error("Error when try to process index value monitor event.", e);
			}
		}
	}
	
	// 发送到kafka, 并保存到数据库
	private void generateAndPublishMonitorEvent(StandardIndex index, 
			MonitorItemRecord itemRecord, Pair<MonitorTriggerRecord, Boolean> matchTrigger) {
		log.info("Begin to generate mointor event for StandardIndex {}", index);
		MonitorEventRecord event = generateMonitorEvent(index, itemRecord, matchTrigger);
		basicDataBiz.saveMonitorEvent(event);
		// 发布事件
		moniEventPublisher.publishMonitorEvent(event, index.getItemId());
	}
	
	private MonitorEventRecord generateMonitorEvent(StandardIndex index, 
			MonitorItemRecord itemRecord, Pair<MonitorTriggerRecord, Boolean> matchTrigger) {
		
		MonitorTriggerRecord trigger = matchTrigger.getLeft();
		
		MonitorEventRecord event = new MonitorEventRecord();
		event.setEventId(UUID.randomUUID().toString());
		event.setSourceType(EVENT_SOURCE.TRIGGERS.name());
		event.setSourceId(trigger.getTriggerId());
		
		String valueCode = matchTrigger.getRight() ? EVENT_VALUE.EXCEPTION.getCode() : EVENT_VALUE.NORMAL.getCode();
		event.setValue(valueCode);
		event.setAcknowledged(EVENT_ACKNOWLEDGE_STATUS.NO_ACKNOWLEDGE.getStatusCode());
		long currMill = System.currentTimeMillis();
		event.setClock(new Long(TimeUnit.MILLISECONDS.toSeconds(currMill)).intValue());
		event.setNs(new Long(TimeUnit.MILLISECONDS.toNanos(currMill)).intValue());
		
		event.setObjectType(index.getObjectType());
		event.setObjectId(index.getObjectId());
		
		BizObjectIndexThreshold bizObj = new BizObjectIndexThreshold();
		bizObj.setExtendObj(index.getExtendObj());
		bizObj.setObjectType(index.getObjectType());
		bizObj.setObjectId(index.getObjectId());
		bizObj.setRoomId(index.getRoomId());
		bizObj.setItemId(index.getItemId());
		bizObj.setItemName(itemRecord.getName());
		bizObj.setClock(index.getClock());
		bizObj.setItemValue(index.getValue());
		bizObj.setTriggerId(trigger.getTriggerId());
		bizObj.setTriggerName(trigger.getName());
		bizObj.setPriority(trigger.getPriority());
		bizObj.setStatus(valueCode);
		bizObj.setGroupKey(index.getGroupKey());
		bizObj.setGroupDesc(index.getGroupDesc());
		bizObj.setRemark(index.getRemark());
		event.setObject(JsonUtil.toJacksonJson(bizObj));
		return event;
	}
}
