package com.aspire.mirror.monitor.server.biz;

import com.aspire.mirror.log.api.dto.LogAlertRuleDetail;
import com.aspire.mirror.log.api.dto.SysLogSearchRequest;

public interface SyslogAlertScheduleBiz {
    void SyslogAlertScheduleService(LogAlertRuleDetail logAlertRuleDetail, SysLogSearchRequest sysLogSearchRequest);
}
