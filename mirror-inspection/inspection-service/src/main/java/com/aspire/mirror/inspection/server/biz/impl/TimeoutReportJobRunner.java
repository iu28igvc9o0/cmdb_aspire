package com.aspire.mirror.inspection.server.biz.impl;

import com.aspire.mirror.inspection.api.dto.constant.TaskStatus;
import com.aspire.mirror.inspection.api.dto.constant.TaskType;
import com.aspire.mirror.inspection.api.dto.model.ReportDTO;
import com.aspire.mirror.inspection.api.dto.model.ReportDataWrap;
import com.aspire.mirror.inspection.server.biz.ReportBiz;
import com.aspire.mirror.inspection.server.biz.ReportDataGenerateBiz;
import com.aspire.mirror.inspection.server.dao.ReportItemDao;
import com.aspire.mirror.inspection.server.dao.TaskDao;
import com.aspire.mirror.inspection.server.dao.po.Report;
import com.aspire.mirror.inspection.server.dao.po.ReportItem;
import com.aspire.mirror.inspection.server.dao.po.ReportResultStatistic;
import com.aspire.mirror.inspection.server.dao.po.Task;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
* 超时巡检报表生成    <br/>
* Project Name:inspection-service
* File Name:TimeoutReportJobRunner.java
* Package Name:com.aspire.mirror.inspection.server.biz.impl
* ClassName: TimeoutReportJobRunner <br/>
* date: 2018年9月3日 下午3:57:53 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Service
class TimeoutReportJobRunner implements ApplicationRunner {
	private static final String			TIMEOUT_REPORT_GLOBAL_LOCK	= "timeoutInspectReportLock";

	@Autowired
	private RedissonClient				redissonClient;

	@Value("${reportData.timeout:300}")
	private Integer						timeout;

	@Value("${reportData.checkInterval:10}")
	private Integer						interval;

	@Autowired
	private ReportBiz					reportBiz;

	@Autowired
	private ReportItemDao reportItemDao;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private ReportDataGenerateBiz		reportDataBiz;

	private ScheduledThreadPoolExecutor	executor;
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
			private final AtomicInteger seq = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, TimeoutReportJobRunner.class.getSimpleName() + "_" + seq.getAndIncrement());
			}
		});
		executor.setMaximumPoolSize(1);
		scheduleTimeoutReportDataGenerate();
	}
	
	/**
	* 调度超时报表生成. <br/>
	 * 	*
	 * 	* 作者： pengguihua
	*/  
	private void scheduleTimeoutReportDataGenerate() {
		executor.scheduleAtFixedRate(buildTask(), 0, interval, TimeUnit.SECONDS);
	}
	
	private Runnable buildTask() {
		// 获取分布式锁
		final RLock globalLock = redissonClient.getLock(TIMEOUT_REPORT_GLOBAL_LOCK);
		return new Runnable() {
			@Override
			public void run() {
				try {
					globalLock.lock(); // 锁
					handleTimeoutInpsectReports();
				} catch (Throwable e) {
					log.error("Error to execute the timeout inspection report data generate job.", e);
				} finally {
					if (globalLock.isHeldByCurrentThread()) {
						globalLock.unlock();
					}
				}
			}
		};
	}
	
	/**
	* 方法标注为public, 是为了使用springAop的事务只支持public的方法<br/>
	* 如果使用AspectJ的事务增强, 可以支持private方法, 但AspectJ为事务注解动态生成字节码时，无法和lombok注解兼容,
	* 为了简单，暂时使用spring自带aop的事务. <br/>
	*
	* 作者： pengguihua
	*/  
	@Transactional
	public void handleTimeoutInpsectReports() {
		List<ReportDTO> timeoutReportList = getTimeoutReport();
		log.info("Check the timeout inpsection reports, the count is {}", timeoutReportList.size());
		updateTimeoutReportData(timeoutReportList);
		for (ReportDTO report : timeoutReportList) {
			try {
				ReportDataWrap reportData = reportDataBiz.generateReportData(report.getReportId());
				reportDataBiz.reportDataNotify(reportData);
			} catch (Exception e) {
				String msg = String.format(
						"Error to generate timeout inpsection report for report: %s", report.getReportId());
				log.error(msg, e);
			}
		}
	}
	
	// 获取超时报表
	private List<ReportDTO> getTimeoutReport() {
		List<ReportDTO> resultList = new ArrayList<>();
		ReportDTO param = new ReportDTO();
		param.setStatus(Report.STATUS_RUNNING);
		List<ReportDTO> runReportList = reportBiz.select(param);
		if (runReportList == null) {
			return resultList;
		}
		long nowMill = System.currentTimeMillis();
		for (ReportDTO report : runReportList) {
			if (nowMill - report.getCreateTime().getTime() > TimeUnit.SECONDS.toMillis(timeout)) {
				resultList.add(report);
			}
		}
		return resultList;
	}
	
	// 更新报表状态数据
	private void updateTimeoutReportData(List<ReportDTO> timeoutReportList) {
		Date finishTime = new Date();
		for (ReportDTO report : timeoutReportList) {
			report.setStatus(Report.STATUS_FINISHED);
			int count = reportItemDao.selectCountByStatus(ReportItem.STATUS_NORESULT, report.getReportId());
			if (count > 0) {
				report.setBizStatus(Report.BIZ_STATUS_FAIL);
			} else {
				report.setBizStatus(Report.BIZ_STATUS_SUCCESS);
			}
			report.setFinishTime(finishTime);
			report.setTotalTime(report.calcTotalSeconds());
			// 设置巡检结果
			ReportResultStatistic statisticValue = reportItemDao.selectNumStatistic(report.getReportId());
			String result = "扫描机器{0}台，扫描项共计{1}，结果项共计{2} 异常项<span style=\"color: red;font-weight: 800;" +
					"\">{3}</span>，正常项<span " +
					"style=\"color: green;font-weight: 800;\">{4}</span>，{5}项未取值,{6}项人工处理";
			result = MessageFormat.format(result, statisticValue.getDeviceNum(), statisticValue.getItemNum(), statisticValue.getResultNum(),
					statisticValue.getExceptionNum(), statisticValue.getNormalNum(),
					statisticValue.getNoResultNum(), statisticValue.getArtificialJudgmentNum());
			report.setResult(result);
			reportBiz.updateByPrimaryKeySelective(report);

			Task reqTask = taskDao.selectByPrimaryKey(report.getTaskId());
			if (reqTask.getType().equals(TaskType.HANDLED.getKey())) {
				Task task = new Task();
				task.setTaskId(report.getTaskId());
				task.setStatus(TaskStatus.OFF.getValue());
				taskDao.updateByPrimaryKeySelective(task);
			}
		}
	}
	
	@PreDestroy
	private void preDestroy() {
		if (executor != null) {
			executor.shutdownNow();
		}
	}
}
