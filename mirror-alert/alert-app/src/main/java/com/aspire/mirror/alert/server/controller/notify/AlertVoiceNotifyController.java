package com.aspire.mirror.alert.server.controller.notify;

import com.aspire.mirror.alert.api.dto.notify.AlertVoiceNotifyContentReq;
import com.aspire.mirror.alert.api.dto.notify.AlertVoiceNotifyReq;
import com.aspire.mirror.alert.api.service.notify.AlertVoiceNotifyService;
import com.aspire.mirror.alert.server.biz.notify.AlertVoiceNotifyBiz;
import com.aspire.mirror.alert.server.dao.notify.po.AlertVoiceNotifyReqDTO;
import com.aspire.mirror.alert.server.vo.notify.AlertVoiceNotifyContentVo;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.aspire.mirror.alert.server.util.PayloadParseUtil.jacksonBaseParse;

@RestController
@Slf4j
public class AlertVoiceNotifyController implements AlertVoiceNotifyService {

    @Autowired
    private AlertVoiceNotifyBiz alertVoiceNotifyBiz;

    @Override
    public String createdAlertVoiceNotify(@RequestBody AlertVoiceNotifyReq request) {
        AlertVoiceNotifyReqDTO alertVoiceNotifyReqDTO = jacksonBaseParse( AlertVoiceNotifyReqDTO.class, request);
        return alertVoiceNotifyBiz.createdAlertVoiceNotify(alertVoiceNotifyReqDTO);
    }

    @Override
    public Object getAlertVoiceNotify(@RequestParam("creator") String creator) {
        return alertVoiceNotifyBiz.getAlertVoiceNotify(creator);
    }

    @Override
    public ResponseEntity<String> getAlertVoiceNotifyContent(@RequestBody AlertVoiceNotifyContentReq request) {
        return alertVoiceNotifyBiz.getVoiceContent(PayloadParseUtil.jacksonBaseParse(AlertVoiceNotifyContentVo.class, request));
    }
}
