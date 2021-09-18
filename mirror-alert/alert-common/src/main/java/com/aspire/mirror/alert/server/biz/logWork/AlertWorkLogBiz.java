package com.aspire.mirror.alert.server.biz.logWork;

import com.aspire.mirror.alert.server.dao.logWork.po.AlertWorkConfig;

public interface AlertWorkLogBiz {

    String createdAlerts(AlertWorkConfig alertWorkConfig);

    AlertWorkConfig getAlertWorkConfig();

    Object getWorkLogInfo(String workName,String workDate,String workTime,String work);

    Object getWorkLogList(String workName, String workMonth);

}
