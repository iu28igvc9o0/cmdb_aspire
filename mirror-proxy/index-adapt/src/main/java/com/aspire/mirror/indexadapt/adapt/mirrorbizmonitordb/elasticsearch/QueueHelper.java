package com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ES 批处理入库类
 * @author lupeng
 *
 */
@Component
public class QueueHelper {
	
	
	@Autowired
	private MirrorElasticSearchClient elasticSearcHelper;
	
	private static Logger logger = LoggerFactory.getLogger(QueueHelper.class);
	
	
	private static final ArrayBlockingQueue<IndexRequestBuilder> queue = new ArrayBlockingQueue<IndexRequestBuilder>(
			100000);	//队列
	
	private static final int SAVE_TIME_STEP_MILLIS = 6 * 1000 ; 	//保存间隔时间：毫秒
	
	private static final int SAVE_LINE_NUMS = 1000;		//批量提交条数
	
	private static long lastWriteTime;  // 上次保存数据时间
	
	public QueueHelper() {
		startUp();
	}
	
	private  void startUp() {
//		startUpLine();
		startUpTimerTask();
	}
	
	public void put(IndexRequestBuilder lrb) {
		try {
			if(null != lrb) {
				queue.put(lrb);
			}
		}catch (Exception e) {
			logger.error("队列新增数据失败 ...", e);
		}
	}
	
	private void savaToEs(List<IndexRequestBuilder> lrbs) {
		try {
			if(lrbs.isEmpty()) {
				return;
			}
			long startTime=System.currentTimeMillis();
			BulkRequestBuilder bulkRequest = elasticSearcHelper.getClient().prepareBulk();
			for(IndexRequestBuilder lrb : lrbs) {
				bulkRequest.add(lrb);
			}
			BulkResponse bulkResponse = bulkRequest.execute().actionGet();
			if (bulkResponse.hasFailures()) {
				logger.error("es批量入库失败 :"+bulkResponse.getItems().toString());
			}
			long endTime=System.currentTimeMillis();
			float excTime = (float)(endTime-startTime)/1000;
			logger.info("批处理["+bulkRequest.numberOfActions()+"]条提交到es成功 ！,耗时："+excTime+"秒");
		}catch (Exception e) {
			logger.error("es批量入库异常！",e);
		}finally {
			lastWriteTime = System.currentTimeMillis();
		}
		
	}
	
	/**
	 * 行处理线程任务
	 */
	private  void startUpLine() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (queue.size() >= SAVE_LINE_NUMS) {
						try {
							List<IndexRequestBuilder> list = new ArrayList<IndexRequestBuilder>();
							int nums = drainTo(list);
							if (nums > 0) {
								logger.info("行处理任务批处理["+nums+"]条！");
								savaToEs(list);
							} 
						} catch (Exception e) {
							logger.error("行处理线程异常！", e);
						}
					}
				}
			}
		}).start();
	}
	
	/**
	 * 定时处理线程，每6秒执行一次
	 */
	private  void startUpTimerTask() {
		lastWriteTime = System.currentTimeMillis();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				long passTime = System.currentTimeMillis() - lastWriteTime;
				if (passTime < SAVE_TIME_STEP_MILLIS-1000) {
					return;
				}
				try {
					List<IndexRequestBuilder> list = new ArrayList<IndexRequestBuilder>();
					int nums = drainTo(list);
					if (nums > 0) {
						logger.info("定时任务批处理["+nums+"]条！");
						savaToEs(list);
					} 
				} catch (Exception e) {
					logger.error("定时处理线程异常！", e);
				}
			}
		}, SAVE_TIME_STEP_MILLIS, SAVE_TIME_STEP_MILLIS);
	}
	
	/**
	 * 从当队列中取出所有采集数据/
	 * 
	 * @param list
	 * @return
	 */
	private static int drainTo(List<IndexRequestBuilder> list) {
		try {
			return queue.drainTo(list);
		} catch (Exception e) {
			logger.error("队列数据整体移出异常！", e);
		}
		return 0;
	}
}
