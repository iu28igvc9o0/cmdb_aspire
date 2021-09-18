package com.aspire.mirror.monitor.server.schedule;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.api.dto.LogAlertRuleDetail;
import com.aspire.mirror.log.api.dto.LogAlertRuleListReq;
import com.aspire.mirror.log.api.dto.SysLogSearchRequest;
import com.aspire.mirror.monitor.server.biz.SyslogAlertScheduleBiz;
import com.aspire.mirror.monitor.server.client.SysLogServiceClient;
import com.aspire.mirror.monitor.server.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@EnableScheduling
@Slf4j
public class SysLogAlertSchedule {

    @Autowired
    private SyslogAlertScheduleBiz syslogAlertScheduleBiz;
    @Autowired
    private SysLogServiceClient sysLogServiceClient;

    @Scheduled(fixedDelay = 60000)
    public void init () {
        log.info("------系统日志告警定时任务开启------");
        log.info("StartTime Of Creating Sys Log Alert is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(  )));

        try {
            LogAlertRuleListReq logAlertRuleListReq = new LogAlertRuleListReq();
            logAlertRuleListReq.setRunStatus("1");
            logAlertRuleListReq.setIsPage(-1);
            // 获取日志告警列表
            PageResponse<LogAlertRuleDetail> logAlertRuleList = sysLogServiceClient.getLogAlertRuleList(logAlertRuleListReq);
            if (!CollectionUtils.isEmpty(logAlertRuleList.getResult())) {
                for (LogAlertRuleDetail logAlertRuleDetail : logAlertRuleList.getResult()) {
                    SysLogSearchRequest sysLogSearchRequest = new SysLogSearchRequest();
                    sysLogSearchRequest.setIp(logAlertRuleDetail.getIp());
                    sysLogSearchRequest.setPool(logAlertRuleDetail.getIdcType());
                    Date date = new Date();
                    String startTime = DateUtils.getTime(logAlertRuleDetail.getFilterTime());
                    String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    sysLogSearchRequest.setCreateTimeStart(startTime);
                    sysLogSearchRequest.setCreateTimeEnd(endTime);
//                    sysLogSearchRequest.setCreateTimeStart("2019-07-18 00:00:00");
//                    sysLogSearchRequest.setCreateTimeEnd("2019-07-20 00:00:00");
                    sysLogSearchRequest.setIsExport("0");
                    sysLogSearchRequest.setPageNo("1");
                    sysLogSearchRequest.setPageSize("10000");
                    sysLogSearchRequest.setInclude(logAlertRuleDetail.getInclude());
                    sysLogSearchRequest.setIncludeKey(logAlertRuleDetail.getIncludeKey());
                    sysLogSearchRequest.setNoInclude(logAlertRuleDetail.getNoInclude());
                    sysLogSearchRequest.setNoIncludeKey(logAlertRuleDetail.getNoIncludeKey());
                    // 根据规则获取日志数据
                    syslogAlertScheduleBiz.SyslogAlertScheduleService(logAlertRuleDetail, sysLogSearchRequest);
                }
            }
        } catch (Exception e) {
            log.error("Schedule Create Sys LogAlert Error is {}", e);
        }
        log.info("------系统日志告警定时任务关闭------");
        log.info("EndTime Of Creating Sys Log Alert is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(  )));
    }
}
