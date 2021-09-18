package com.aspire.mirror.indexadapt.adapt.inspectiondb;

import static com.aspire.mirror.indexadapt.adapt.IndexAdaptConst.DISTRIBUTE_LOCK_PREFIX;
import static com.aspire.mirror.indexadapt.adapt.IndexAdaptConst.INDEX_SEQ_CACHE_PREFIX;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexadapt.adapt.AbstractIndexDataListAdapter;
import com.aspire.mirror.indexadapt.adapt.inspectiondb.model.InpsectionRawIndex;
import com.aspire.mirror.indexadapt.adapt.inspectiondb.service.InspectionIndexService;
import com.aspire.mirror.indexadapt.helper.DistributeLockHelper;
import com.aspire.mirror.indexadapt.helper.SimpleCacheHelper;

import lombok.extern.slf4j.Slf4j;

/**
* 从巡检数据库中适配    <br/>
* Project Name:index-proxy
* File Name:MiguBizMonitorIndexDataListAdapter.java
* Package Name:com.aspire.mirror.indexadapt.adapt.inspectiondb
* ClassName: MiguBizMonitorIndexDataListAdapter <br/>
* date: 2018年8月6日 下午3:57:07 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Component
@ConditionalOnProperty("indexAdapter.inspectionDb.switch")
class InspectionIndexDataListAdapter extends AbstractIndexDataListAdapter<InpsectionRawIndex> {
	@Value("${indexAdapter.inspectionDb.identity}")
	private String					identity;

	@Autowired
	private InspectionIndexService	service;

	@Autowired
	private SimpleCacheHelper		cacheHelper;
	@Autowired
	private DistributeLockHelper	distLockHelper;
	private Lock					rLock;
	private int						lastSeqNum;		// 最近的指标序号
	
	@PostConstruct
	private void initRLock() {
		rLock = distLockHelper.getLock(DISTRIBUTE_LOCK_PREFIX + identity);
	}
	
	@Override
	public String getAdapterIdentity() {
		return identity;
	}
	
	@Override
	protected List<InpsectionRawIndex> fetchRawIndexDataList0() {
		return service.fetchInspectionIndexList(lastSeqNum);
	}
	
	@Override
	protected List<StandardIndex> adapt2StandardIndex0(List<InpsectionRawIndex> rawIndexList) {
		if (CollectionUtils.isEmpty(rawIndexList)) {
			return null;
		}
		List<StandardIndex> parseList = new ArrayList<StandardIndex>();
		for (InpsectionRawIndex rawIdx : rawIndexList) {
			parseList.add(rawIdx.toStandardIndex());
		}
		return parseList;
	}
	
	@Override
	public boolean preHandleAdapt() {
		try {
			if (!rLock.tryLock()) {
				return false;
			}
			Integer cacheIdxSeq = getRedisCacheIndexSeq();
			lastSeqNum = cacheIdxSeq == null ? 0 : cacheIdxSeq.intValue();
			log.debug(getAdapterIdentity() + " begin to run with idxSeq {}.", cacheIdxSeq);
			return true;
		} catch (Throwable e) {
			log.error(format("Error to get lock for adapter '%s'", getAdapterIdentity()), e);
			releaseLock();
		}
		return false;
	}
	
	@Override
	public void postHandleAdapt() {
		try {
			if (CollectionUtils.isEmpty(rawIndexDataList)) {
				return;
			}
			Collections.sort(rawIndexDataList);
			int maxIndexSeq = rawIndexDataList.get(0).getIndexSeq();
			refreshCacheSeqNum(maxIndexSeq);
		} finally {
			releaseLock();
		}
	}
	
	private Integer getRedisCacheIndexSeq() {
		String cacheIdxSeq = cacheHelper.getValueByKey(INDEX_SEQ_CACHE_PREFIX + getAdapterIdentity());
		return cacheIdxSeq == null ? null : Integer.valueOf(cacheIdxSeq);
	}
	
	private void refreshCacheSeqNum(int maxIndexSeq) {
		log.debug(getAdapterIdentity() + " begin to update idxSeq with value {}.", maxIndexSeq);
		cacheHelper.putCacheEntry(INDEX_SEQ_CACHE_PREFIX + getAdapterIdentity(), String.valueOf(maxIndexSeq));
	}
	
	private void releaseLock() {
		rLock.unlock();
	}
	
	@Override
	public void handleException() {
		releaseLock();
	}
}
