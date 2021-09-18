package com.aspire.mirror.indexproxy.selfmonitor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.domain.MonitorDynamicThresholdRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;
import com.aspire.mirror.indexproxy.selfmonitor.domain.SelfMoniCollectItemFetchVal;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: SelfMoniItemValDynamicTriggerMatch
 * <p/>
 *
 * 类功能描述: 自采集监控项值动态阈值匹配
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年11月5日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
@Qualifier("dynamic")
class SelfMoniItemValDynamicTriggerMatch implements ISelfMoniItemValTriggerMatch {

	/** 
	 * 方法重写 <br/>
	 * 功能描述： 自采集监控项值动态阈值触发器匹配
	 * <p>
	 * @param selfMoniItemVal
	 * @return 
	 */
	@Override
	public List<Pair<MonitorTriggerRecord, SelfMoniTiggerMatchResult>> itemValTriggerMatch(SelfMoniCollectItemFetchVal selfMoniItemVal) {
		List<Pair<MonitorTriggerRecord, SelfMoniTiggerMatchResult>> resultList = new ArrayList<>();
		boolean matchFlag = false; 
		for (Pair<MonitorTriggerRecord, MonitorDynamicThresholdRecord> trigger : 
				selfMoniItemVal.getCollectItem().getTriggerDynamicThresholdPairList()) {
			if (matchFlag) {
				// 已经匹配到高级别的触发器, 则低级别的触发器自动设置成不匹配
				resultList.add(Pair.of(trigger.getLeft(), SelfMoniTiggerMatchResult.from(false)));
				continue;
			}
			
			Pair<Double, Double> lowHighThreshold = trigger.getRight().resolveClockValueSpan(selfMoniItemVal.getClockTime());
			if (lowHighThreshold == null) {
				log.warn("There is no trigger dynamic threshold json meta with trigger with id {}.", 
						trigger.getLeft().getTriggerId());
//				resultList.add(Pair.of(trigger.getLeft(), SelfMoniTiggerMatchResult.from(false, null)));
				continue;
			}
			Double parseItemVal = Double.parseDouble(selfMoniItemVal.getCollectVal().toString());
			Double lowThreshold = lowHighThreshold.getLeft().doubleValue();
			Double highThreshold = lowHighThreshold.getRight().doubleValue();
			if (parseItemVal.doubleValue() < lowThreshold) {
				resultList.add(Pair.of(trigger.getLeft(), SelfMoniTiggerMatchResult.from(true, " < ", lowThreshold)));
				matchFlag = true;
			} else if (parseItemVal.doubleValue() > highThreshold) {
				resultList.add(Pair.of(trigger.getLeft(), SelfMoniTiggerMatchResult.from(true, " > ", highThreshold)));
				matchFlag = true;
			} else {
				resultList.add(Pair.of(trigger.getLeft(), SelfMoniTiggerMatchResult.from(false)));
				matchFlag = false;
			}
		}
		return resultList;
	}
}
