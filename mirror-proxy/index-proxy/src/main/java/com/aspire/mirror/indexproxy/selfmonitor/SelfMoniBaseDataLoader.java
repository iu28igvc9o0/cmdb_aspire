package com.aspire.mirror.indexproxy.selfmonitor;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.biz.BasicDataBiz;
import com.aspire.mirror.indexproxy.domain.MonitorDynamicThresholdRecord;
import com.aspire.mirror.indexproxy.domain.MonitorItemRecord;
import com.aspire.mirror.indexproxy.domain.MonitorObjectRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTempMapObjRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTemplateRecord;
import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;
import com.aspire.mirror.indexproxy.helper.DistributeLockHelper;
import com.aspire.mirror.indexproxy.selfmonitor.domain.SelfMoniBaseDataReloadParam;
import com.aspire.mirror.indexproxy.selfmonitor.domain.SelfMoniBasicDataWholeModel;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: index-proxy 
 * <p/>
 * 
 * 类名: SelfMoniBaseDataLoader
 * <p/>
 *
 * 类功能描述: 监控自采集基础数据加载器
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
class SelfMoniBaseDataLoader {
	private static final Long			FORCE_REFRESH_INTERVAL	= TimeUnit.HOURS.toMillis(1);
	
	@Value("${proxyIdentityConfig.id}")
    private String proxyIdentity;
	
	@Autowired
	private BasicDataBiz 				basicDataBiz;

	@Autowired
	private SelfMoniDataCollectFacade 	selfCollectFacade;
	
	@Autowired
	private DistributeLockHelper 		disLockHelper;
	
	private Lock						distLock;		
	
	private final Lock					lock					= new ReentrantLock(true);
	// the following variables are all protected by the 'lock' lock;
	private final AtomicBoolean 		needRefereshFlag		= new AtomicBoolean(false);
	private volatile long 				lastRefreshMill 		= 0;	
	private SelfMoniBaseDataReloadParam latestReloadParam;
//	private SelfMoniBasicDataWholeModel shardingWholeModel;
	
	
	@PostConstruct
	private void init() {
		distLock = disLockHelper.getReadWriteLock(proxyIdentity + "_sync").readLock();
		new Thread(() -> loopRefreshSelfMoniBaseData()).start();
	}
	
	/** 
	 * 功能描述: 循环检测分片参数变化并加载基础数据  
	 * <p>
	 */
	private void loopRefreshSelfMoniBaseData() {
		log.info("Begin to schedule the 'loopRefreshSelfMoniBaseData()' method.");
		SelfMoniBaseDataReloadParam updateParam = null;
		boolean tryFlag = false;
		while (true) {
			try {
				updateParam = null;	 // 每次遍历开始都重置为null
				tryFlag = lock.tryLock(500, TimeUnit.MILLISECONDS);
				if (!tryFlag) {
					continue;
				}
				if (!needRefereshFlag.get()) {
					continue;
				}
				updateParam = latestReloadParam;
				needRefereshFlag.set(false);		// 重置标记位
			} catch (Throwable e) {
				log.error(null, e);
			} finally {
				try {
					if (updateParam != null) {
						lastRefreshMill = System.currentTimeMillis();
					}	
					// 先解锁
					if (tryFlag) {
						lock.unlock();
					}
					// 应用最新数据
					if (updateParam != null) {
						refreshLoadBasedataInAll(updateParam);
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
	 * 功能描述: 自研监控基础数据刷新加载
	 * <p>
	 * @param param
	 */
	@XxlJob("shardingSelfMoniBaseDataLoad")
	public ReturnT<String> refreshLoadSelfMoniBasedata(String param) {
		// 获取分片参数
		ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
		SelfMoniBaseDataReloadParam currParam = new SelfMoniBaseDataReloadParam(shardingVO.getIndex(), shardingVO.getTotal());
		log.info("Run refreshLoadSelfMoniBasedata() with params: shardingIdx=" + shardingVO.getIndex() 
				+ "|shardingTotal=" + shardingVO.getTotal());
//		SelfMoniBaseDataReloadParam currParam = new SelfMoniBaseDataReloadParam(0, 1);
		Pair<Boolean, String> result = applySelfMoniBasedataLoadParam(currParam);
		XxlJobLogger.log(result.getRight());
		int returnCode = result.getKey() ? ReturnT.SUCCESS_CODE : ReturnT.FAIL_CODE;
		return new ReturnT<String>(returnCode, result.getRight());
	}
	
	/** 
	 * 功能描述: 应用基础数据重载 参数
	 * <p>
	 * @param currParam
	 * @return
	 */
	Pair<Boolean, String> applySelfMoniBasedataLoadParam(final SelfMoniBaseDataReloadParam currParam) {
		Boolean returnFlag = Boolean.TRUE;
		String tip = null;
		Long occurMill = null;
		try {
			distLock.lock();
			lock.lock();
			occurMill = System.currentTimeMillis();
			
			/* 
			 * 分片定时任务检测自研监控基础数据是否需要刷新, 逻辑如下：
			 * 1. 第一次加载 (lastLoadParam==null)
			 * 2. 与上次的刷新时间间隔达到 FORCE_REFRESH_INTERVAL
			 * 3. 与上次的加载参数相比，存在分片信息变化
			 */ 
			if (latestReloadParam == null || occurMill - lastRefreshMill >= FORCE_REFRESH_INTERVAL
						|| latestReloadParam.isShardingInfoDiff(currParam)) {
				latestReloadParam = currParam;
				needRefereshFlag.set(true);
				TimeUnit.SECONDS.sleep(2);  // 暂停2s
				tip = "The applySelfMoniBasedataLoadParam() request is accepted at " + occurMill;
			}
			else {
				tip = "Do nothing with applySelfMoniBasedataLoadParam() at " + occurMill;
			}
			log.info(tip);
		} 
		catch (Throwable e) {
			tip = "Error when applySelfMoniBasedataLoadParam() at " + occurMill;
			returnFlag = Boolean.FALSE;
			log.error(tip, e);
		} finally {
			try {
				lock.unlock();
			} finally {
				distLock.unlock();
			}
		}
		return Pair.of(returnFlag, tip);
	}
	
	/** 
	 * 功能描述: 全量刷新  
	 * <p>
	 */
	private void refreshLoadBasedataInAll(final SelfMoniBaseDataReloadParam updateParam) {
		int shardingIdx = updateParam.getShardingIndex();
		int shardingTotal = updateParam.getShardingTotal();
		log.info("Begin to refreshLoadBasedataInAll with params: shardingIdx=" + shardingIdx + "|shardingTotal=" + shardingTotal);
		List<MonitorObjectRecord> objectList = basicDataBiz.querySelfMonitorObjectList(shardingIdx, shardingTotal);
		List<MonitorTemplateRecord> templateList = basicDataBiz.querySelfMonitorTemplateList(shardingIdx, shardingTotal);
		List<MonitorTempMapObjRecord> tempMapObjList = basicDataBiz.querySelfMonitorTempMapObjList(shardingIdx, shardingTotal);
		List<MonitorItemRecord> itemList = basicDataBiz.querySelfMonitorItemList(shardingIdx, shardingTotal);
		List<MonitorTriggerRecord> triggerList = basicDataBiz.querySelfMonitorTriggerList(shardingIdx, shardingTotal);
		List<MonitorDynamicThresholdRecord> dynamicThresholdList = basicDataBiz.querySelfMonitorDynamicThresholdList(shardingIdx, shardingTotal);
		SelfMoniBasicDataWholeModel wholeModel 
				= new SelfMoniBasicDataWholeModel(objectList, templateList, tempMapObjList, itemList, triggerList, dynamicThresholdList);
		// 应用最新的基础数据到自监控 执行器
		selfCollectFacade.applySelfMoniCollectItemList(wholeModel.resolve2SelfMoniCollectItemList());
		log.info("Finish the refreshLoadBasedataInAll with params: " + shardingIdx + "|" + shardingTotal);
	}
}
