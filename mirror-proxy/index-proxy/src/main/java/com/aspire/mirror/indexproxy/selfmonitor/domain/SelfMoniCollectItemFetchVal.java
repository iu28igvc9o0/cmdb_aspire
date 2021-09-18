package com.aspire.mirror.indexproxy.selfmonitor.domain;

import java.util.Map;

import com.aspire.mirror.indexproxy.config.ProxyIdentityConfig;

import lombok.Getter;
import lombok.ToString;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: SelfMoniCollectItemFetchVal
 * <p/>
 *
 * 类功能描述: 自监控指标采集值对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年11月2日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Getter
@ToString
public class SelfMoniCollectItemFetchVal {
	private SelfMoniCollectItem	collectItem;	// 自采集项
	private Integer				clockTime;		// unix时间,单位 s
	private Object				collectVal;		// 采集值
	private Map<String, Object> extraAttrs;		// 额外属性
	
	private SelfMoniCollectItemFetchVal() {}
	
	public static SelfMoniCollectItemFetchVal from(SelfMoniCollectItem collectItem, Integer clockTime, Object collectVal) {
		return from(collectItem, clockTime, collectVal, null);
	}
	
	public static SelfMoniCollectItemFetchVal from(
			SelfMoniCollectItem collectItem, Integer clockTime, Object collectVal, Map<String, Object> extraAttrs) {
		SelfMoniCollectItemFetchVal fetchVal = new SelfMoniCollectItemFetchVal();
		fetchVal.collectItem = collectItem;
		fetchVal.clockTime = clockTime;
		fetchVal.collectVal = collectVal;
		fetchVal.extraAttrs = extraAttrs;
		return fetchVal;
	}
	
	public Object getExtraAttrByKey(String key) {
		return extraAttrs == null ? null : extraAttrs.get(key);
	}
	
	public String getGlobalUniqueFetchValId(ProxyIdentityConfig proxyCfg) {
		return getUniqueItemIdentity(proxyCfg) + "_" + clockTime;
	}
	
	public String getUniqueItemIdentity(ProxyIdentityConfig proxyCfg) {
		return String.join("_", proxyCfg.getPool(), collectItem.getMoniObj().getObjectType(), 
				collectItem.getMoniObj().getObjectId(), collectItem.getMoniItem().getItemId());
	}
}
