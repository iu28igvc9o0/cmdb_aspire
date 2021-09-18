package com.migu.tsg.microservice.atomicservice.composite.controller.notify;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.api.dto.notify.AlertVoiceNotifyContentReq;
import com.aspire.mirror.alert.api.dto.notify.AlertVoiceNotifyReq;
import com.aspire.mirror.composite.payload.notify.CompAlertVoiceNotifyContentReq;
import com.aspire.mirror.composite.payload.notify.CompAlertVoiceNotifyReq;
import com.aspire.mirror.composite.service.notify.IComAlertVoiceNotifyService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.notify.AlertVoiceNotifyServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CompAlertVoiceNotifyController implements IComAlertVoiceNotifyService {

    @Autowired
    private AlertVoiceNotifyServiceClient client;


    @Override
    public String createdAlertVoiceNotify(@RequestBody CompAlertVoiceNotifyReq request) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        request.setCreator(authCtx.getUser().getUsername());
        AlertVoiceNotifyReq alertVoiceNotifyContentReq = PayloadParseUtil.jacksonBaseParse(AlertVoiceNotifyReq.class, request);
        log.info("#=====> create alert voice notify entity: {}", JSONObject.toJSONString(alertVoiceNotifyContentReq));
        return client.createdAlertVoiceNotify(alertVoiceNotifyContentReq);
    }

    @Override
    public Object getAlertVoiceNotify() {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        return client.getAlertVoiceNotify(authCtx.getUser().getUsername());
    }

    @Override
    public ResponseEntity<String> getAlertVoiceNotifyContent(@RequestBody CompAlertVoiceNotifyContentReq request) {
        AlertVoiceNotifyContentReq alertVoiceNotifyContentReq = PayloadParseUtil.jacksonBaseParse(AlertVoiceNotifyContentReq.class, request);
        return client.getAlertVoiceNotifyContent(alertVoiceNotifyContentReq);
    }
}
