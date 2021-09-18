package com.aspire.mirror.indexproxy.selfmonitor.zabbix;

import static com.aspire.mirror.indexproxy.domain.MonitorItemRecord.VALUE_TYPE_FLOAT;
import static com.aspire.mirror.indexproxy.domain.MonitorItemRecord.VALUE_TYPE_UINT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheGetResult;
import com.alicp.jetcache.CacheResult;
import com.alicp.jetcache.CacheResultCode;
import com.alicp.jetcache.anno.CacheConsts;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.aspire.mirror.indexproxy.config.ProxyIdentityConfig;
import com.aspire.mirror.indexproxy.domain.MonitorDynamicThresholdRecord;
import com.aspire.mirror.indexproxy.domain.MonitorObjectRecord;
import com.aspire.mirror.indexproxy.selfmonitor.AbstractSelfMoniDataCollectExecutor;
import com.aspire.mirror.indexproxy.selfmonitor.SelfMoniCollectResultCallbackFacade;
import com.aspire.mirror.indexproxy.selfmonitor.domain.SelfMoniCollectItem;
import com.aspire.mirror.indexproxy.selfmonitor.domain.SelfMoniCollectItemFetchVal;
import com.aspire.mirror.indexproxy.selfmonitor.zabbix.domain.ZbxHistoryQueryParam;
import com.aspire.mirror.indexproxy.selfmonitor.zabbix.domain.ZbxItemHistoryRecord;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: ZabbixSelfMoniItemCollectExecutor
 * <p/>
 *
 * 类功能描述: 从ZABBIX系统执行指标自采集的执行器
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
@Slf4j
@Component
class ZabbixSelfMoniItemCollectExecutor extends AbstractSelfMoniDataCollectExecutor {
	public static final String 			SYS_TYPE_ZABBIX_SYNC 										= "AIOPS";
	
	// the access of this variable should be protected by the super.lock
	private final Map<MonitorObjectRecord, List<ZbxDeviceCollectItemWrap>> zbxHistoryItemList 		= new HashMap<>();
	private final Map<MonitorObjectRecord, List<ZbxDeviceCollectItemWrap>> zbxHistoryUintItemList 	= new HashMap<>();
	
	@Autowired
	private ProxyIdentityConfig		proxyConfig;
	
	@Autowired
	private ZabbixSelfMoniBiz 		zabbixSelfMoniBiz;
	
	protected ThreadPoolExecutor 	execPool;
	
	@CreateCache(name="self_moni:zbx_item_host_seq", localExpire=20, timeUnit=TimeUnit.MINUTES, 
			expire=CacheConsts.UNDEFINED_INT, cacheType = CacheType.BOTH, localLimit = 100000)
	private Cache<String, Long> zbxHostSeqNoCache;
	
	@PostConstruct
	private void initExecPool() {
		execPool = new ThreadPoolExecutor(proxyConfig.getCollectPoolSize(), proxyConfig.getCollectPoolSize(), 30, 
				TimeUnit.MINUTES, new LinkedBlockingQueue<>());
	}
	
	@Override
	protected List<SelfMoniCollectItem> resolveAwareCollectItemList(final List<SelfMoniCollectItem> collectItemList) {
		List<SelfMoniCollectItem> filterCollectItemList = collectItemList.stream().filter(collectItem -> 
				SYS_TYPE_ZABBIX_SYNC.equals(collectItem.getMoniTemp().getSysType()) 
					&& CollectionUtils.isNotEmpty(collectItem.getTriggerDynamicThresholdPairList())
				).collect(Collectors.toList());
		log.info("Resolve aware collectItemList with items count: {}.", filterCollectItemList.size());
		return filterCollectItemList;
	}
	
	@Override
	protected void postProcessAwareCollectItemList(final List<SelfMoniCollectItem> awareCollectItemList) {
		Map<MonitorObjectRecord, List<ZbxDeviceCollectItemWrap>> floatMap = prepareZbxWrapItemListByMoniObj(
				VALUE_TYPE_FLOAT, awareCollectItemList);
		Map<MonitorObjectRecord, List<ZbxDeviceCollectItemWrap>> uintMap = prepareZbxWrapItemListByMoniObj(
				VALUE_TYPE_UINT, awareCollectItemList);
		try {
			lock.lock();
			zbxHistoryItemList.clear();
			zbxHistoryUintItemList.clear();
			zbxHistoryItemList.putAll(floatMap);
			zbxHistoryUintItemList.putAll(uintMap);
			int floatZbxItemCount = floatMap.values().stream().mapToInt(list -> list.size()).sum();
			int intZbxItemCount = uintMap.values().stream().mapToInt(list -> list.size()).sum();
			log.info("The count of zabbix host with item value type 'float' is {}, itemList count is {}.", floatMap.size(), floatZbxItemCount);
			log.info("The count of zabbix host with item value type 'Integer' is {}, itemList count is {}.", uintMap.size(), intZbxItemCount);
		} finally {
			lock.unlock();
		}
	}
	
	/** 
	 * 功能描述: 根据监控设备分组，组装出ZABBIX采集需要的对象信息  
	 * <p>
	 * @return
	 */
	private Map<MonitorObjectRecord, List<ZbxDeviceCollectItemWrap>> prepareZbxWrapItemListByMoniObj(String valueType, 
			final List<SelfMoniCollectItem> filterCollectItemList) {
		final Map<MonitorObjectRecord, List<ZbxDeviceCollectItemWrap>> group = new HashMap<>();
		for (SelfMoniCollectItem model : filterCollectItemList) {
			if (!valueType.equals(model.getMoniItem().getValueType())) {
				continue;
			}
			ZbxDeviceCollectItemWrap wrap = ZbxDeviceCollectItemWrap.from(model);
			if (wrap == null) {
				continue;
			}
			MonitorObjectRecord moniObj = model.getMoniObj();
			List<ZbxDeviceCollectItemWrap> zbxWrapItemlist = group.get(moniObj);
			if (zbxWrapItemlist == null) {
				zbxWrapItemlist = new ArrayList<ZbxDeviceCollectItemWrap>();
				group.put(moniObj, zbxWrapItemlist);
			}
			zbxWrapItemlist.add(wrap);
		}
		return group;
	}

	@Override
	protected void executeCollect0(final SelfMoniCollectResultCallbackFacade callback) {
		try {
			lock.lock();
			long startMill = System.currentTimeMillis();
			final AtomicLong fetchCount = new AtomicLong(0);
			final CountDownLatch latch = new CountDownLatch(zbxHistoryItemList.size() + zbxHistoryUintItemList.size());
			long lastestHisSeq = zabbixSelfMoniBiz.queryHistoryLatestSequenceNo();
			long lastestHisUnitSeq = zabbixSelfMoniBiz.queryHistoryUintLatestSequenceNo();
			for (Map.Entry<MonitorObjectRecord, List<ZbxDeviceCollectItemWrap>> entry : zbxHistoryItemList.entrySet()) {
				execPool.submit(() -> executeCollectByZabbixHost(latch, VALUE_TYPE_FLOAT, entry, callback, lastestHisSeq, fetchCount));
			}
			for (Map.Entry<MonitorObjectRecord, List<ZbxDeviceCollectItemWrap>> entry : zbxHistoryUintItemList.entrySet()) {
				execPool.submit(() -> executeCollectByZabbixHost(latch, VALUE_TYPE_UINT, entry, callback, lastestHisUnitSeq, fetchCount));
			}
			latch.await();
			log.info("{} fetch item value count {} from {} hosts, cost {} ms.", getClass().getSimpleName(), 
					fetchCount.get(), zbxHistoryItemList.size() + zbxHistoryUintItemList.size(), 
					System.currentTimeMillis() - startMill);
		} catch (Throwable e) {
			log.error("Error when executeCollect0() by {} at {}.", getClass().getSimpleName(), System.currentTimeMillis(), e);
		} finally {
			lock.unlock();
		}
	}
	
	/** 
	 * 功能描述: 采集ZABBIX监控项值  
	 * <p>
	 * @param latch
	 * @param valueType
	 * @param entry
	 */
	private void executeCollectByZabbixHost(final CountDownLatch latch, final String valueType, 
				final Map.Entry<MonitorObjectRecord, List<ZbxDeviceCollectItemWrap>> entry, 
				final SelfMoniCollectResultCallbackFacade callback, final long lastestHisSeq, final AtomicLong fetchCount) {
		try {
			final MonitorObjectRecord zbxMoniHost = entry.getKey();
			final List<ZbxDeviceCollectItemWrap> itemMetaList = entry.getValue();
			String hostSeqKey = resolveMoniObjectCacheKey(zbxMoniHost, valueType);
			CacheGetResult<Long> result = zbxHostSeqNoCache.GET(hostSeqKey);
			
			// 第一次采集设备时, 直接把最新的sequence_no存入缓存, 作为下次采集的起始sequence_no
			if (result.getResultCode() == CacheResultCode.NOT_EXISTS) {
				updateZbxHostSeqNoCache(hostSeqKey, lastestHisSeq);
				return;
			}
			
			Long hostId = zbxMoniHost.retriveExtendAttr("hostid", Number.class).longValue();
			ZbxHistoryQueryParam queryParam = ZbxHistoryQueryParam.bySequence(hostId, result.getValue());
			List<ZbxItemHistoryRecord> itemHistoryList = null;
			if (VALUE_TYPE_FLOAT.equals(valueType)) {
				itemHistoryList = zabbixSelfMoniBiz.queryHistoryBySequenceNo(queryParam);
			} 
			else if (VALUE_TYPE_UINT.equals(valueType)) {
				itemHistoryList = zabbixSelfMoniBiz.queryHistoryUintBySequenceNo(queryParam);
			}
			List<SelfMoniCollectItemFetchVal> itemFetchValList = constructCollectItemFetchValList(itemHistoryList, itemMetaList);
			fetchCount.addAndGet(itemFetchValList.size());
			Map<String, Object> params = new HashMap<>();
			params.put("sourceSysType", SYS_TYPE_ZABBIX_SYNC); 
			params.put("valueType", valueType);
			callback.chainPostProcess(params, itemFetchValList);
			
			// 主机的sequece_no写入缓存
			if (itemHistoryList == null || itemHistoryList.isEmpty()) {
				// 当前周期未采集到host的item时，也更新最新sequence_no, 以便减少下次采集时的数据量,提高查询性能
				updateZbxHostSeqNoCache(hostSeqKey, lastestHisSeq);
			} else {
				Long lastSeq = itemHistoryList.get(itemHistoryList.size() - 1).getSequenceNo();
				updateZbxHostSeqNoCache(hostSeqKey, lastSeq);
			}
		} finally {
			latch.countDown();
		}
	}
	
	private void updateZbxHostSeqNoCache(String hostSeqKey, Long lastSeq) {
		CacheResult cacheResult = zbxHostSeqNoCache.PUT(hostSeqKey, lastSeq);
		cacheResult.future().thenRun(() -> {
			if (!cacheResult.isSuccess()) {
				log.warn("The cache with key = {}, value = {} is failed.", hostSeqKey, lastSeq);
			}
		});
	}
	
	public List<SelfMoniCollectItemFetchVal> constructCollectItemFetchValList(
			final List<ZbxItemHistoryRecord> itemHistoryList, final List<ZbxDeviceCollectItemWrap> itemMetaList) {
		List<SelfMoniCollectItemFetchVal> historyFetchList = new ArrayList<>();
		for (ZbxItemHistoryRecord historyVal : itemHistoryList) {
			Optional<ZbxDeviceCollectItemWrap> matchMeta 
				= itemMetaList.stream().filter(meta -> historyVal.getItemId().toString().equals(meta.getHostItemId())).findFirst();
			if (matchMeta.isPresent()) {
				Map<String, Object> extraAttrs = new HashMap<>();
				extraAttrs.put("z_itemId", historyVal.getItemId());
				extraAttrs.put("alert_item_key", historyVal.getItemKey());
				historyFetchList.add(SelfMoniCollectItemFetchVal.from(
						matchMeta.get().getSelfMoniCollectItem(), historyVal.getClock(), historyVal.getValue(), extraAttrs));
			}
		}
		return historyFetchList; 
	}
	
	private String resolveMoniObjectCacheKey(final MonitorObjectRecord moniObj, final String valueType) {
		return "selfmoni:zbxhostseq_" + VALUE_TYPE_FLOAT + ":" + moniObj.getObjectType() + "_" + moniObj.getObjectId();
	}
	
	/** 
	 * ZABBIX 中设备监控项实例采集包装对象
	 */
	@Getter
	@EqualsAndHashCode(of="hostItemId")
	private static class ZbxDeviceCollectItemWrap {
		private final SelfMoniCollectItem selfMoniCollectItem;
		private final String hostItemId;	// 对应到ZABBIX中主机监控项实例id
		
		private ZbxDeviceCollectItemWrap(SelfMoniCollectItem selfMoniCollectItem, String hostItemId) {
			this.selfMoniCollectItem = selfMoniCollectItem;
			this.hostItemId = hostItemId;
		}
		
		public static ZbxDeviceCollectItemWrap from(final SelfMoniCollectItem collectItem) {
			if (CollectionUtils.isEmpty(collectItem.getTriggerDynamicThresholdPairList())) {
				return null;
			}
			MonitorDynamicThresholdRecord dynamic = collectItem.getTriggerDynamicThresholdPairList().get(0).getRight();
			if (null == dynamic) {
				return null;
			}
			return new ZbxDeviceCollectItemWrap(collectItem, dynamic.getDeviceItemId());
		}
	}	
}
