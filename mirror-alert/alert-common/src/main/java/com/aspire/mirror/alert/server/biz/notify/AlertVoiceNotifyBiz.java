package com.aspire.mirror.alert.server.biz.notify;

import com.aspire.mirror.alert.server.dao.notify.po.AlertVoiceNotifyDetail;
import com.aspire.mirror.alert.server.dao.notify.po.AlertVoiceNotifyReqDTO;
import com.aspire.mirror.alert.server.vo.notify.AlertVoiceNotifyContentVo;
import org.springframework.http.ResponseEntity;

public interface AlertVoiceNotifyBiz {

    String createdAlertVoiceNotify(AlertVoiceNotifyReqDTO request);

    AlertVoiceNotifyDetail getAlertVoiceNotify(String creator);

    ResponseEntity<String> getVoiceContent(AlertVoiceNotifyContentVo request);

}
