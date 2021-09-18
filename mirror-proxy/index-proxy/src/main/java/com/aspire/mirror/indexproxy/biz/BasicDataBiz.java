package com.aspire.mirror.indexproxy.biz;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.indexproxy.dao.BasicDataDao;
import com.aspire.mirror.indexproxy.domain.ItemStandardAlertDef;
import com.aspire.mirror.indexproxy.domain.MonitorActionRecord;
import com.aspire.mirror.indexproxy.domain.MonitorDynamicThresholdRecord;
import com.aspire.mirror.indexproxy.domain.MonitorEventRecord;
import com.aspire.mirror.indexproxy.domain.MonitorItemRecord;
import com.aspire.mirror.indexproxy.domain.MonitorObjectRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTempMapObjRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTemplateRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;

@Service
@Transactional
public class BasicDataBiz {
	@Autowired
	private BasicDataDao dao;

	public Integer getDataItemSyncSeq(String itemIdentity) {
		Integer syncMark = dao.getDataItemSyncSeq(itemIdentity);
		if (syncMark != null) {
			return syncMark;
		}
		dao.insertDataItemSyncMark(itemIdentity, new Timestamp(System.currentTimeMillis()));
		return dao.getDataItemSyncSeq(itemIdentity);
	}
	
	public void updateDataItemSyncMark(String itemIdentity, int syncSeq) {
		dao.updateDataItemSyncMark(itemIdentity, syncSeq, new Timestamp(System.currentTimeMillis()));
	}

	public void saveMonitorTemplate(MonitorTemplateRecord template) {
		dao.removeMonitorTemplateById(template.getTemplateId());
		dao.insertMonitorTemplate(template);

		dao.removeTemplateObjectByTemplateId(template.getTemplateId());
		if (!CollectionUtils.isEmpty(template.getTemplateObjectList())) {
			dao.batchSaveTemplateObject(template.getTemplateObjectList());
		}
	}
	
	public void removeMonitorTemplate(String templateId) {
		dao.removeMonitorTemplateById(templateId);
		dao.removeTemplateObjectByTemplateId(templateId);
	}
	
	public MonitorTemplateRecord getMonitorTemplateById(String templateId) {
		return dao.getMonitorTemplateById(templateId);
	}
	
	public void saveMonitorItem(MonitorItemRecord item) {
		dao.removeMonitorItemById(item.getItemId());
		dao.insertMonitorItem(item);
	}
	
	public void removeMonitorItem(String itemId) {
		dao.removeMonitorItemById(itemId);
	}
	
	public MonitorItemRecord getMonitorItemById(String itemId) {
		return dao.getMonitorItemById(itemId);
	}
	
	public void saveMonitorTrigger(MonitorTriggerRecord trigger) {
		dao.removeMonitorTriggerById(trigger.getTriggerId());
		dao.insertMonitorTrigger(trigger);
	}
	
	public void removeMonitorTrigger(String triggerId) {
		dao.removeMonitorTriggerById(triggerId);
	}
	
	public void saveMonitorAction(MonitorActionRecord action) {
		dao.removeMonitorActionById(action.getActionId());
		dao.insertMonitorAction(action);
	}
	
	public void removeMonitorAction(String actionId) {
		dao.removeMonitorActionById(actionId);
	}
	
	public List<MonitorActionRecord> getMonitorActionListByTriggerId(String triggerId) {
		return dao.getMonitorActionListByTriggerId(triggerId);
	}
	
	public List<MonitorTriggerRecord> getOrderedTriggerListByItemId(String itemId) {
		List<MonitorTriggerRecord> triggerList = dao.getTriggerListByItemId(itemId);
		if (triggerList == null) {
			return new ArrayList<MonitorTriggerRecord>();
		}
		Collections.sort(triggerList);
		return triggerList;
	}
	
	public void saveMonitorEvent(MonitorEventRecord event) {
		dao.insertMonitorEvent(event);
	} 
	
	
	//// ------------------------------ main for self monitor ------------------------------
	@Transactional(isolation=Isolation.REPEATABLE_READ, readOnly=true)
	public List<MonitorObjectRecord> querySelfMonitorObjectList(int shardingIdx, int shardingTotal) {
		return dao.querySelfMonitorObjectList(shardingIdx, shardingTotal);
	}
	
	@Transactional(isolation=Isolation.REPEATABLE_READ, readOnly=true)
	public List<MonitorTemplateRecord> querySelfMonitorTemplateList(int shardingIdx, int shardingTotal) {
		return dao.querySelfMonitorTemplateList(shardingIdx, shardingTotal);
	}
	
	@Transactional(isolation=Isolation.REPEATABLE_READ, readOnly=true)
	public List<MonitorTempMapObjRecord> querySelfMonitorTempMapObjList(int shardingIdx, int shardingTotal) {
		return dao.querySelfMonitorTempMapObjList(shardingIdx, shardingTotal);
	}
	
	@Transactional(isolation=Isolation.REPEATABLE_READ, readOnly=true)
	public List<MonitorItemRecord> querySelfMonitorItemList(int shardingIdx, int shardingTotal) {
		return dao.querySelfMonitorItemList(shardingIdx, shardingTotal);
	}
	
	@Transactional(isolation=Isolation.REPEATABLE_READ, readOnly=true)
	public List<MonitorTriggerRecord> querySelfMonitorTriggerList(int shardingIdx, int shardingTotal) {
		return dao.querySelfMonitorTriggerList(shardingIdx, shardingTotal);
	}
	
	@Transactional(isolation=Isolation.REPEATABLE_READ, readOnly=true)
	public List<MonitorDynamicThresholdRecord> querySelfMonitorDynamicThresholdList(int shardingIdx, int shardingTotal) {
		return dao.querySelfMonitorDynamicThresholdList(shardingIdx, shardingTotal);
	}

	public void batchSaveMonitorObject(List<MonitorObjectRecord> eachList) {
		dao.batchSaveMonitorObject(eachList);
	}

	public void removeMonitorObjectByType(String monitorType) {
		dao.removeMonitorObjectByType(monitorType);
	}

	public void saveMonitorTriggerDynamicModel(MonitorDynamicThresholdRecord dataItem) {
		dao.removeMonitorTriggerDynamicModelById(dataItem.getModelId());
		dao.saveMonitorTriggerDynamicModel(dataItem);
	}

	public void removeMonitorTriggerDynamicModel(String modelId) {
		dao.removeMonitorTriggerDynamicModelById(modelId);
	}
	
	public List<ItemStandardAlertDef> queryAllItemStandardAlertDefList() {
		return dao.queryAllItemStandardAlertDefList();
	}
}
