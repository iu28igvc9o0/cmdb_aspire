package com.aspire.mirror.alert.server.controller.logWork;

import com.aspire.mirror.alert.api.dto.logWork.AlertWorkConfigDetail;
import com.aspire.mirror.alert.api.service.logWork.AlertLogWorkService;
import com.aspire.mirror.alert.server.biz.logWork.AlertWorkLogBiz;
import com.aspire.mirror.alert.server.dao.logWork.po.AlertWorkConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.aspire.mirror.alert.server.util.PayloadParseUtil.jacksonBaseParse;

@RestController
@Slf4j
public class AlertWorkLogController implements AlertLogWorkService {

    @Autowired
    private AlertWorkLogBiz alertWorkLogBiz;

    @Override
    public String createdAlerts(@RequestBody com.aspire.mirror.alert.api.dto.logWork.AlertWorkConfig request) {
        AlertWorkConfig alertWorkConfig = jacksonBaseParse(AlertWorkConfig.class, request);
        return alertWorkLogBiz.createdAlerts(alertWorkConfig);
    }

    @Override
    public AlertWorkConfigDetail getAlertWorkConfig() {
        AlertWorkConfig alertWorkConfig = alertWorkLogBiz.getAlertWorkConfig();
        return jacksonBaseParse(AlertWorkConfigDetail.class, alertWorkConfig);
    }

    @Override
    public Object getWorkLogInfo(@RequestParam(value = "workName", required = false) String workName,
                                 @RequestParam(value = "workDate", required = false) String workDate,
                                 @RequestParam(value = "workTime", required = false) String workTime,
                                 @RequestParam(value = "work", required = false) String work) {
        return alertWorkLogBiz.getWorkLogInfo(workName,workDate,workTime,work);
    }

    @Override
    public Object getWorkLogList(@RequestParam(value = "workName", required = false) String workName,
                                 @RequestParam(value = "workMonth", required = false) String workMonth) {
        return alertWorkLogBiz.getWorkLogList(workName,workMonth);
    }
}
