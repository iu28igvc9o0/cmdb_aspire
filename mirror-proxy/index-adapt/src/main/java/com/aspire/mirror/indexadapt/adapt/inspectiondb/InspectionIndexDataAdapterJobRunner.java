package com.aspire.mirror.indexadapt.adapt.inspectiondb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexadapt.adapt.AbstractIndexDataAdapterJobRunner;
import com.aspire.mirror.indexadapt.adapt.AbstractIndexDataListAdapter;

/**
* 巡检指标适配调度业务执行    <br/>
* Project Name:index-proxy
* File Name:BizMonitorIndexDataAdapterJobRunner.java
* Package Name:com.aspire.mirror.indexadapt.adapt.inspectiondb
* ClassName: BizMonitorIndexDataAdapterJobRunner <br/>
* date: 2018年10月10日 下午6:58:18 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Component
@ConditionalOnProperty("indexAdapter.inspectionDb.switch")
class InspectionIndexDataAdapterJobRunner extends AbstractIndexDataAdapterJobRunner {
	@Autowired
	private InspectionIndexDataListAdapter inpsectionAdapter;
	
	@Override
	@Scheduled(fixedDelayString = "${indexAdapter.inspectionDb.fixDelay}")
	public void runAdaptJob() {
		super.runAdaptJob();
	}
	
	@Override
	protected AbstractIndexDataListAdapter<?> getIndexDataListAdapter() {
		return inpsectionAdapter;
	}
}
