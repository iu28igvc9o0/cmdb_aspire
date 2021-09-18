package com.aspire.mirror.indexadapt.adapt;

import static java.lang.String.format;

import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

/**
* 指标适配任务执行器    <br/>
* Project Name:index-proxy
* File Name:IndexDataAdapterJobRunner.java
* Package Name:com.aspire.mirror.indexadapt.adapt
* ClassName: IndexDataAdapterJobRunner <br/>
* date: 2018年8月24日 下午4:01:02 <br/>
* @author pengguihua
* @since JDK 1.6
*/ 
@Slf4j
public abstract class AbstractIndexDataAdapterJobRunner {
	
	/**
	* 获取要运行的适配器实现. <br/>
	*
	* 作者： pengguihua
	* @return
	*/  
	protected abstract AbstractIndexDataListAdapter<?> getIndexDataListAdapter();
	
	/**
	* 调度方法, 子类可以覆写此方法, 标注不同的调度策略. <br/>
	*
	* 作者： pengguihua
	*/  
	@Scheduled(cron = "0 */1 * * * ?")
	public void runAdaptJob() {
		runAdapt();
	}
	
	protected final void runAdapt() {
		AbstractIndexDataListAdapter<?> adapter = getIndexDataListAdapter();
		final String adapterIdentity = adapter.getAdapterIdentity();
		long timeMark = System.currentTimeMillis();
		log.info("Index adapter with identity {} begin to run for timeMark {}.", adapterIdentity, timeMark);
		try {
			boolean preFlag = adapter.preHandleAdapt();
			if (!preFlag) {
				log.info("Index adapter with identity {} for timeMark {} skips runing as the preHandle process is false.", 
						adapterIdentity, timeMark);
				return;
			}
			adapter.fetchRawIndexDataList();
			adapter.publishStandardIndexDataList(adapter.adapt2StandardIndex());
			adapter.postHandleAdapt();
			log.info("Index adapter with identity {} had finished normal for timeMark {}.", adapterIdentity, timeMark);
		} catch (Throwable e) {
			log.error(format("Exception when run adapter with identity '%s'", adapterIdentity), e);
			try {
				adapter.handleException();
			} catch (Throwable he) {
				log.error("Error when invoke the adapter.handleException() for identity {}.", adapterIdentity, he);
			}
		}
	}
}
