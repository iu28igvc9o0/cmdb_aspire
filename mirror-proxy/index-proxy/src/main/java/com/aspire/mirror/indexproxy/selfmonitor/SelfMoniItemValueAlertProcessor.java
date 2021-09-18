package com.aspire.mirror.indexproxy.selfmonitor;

import static com.aspire.mirror.indexproxy.selfmonitor.domain.AlertMessageModel.MONI_RESULT_ACTIVE;
import static com.aspire.mirror.indexproxy.selfmonitor.domain.AlertMessageModel.MONI_RESULT_REVOKE;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheResult;
import com.alicp.jetcache.CacheResultCode;
import com.alicp.jetcache.anno.CacheConsts;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.aspire.mirror.indexproxy.biz.BasicDataBiz;
import com.aspire.mirror.indexproxy.config.ProxyIdentityConfig;
import com.aspire.mirror.indexproxy.config.SelfMoniAlertLevelMapConfig;
import com.aspire.mirror.indexproxy.domain.ItemStandardAlertDef;
import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;
import com.aspire.mirror.indexproxy.selfmonitor.ISelfMoniItemValTriggerMatch.SelfMoniTiggerMatchResult;
import com.aspire.mirror.indexproxy.selfmonitor.SelfMoniCollectResultCallbackFacade.ISelfMoniDataCollectResultCallback;
import com.aspire.mirror.indexproxy.selfmonitor.domain.AlertMessageModel;
import com.aspire.mirror.indexproxy.selfmonitor.domain.SelfMoniCollectItemFetchVal;
import com.aspire.mirror.indexproxy.util.JsonUtil;
import com.aspire.mirror.indexproxy.util.Md5Util;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: SelfMoniItemValueAlertProcessor
 * <p/>
 *
 * 类功能描述: 自采集监控项值告警处理器
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
@Order(100)
@Component
class SelfMoniItemValueAlertProcessor implements ISelfMoniDataCollectResultCallback {
	@Autowired
	@Qualifier("dynamic")
	private ISelfMoniItemValTriggerMatch	dynamicTriggerMatch;
	
	@Autowired
	private ProxyIdentityConfig				proxyConfig;
	
	@Autowired
	private SelfMoniAlertLevelMapConfig		alertLevelMap;
	
	@Autowired
	private BasicDataBiz					basicDataBiz;
	
	private final List<ItemStandardAlertDef> itemStandardAlertDefList = new ArrayList<>();

	@Autowired(required=false)
	@Qualifier("centerRoom")
	private KafkaTemplate<String, String>	kafkaTemplate;
	
	@Value("${selfMoni.alertKafkaTopic:TOPIC_SYSTEM_ALERTS}")
	private String 							alertTopic;
	
	@CreateCache(name="alertMessageMeta:alertFlag", localExpire=20, timeUnit=TimeUnit.MINUTES, 
			expire=CacheConsts.UNDEFINED_INT, cacheType = CacheType.BOTH, localLimit = 100000)
	@CacheRefresh(timeUnit = TimeUnit.MINUTES, refresh = 2, stopRefreshAfterLastAccess = 2)
	private Cache<String, Boolean> 			alertMsgMetaCache;
	
	/** 
	 * 功能描述: 缓存所有监控项标准告警定义  
	 * <p>
	 */
	@PostConstruct
	private void loadItemStandardAlertDef() {
		itemStandardAlertDefList.addAll(basicDataBiz.queryAllItemStandardAlertDefList());
		log.info(itemStandardAlertDefList.size() + "");
	}
	
	@Override
	public void postProcess(Map<String, Object> callbackParams, List<SelfMoniCollectItemFetchVal> itemFetchValList) {
		for (SelfMoniCollectItemFetchVal fetchVal : itemFetchValList) {
			List<Pair<MonitorTriggerRecord, SelfMoniTiggerMatchResult>> matchResult = null;
			if (fetchVal.getCollectItem().isDynamicTriggersAttach()) {
				matchResult = dynamicTriggerMatch.itemValTriggerMatch(fetchVal);
			}
			if (matchResult == null) {
				log.warn("There is no trigger match result with fetch-item: objectType={}, objectId={}, itemId={}.", 
						fetchVal.getCollectItem().getMoniObj().getObjectType(), fetchVal.getCollectItem().getMoniObj().getObjectId(), 
						fetchVal.getCollectItem().getMoniItem().getItemId());
				continue;
			}
			processItemValueAlert(fetchVal, matchResult);
		}
	}
	
	private void processItemValueAlert(final SelfMoniCollectItemFetchVal fetchVal, 
					final List<Pair<MonitorTriggerRecord, SelfMoniTiggerMatchResult>> matchResult) {
		for (Pair<MonitorTriggerRecord, SelfMoniTiggerMatchResult> entry : matchResult) {
			MonitorTriggerRecord trigger = entry.getLeft();
			SelfMoniTiggerMatchResult result = entry.getRight();
			try {
				generateSendAlertMessage(fetchVal, trigger, result);
			} catch (Exception e) {
				log.error(null, e);
			}
		}
	}
	
	private void generateSendAlertMessage(final SelfMoniCollectItemFetchVal fetchVal, MonitorTriggerRecord trigger, 
			SelfMoniTiggerMatchResult result) {
		String alertItemKey = String.valueOf(fetchVal.getExtraAttrByKey("alert_item_key"));
		alertItemKey = alertItemKey == null ? fetchVal.getCollectItem().getMoniItem().getKey() : alertItemKey;
		String itemIdentityMd5 = Md5Util.getMD5String(
				fetchVal.getUniqueItemIdentity(proxyConfig) + "_"  + alertItemKey + "_" + trigger.getPriority());
		boolean msgFlag = result.isMatch();
		if (!msgFlag) {
			Boolean cacheFlag = alertMsgMetaCache.get(itemIdentityMd5);
			if (cacheFlag != null && cacheFlag.booleanValue()) {
				msgFlag = true;
			}
		}
		
		if (msgFlag) {
			AlertMessageModel msg = new AlertMessageModel();
			msg.setAlertId(itemIdentityMd5);
			String mapLevel = alertLevelMap.getAlertLevelMap().get(trigger.getPriority());
			mapLevel = mapLevel == null ? "2" : mapLevel;
			msg.setAlertLevel(mapLevel);
			msg.setItemKey(alertItemKey);
			msg.setMoniResult(result.isMatch() ? MONI_RESULT_ACTIVE : MONI_RESULT_REVOKE);
			msg.setDeviceIP(fetchVal.getCollectItem().getMoniObj().getName());
			msg.setMoniIndexValue(fetchVal.getCollectVal() + fetchVal.getCollectItem().getMoniItem().getUnits());
			msg.setCurMoniTime(new Date(fetchVal.getClockTime() * 1000l));
			msg.setAlertStartTime(msg.getCurMoniTime());
			msg.setCurMoniValue(fetchVal.getCollectVal() + fetchVal.getCollectItem().getMoniItem().getUnits());
			// 处理需要替换参数的相关属性
			popupAlertAttrsWithParamKeyValue(msg, alertItemKey, fetchVal, result);
			msg.setSource(fetchVal.getCollectItem().getMoniTemp().getSysType());
			Object zItemId = fetchVal.getExtraAttrByKey("z_itemId");
			msg.setZItemId(zItemId == null ? null : Long.valueOf(zItemId.toString()));
			
			if (log.isDebugEnabled()) {
				log.debug("Begin to send selfMoni item value alert: " + JsonUtil.toJacksonJson(msg));
				log.debug("The according selfMoni fetch item val detail: " + JsonUtil.toJacksonJson(fetchVal));
			}
			kafkaTemplate.send(alertTopic, fetchVal.getCollectItem().getMoniObj().getObjectId(), JsonUtil.toJacksonJson(msg));
			CacheResult resp = alertMsgMetaCache.PUT(itemIdentityMd5, result.isMatch());
			if (CacheResultCode.SUCCESS != resp.getResultCode()) {
				log.warn("There are some errors when put the alertMsgMeta of key {} with value {}.", itemIdentityMd5, result.isMatch());
			}
		}
	}
	
	private void popupAlertAttrsWithParamKeyValue(final AlertMessageModel msg, final String alertItemKey,
			final SelfMoniCollectItemFetchVal fetchVal, final SelfMoniTiggerMatchResult result) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("HOST.IP", fetchVal.getCollectItem().getMoniObj().getName());
		paramMap.put("HOST.HOST", fetchVal.getCollectItem().getMoniObj().getName());
		String itemUnit = fetchVal.getCollectItem().getMoniItem().getUnits();
		itemUnit = itemUnit == null ? "" : itemUnit;
		paramMap.put("ITEM.KEY", alertItemKey);
		paramMap.put("ITEM.VALUE", String.valueOf(fetchVal.getCollectVal()) + itemUnit);
		paramMap.put("JUDGE.SIGN", String.valueOf(result.getJudgeSign()));
		paramMap.put("THRESHOLD.VALUE", String.valueOf(result.getThresholdVal()) + itemUnit);
		
		String moniObject = fetchVal.getCollectItem().getMoniItem().getName();
		String alertTitle = null;
		String alertContent = null;
		boolean isAlertItemDefExist = false;
		
		Optional<ItemStandardAlertDef> matchDef = itemStandardAlertDefList.stream().filter(def -> {
			return alertItemKey.startsWith(def.getItemKeyPrefix());
		}).findFirst();
		
		isAlertItemDefExist = matchDef.isPresent();
		if (isAlertItemDefExist) {
			ItemStandardAlertDef itemStandardAlertDef = matchDef.get();
			paramMap.putAll(itemStandardAlertDef.resolveItemKeyParamKeyValuePair(alertItemKey));
			alertTitle = itemStandardAlertDef.getAlertTitle();
			alertContent = itemStandardAlertDef.getAlertContentTemplate();
		}
		else {
			log.info("There is no item standard alert info define for item_key: {}", alertItemKey);
			if (result.isMatch()) {
				alertTitle = "Problem: {HOST.IP}: {ITEM.KEY}: 当前值: {ITEM.VALUE}{JUDGE.SIGN}{THRESHOLD.VALUE}(阈值).";
			} else {
				alertTitle = "Problem revoke: {HOST.IP}: {ITEM.KEY}: 当前值: {ITEM.VALUE}.";
			}
			alertContent = alertTitle;
		}
		
		msg.setMonitorObject(replaceWithParamKeyValue(moniObject, paramMap));
		msg.setKeyComment(replaceWithParamKeyValue(alertTitle, paramMap));
		msg.setSubject(msg.getKeyComment());
		msg.setMonitorIndex(replaceWithParamKeyValue(alertContent, paramMap));
	}
	
	private String replaceWithParamKeyValue(String targetAttr, Map<String, Object> paramMap) {
		if (StringUtils.isBlank(targetAttr)) {
			return targetAttr;
		}
		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
			targetAttr = targetAttr.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
			targetAttr = targetAttr.replace("{#" + entry.getKey() + "}", String.valueOf(entry.getValue()));
		}
		return targetAttr;
	}
}
