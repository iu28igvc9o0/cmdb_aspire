package com.aspire.mirror.indexproxy.selfmonitor.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.aspire.mirror.indexproxy.domain.MonitorDynamicThresholdRecord;
import com.aspire.mirror.indexproxy.domain.MonitorItemRecord;
import com.aspire.mirror.indexproxy.domain.MonitorObjectRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTempMapObjRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTemplateRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;

import lombok.Getter;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: SelfMoniBasicDataWholeModel
 * <p/>
 *
 * 类功能描述: 自监控基础数据包装数据对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年10月27日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Getter
public final class SelfMoniBasicDataWholeModel {
	private final List<MonitorObjectRecord>				objectList;
	private final List<MonitorTemplateRecord>			templateList;
	private final List<MonitorTempMapObjRecord> 		tempMapObjList;
	private final List<MonitorItemRecord>				itemList;
	private final List<MonitorTriggerRecord>			triggerList;
	private final List<MonitorDynamicThresholdRecord>	dynamicThresholdList;
	
	public SelfMoniBasicDataWholeModel(List<MonitorObjectRecord> objectList, List<MonitorTemplateRecord> templateList, 
			List<MonitorTempMapObjRecord> tempMapObjList, List<MonitorItemRecord> itemList, 
			List<MonitorTriggerRecord> triggerList, List<MonitorDynamicThresholdRecord> dynamicThresholdList) {
		this.objectList = objectList;
		this.templateList = templateList;
		this.tempMapObjList = tempMapObjList;
		this.itemList = itemList;
		this.triggerList = triggerList;
		this.dynamicThresholdList = dynamicThresholdList;
		this.dynamicThresholdList.forEach(dynamic -> dynamic.resolveModelJsonMeta());
		linkBasicDataEntitiesRelations();
	}
	
	/* 
	 * 各实体之间的映射关系
	 */
	private final Map<MonitorObjectRecord, List<MonitorTemplateRecord>> objectMapTemplateList = new HashMap<>();
	private final Map<MonitorTemplateRecord, List<MonitorItemRecord>> templateMapItemList = new HashMap<>();
	private final Map<MonitorItemRecord, List<MonitorTriggerRecord>> itemMapTriggerList = new HashMap<>();
	private final Map<MonitorTriggerRecord, List<MonitorDynamicThresholdRecord>> triggerMapDynamicThresholdList = new HashMap<>();
	
	/** 
	 * 功能描述: 拼接各实体之间的关系  
	 * <p>
	 */
	private void linkBasicDataEntitiesRelations() {
		linkObjectMapTemplateList();
		linkTemplateMapItemList();
		linkItemMapTriggerList();
		linkTriggerMapDynamicThresholdList();
	}
	
	private void linkObjectMapTemplateList() {
		objectList.stream().forEach(obj -> {
			List<String> referTempIdList = tempMapObjList.stream().filter(map -> {
				return obj.getObjectType().equals(map.getObjectType()) 
						&& obj.getObjectId().equals(map.getObjectId());
			}).map(map -> map.getTemplateId()).collect(Collectors.toList());
			
			if (referTempIdList.size() > 0) {
				List<MonitorTemplateRecord> referTempList = templateList.stream().filter(
						temp -> referTempIdList.contains(temp.getTemplateId())).collect(Collectors.toList());
				objectMapTemplateList.put(obj, referTempList);
			}
		});
	}
	
	private void linkTemplateMapItemList() {
		templateList.stream().forEach(temp -> {
			List<MonitorItemRecord> referItemList = this.itemList.stream().filter(item -> {
				return temp.getTemplateId().equals(item.getTemplateId());
			}).collect(Collectors.toList());
			templateMapItemList.put(temp, referItemList);
		});
	}
	
	private void linkItemMapTriggerList() {
		itemList.stream().forEach(item -> {
			List<MonitorTriggerRecord> referTriggerList = this.triggerList.stream().filter(trigger -> {
				return trigger.getItemId().equals(item.getItemId());
			}).collect(Collectors.toList());
			itemMapTriggerList.put(item, referTriggerList);
		});
	}
	
	/** 
	 * 功能描述: 由于dynamicThreshold(动态阈值)是针对具体的某个监控对象(moniObject), 
	 * 所以一个moni_trigger对应到多个moni_object时, 每个moni_object都会在  dynamicThreshold表中有一条记录, 
	 * 故此处为1对多的关系
	 * <p>
	 */
	private void linkTriggerMapDynamicThresholdList() {
		triggerList.stream().forEach(trigger -> {
			List<MonitorDynamicThresholdRecord> referThresholdList = this.dynamicThresholdList.stream().filter(threshold -> {
				return trigger.getTriggerId().equals(threshold.getTriggerId());
			}).collect(Collectors.toList());
			triggerMapDynamicThresholdList.put(trigger, referThresholdList);
		});
	}
	
	/** 
	 * 功能描述: 组装出自采集监控项信息列表
	 * <p>
	 * @return
	 */
	public List<SelfMoniCollectItem> resolve2SelfMoniCollectItemList() {
		final List<SelfMoniCollectItem> resultList = new ArrayList<>();
		for (Map.Entry<MonitorObjectRecord, List<MonitorTemplateRecord>> entry : objectMapTemplateList.entrySet()) {
			final MonitorObjectRecord moniObj = entry.getKey();
			entry.getValue().forEach(temp -> {
				List<MonitorItemRecord> itemList = templateMapItemList.get(temp);
				if (CollectionUtils.isEmpty(itemList)) {
					return;
				}
				itemList.stream().forEach(item -> {
					List<MonitorTriggerRecord> triggerList = itemMapTriggerList.get(item);
					if (CollectionUtils.isEmpty(triggerList)) {
						resultList.add(SelfMoniCollectItem.from(moniObj, temp, item, null));
						return;
					}
					
					List<MonitorDynamicThresholdRecord> moniObjDynamicList = new ArrayList<>();
					triggerList.forEach(trigger -> {
						List<MonitorDynamicThresholdRecord> dynamicList = triggerMapDynamicThresholdList.get(trigger);
						List<MonitorDynamicThresholdRecord> filterList = null;
						if (CollectionUtils.isNotEmpty(dynamicList)) {
							// 对于ZABBIX的"新发现监控项", trigger在dynamicThreshold中可能存在多条记录, 其device_item_id不同
							filterList = dynamicList.stream().filter(dynamic -> {
								return moniObj.getObjectId().equals(dynamic.getDeviceId())
										&& trigger.getTriggerId().equals(dynamic.getTriggerId());
							}).collect(Collectors.toList());
							moniObjDynamicList.addAll(filterList);
						}
						if (CollectionUtils.isEmpty(filterList)) {
							resultList.add(SelfMoniCollectItem.from(
									moniObj, temp, item, Collections.singletonList(Pair.of(trigger, null))));
						}
					});
					
					// 对于ZABBIX的"新发现监控项", trigger在dynamicThreshold中可能存在多条记录, 其device_item_id不同;
					// 同时由于一个device_item_id可以被多个不同级别触发器关联, 所以在dynamicThreshold中会重复(trigger_id不同)
					Map<String, List<MonitorDynamicThresholdRecord>> deviceItemGroup = moniObjDynamicList.stream()
							.filter(dyna -> StringUtils.isNotBlank(dyna.getDeviceItemId()))
							.collect(Collectors.groupingBy((dyna) -> dyna.getDeviceItemId()));
					
					for (List<MonitorDynamicThresholdRecord> deviceItemDynamicList : deviceItemGroup.values()) {
						List<Pair<MonitorTriggerRecord, MonitorDynamicThresholdRecord>> pairList = deviceItemDynamicList.stream().map(
							dyna -> { 
								MonitorTriggerRecord trigger = triggerList.stream().filter(
										t -> t.getTriggerId().equals(dyna.getTriggerId())).findFirst().get();
								return Pair.of(trigger, dyna);
							}
						).collect(Collectors.toList());
						resultList.add(SelfMoniCollectItem.from(moniObj, temp, item, pairList));
					}
				});
			});
		}
		return resultList;
	}
}
