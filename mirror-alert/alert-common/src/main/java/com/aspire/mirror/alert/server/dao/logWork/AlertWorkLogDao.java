package com.aspire.mirror.alert.server.dao.logWork;

import com.aspire.mirror.alert.server.dao.logWork.po.AlertWorkConfig;
import com.aspire.mirror.alert.server.dao.logWork.po.AlertWorkLogInfo;
import com.aspire.mirror.alert.server.vo.logWork.AlertWorkLogInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlertWorkLogDao {

    void insertAlertWorkConfig(AlertWorkConfig request);

    void updateAlertWorkConfig(AlertWorkConfig request);

    AlertWorkConfig getAlertWorkConfig();

    List<AlertWorkLogInfoVo> getAlertListByAlerts(AlertWorkLogInfo alertWorkLogTaskRequest);

    List<AlertWorkLogInfoVo> getAlertListByAlertsHis(AlertWorkLogInfo alertWorkLogTaskRequest);

    List<Map<String,Object>> getAlertsFromAlert(AlertWorkLogInfo alertWorkLogTaskRequest);

}
