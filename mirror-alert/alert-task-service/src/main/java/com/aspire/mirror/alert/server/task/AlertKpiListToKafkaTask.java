package com.aspire.mirror.alert.server.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.alert.server.clientservice.elasticsearch.MonitorKpiServiceClient;
import com.aspire.mirror.alert.server.dao.alert.AlertRestfulDao;
import com.aspire.mirror.alert.server.dao.alert.po.AlertStandardizationLog;
import com.aspire.mirror.alert.server.dao.alert.po.KpiBook;
import com.aspire.mirror.alert.server.dao.alert.po.KpiListData;
import com.aspire.mirror.alert.server.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@EnableScheduling
@Component
@ConditionalOnProperty(value = "AlertKpiListToKafkaTask.flag", havingValue = "normal")
@Slf4j
public class AlertKpiListToKafkaTask {

	@Autowired
	private TransportClient transportClient;
	private static final Logger LOGGER = Logger.getLogger(AlertKpiListToKafkaTask.class);
	@Autowired
	private AlertRestfulDao alertRestfulDao;

	@Value("${AlertKpiListToKafkaTask.intervalMinute:5}")
	private int intervalMinute;

	@Value("${AlertKpiListToKafkaTask.delayMinute:0}")
	private int delayMinute;

	@Value("${AlertKpiListToKafkaTask.intervalMinuteSuyan:10}")
	private int intervalMinuteSuyan;

	@Value("${AlertKpiListToKafkaTask.delayMinuteSuyan:10}")
	private int delayMinuteSuyan;
	
	@Value("${AlertKpiListToKafkaTask.hisDuration:30}")
	private int hisDuration;

	@Value("${AlertKpiListToKafkaTask.size:2000}")
	private int size;

	private final static String HISTORY_PRE = "history-*";
	private final static String HISTORY_UINT_PRE = "history_uint-*";

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	private MonitorKpiServiceClient monitorKpiServiceClient;
	
	private Map<String,Object> map = Maps.newHashMap() ;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	int minute = 5;

	/**
	 * 告警自动化派单定时任务
	 */
	//@Scheduled(cron = "${AlertKpiListToKafkaTask.cron:0 */2 * * * ?}")
	public void syncMonitorData() {

		log.info("synchronization-syncMonitorData-to-kafka--begin*****************************");
		List<KpiBook> kpiList = alertRestfulDao.getKpiBook(null);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE)) / 5 * 5);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.MINUTE, -delayMinute);
		Date endTime = calendar.getTime();
		calendar.add(Calendar.MINUTE, -intervalMinute);
		Date startTime = calendar.getTime();
		int duration = this.hisDuration;
		
		for (KpiBook kpi : kpiList) {
			String topic = kpi.getSubTopic();
			String key = kpi.getId()+"";
			if(map.containsKey(key)) {
				continue;
			}else {
				map.put(key, kpi);
			}
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						List<KpiListData> kpiList = kpi.getKpiList();
						for (KpiListData req : kpiList) {
							// 查询日志
							AlertStandardizationLog standlast = alertRestfulDao.getLastStandardizationLog(2,
									req.getId());
							Date startTimeTemp = null;
							Date endTimeTemp = null;
							if (null != standlast) {
								startTimeTemp = standlast.getToTime();
								if (startTimeTemp.getTime() > startTime.getTime()) {
									return;
								} else if (startTimeTemp.getTime()+1000 < startTime.getTime()) {
									for(long i=startTimeTemp.getTime();i+1000<endTime.getTime();) {
										long end = i+duration*60*1000;//30分钟查询
									
										if(end>endTime.getTime()) {
											endTimeTemp = endTime;
										}
										getData(startTimeTemp, endTimeTemp, req, topic, false);
										i = endTimeTemp.getTime();
									}
									return;
									/*
									 * // 
									 * 执行历史没有执行的任务 while (startTimeTemp.getTime() < startTime.getTime()) {
									 * Calendar calendar = Calendar.getInstance(); calendar.setTime(startTimeTemp);
									 * calendar.add(Calendar.MINUTE, intervalMinuteSuyan); endTimeTemp =
									 * calendar.getTime();
									 * 
									 * getData(startTimeTemp, endTimeTemp, req, topic, false); startTimeTemp =
									 * endTimeTemp; }
									 */

								}

							}
							getData(startTime, endTime, req, topic, false);
						}
					} catch (Exception e) {
						log.error("同步性能数据报错,id:{}", e,kpi.getId());

					}finally {
						map.remove(key);
					}
				}
			};
			taskExecutor.execute(runnable);
		}

		LOGGER.info("synchronization-syncMonitorData-to-kafka--end*****************************");

	}

	@Scheduled(cron = "${AlertKpiListToKafkaTask.cronSuyan:0 */10 * * * ?}")
	public void syncSuyanMonitorData() {

		log.info("synchronization-syncSuyanMonitorData-to-kafka--begin*****************************");
		List<KpiBook> kpiList = alertRestfulDao.getKpiBook(null);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE)) / 10 * 10);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND,0);
		calendar.add(Calendar.MINUTE, -delayMinuteSuyan);
		Date endTime = calendar.getTime();

		calendar.add(Calendar.MINUTE, -intervalMinuteSuyan);
		Date startTime = calendar.getTime();
		//int duration = this.hisDuration;
		for (KpiBook kpi : kpiList) {
			//String topic = kpi.getSubTopic();
			String key = kpi.getId()+"";
			if(map.containsKey(key)) {
				continue;
			}else {
				map.put(key, kpi);
			}
			/*
			 * Runnable runnable = new Runnable() { public void run() { String k = key; try
			 * { List<KpiListData> kpiList = kpi.getKpiList(); for (KpiListData req :
			 * kpiList) { // 查询日志 AlertStandardizationLogDTO standlast =
			 * alertRestfulDao.getLastStandardizationLog(2, req.getId()); Date startTimeTemp
			 * = null; Date endTimeTemp = null; if (null != standlast) { startTimeTemp =
			 * standlast.getToTime(); if (startTimeTemp.getTime() > startTime.getTime()) {
			 * continue; } else if (startTimeTemp.getTime() < startTime.getTime()) {
			 * for(long i=startTimeTemp.getTime();i<endTime.getTime();) { long end =
			 * i+duration*60*1000;//30分钟查询
			 * 
			 * if(end>endTime.getTime()) { endTimeTemp = endTime; }else { endTimeTemp = new
			 * Date(end); } getData(new Date(i), endTimeTemp, req, topic, false); i =
			 * endTimeTemp.getTime(); } continue; }
			 * 
			 * 
			 * } getData( startTime, endTime, req, topic, true); } } catch (Exception e) {
			 * log.error("同步性能数据报错,id:{}",kpi.getId(), e); }finally { map.remove(k); } } };
			 */
			MyThread2 thread = new MyThread2(key,kpi,startTime,endTime);
			taskExecutor.execute(thread);
		}

		LOGGER.info("synchronization-syncSuyanMonitorData-to-kafka--end*****************************");

	}

	
	public  class  MyThread2 implements  Runnable
	{
	    private  String key;
	    private KpiBook kpi;
	    private Date startTime;
	    private Date endTime;
	    public  MyThread2(String key, KpiBook kpi, Date startTime, Date endTime)
	    {
	        this.key = key;
	        this.kpi = kpi;
	        this.startTime = startTime;
	        this.endTime = endTime;
	    }
	    public  void  run()
	    {
			try {
				String topic= kpi.getSubTopic();
				List<KpiListData> kpiList = kpi.getKpiList();
				for (KpiListData req : kpiList) {
					// 查询日志
					AlertStandardizationLog standlast = alertRestfulDao.getLastStandardizationLog(2,
							req.getId());
					Date startTimeTemp = null;
					Date endTimeTemp = null;
					if (null != standlast) {
						startTimeTemp = standlast.getToTime();
						if (startTimeTemp.getTime() > startTime.getTime()) {
							continue;
						} else if (startTimeTemp.getTime() < startTime.getTime()) {
							for(long i=startTimeTemp.getTime();i<endTime.getTime();) {
								long end = i+hisDuration*60*1000;//30分钟查询
							
								if(end>endTime.getTime()) {
									endTimeTemp = endTime;
								}else {
									endTimeTemp = new Date(end);
								}
								getData(new Date(i), endTimeTemp, req, topic, false);
								i = endTimeTemp.getTime();
							}
							continue;
						}


					} 
					getData( startTime, endTime, req, topic, true);
				}
			} catch (Exception e) {
				log.error("同步性能数据报错,id:{}",kpi.getId(), e);
			}finally {
				map.remove(key);
			}
	    }
	   
	}
	
	
	private void getData(
			Date startTime, Date endTime, KpiListData req,
			String topic, boolean suyanFlag) {
		AlertStandardizationLog standLog = new AlertStandardizationLog();
		try {
			Date now = new Date();
			standLog.setConfigId(req.getId());
			standLog.setFromTime(startTime);
			standLog.setToTime(endTime);
			standLog.setExecTime(endTime);
			standLog.setExecTime(now);
			standLog.setType(2);
			/*
			 * if (suyanFlag) { standLog.setType(3); }
			 */

			List<String> indexList = DateUtil.getIndexList(startTime, endTime, HISTORY_PRE);
			List<String> indexList1 = DateUtil.getIndexList(startTime, endTime, HISTORY_UINT_PRE);
			indexList.addAll(indexList1);

			log.info("index:{}", indexList);

			log.info("getSuyanData--begin*****************************");
			// 设置查询条件
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

			RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
			timeRange.gte(startTime.getTime() / 1000);
			timeRange.lt(endTime.getTime() / 1000);
			queryBuilder.must(timeRange);

			String idcType = req.getIdc_type();
			String roomId = req.getRoomId();
			String keys = req.getKeys_value();
			String deviceClass = req.getDeviceClass();
			String deviceType = req.getDeviceType();
			String pod = req.getPod();
			/*
			 * if (suyanFlag) { queryBuilder.must(QueryBuilders.existsQuery("suyanResid"));
			 * } else { queryBuilder.mustNot(QueryBuilders.existsQuery("suyanResid")); }
			 */

			if (StringUtils.isNotBlank(idcType)) {
				queryBuilder.must(QueryBuilders.termsQuery("idcType.keyword", idcType.split(",")));
			}
			if (StringUtils.isNotBlank(pod)) {
				queryBuilder.must(QueryBuilders.termsQuery("podName.keyword", pod.split(",")));
			}
			if (StringUtils.isNotBlank(deviceClass)) {
				queryBuilder.must(QueryBuilders.termsQuery("deviceClass.keyword", deviceClass.split(",")));
			}
			if (StringUtils.isNotBlank(deviceType)) {
				queryBuilder.must(QueryBuilders.termsQuery("deviceType.keyword", deviceType.split(",")));
			}
			if (StringUtils.isNotBlank(roomId)) {
				queryBuilder.must(QueryBuilders.termsQuery("roomId.keyword", roomId.split(",")));
			}
			if (StringUtils.isNotBlank(keys)) {
				BoolQueryBuilder shouldBuilder = QueryBuilders.boolQuery();
				String[] keysArr = keys.split(",,/;");
				for (String key : keysArr) {
					// BoolQueryBuilder keyBuilder = QueryBuilders.boolQuery();
					// keyBuilder.must();
					shouldBuilder.should(QueryBuilders.matchPhraseQuery("item", key));
				}
				queryBuilder.must(shouldBuilder);

			}
			
			// request.setQuery(queryBuilder).setSize(10000);
			String[] indexs = monitorKpiServiceClient.getClusterIndexOut(JSONObject.fromObject(req),
					indexList.toArray(new String[] {}));
			
			log.info("查询es请求索引： {}", indexs);
			SearchRequestBuilder request = transportClient
					.prepareSearch(indexs)
					.setSearchType(SearchType.DEFAULT).setQuery(queryBuilder).setSize(size)
					.setScroll(TimeValue.timeValueMinutes(minute));
			log.info("查询es请求： {}", request);
			SearchResponse scrollResponse = request.execute().actionGet();
			long total = scrollResponse.getHits().totalHits;
			log.info("data--size:{}", total);
			log.info("topic:{}", topic);
			while (scrollResponse.getHits().getHits().length > 0) {
				List<Map<String, Object>> list = Lists.newArrayList();
				for (SearchHit searchHit : scrollResponse.getHits().getHits()) {
					// System.out.println(searchHit.getSource().toString());
					Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
					list.add(sourceAsMap);
				}
				
				kafkaTemplate.send(topic, JSON.toJSONString(list));
				/*
				 * try { Thread.sleep(5000); } catch (InterruptedException e) {
				 * e.printStackTrace(); }
				 */
				scrollResponse = transportClient.prepareSearchScroll(scrollResponse.getScrollId())
						.setScroll(TimeValue.timeValueMinutes(minute)).execute().actionGet();
			}
			standLog.setContent("查询数据:"+total+"条");
			standLog.setStatus("success");
			log.info("getSuyanData--end*****************************");
		} catch (Exception e) {
			log.error("同步性能数据报错,sub_id:{}", req.getId(), e);
			standLog.setStatus("fail");
			standLog.setContent(e.getClass().getName() + ":" + e.getMessage().toString());
		} finally {
			Date createTime = new Date();
			standLog.setCreateTime(createTime);
			standLog.setExecDuration((createTime.getTime() / 1000 - standLog.getExecTime().getTime() / 1000) + "");
			alertRestfulDao.insertStandardizationLog(standLog);
		}

	}
}
