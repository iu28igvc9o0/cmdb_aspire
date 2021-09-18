package com.aspire.mirror.indexproxy.selfmonitor;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.selfmonitor.domain.SelfMoniCollectItemFetchVal;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: SelfMoniCollectResultCallbackFacade
 * <p/>
 *
 * 类功能描述: 监控项自采集结果回调入口
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年11月4日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
public class SelfMoniCollectResultCallbackFacade {
	
	@Autowired(required=false)
	private List<ISelfMoniDataCollectResultCallback> callbackChain;
	
	
	/** 
	 * 功能描述: 自采集结果回调处理链  
	 * <p>
	 * @param callbackParams	回调参数：  sourceSysType 自采集监控项来源系统(ZABBIX, REDFISH, PROMETHEUS), 其它属性可以个性化添加	
	 * @param itemFetchValList
	 */
	public void chainPostProcess(Map<String, Object> callbackParams, List<SelfMoniCollectItemFetchVal> itemFetchValList) {
		if (CollectionUtils.isEmpty(callbackChain)) {
			log.warn("There is no ISelfMoniDataCollectResultCallback implementations ... ");
			return;
		}
		for (ISelfMoniDataCollectResultCallback callback : callbackChain) {
			try {
				callback.postProcess(callbackParams, itemFetchValList);
			} catch (Throwable e) {
				log.error("Error when invoke the callback '{}'.", callback.getClass().getSimpleName(), e);
			}
		}
	}
	
	/** 
	 *
	 * 项目名称: index-proxy 
	 * <p/>
	 * 
	 * 类名: ISelfMoniDataCollectResultCallback
	 * <p/>
	 *
	 * 类功能描述: 采集结果回调接口
	 * <p/>
	 *
	 * @author	pengguihua
	 *
	 * @date	2020年11月4日  
	 *
	 * @version	V1.0 
	 * <br/>
	 *
	 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
	 *
	 */
	public interface ISelfMoniDataCollectResultCallback {
		
		/** 
		 * 功能描述: 监控项自采集结果回调处理  
		 * <p>
		 * @param callbackParams 	回调参数：  sourceSysType 自采集监控项来源系统(ZABBIX, REDFISH, PROMETHEUS), 其它属性可以个性化添加
		 * @param itemFetchValList	
		 */
		public void postProcess(Map<String, Object> callbackParams, List<SelfMoniCollectItemFetchVal> itemFetchValList);
	}
}
