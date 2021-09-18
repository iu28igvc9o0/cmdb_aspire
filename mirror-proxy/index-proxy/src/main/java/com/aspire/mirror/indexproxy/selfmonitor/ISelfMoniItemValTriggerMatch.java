package com.aspire.mirror.indexproxy.selfmonitor;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;
import com.aspire.mirror.indexproxy.selfmonitor.domain.SelfMoniCollectItemFetchVal;

import lombok.Getter;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: ISelfMoniItemValTriggerMatch
 * <p/>
 *
 * 类功能描述: 自监控采集值触发器匹配
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
interface ISelfMoniItemValTriggerMatch {
	/** 
	 * 功能描述: 自采集监控项值触发器匹配接口  
	 * <p>
	 * @param selfMoniItemVal
	 * @return
	 */
	List<Pair<MonitorTriggerRecord, SelfMoniTiggerMatchResult>> itemValTriggerMatch(final SelfMoniCollectItemFetchVal selfMoniItemVal);
	
	/** 
	 * 触发器匹配结果接口
	 */
	@Getter
	public static final class SelfMoniTiggerMatchResult {
		private boolean	match;
		private String	judgeSign;		// > >= = < <=
		private Object	thresholdVal;
		private Object	extraMeta;
		
		private SelfMoniTiggerMatchResult() {}
		
		public static SelfMoniTiggerMatchResult from(boolean matchFlag) {
			return SelfMoniTiggerMatchResult.from(matchFlag, null, null, null);
		}
		
		public static SelfMoniTiggerMatchResult from(boolean matchFlag, String judgeSign, Object thresholdVal) {
			return SelfMoniTiggerMatchResult.from(matchFlag, judgeSign, thresholdVal, null);
		}
		
		public static SelfMoniTiggerMatchResult from(boolean matchFlag, String judgeSign, Object thresholdVal, Object extraMeta) {
			SelfMoniTiggerMatchResult result = new SelfMoniTiggerMatchResult();
			result.match = matchFlag;
			result.judgeSign = judgeSign;
			result.thresholdVal = thresholdVal;
			result.extraMeta = extraMeta;
			return result;
		}
	}
}
