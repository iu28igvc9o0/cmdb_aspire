package com.aspire.mirror.alert.server.biz.notify;

import com.aspire.mirror.alert.server.dao.notify.po.AlertNotifyConfigReqDTO;

import java.util.List;
import java.util.Map;

public interface AlertNotifyConfigBiz {

    Object getAlertNotifyConfigList(String name,
                                    String isOpen,
                                    String notifyType,
                                    String alertFilter,
                                    String notifyObj,
                                    String isRecurrenceInterval,
                                    String sendTimeStart,
                                    String sendTimeEnd,
                                    int pageNum,
                                    int pageSize);

    String createAlertNotifyConfig(AlertNotifyConfigReqDTO request);

    String updateAlertNotifyConfig(AlertNotifyConfigReqDTO request);

    Object getAlertNotifyConfigDetail(String uuid);

    String deleteAlertNotifyConfig(List<String> uuidList);

    String openAlertNotifyConfig(List<String> uuidList);

    String closeAlertNotifyConfig(List<String> uuidList);

    String copyAlertNotifyConfig(String uuid);

    void sendAlertNotifyConfigNew();

    void resendAlertNotifyConfigNew();

    void resendAlertNotifyConfig();

    Map<String, String> getAlertNotifyConfigRule();

    String updateAlertNotifyConfigRule(Map<String,String> req);
}
