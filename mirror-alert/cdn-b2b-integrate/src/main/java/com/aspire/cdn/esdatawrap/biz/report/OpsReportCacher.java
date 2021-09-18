package com.aspire.cdn.esdatawrap.biz.report;

import java.lang.reflect.Method;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.aspire.cdn.esdatawrap.anno.CdnReportCacheMark;
import com.aspire.cdn.esdatawrap.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: OpsReportCacher
 * <p/>
 *
 * 类功能描述: 报表结果缓存器
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年8月7日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
@ConditionalOnExpression("${ops-report.switch:false}")
class OpsReportCacher implements ApplicationRunner {
	// 用户维度报表
	public static final String 					REPORT_CACHE_KEY_PREFIX 					= "cdnOpsReport:";
	public static final String					BANDWIDTH_REPORT							= "bandwidthReport";
	public static final String					SERVICE_SUCC_PERCENT_REPORT					= "serviceSuccPercentReport";
	public static final String					FILE_HIT_SUC_PERCENT_REPORT					= "fileHitSucPercentReport";
	public static final String					REQUEST_COUNT_REPORT						= "requestCountReport";
	public static final String					RETURN_STATUS_SUC_PERCENT_REPORT			= "returnStatusSucPercentReport";
	public static final String					DOWNLOADRATE_BY_PROVINCE_REPORT				= "downloadRateByProvinceReport";
	public static final String					INITBIT_DELAY_TIME_REPORT					= "initBitDelayTimeReport";
	public static final String					WHOLE_COUNTRY_HTTP_STATUS_WEIGHT			= "wholeCountryHttpStatusWeight";
	public static final String					PERCENT_5XX_RANK_BY_PROVINCE				= "percent5xxRankByProvince";
	
	// 集中监控统计
	public static final String					CONCENTRATE_STATISTIC_01_REPORT				= "concentrateStatistic01Report";
	public static final String					CONCENTRATE_STATISTIC_02_REPORT				= "concentrateStatistic02Report";
	public static final String					CONCENTRATE_STATISTIC_03_REPORT				= "concentrateStatistic03Report";
	public static final String					CONCENTRATE_STATISTIC_04_REPORT				= "concentrateStatistic04Report";
	public static final String					CONCENTRATE_STATISTIC_05_REPORT				= "concentrateStatistic05Report";
	public static final String					CONCENTRATE_STATISTIC_06_REPORT				= "concentrateStatistic06Report";
	
	// 地市维度报表key
	public static final String					CITY_BASE_SERVICE_SUCC_PERCENT_REPORT		= "%s_serviceSuccPercentReport";
	public static final String					CITY_BASE_FILE_HIT_SUC_PERCENT_REPORT		= "%s_fileHitSucPercentReport";
	public static final String					CITY_BASE_BANDWIDTH_REPORT					= "%s_bandwidthReport";
	public static final String					CITY_BASE_DOWNLOADRATE_BY_PROVINCE_REPORT	= "%s_downloadRateByProvinceReport";
	public static final String					CITY_BASE_REQUEST_COUNT_REPORT				= "%s_requestCountReport";
	

	@Autowired
	private OpsReportBiz						opsReportBiz;

	@Autowired(required = false)
	private RedissonClient						redissonClient;

	private final ScheduledThreadPoolExecutor	EXECUTORS			= initScheduleExecutor(2);

	private ScheduledThreadPoolExecutor initScheduleExecutor(final int poolsize) {
		return new ScheduledThreadPoolExecutor(poolsize, new ThreadFactory() {
			private final AtomicInteger seq = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, OpsReportCacher.class.getSimpleName() + "_" + seq.getAndIncrement());
			}
		});
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Begin to init load ops report cache result.");
		fetchReportResultAndCache();
		log.info("Finish the loading of ops report cache result.");
	}
	
	/** 
	 * 功能描述: 根据键获取缓存的报表结果  
	 * <p>
	 * @param key
	 * @param resultClazz
	 * @return
	 */
	public <T> T getCacheReportResultByKey(String key, Class<T> resultClazz) {
		if (redissonClient == null) {
			log.warn("Please first initalize the redisson to boost the report cache function.");
			return null;
		}
		if (!key.startsWith(REPORT_CACHE_KEY_PREFIX)) {
			key = REPORT_CACHE_KEY_PREFIX + key;
		}
		RBucket<Object> bucket = redissonClient.getBucket(key);
		if (!bucket.isExists()) {
			return null;
		}
		Object cacheResult = bucket.get();
		return cacheResult == null ? null : JsonUtil.jacksonConvert(cacheResult, resultClazz);
	}
	
	/** 
	 * 功能描述: 每个小时开始时，读取并缓存报表数据  
	 * <p>
	 */
	@Scheduled(cron = "0 0 0/1 * * ? ")
	private void fetchReportResultAndCache() {
		if (redissonClient == null) {
			log.warn("Please first initalize the redisson to boost the report cache function.");
			return;
		}
		log.info("Begin to refresh cache of the ops report at {}.", System.currentTimeMillis());
		
		Method[] methods = ReflectionUtils.getDeclaredMethods(opsReportBiz.getClass());
		for (Method m : methods) {
			final CdnReportCacheMark anno = m.getAnnotation(CdnReportCacheMark.class);
			if (anno == null) {
				continue;
			}
			EXECUTORS.submit(new Runnable() {
				@Override
				public void run() {
					try {
						String suffixCacheKey = anno.value();
						String[] loopKeys = anno.loopKeys();
						if (loopKeys.length == 0) {
							runReportMethodCache(m, null, REPORT_CACHE_KEY_PREFIX + suffixCacheKey);
							return;
						}
						// 针对传参遍历查询的报表，由于遍历查询会比较耗时, 放在一个单独线程里跑
						fetchReportResultAndCacheWithLoopKeys(m, suffixCacheKey, loopKeys);
					} catch (Exception e) {
						log.error("Error when fetchReportResultAndCache.", e);
					}
				}
			});
		}
	}
	
	private void runReportMethodCache(final Method m, final String methodParam, final String cacheKey) throws Exception {
		log.info("Begin to run ops report cache method {} with params {}, the cachekey is {}.", m.getName(), methodParam, cacheKey);
		Object[] params = null;
		params = methodParam == null ? new Object[] {} : new Object[] {methodParam};
		m.setAccessible(true);
		Object result = m.invoke(opsReportBiz, params);
		if (result != null) {
			RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
			bucket.set(JsonUtil.toJacksonJson(result));
		}
	}
	
	/** 
	 * 功能描述: 针对同一类报表，遍历查找多个keys并缓存
	 * <p>
	 */
	private void fetchReportResultAndCacheWithLoopKeys(final Method m, final String suffixKeyTemplate, final String[] loopKeys) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
				for (String key : loopKeys) {
					String suffixCacheKey = String.format(suffixKeyTemplate, key);
					try {
						runReportMethodCache(m, key, REPORT_CACHE_KEY_PREFIX + suffixCacheKey);
					} catch (Exception e) {
						log.error("Error when run method '{}' with param '{}'.", m.getName(), key, e);
					}
				}
//			}
//		}).start();
	}
}
