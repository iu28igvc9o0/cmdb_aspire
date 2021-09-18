package com.aspire.mirror.indexproxy.basicdatasync;

import java.util.List;

import com.aspire.mirror.indexproxy.config.ProxyIdentityConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import com.aspire.mirror.indexproxy.biz.BasicDataBiz;
import com.aspire.mirror.indexproxy.domain.BasicDataOperateAware;

/**
* 基础数据同步抽象实现    <br/>
* Project Name:index-proxy
* File Name:AbstractBasicItemDataSync.java
* Package Name:com.aspire.mirror.indexproxy.basicdatasync
* ClassName: AbstractBasicItemDataSync <br/>
* date: 2018年8月14日 下午4:25:31 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
abstract class AbstractBasicDataListSync<T extends BasicDataOperateAware> implements IBasicDataListSync<T> {
	@Autowired
	protected BasicDataBiz basicDataBiz;
	protected Pair<Integer, List<T>> fetchResult;

	@Autowired
	protected ProxyIdentityConfig proxyIdentityConfig;



	@Override
	public final int getStartSyncSeq() {
		return basicDataBiz.getDataItemSyncSeq(getSyncItemIdentity());
	}
	
	@Override
	public final Pair<Integer, List<T>> fetchSyncItemDataList() {
		fetchResult = fetchsyncItemDataList0();
		return fetchResult;
	}
	
	// 子类实现
	protected abstract Pair<Integer, List<T>> fetchsyncItemDataList0();
	
	@Override
	public final void processSyncDataList() {
		if (fetchResult == null || CollectionUtils.isEmpty(fetchResult.getValue())) {
			return;
		}
		
		for (T dataItem : fetchResult.getValue()) {
			if (dataItem.getOperateType() == BasicDataOperateAware.Operate.D) {
				processRemoveBasicData(dataItem);
				continue;
			}
			processUpdateBasicData(dataItem);
		}
		basicDataBiz.updateDataItemSyncMark(getSyncItemIdentity(), fetchResult.getKey());
	}
	
	// 子类实现
	protected abstract void processRemoveBasicData(T dataItem);
	// 子类实现
	protected abstract void processUpdateBasicData(T dataItem);
}
