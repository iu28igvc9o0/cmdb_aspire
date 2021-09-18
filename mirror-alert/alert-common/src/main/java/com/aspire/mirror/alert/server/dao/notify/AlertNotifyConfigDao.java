package com.aspire.mirror.alert.server.dao.notify;

import com.aspire.mirror.alert.server.dao.notify.po.AlertNotifyConfigDetail;
import com.aspire.mirror.alert.server.dao.notify.po.AlertNotifyConfigLog;
import com.aspire.mirror.alert.server.dao.notify.po.AlertNotifyConfigReceiverDetail;
import com.aspire.mirror.alert.server.dao.notify.po.AlertNotifyConfigReqDTO;
import feign.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlertNotifyConfigDao {

    void createAlertNotifyConfig(AlertNotifyConfigReqDTO request);

    void createAlertNotifyConfigReceiver(List<Map<String, String>> request);

    List<Map<String, Object>> getAlertNotifyConfigList(Map<String, Object> request);

    AlertNotifyConfigDetail getAlertNotifyConfigDetail(@Param("uuid") String uuid);

    AlertNotifyConfigReceiverDetail getAlertNotifyConfigReceiverDetail(@Param("alertNotifyConfigId") String alertNotifyConfigId);

    void deleteAlertNotifyConfig(List<String> uuidList);

    void deleteAlertNotifyConfigReceiver(List<String> uuidList);

    void updateAlertNotifyConfigForOpen(Map<String, Object> request);

    Map<String, Object> getAlertFilterSceneData(@Param("alert_filter_scene_id") int id);

    List<AlertNotifyConfigDetail> getAlertNotifyConfig(Map<String, String> request);

    List<Map<String, Object>> getAlertsFromOperationRecord(Map<String, Object> param);

    void insertAlertNotifyConfigLog(List<AlertNotifyConfigLog> req);

    void updateAlertNotifyConfig(Map<String, Object> param);

    List<Map<String,String>> getResendData();

    List<Map<String,Object>> getHisAlerts(Map<String, Object> param);

    List<Map<String,String>> getAlertNotifyConfigRule();

    void updateAlertNotifyConfigRule(Map<String, String> req);

    List<Map<String,String>> getSendAlertNotify(Map<String, String> req);

    List<Map<String, Object>> getReSendAlerts(Map<String, Object> map);
}
