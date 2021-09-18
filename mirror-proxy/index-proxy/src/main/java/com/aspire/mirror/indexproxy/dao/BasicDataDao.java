package com.aspire.mirror.indexproxy.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.indexproxy.domain.ItemStandardAlertDef;
import com.aspire.mirror.indexproxy.domain.MonitorActionRecord;
import com.aspire.mirror.indexproxy.domain.MonitorDynamicThresholdRecord;
import com.aspire.mirror.indexproxy.domain.MonitorEventRecord;
import com.aspire.mirror.indexproxy.domain.MonitorItemRecord;
import com.aspire.mirror.indexproxy.domain.MonitorObjectRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTempMapObjRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTemplateRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;
import com.aspire.mirror.indexproxy.domain.TemplateObjectRecord;

@Mapper
public interface BasicDataDao {
	
	public Integer getDataItemSyncSeq(@Param(value = "itemIdentity") String itemIdentity);
	
	public void insertDataItemSyncMark(@Param(value = "itemIdentity") String itemIdentity, 
			@Param(value = "syncTime") Timestamp syncTime);

	public void updateDataItemSyncMark(@Param(value = "itemIdentity") String itemIdentity, 
			@Param(value = "syncSeq") int syncSeq, @Param(value = "syncTime") Timestamp syncTime);
	
	
	public void removeMonitorTemplateById(@Param(value = "templateId") String templateId);
	public void insertMonitorTemplate(MonitorTemplateRecord template);
	public MonitorTemplateRecord getMonitorTemplateById(@Param(value = "templateId") String templateId);
	
	public void removeMonitorItemById(@Param(value = "itemId") String itemId);
	public void insertMonitorItem(MonitorItemRecord item);
	public MonitorItemRecord getMonitorItemById(@Param(value = "itemId") String itemId);
	
	public void removeMonitorTriggerById(@Param(value = "triggerId") String triggerId);
	public void insertMonitorTrigger(MonitorTriggerRecord trigger);
	
	public void removeMonitorActionById(@Param(value = "actionId") String actionId);
	public void insertMonitorAction(MonitorActionRecord action);
	
	public List<MonitorActionRecord> getMonitorActionListByTriggerId(@Param(value = "triggerId") String triggerId);
	public List<MonitorTriggerRecord> getTriggerListByItemId(@Param(value = "itemId") String itemId);
	
	public void insertMonitorEvent(MonitorEventRecord event);
	
	public String getCacheValByKey(@Param(value = "cacheKey") String cacheKey);
	public void removeCacheKey(@Param(value = "cacheKey") String cacheKey);
	public void putCacheEntry(@Param(value = "cacheKey") String cacheKey, @Param(value = "cacheValue") String cacheVal);
	
	
	////------------------------------ main for self monitor ------------------------------
	List<MonitorObjectRecord> querySelfMonitorObjectList(@Param(value="shardingIdx") int shardingIdx, @Param(value="shardingTotal") int shardingTotal);
	List<MonitorTemplateRecord> querySelfMonitorTemplateList(@Param(value="shardingIdx") int shardingIdx, @Param(value="shardingTotal") int shardingTotal);
	List<MonitorTempMapObjRecord> querySelfMonitorTempMapObjList(@Param(value="shardingIdx") int shardingIdx, @Param(value="shardingTotal") int shardingTotal);
	List<MonitorItemRecord> querySelfMonitorItemList(@Param(value="shardingIdx") int shardingIdx, @Param(value="shardingTotal") int shardingTotal);
	List<MonitorTriggerRecord> querySelfMonitorTriggerList(@Param(value="shardingIdx") int shardingIdx, @Param(value="shardingTotal") int shardingTotal);
	List<MonitorDynamicThresholdRecord> querySelfMonitorDynamicThresholdList(@Param(value="shardingIdx") int shardingIdx, @Param(value="shardingTotal") int shardingTotal);

    void batchSaveMonitorObject(List<MonitorObjectRecord> list);

	void removeMonitorObjectByType(@Param("objectType") String objectType);

    void removeTemplateObjectByTemplateId(@Param("templateId") String templateId);

	void batchSaveTemplateObject(List<TemplateObjectRecord> templateObjectList);

	void removeMonitorTriggerDynamicModelById(@Param("modelId") String modelId);

	void saveMonitorTriggerDynamicModel(MonitorDynamicThresholdRecord dataItem);
	
	List<ItemStandardAlertDef> queryAllItemStandardAlertDefList();
}
