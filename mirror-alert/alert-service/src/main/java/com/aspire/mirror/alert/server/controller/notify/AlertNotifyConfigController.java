package com.aspire.mirror.alert.server.controller.notify;

import com.aspire.mirror.alert.api.dto.notify.AlertNotifyConfigReq;
import com.aspire.mirror.alert.api.service.notify.AlertNotifyConfigService;
import com.aspire.mirror.alert.server.biz.notify.AlertNotifyConfigBiz;
import com.aspire.mirror.alert.server.dao.notify.po.AlertNotifyConfigReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.aspire.mirror.alert.server.util.PayloadParseUtil.jacksonBaseParse;

@RestController
public class AlertNotifyConfigController implements AlertNotifyConfigService {

    @Autowired
    private AlertNotifyConfigBiz alertNotifyConfigBiz;

    @Override
    public Object getAlertNotifyConfigList(@RequestParam( value = "name", required = false) String name,
                                           @RequestParam( value = "isOpen", required = false) String isOpen,
                                           @RequestParam( value = "notifyType", required = false) String notifyType,
                                           @RequestParam( value = "alertFilter", required = false) String alertFilter,
                                           @RequestParam( value = "notifyObj", required = false) String notifyObj,
                                           @RequestParam( value = "isRecurrenceInterval", required = false) String isRecurrenceInterval,
                                           @RequestParam( value = "sendTimeStart", required = false) String sendTimeStart,
                                           @RequestParam( value = "sendTimeEnd", required = false) String sendTimeEnd,
                                           @RequestParam( value = "pageNum", required = false) int pageNum,
                                           @RequestParam( value = "pageSize", required = false) int pageSize) {
        return alertNotifyConfigBiz.getAlertNotifyConfigList(
                name,isOpen,notifyType,alertFilter,notifyObj,isRecurrenceInterval,sendTimeStart,sendTimeEnd,pageNum,pageSize);
    }

    @Override
    public String createAlertNotifyConfig(@RequestBody AlertNotifyConfigReq request) {
        AlertNotifyConfigReqDTO alertNotifyConfigReqDTO = jacksonBaseParse(AlertNotifyConfigReqDTO.class, request);
        return alertNotifyConfigBiz.createAlertNotifyConfig(alertNotifyConfigReqDTO);
    }

    @Override
    public String updateAlertNotifyConfig(@RequestBody AlertNotifyConfigReq request) {
        AlertNotifyConfigReqDTO alertNotifyConfigReqDTO = jacksonBaseParse(AlertNotifyConfigReqDTO.class, request);
        return alertNotifyConfigBiz.updateAlertNotifyConfig(alertNotifyConfigReqDTO);
    }

    @Override
    public Object getAlertNotifyConfigDetail(@RequestParam("uuid") String uuid) {
        return alertNotifyConfigBiz.getAlertNotifyConfigDetail(uuid);
    }

    @Override
    public String deleteAlertNotifyConfig(@RequestBody List<String> uuidList) {
        return alertNotifyConfigBiz.deleteAlertNotifyConfig(uuidList);
    }

    @Override
    public String openAlertNotifyConfig(@RequestBody List<String> uuidList) {
        return alertNotifyConfigBiz.openAlertNotifyConfig(uuidList);
    }

    @Override
    public String closeAlertNotifyConfig(@RequestBody List<String> uuidList) {
        return alertNotifyConfigBiz.closeAlertNotifyConfig(uuidList);
    }

    @Override
    public String copyAlertNotifyConfig(@RequestParam("uuid") String uuid) {
        return alertNotifyConfigBiz.copyAlertNotifyConfig(uuid);
    }

    @Override
    public void send() {
        alertNotifyConfigBiz.sendAlertNotifyConfigNew();
    }

    @Override
    public void resend() {
        alertNotifyConfigBiz.resendAlertNotifyConfigNew();
    }

    @Override
    public Map<String, String> getAlertNotifyConfigRule() {
        return alertNotifyConfigBiz.getAlertNotifyConfigRule();
    }

    @Override
    public String updateAlertNotifyConfigRule(@RequestBody Map<String,String> req) {
        return alertNotifyConfigBiz.updateAlertNotifyConfigRule(req);
    }
}
