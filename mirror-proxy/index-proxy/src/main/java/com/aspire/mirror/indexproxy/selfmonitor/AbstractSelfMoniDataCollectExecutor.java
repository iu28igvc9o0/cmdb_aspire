package com.aspire.mirror.indexproxy.selfmonitor;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.aspire.mirror.indexproxy.selfmonitor.domain.SelfMoniCollectItem;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: AbstractSelfMoniDataCollectExecutor
 * <p/>
 *
 * 类功能描述: 监控自采集执行器抽象类
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
public abstract class AbstractSelfMoniDataCollectExecutor {
	protected List<SelfMoniCollectItem>	awareCollectItemList;
	protected final Lock				lock				= new ReentrantLock();
	protected final AtomicBoolean		isCollectRunning	= new AtomicBoolean(false);
	protected Long						lastStartMill;
	private final Random 				random 				= new SecureRandom();
	
	/** 
	 * 功能描述: 应用采集监控项信息  
	 * <p>
	 * @param collectItemList
	 */
	public final void applyCollectItemList(final List<SelfMoniCollectItem> collectItemList) {
		log.info("{} begin to applyCollectItemList at {}.", getClass().getSimpleName(), System.currentTimeMillis());
		boolean loopFlag = true;
		boolean tryFlag = false;
		while (loopFlag) {
			try {
				if (isCollectRunning.get()) {
					TimeUnit.MILLISECONDS.sleep(200);
					continue;
				}
				tryFlag = lock.tryLock(random.nextInt(300), TimeUnit.MILLISECONDS);
				if (tryFlag && !isCollectRunning.get()) {
					loopFlag = false;
					awareCollectItemList = resolveAwareCollectItemList(collectItemList);
					postProcessAwareCollectItemList(awareCollectItemList);
					log.info("{} finished applyCollectItemList at {}.", getClass().getSimpleName(), System.currentTimeMillis());
				}
			} catch (Throwable e) {
				log.error("Exception when invoke applyCollectItemList() from {}.", getClass().getSimpleName(), e);
			} finally {
				if (tryFlag) {
					lock.unlock();
				}
			}
		}
	}
	
	/** 
	 * 功能描述: 获取感兴趣的自监控采集项 ---子类实现  
	 * <p>
	 * @param collectItemList
	 * @return
	 */
	protected abstract List<SelfMoniCollectItem> resolveAwareCollectItemList(final List<SelfMoniCollectItem> collectItemList);
	
	/** 
	 * 功能描述: 解析完采集监控项的后处理方法 (比如进行进一步的数据组装等等), 子类可以覆盖实现  
	 * <p>
	 * @param awareCollectItemList
	 */
	protected void postProcessAwareCollectItemList(final List<SelfMoniCollectItem> awareCollectItemList) {
		
	}
	
	/** 
	 * 功能描述: 执行监控指标自采集  
	 * <p>
	 */
	public final void executeCollect(final SelfMoniCollectResultCallbackFacade callback) {
		int tryCount = 0;
		int maxTryCount = 5;
		boolean loopFlag = true;
		boolean tryFlag = false;
		boolean executeFlag = false;
		Long timeMark = null; 
		int sleepSec = 2;	// 每次重试后暂停2s
		
		while (loopFlag) {
			try {
				executeFlag = false; 
				tryFlag = false;		// reset to false by every loop
				
				if (tryCount == maxTryCount) { 
					// log if this turn is failed as the previous process is still running and reach the max try count.
					log.warn("The previous selfMoni data collect process started at {} is still running for {}, "
							+ "this turn now is skipped after {} tries.", lastStartMill, getClass().getSimpleName(), maxTryCount);
					loopFlag = false;
					break;
				}
				
				if (isCollectRunning.get()) {
					continue;
				}
				tryFlag = lock.tryLock(200, TimeUnit.MILLISECONDS);
				if (!tryFlag) {
					continue;
				}
				
				if (!isCollectRunning.get()) {
					executeFlag = true;
					loopFlag = false;
				} 
			} catch (Throwable e) {
				log.error("Exception when invoke executeCollect() from {} at {}.", 
								getClass().getSimpleName(), System.currentTimeMillis(), e);
			} finally {
				if (executeFlag) {
					timeMark = System.currentTimeMillis();
					lastStartMill = timeMark;
					isCollectRunning.compareAndSet(false, true);
				}
				if (tryFlag) {
					lock.unlock();
				}
				try {
					if (executeFlag) {
						log.info("{} do the executeCollect action started at {}.", getClass().getSimpleName(), timeMark);
						executeCollect0(callback);
						log.info("{} finish the executeCollect action started at {}, cost {} ms.", 
								getClass().getSimpleName(), timeMark, System.currentTimeMillis() - timeMark);
					}
					if (loopFlag) {
						TimeUnit.SECONDS.sleep(sleepSec);
					}
				} catch (Throwable ee) {
					log.error(null, ee);
				} finally {
					tryCount++;
					if (executeFlag) {
						isCollectRunning.compareAndSet(true, false);
					}
				}
			}
		}
	}
	
	/** 
	 * 功能描述: 由子类实现具体的采集逻辑  
	 * <p>
	 */
	protected abstract void executeCollect0(final SelfMoniCollectResultCallbackFacade callback);
	
}