package com.aspire.mirror.indexproxy.selfmonitor;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.selfmonitor.domain.SelfMoniCollectItem;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: SelfMoniDataCollectFacade
 * <p/>
 *
 * 类功能描述: 监控自采集入口Facade
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年10月23日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
class SelfMoniDataCollectFacade {
	private final Lock									lock				= new ReentrantLock(true);
	private final AtomicBoolean							baseDataUpdateFlag	= new AtomicBoolean(false);
	private List<SelfMoniCollectItem>					latestSelfMoniCollectItemList;

	@Autowired(required = false)
	private List<AbstractSelfMoniDataCollectExecutor>	collectExecutors;
	
	@Autowired
	private SelfMoniCollectResultCallbackFacade			callback;

	private ThreadPoolExecutor							pool;
	
	@PostConstruct
	private void init() {
		initExecutors();
		new Thread(() -> loopApplySelfMoniBasicDataWholeModel()).start();
	}
	
	private void initExecutors() {
		if (CollectionUtils.isEmpty(collectExecutors)) {
			return;
		}
		pool = new ThreadPoolExecutor(collectExecutors.size() * 4, collectExecutors.size() * 8, 20, 
				TimeUnit.MINUTES, new ArrayBlockingQueue<>(collectExecutors.size() * 8));
	}
	
	/** 
	 * 功能描述: 循环应用最新的数据 
	 * 注意： 此方法因为是无限循环, 勿在主线程中跑
	 * <p>
	 */
	private void loopApplySelfMoniBasicDataWholeModel() {
		if (CollectionUtils.isEmpty(collectExecutors)) {
			log.info("As there is no self-Moni collect executors, 'loopApplySelfMoniBasicDataWholeModel()' will just return.");
			return;
		}
		List<SelfMoniCollectItem> updateModel = null;
		boolean tryFlag = false;
		while (true) {
			try {
				updateModel = null;	 // 每次遍历开始都重置为null
				tryFlag = lock.tryLock(100, TimeUnit.MILLISECONDS);
				if (!tryFlag) {
					continue;
				}
				if (!baseDataUpdateFlag.get()) {
					continue;
				}
				updateModel = latestSelfMoniCollectItemList;
				baseDataUpdateFlag.set(false);	// 重置标记位
			} catch (Throwable e) {
				log.error(null, e);
			} finally {
				try {
					// 先解锁
					if (tryFlag) {
						lock.unlock();
					}
					// 应用最新数据
					if (updateModel != null) {
						final List<SelfMoniCollectItem> finalUpdateModel = updateModel;
						final CountDownLatch latch = new CountDownLatch(collectExecutors.size());
						collectExecutors.forEach(executor -> {
							pool.submit(()-> {
								try {
									executor.applyCollectItemList(finalUpdateModel);
								} finally {
									latch.countDown();
								}
							});
						});
						latch.await();
					} else {
						TimeUnit.SECONDS.sleep(1);
					}
				} catch (Throwable e) {
					log.error(null, e);
				}
			}
		}
	}
	
	/** 
	 * 功能描述: 应用最新的基础数据  
	 * <p>
	 * @param baseDataWholeModel
	 */
	public void applySelfMoniCollectItemList(final List<SelfMoniCollectItem> selfMoniCollectItemList) {
		log.info("Begin the applySelfMoniCollectItemList() with itemListSize: {}.", selfMoniCollectItemList.size());
		try {
			lock.lock();
			this.latestSelfMoniCollectItemList = selfMoniCollectItemList;
			baseDataUpdateFlag.set(true);
		} finally {
			lock.unlock();
		}
	}
	
	/** 
	 * 功能描述: 监控项自采集xxlJob执行入口
	 * <p>
	 * @param param
	 */
	@XxlJob("shardingSelfMoniDataCollect")
	public ReturnT<String> runSelfMoniDataCollect(String param) {
		// 获取分片参数
		// ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
		if (CollectionUtils.isEmpty(collectExecutors)) {
			String tip = "As there is no self-Moni collect executors, 'runSelfMoniDataCollect()' will just return.";
			log.info(tip);
			XxlJobLogger.log(tip);
			new ReturnT<String>(ReturnT.SUCCESS_CODE, tip);
		}
		
		log.info("Begin to drive all the self-Moni collect executors to run once at {}.", System.currentTimeMillis());
		collectExecutors.forEach(executor -> pool.submit(() -> executor.executeCollect(callback)));
		return ReturnT.SUCCESS;
	}
}
