package com.aspire.mirror.indexproxy.basicdatasync;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

import javax.annotation.PreDestroy;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.helper.DistributeLockHelper;

import lombok.extern.slf4j.Slf4j;

/**
* 指标基础数据同步任务执行器    <br/>
* Project Name:index-proxy
* File Name:BasicDataSyncJobRunner.java
* Package Name:com.aspire.mirror.indexproxy.basicdatasync
* ClassName: BasicDataSyncJobRunner <br/>
* date: 2018年8月24日 下午4:02:26 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Order(0)
//@Component
class BasicDataSyncJobRunner implements ApplicationRunner {
	@Autowired
	private DistributeLockHelper		disLockHelper;

	@Autowired
	private List<IBasicDataListSync<?>>	dataListSyncList;

	@Value("${basicDataSync.proxyIdentity}")
	private String						proxyIdentity;

	@Value("${basicDataSync.interval}")
	private Integer						interval;

	private ScheduledThreadPoolExecutor	executor;


	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (CollectionUtils.isEmpty(dataListSyncList)) {
			log.warn("As there is no dataListSync defined, the data sync job runner will be omited.");
			return;
		}
		executor = new ScheduledThreadPoolExecutor(dataListSyncList.size(), new ThreadFactory() {
			private final AtomicInteger seq = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, BasicDataSyncJobRunner.class.getSimpleName() + "_" + seq.getAndIncrement());
			}
		});
		executor.setMaximumPoolSize(dataListSyncList.size());
		scheduleSyncJobs(executor);
	}

	/**
	* 调度同步任务. <br/>
	*
	* 作者： pengguihua
	*/
	private void scheduleSyncJobs(ScheduledThreadPoolExecutor executor) {
		for (IBasicDataListSync<?> syncItem : dataListSyncList) {
			executor.scheduleWithFixedDelay(buildTask(syncItem), 0, interval, TimeUnit.SECONDS);
		}
	}

	private Runnable buildTask(final IBasicDataListSync<?> syncItem) {
		// 获取分布式锁
		String syncLockName = proxyIdentity + "_sync_" + syncItem.getSyncItemIdentity();
		final Lock syncLock = disLockHelper.getLock(syncLockName);

		return new Runnable() {
			@Override
			public void run() {
				try {
					// 获取分布式锁
					if (!syncLock.tryLock()) {
						return;
					}
					syncItem.fetchSyncItemDataList();
					syncItem.processSyncDataList();
				} catch (Throwable e) {
					log.error("Error to sync datalist.", e);
				} finally {
					syncLock.unlock();
				}
			}
		};
	}

	@PreDestroy
	private void preDestroy() {
		if (executor != null) {
			executor.shutdownNow();
		}
	}
}
