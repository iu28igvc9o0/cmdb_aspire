package com.migu.tsg.microservice.atomicservice.composite.controller.notify;

import com.aspire.mirror.alert.api.dto.notify.AlertNotifyConfigReq;
import com.aspire.mirror.composite.payload.notify.ComAlertNotifyConfigReq;
import com.aspire.mirror.composite.service.notify.IComAlertNotifyConfigService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.notify.AlertNotifyConfigServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
public class ComAlertNotifyConfigController implements IComAlertNotifyConfigService {

    @Autowired
    private AlertNotifyConfigServiceClient alertNotifyConfigServiceClient;

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
        return alertNotifyConfigServiceClient.getAlertNotifyConfigList(
                name,isOpen,notifyType,alertFilter,notifyObj,isRecurrenceInterval,sendTimeStart,sendTimeEnd,pageNum,pageSize);
    }

    @Override
    public String createAlertNotifyConfig(@RequestBody ComAlertNotifyConfigReq request) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        request.setCreator(authCtx.getUser().getUsername());
        AlertNotifyConfigReq alertNotifyConfigReq = PayloadParseUtil.jacksonBaseParse(AlertNotifyConfigReq.class, request);
        return alertNotifyConfigServiceClient.createAlertNotifyConfig(alertNotifyConfigReq);
    }

    @Override
    public String updateAlertNotifyConfig(@RequestBody ComAlertNotifyConfigReq request) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        request.setCreator(authCtx.getUser().getUsername());
        AlertNotifyConfigReq alertNotifyConfigReq = PayloadParseUtil.jacksonBaseParse(AlertNotifyConfigReq.class, request);
        return alertNotifyConfigServiceClient.updateAlertNotifyConfig(alertNotifyConfigReq);
    }

    @Override
    public Object getAlertNotifyConfigDetail(@RequestParam("uuid") String uuid) {
        return alertNotifyConfigServiceClient.getAlertNotifyConfigDetail(uuid);
    }

    @Override
    public String deleteAlertNotifyConfig(@RequestBody List<String> uuidList) {
        return alertNotifyConfigServiceClient.deleteAlertNotifyConfig(uuidList);
    }

    @Override
    public String openAlertNotifyConfig(@RequestBody List<String> uuidList) {
        return alertNotifyConfigServiceClient.openAlertNotifyConfig(uuidList);
    }

    @Override
    public String closeAlertNotifyConfig(@RequestBody List<String> uuidList) {
        return alertNotifyConfigServiceClient.closeAlertNotifyConfig(uuidList);
    }

    @Override
    public String copyAlertNotifyConfig(@RequestParam("uuid") String uuid) {
        return alertNotifyConfigServiceClient.copyAlertNotifyConfig(uuid);
    }

    @Override
    public Map<String, String> getAlertNotifyConfigRule() {
        return alertNotifyConfigServiceClient.getAlertNotifyConfigRule();
    }

    @Override
    public String updateAlertNotifyConfigRule(@RequestBody Map<String, String> req) {
        return alertNotifyConfigServiceClient.updateAlertNotifyConfigRule(req);
    }

    @Override
    public boolean getOperationPermission(@RequestParam("creator") String creator) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        RequestAuthContext.RequestHeadUser user = authCtx.getUser();
        return user.isSuperUser() || user.isAdmin() || creator.equals(user.getUsername());
    }
}
