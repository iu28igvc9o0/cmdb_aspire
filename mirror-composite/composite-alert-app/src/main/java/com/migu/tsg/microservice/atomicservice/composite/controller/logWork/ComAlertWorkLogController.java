package com.migu.tsg.microservice.atomicservice.composite.controller.logWork;

import com.aspire.mirror.alert.api.dto.logWork.AlertWorkConfig;
import com.aspire.mirror.alert.api.dto.logWork.AlertWorkConfigDetail;
import com.aspire.mirror.composite.payload.logWork.ComAlertWorkConfigData;
import com.aspire.mirror.composite.payload.logWork.CompAlertWorkConfigDetail;
import com.aspire.mirror.composite.service.logWork.IComAlertWorkLogService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.logWork.AlertWorkLogServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

@RestController
public class ComAlertWorkLogController implements IComAlertWorkLogService {

    @Autowired
    private AlertWorkLogServiceClient alertWorkLogServiceClient;

    @Override
    public String createdAlertWorkConfig(@RequestBody ComAlertWorkConfigData comAlertWorkConfigData) {

        AlertWorkConfig alertWorkConfig = PayloadParseUtil.jacksonBaseParse(AlertWorkConfig.class, comAlertWorkConfigData );
        return alertWorkLogServiceClient.createdAlerts(alertWorkConfig);
    }

    @Override
    public CompAlertWorkConfigDetail getAlertWorkConfig() {
        AlertWorkConfigDetail alertWorkConfig = alertWorkLogServiceClient.getAlertWorkConfig();
        return jacksonBaseParse(CompAlertWorkConfigDetail.class, alertWorkConfig);
    }

    @Override
    public Object getWorkLogInfo(@RequestParam(value = "workName") String workName,
                                 @RequestParam(value = "workDate") String workDate,
                                 @RequestParam(value = "workTime") String workTime,
                                 @RequestParam(value = "work") String work) {
        return alertWorkLogServiceClient.getWorkLogInfo(workName,workDate,workTime,work);
    }

    @Override
    public Object getWorkLogList(@RequestParam(value = "workName", required = false) String workName,
                                 @RequestParam(value = "workMonth", required = false) String workMonth) {
        return alertWorkLogServiceClient.getWorkLogList(workName, workMonth);
    }

}
