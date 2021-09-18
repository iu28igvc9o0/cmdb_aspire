package com.aspire.mirror.indexproxy.selfmonitor.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.aspire.mirror.indexproxy.domain.MonitorDynamicThresholdRecord;
import com.aspire.mirror.indexproxy.domain.MonitorItemRecord;
import com.aspire.mirror.indexproxy.domain.MonitorObjectRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTemplateRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;

import lombok.Getter;
import lombok.ToString;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: SelfMoniCollectItem
 * <p/>
 *
 * 类功能描述: 自研监控采集项信息对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年10月28日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Getter
@ToString
public class SelfMoniCollectItem {
	private MonitorObjectRecord												moniObj;
	private MonitorTemplateRecord											moniTemp;
	private MonitorItemRecord												moniItem;
	private List<Pair<MonitorTriggerRecord, MonitorDynamicThresholdRecord>>	triggerDynamicThresholdPairList;
	
	private SelfMoniCollectItem() {}
	
	public static SelfMoniCollectItem from(MonitorObjectRecord moniObj, MonitorTemplateRecord moniTemp, MonitorItemRecord moniItem, 
			List<Pair<MonitorTriggerRecord, MonitorDynamicThresholdRecord>> triggerMapDynamicThreshold) {
		SelfMoniCollectItem item = new SelfMoniCollectItem();
		item.moniObj = moniObj;
		item.moniTemp = moniTemp;
		item.moniItem = moniItem;
		triggerMapDynamicThreshold = triggerMapDynamicThreshold == null ? new ArrayList<>() : triggerMapDynamicThreshold;
		Collections.sort(triggerMapDynamicThreshold);
		item.triggerDynamicThresholdPairList = triggerMapDynamicThreshold;
		return item;
	}
	
	public boolean isDynamicTriggersAttach() {
		if (triggerDynamicThresholdPairList == null) {
			return false;
		}
		return triggerDynamicThresholdPairList.stream().filter(pair -> {
			return MonitorTriggerRecord.TYPE_DYNAMIC.toString().equals(pair.getLeft().getType());
		}).findAny().isPresent();
	}
}
