package com.aspire.mirror.collect.biz.itemvaluefetch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.aspire.mirror.collect.api.payload.ObjectItemValueWrap;
import com.aspire.mirror.collect.api.payload.FetchObjectItemsValueRequest;

import lombok.extern.slf4j.Slf4j;

/**
* 监控项值抓取处理链    <br/>
* Project Name:collect-service
* File Name:MonitorItemValueFetchChain.java
* Package Name:com.aspire.mirror.collect.biz
* ClassName: MonitorItemValueFetchChain <br/>
* date: 2018年9月6日 上午9:53:20 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Component
@Primary
public class MonitorItemValueFetchChain implements IMonitorItemValueFetch {
	@Autowired
	@Qualifier("itemValFetchImpl")
	private List<IMonitorItemValueFetch> itemValFetchImplList;
	
	@Override
	public void fetchMonitorItemValues(
			FetchObjectItemsValueRequest request, final List<ObjectItemValueWrap> resultHolder) {
		
		for (IMonitorItemValueFetch itemValFetchImpl : itemValFetchImplList) {
			try {
				itemValFetchImpl.fetchMonitorItemValues(request, resultHolder);
			} catch (Throwable e) {
				String tip = "Error when fecth monitor item values in fetch implementation: %s";
				tip = String.format(tip, itemValFetchImpl.getClass().getSimpleName());
				log.error(tip, e);
			}
		}
	}
}
